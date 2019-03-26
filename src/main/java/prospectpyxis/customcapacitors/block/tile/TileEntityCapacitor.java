package prospectpyxis.customcapacitors.block.tile;

import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import prospectpyxis.customcapacitors.CustomCapacitors;
import prospectpyxis.customcapacitors.data.CapacitorData;
import prospectpyxis.customcapacitors.network.MessageCapacitorColor;
import prospectpyxis.customcapacitors.network.MessageCapacitorFaces;
import prospectpyxis.customcapacitors.network.MessageReqCapacitorColor;
import prospectpyxis.customcapacitors.network.MessageReqCapacitorFaces;
import prospectpyxis.customcapacitors.registry.CapacitorRegistry;
import prospectpyxis.pyxislib.energy.EnergyManager;

import javax.annotation.Nullable;

public class TileEntityCapacitor extends TileEntity implements ITickable {

    public CapacitorData data = new CapacitorData();
    public int baseColor = 0xFFFFFF;
    public int trimColor = 0xFFFFFF;

    public EnergyManager eContainer = new EnergyManager(0, 0);
    public EnergyManager eReceiver = new EnergyManager(0, 0);
    public EnergyManager eExtractor = new EnergyManager(0, 0);

    // 0 is empty, 1 is input, 2 is output
    // Follows standard EnumFacing indices, so order is D-U-N-S-W-E
    public int[] FACES = new int[]{0, 0, 0, 0, 0, 0};

    @Override
    public void update() {
        if (world.getTotalWorldTime() % data.storageLossData.lossDelay == 0) {
            int minThreshold = data.storageLossData.minThreshold == -1 ? 0 : data.storageLossData.minThreshold;
            int maxThreshold = data.storageLossData.maxThreshold == -1 ? eContainer.getMaxEnergyStored() + 1 : data.storageLossData.maxThreshold;
            if (data.storageLossType != CapacitorData.EnumLossType.NONE && eContainer.getEnergyStored() >= minThreshold && eContainer.getEnergyStored() < maxThreshold) {
                switch (data.storageLossType) {
                    case CONSTANT:
                        eContainer.extractEnergy((int) Math.floor(data.storageLossData.lossValue), false);
                        break;
                    case PERCENTAGE:
                        eContainer.extractEnergy(Math.round(eContainer.getEnergyStored() * data.storageLossData.lossValue), false);
                        break;
                    case PERCENTAGE_INVERTED:
                        eContainer.extractEnergy(Math.round
                                ((eContainer.getMaxEnergyStored() - eContainer.getEnergyStored()) * data.storageLossData.lossValue), false);
                        break;
                    default:
                }
            }
        }

        for (EnumFacing ff : EnumFacing.values()) {
            int fi = ff.getIndex();
            if (FACES[fi] == 2) {
                TileEntity te = world.getTileEntity(pos.offset(ff));
                if (te != null && te.hasCapability(CapabilityEnergy.ENERGY, ff.getOpposite())) {
                    te.getCapability(CapabilityEnergy.ENERGY, ff.getOpposite())
                            .receiveEnergy(eExtractor.extractEnergy(data.maxOutputRate, false), false);
                }
            }
        }
    }

    @Override
    public boolean hasFastRenderer() {
        return true;
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing)
    {
        if (capability == CapabilityEnergy.ENERGY) {
            if (facing == null || FACES[facing.getIndex()] != 0) return true;
        }
        return super.hasCapability(capability, facing);
    }

    @Nullable
    @SuppressWarnings("unchecked")
    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        if (capability == CapabilityEnergy.ENERGY) {
            if (facing == null) return (T)eContainer;
            else if (FACES[facing.getIndex()] == 1) return (T)eReceiver;
            else if (FACES[facing.getIndex()] == 2) return (T)eExtractor;
        }
        return super.getCapability(capability, facing);
    }

    @Override
    public void onLoad() {
        if (world.isRemote) {
            CustomCapacitors.NETWORKING.sendToServer(new MessageReqCapacitorColor(this));
            CustomCapacitors.NETWORKING.sendToServer(new MessageReqCapacitorFaces(this));
        }

    }

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState)
    {
        return (oldState.getBlock() != newState.getBlock());
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setInteger("energy", eContainer.getEnergyStored());
        compound.setString("capid", data.id);
        return super.writeToNBT(compound);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        
        this.data = CapacitorRegistry.getDataById(compound.getString("capid")) == null ? new CapacitorData() : CapacitorRegistry.getDataById(compound.getString("capid"));

        baseColor = data.getColorBase();
        trimColor = data.getColorTrim();

        if (world != null && !world.isRemote) {
            CustomCapacitors.NETWORKING.sendToAllAround(
                    new MessageCapacitorColor(this),
                    new NetworkRegistry.TargetPoint(world.provider.getDimension(), pos.getX(), pos.getY(), pos.getZ(), 64)
            );
        }

        if (data != null) {
            eContainer = new EnergyManager(data.capacity, data.maxInputRate, data.maxOutputRate) {
                @Override
                public int receiveEnergy(int maxReceive, boolean simulate) {
                    switch (data.inputLossType) {
                        case CONSTANT:
                            maxReceive = maxReceive - (int) Math.floor(data.inputLossValue);
                            break;
                        case PERCENTAGE:
                            maxReceive = maxReceive - Math.round(maxReceive * data.inputLossValue);
                            break;
                        case PERCENTAGE_INVERTED:
                            maxReceive = maxReceive - (int) ((this.maxReceive - maxReceive) * data.inputLossValue);
                            break;
                        default:
                    }
                    return super.receiveEnergy(maxReceive, simulate);
                }
            };

            eReceiver = new EnergyManager(0, data.maxInputRate) {
                @Override
                public boolean canExtract() {
                    return false;
                }

                @Override
                public int receiveEnergy(int maxReceive, boolean simulate) {
                    return eContainer.receiveEnergy(maxReceive, simulate);
                }
            };

            eExtractor = new EnergyManager(0, data.maxOutputRate) {
                @Override
                public boolean canReceive() {
                    return false;
                }

                @Override
                public int extractEnergy(int maxExtract, boolean simulate) {
                    return eContainer.extractEnergy(maxExtract, simulate);
                }
            };
        }
        eContainer.setEnergy(compound.getInteger("energy"));
    }

    public void notifyFaceChanges() {
        if (!world.isRemote) {
            CustomCapacitors.NETWORKING.sendToAllAround(
                    new MessageCapacitorFaces(this),
                    new NetworkRegistry.TargetPoint(world.provider.getDimension(), pos.getX(), pos.getY(), pos.getZ(), 64)
            );
        }
    }

}

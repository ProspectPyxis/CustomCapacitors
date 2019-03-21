package prospectpyxis.customcapacitors.block.tile;

import com.google.common.collect.ImmutableMap;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import prospectpyxis.customcapacitors.CustomCapacitors;
import prospectpyxis.customcapacitors.block.BlockCapacitor;
import prospectpyxis.customcapacitors.data.CapacitorData;
import prospectpyxis.customcapacitors.registry.CapacitorRegistry;
import prospectpyxis.pyxislib.energy.EnergyManager;

import javax.annotation.Nullable;

public class TileEntityCapacitor extends TileEntity implements ITickable {

    public CapacitorData data = new CapacitorData();

    public EnergyManager eContainer = new EnergyManager(0, 0);
    public EnergyManager eReceiver = new EnergyManager(0, 0);
    public EnergyManager eExtractor = new EnergyManager(0, 0);
    @Override
    public void update() {
        if (world.getTotalWorldTime() % data.storageLossData.lossDelay == 0) {
            int minThreshold = data.storageLossData.minThreshold == -1 ? 0 : data.storageLossData.minThreshold;
            int maxThreshold = data.storageLossData.maxThreshold == -1 ? eContainer.getMaxEnergyStored() + 1 : data.storageLossData.minThreshold;
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

        ImmutableMap blockProperties = world.getBlockState(pos).getProperties();
        if (blockProperties.containsKey(BlockCapacitor.FACING)) {
            EnumFacing fc = (EnumFacing)blockProperties.get(BlockCapacitor.FACING);
                TileEntity te = world.getTileEntity(pos.offset(fc));
                if (te != null && te.hasCapability(CapabilityEnergy.ENERGY, fc.getOpposite())) {
                    te.getCapability(CapabilityEnergy.ENERGY, fc.getOpposite())
                            .receiveEnergy(eExtractor.extractEnergy(data.maxOutputRate, false), false);
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
            return true;
        }
        return super.hasCapability(capability, facing);
    }

    @Nullable
    @SuppressWarnings("unchecked")
    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        if (capability == CapabilityEnergy.ENERGY) {
            if (facing == null) return (T)eContainer;
            ImmutableMap blockProperties = world.getBlockState(pos).getProperties();
            if (blockProperties.containsKey(BlockCapacitor.FACING)) {
                if (facing.equals(blockProperties.get(BlockCapacitor.FACING))) {
                    return (T)eExtractor;
                }
                else return (T)eReceiver;
            }
        }
        return super.getCapability(capability, facing);
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
        data = CapacitorRegistry.getDataById(compound.getString("capid")) == null ? new CapacitorData() : CapacitorRegistry.getDataById(compound.getString("capid"));

        if (data != null) {
            if (data.storageLossData.lossDelay < 1) {
                data.storageLossData.lossDelay = 1;
                CustomCapacitors.logger.warn("Warning! Capacitor " + data.id + " has less than 1 loss delay!");
                CustomCapacitors.logger.warn("It will be set to 1, please change the loss delay to a positive number!");
            }

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

        super.readFromNBT(compound);
    }
}

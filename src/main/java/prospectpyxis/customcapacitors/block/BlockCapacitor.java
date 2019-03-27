package prospectpyxis.customcapacitors.block;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import prospectpyxis.customcapacitors.CustomCapacitors;
import prospectpyxis.customcapacitors.block.tile.TileEntityCapacitor;
import prospectpyxis.customcapacitors.data.CapacitorData;
import prospectpyxis.customcapacitors.registry.CapacitorRegistry;
import prospectpyxis.pyxislib.block.BlockWithTileEntity;
import prospectpyxis.pyxislib.block.IWrenchable;

import javax.annotation.Nullable;

public class BlockCapacitor extends BlockWithTileEntity<TileEntityCapacitor> implements IWrenchable {

    public BlockCapacitor() {
        super(Material.IRON);

        setUnlocalizedName("custom_capacitor");
        setRegistryName("custom_capacitor");
        setCreativeTab(CustomCapacitors.ctab);

        setHardness(2f);
        setResistance(10f);
    }

    @Override
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT_MIPPED;
    }

    @Override
    @SuppressWarnings("deprecation")
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.INVISIBLE;
    }

    @Override
    public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        TileEntityCapacitor tec = world.getTileEntity(pos) instanceof TileEntityCapacitor ? (TileEntityCapacitor)world.getTileEntity(pos) : null;
        if (tec != null) {
            ItemStack item = new ItemStack(this);
            NBTTagCompound data = new NBTTagCompound();
            tec.writeToNBT(data);
            if (!tec.data.retainEnergy) data.removeTag("energy");
            data.removeTag("x");
            data.removeTag("y");
            data.removeTag("z");
            data.removeTag("id");
            NBTTagCompound nbt = new NBTTagCompound();
            nbt.setTag("BlockEntityTag", data);
            item.setTagCompound(nbt);
            drops.add(item);
        }
    }

    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
        TileEntityCapacitor tec = world.getTileEntity(pos) instanceof TileEntityCapacitor ? (TileEntityCapacitor)world.getTileEntity(pos) : null;
        if (tec != null) {
            ItemStack item = new ItemStack(this);
            NBTTagCompound data = new NBTTagCompound();
            tec.writeToNBT(data);
            data.removeTag("energy");
            data.removeTag("baseColor");
            data.removeTag("trimColor");
            data.removeTag("x");
            data.removeTag("y");
            data.removeTag("z");
            data.removeTag("id");
            NBTTagCompound nbt = new NBTTagCompound();
            nbt.setTag("BlockEntityTag", data);
            item.setTagCompound(nbt);
            return item;
        }
        return null;
    }

    @Override
    public boolean removedByPlayer(IBlockState state, World world, BlockPos pos, EntityPlayer player, boolean willHarvest)
    {
        if (willHarvest) return true;
        return super.removedByPlayer(state, world, pos, player, willHarvest);
    }

    @Override
    public void harvestBlock(World world, EntityPlayer player, BlockPos pos, IBlockState state, @Nullable TileEntity te, ItemStack tool)
    {
        super.harvestBlock(world, player, pos, state, te, tool);
        world.setBlockToAir(pos);
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        EnumFacing facing = EnumFacing.getDirectionFromEntityLiving(pos, placer);

        if (placer.isSneaking()) facing = facing.getOpposite();

        TileEntityCapacitor tec = worldIn.getTileEntity(pos) instanceof TileEntityCapacitor ? (TileEntityCapacitor)worldIn.getTileEntity(pos) : null;
        if (tec != null) {
            tec.input_faces[facing.getIndex()] = 2;
            tec.input_faces[facing.getOpposite().getIndex()] = 1;
            tec.notifyFaceChanges();
        }
    }

    @Override
    public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> items) {
        for (CapacitorData d : CapacitorRegistry.LOADED_CAPACITORS) {
            NBTTagCompound data = new NBTTagCompound();
            NBTTagCompound nbt = new NBTTagCompound();
            data.setString("capid", d.id);
            nbt.setTag("BlockEntityTag", data);

            ItemStack subitm = new ItemStack(this);
            subitm.setTagCompound(nbt);

            items.add(subitm);
        }
    }

    @Override
    public void applyWrench(World worldIn, BlockPos pos, EnumFacing facing, boolean isPlayerSneaking) {
        TileEntityCapacitor tec = worldIn.getTileEntity(pos) instanceof TileEntityCapacitor ? (TileEntityCapacitor)worldIn.getTileEntity(pos) : null;
        if (tec == null) return;
        if (isPlayerSneaking) {
            facing = facing.getOpposite();
        }
        if (tec.input_faces[facing.getIndex()] == 0) tec.input_faces[facing.getIndex()] = 1;
        else if (tec.input_faces[facing.getIndex()] == 1) tec.input_faces[facing.getIndex()] = 2;
        else if (tec.input_faces[facing.getIndex()] == 2) tec.input_faces[facing.getIndex()] = 0;
    }

    @Override
    public Class<TileEntityCapacitor> getTileEntityClass() {
        return TileEntityCapacitor.class;
    }

    @Nullable
    @Override
    public TileEntityCapacitor createTileEntity(World world, IBlockState iBlockState) {
        return new TileEntityCapacitor();
    }
}

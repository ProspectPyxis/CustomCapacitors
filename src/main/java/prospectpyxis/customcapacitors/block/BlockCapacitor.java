package prospectpyxis.customcapacitors.block;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
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

    public static final PropertyDirection FACING = PropertyDirection.create("facing");

    public BlockCapacitor() {
        super(Material.IRON);

        setUnlocalizedName("custom_capacitor");
        setRegistryName("custom_capacitor");
        setCreativeTab(CustomCapacitors.ctab);
        this.setDefaultState(this.getBlockState().getBaseState().withProperty(FACING, EnumFacing.UP));
    }

    @Override
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT_MIPPED;
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
        worldIn.setBlockState(pos, state.withProperty(FACING, EnumFacing.getDirectionFromEntityLiving(pos, placer)), 3);
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
        if (!isPlayerSneaking) {
            worldIn.setBlockState(pos, worldIn.getBlockState(pos).withProperty(BlockCapacitor.FACING, facing), 3);
        } else {
            worldIn.setBlockState(pos, worldIn.getBlockState(pos).withProperty(BlockCapacitor.FACING, facing.getOpposite()), 3);
        }
    }

    @Override
    @SuppressWarnings("deprecation")
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(FACING, EnumFacing.getFront(meta));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(FACING).getIndex();
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING);
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

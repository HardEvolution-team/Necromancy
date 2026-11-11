package com.electrifiedded.necromancy.block;

import com.electrifiedded.necromancy.Necromancy;
import com.electrifiedded.necromancy.tileentity.TileEntityAltar;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class BlockAltar extends BlockContainer {
    public static final PropertyInteger META = PropertyInteger.create("meta", 0, 3);
    public static int guiID = 0;

    public BlockAltar() {
        super(Material.ROCK);
        setCreativeTab(Necromancy.tabNecromancy);
        setDefaultState(blockState.getBaseState().withProperty(META, 0));
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, META);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(META, meta & 3);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(META);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        TileEntity te = world.getTileEntity(pos);
        if (!(te instanceof TileEntityAltar tileEntity)) return false;
        if (player.isSneaking() && (tileEntity.canSpawn() || player.capabilities.isCreativeMode)) {
            tileEntity.spawn(player);
            return true;
        }
        player.openGui(Necromancy.instance, guiID, world, pos.getX(), pos.getY(), pos.getZ());
        return true;
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state) {
        TileEntity te = world.getTileEntity(pos);
        if (te instanceof TileEntityAltar altar) {
            InventoryHelper.dropInventoryItems(world, pos, altar);
        }
        super.breakBlock(world, pos, state);
    }

    @Override
    public void onPlayerDestroy(World world, BlockPos pos, IBlockState state) {
        if (world.isRemote) return;
        world.setBlockState(pos, Blocks.PLANKS.getDefaultState(), 3);
        int meta = state.getBlock() == this ? state.getValue(META) : 0;
        int dx = 0, dz = 0;
        switch (meta) {
            case 2 -> dz = -1;
            case 0 -> dz = 1;
            case 1 -> dx = -1;
            case 3 -> dx = 1;
        }
        world.setBlockState(pos.add(dx, 0, dz), Blocks.COBBLESTONE.getDefaultState(), 3);
        world.setBlockState(pos.add(dx * 2, 0, dz * 2), Blocks.COBBLESTONE.getDefaultState(), 3);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new TileEntityAltar();
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return net.minecraft.init.Items.AIR;
    }
}

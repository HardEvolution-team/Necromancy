package com.electrifiedded.necromancy.block;

import com.electrifiedded.necromancy.Necromancy;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockAltarBlock extends Block {

    public BlockAltarBlock() {
        super(Material.ROCK);
        setDefaultState(blockState.getBaseState().withProperty(BlockAltar.META, 0));
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, BlockAltar.META);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(BlockAltar.META, meta & 3);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(BlockAltar.META);
    }

    private static int[] towardsAltar(int meta) {
        return switch (meta) {
            case 2 -> new int[]{0, 1};
            case 0 -> new int[]{0, -1};
            case 1 -> new int[]{1, 0};
            default -> new int[]{-1, 0};
        };
    }

    public BlockPos getAltarPos(World world, BlockPos pos) {
        IBlockState state = world.getBlockState(pos);
        if (state.getBlock() != this) return null;
        int[] d = towardsAltar(state.getValue(BlockAltar.META));
        BlockPos next = pos.add(d[0], 0, d[1]);
        BlockPos altar = world.getBlockState(next).getBlock() == this ? pos.add(d[0] * 2, 0, d[1] * 2) : next;
        return world.getBlockState(altar).getBlock() == RegistryBlocksNecromancy.altar ? altar : null;
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        BlockPos altar = getAltarPos(world, pos);
        if (altar == null || player.isSneaking()) return false;
        player.openGui(Necromancy.instance, BlockAltar.guiID, world, altar.getX(), altar.getY(), altar.getZ());
        return true;
    }

    @Override
    public void onPlayerDestroy(World world, BlockPos pos, IBlockState state) {
        if (world.isRemote || state.getBlock() != this) return;
        int[] d = towardsAltar(state.getValue(BlockAltar.META));
        BlockPos next = pos.add(d[0], 0, d[1]);
        if (world.getBlockState(next).getBlock() == this) {
            world.setBlockToAir(next);
            world.setBlockToAir(pos.add(d[0] * 2, 0, d[1] * 2));
        } else {
            world.setBlockToAir(pos.add(-d[0], 0, -d[1]));
            world.setBlockToAir(next);
        }
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.INVISIBLE;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }
}

package com.electrifiedded.necromancy.item;

import com.electrifiedded.necromancy.Necromancy;
import com.electrifiedded.necromancy.block.BlockAltar;
import com.electrifiedded.necromancy.block.RegistryBlocksNecromancy;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemNecronomicon extends Item {
    public ItemNecronomicon() {
        super();
        setCreativeTab(Necromancy.tabNecromancy);
        setMaxStackSize(1);
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (world.getBlockState(pos).getBlock() != Blocks.PLANKS) return EnumActionResult.PASS;
        ItemStack stack = player.getHeldItem(hand);
        int[][] dirs = {{1, 0, 3}, {-1, 0, 1}, {0, 1, 0}, {0, -1, 2}};
        for (int[] d : dirs) {
            BlockPos p1 = pos.add(d[0], 0, d[1]);
            BlockPos p2 = pos.add(d[0] * 2, 0, d[1] * 2);
            if (isCobble(world, p1) && isCobble(world, p2)) {
                world.setBlockState(pos, RegistryBlocksNecromancy.altar.getDefaultState().withProperty(BlockAltar.META, d[2]), 2);
                world.setBlockState(p1, RegistryBlocksNecromancy.altarBlock.getDefaultState().withProperty(BlockAltar.META, d[2]), 2);
                world.setBlockState(p2, RegistryBlocksNecromancy.altarBlock.getDefaultState().withProperty(BlockAltar.META, d[2]), 2);
                stack.shrink(1);
                return EnumActionResult.SUCCESS;
            }
        }
        return EnumActionResult.PASS;
    }

    private static boolean isCobble(World world, BlockPos pos) {
        Block b = world.getBlockState(pos).getBlock();
        return b == Blocks.COBBLESTONE;
    }
}

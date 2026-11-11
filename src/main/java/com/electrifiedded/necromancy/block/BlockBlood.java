package com.electrifiedded.necromancy.block;

import com.electrifiedded.necromancy.Necromancy;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

public class BlockBlood extends BlockFluidClassic {

    public BlockBlood(Fluid fluid) {
        super(fluid, Material.WATER);
        setCreativeTab(Necromancy.tabNecromancy);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random rand) {
        super.randomDisplayTick(state, world, pos, rand);
        if (rand.nextInt(10) == 0
                && world.getBlockState(pos.down()).isSideSolid(world, pos.down(), EnumFacing.UP)
                && !world.getBlockState(pos.down(2)).getMaterial().blocksMovement()) {
            double px = pos.getX() + rand.nextFloat();
            double py = pos.getY() - 1.05D;
            double pz = pos.getZ() + rand.nextFloat();
            world.spawnParticle(EnumParticleTypes.DRIP_LAVA, px, py, pz, 0, 0, 0);
        }
    }
}

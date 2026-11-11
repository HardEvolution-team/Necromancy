package com.electrifiedded.necromancy.generation;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;

import java.util.Random;

public class WorldGenerator implements IWorldGenerator {

    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
        if (world.provider.getDimension() != -1) return;
        int x = chunkX * 16 + 8 + random.nextInt(16);
        int y = random.nextInt(60);
        int z = chunkZ * 16 + 8 + random.nextInt(16);
        new WorldGenNetherChalice().generate(world, random, new BlockPos(x, y, z));
    }
}

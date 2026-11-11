package com.electrifiedded.necromancy.generation.villagecomponent;

import com.electrifiedded.necromancy.Necromancy;
import com.electrifiedded.necromancy.api.NecroEntityBase;
import com.electrifiedded.necromancy.api.NecroEntityRegistry;
import com.electrifiedded.necromancy.entity.RegistryNecromancyEntities;
import net.minecraft.block.BlockStairs;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureVillagePieces;
import net.minecraftforge.fml.common.registry.VillagerRegistry;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ComponentVillageCemetery extends StructureVillagePieces.Village {
    private int averageGroundLevel = -1;

    public ComponentVillageCemetery() {
    }

    public ComponentVillageCemetery(StructureVillagePieces.Start start, int type, Random rand, StructureBoundingBox sbox, EnumFacing facing) {
        super(start, type);
        setCoordBaseMode(facing);
        boundingBox = sbox;
    }

    public static ComponentVillageCemetery build(StructureVillagePieces.Start startPiece, List<StructureComponent> pieces, Random random, int x, int y, int z, EnumFacing facing, int type) {
        StructureBoundingBox sbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, 17, 5, 18, facing);
        return canVillageGoDeeper(sbox) && StructureComponent.findIntersecting(pieces, sbox) == null
                ? new ComponentVillageCemetery(startPiece, type, random, sbox, facing) : null;
    }

    @Override
    public boolean addComponentParts(World world, Random rand, StructureBoundingBox sbb) {
        if (averageGroundLevel < 0) {
            averageGroundLevel = getAverageGroundLevel(world, sbb);
            if (averageGroundLevel < 0) return true;
            boundingBox.offset(0, averageGroundLevel - boundingBox.minY - 1, 0);
        }
        Necromancy.loggerNecromancy.debug("Necromod generating structure, at: " + boundingBox);
        fillWithBlocks(world, sbb, 0, 0, 0, 17, 5, 18, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
        fillWithBlocks(world, sbb, 0, 0, 0, 17, 0, 18, Blocks.GRASS.getDefaultState(), Blocks.GRASS.getDefaultState(), false);
        fillWithBlocks(world, sbb, 0, 1, 0, 17, 1, 0, Blocks.COBBLESTONE_WALL.getDefaultState(), Blocks.COBBLESTONE.getDefaultState(), false);
        fillWithBlocks(world, sbb, 0, 1, 17, 16, 1, 18, Blocks.COBBLESTONE_WALL.getDefaultState(), Blocks.COBBLESTONE.getDefaultState(), false);
        fillWithBlocks(world, sbb, 0, 1, 0, 0, 1, 17, Blocks.COBBLESTONE_WALL.getDefaultState(), Blocks.COBBLESTONE.getDefaultState(), false);
        fillWithBlocks(world, sbb, 17, 1, 0, 17, 1, 18, Blocks.COBBLESTONE_WALL.getDefaultState(), Blocks.COBBLESTONE.getDefaultState(), false);
        for (int i = 0; i < 4; i++)
        {
            setBlockState(world, Blocks.COBBLESTONE.getDefaultState(), 3, 1, 2 + i * 2, sbb);
            if (rand.nextInt(10) == 1)
            {
                generateBodypartChest(world, sbb, rand, 3, 0, 2 + i * 2);
            }
            setBlockState(world, Blocks.SOUL_SAND.getDefaultState(), 4, 0, 2 + i * 2, sbb);
            setBlockState(world, Blocks.SOUL_SAND.getDefaultState(), 5, 0, 2 + i * 2, sbb);
        }
        for (int i = 0; i < 4; i++)
        {
            setBlockState(world, Blocks.SOUL_SAND.getDefaultState(), 11, 0, 2 + i * 2, sbb);
            setBlockState(world, Blocks.SOUL_SAND.getDefaultState(), 12, 0, 2 + i * 2, sbb);
            setBlockState(world, Blocks.COBBLESTONE.getDefaultState(), 13, 1, 2 + i * 2, sbb);
            if (rand.nextInt(10) == 1)
            {
                generateBodypartChest(world, sbb, rand, 13, 0, 2 + i * 2);
            }
        }
        fillWithBlocks(world, sbb, 6, 1, 0, 10, 1, 0, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
        fillWithBlocks(world, sbb, 5, 1, 0, 5, 3, 0, Blocks.COBBLESTONE.getDefaultState(), Blocks.COBBLESTONE.getDefaultState(), false);
        fillWithBlocks(world, sbb, 6, 3, 0, 6, 4, 0, Blocks.COBBLESTONE.getDefaultState(), Blocks.COBBLESTONE.getDefaultState(), false);
        fillWithBlocks(world, sbb, 7, 4, 0, 7, 5, 0, Blocks.COBBLESTONE.getDefaultState(), Blocks.COBBLESTONE.getDefaultState(), false);
        setBlockState(world, Blocks.COBBLESTONE.getDefaultState(), 8, 5, 0, sbb);
        fillWithBlocks(world, sbb, 9, 4, 0, 9, 5, 0, Blocks.COBBLESTONE.getDefaultState(), Blocks.COBBLESTONE.getDefaultState(), false);
        fillWithBlocks(world, sbb, 10, 3, 0, 10, 4, 0, Blocks.COBBLESTONE.getDefaultState(), Blocks.COBBLESTONE.getDefaultState(), false);
        fillWithBlocks(world, sbb, 11, 1, 0, 11, 3, 0, Blocks.COBBLESTONE.getDefaultState(), Blocks.COBBLESTONE.getDefaultState(), false);
        fillWithBlocks(world, sbb, 7, 0, 0, 9, 0, 14, Blocks.GRAVEL.getDefaultState(), Blocks.GRAVEL.getDefaultState(), false);
        fillWithBlocks(world, sbb, 3, 1, 11, 5, 3, 11, Blocks.COBBLESTONE.getDefaultState(), Blocks.COBBLESTONE.getDefaultState(), false);
        fillWithBlocks(world, sbb, 3, 1, 15, 5, 3, 15, Blocks.COBBLESTONE.getDefaultState(), Blocks.COBBLESTONE.getDefaultState(), false);
        fillWithBlocks(world, sbb, 2, 1, 12, 2, 3, 14, Blocks.COBBLESTONE.getDefaultState(), Blocks.COBBLESTONE.getDefaultState(), false);
        fillWithBlocks(world, sbb, 6, 1, 12, 6, 3, 14, Blocks.COBBLESTONE.getDefaultState(), Blocks.COBBLESTONE.getDefaultState(), false);
        fillWithBlocks(world, sbb, 3, 0, 12, 5, 0, 14, Blocks.COBBLESTONE.getDefaultState(), Blocks.COBBLESTONE.getDefaultState(), false);
        fillWithBlocks(world, sbb, 3, 4, 12, 5, 4, 14, Blocks.COBBLESTONE.getDefaultState(), Blocks.COBBLESTONE.getDefaultState(), false);
        setBlockState(world, Blocks.COBBLESTONE.getDefaultState(), 3, 1, 13, sbb);
        for (int i = 0; i < 3; i++)
        {
            setBlockState(world, Blocks.STONE_BRICK_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.NORTH), 3 + i, 4, 11, sbb);
        }
        for (int i = 0; i < 3; i++)
        {
            setBlockState(world, Blocks.STONE_BRICK_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.SOUTH), 3 + i, 4, 15, sbb);
        }
        for (int i = 0; i < 3; i++)
        {
            setBlockState(world, Blocks.STONE_BRICK_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.EAST), 2, 4, 12 + i, sbb);
        }
        for (int i = 0; i < 3; i++)
        {
            setBlockState(world, Blocks.STONE_BRICK_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.WEST), 6, 4, 12 + i, sbb);
        }
        setBlockState(world, Blocks.COBBLESTONE.getDefaultState(), 6, 0, 13, sbb);
        createVillageDoor(world, sbb, rand, 6, 1, 13, EnumFacing.EAST);
        fillWithBlocks(world, sbb, 10, 0, 11, 14, 0, 15, Blocks.COBBLESTONE.getDefaultState(), Blocks.COBBLESTONE.getDefaultState(), false);
        fillWithBlocks(world, sbb, 10, 1, 11, 14, 3, 11, Blocks.PLANKS.getDefaultState(), Blocks.PLANKS.getDefaultState(), false);
        fillWithBlocks(world, sbb, 10, 1, 15, 14, 3, 15, Blocks.PLANKS.getDefaultState(), Blocks.PLANKS.getDefaultState(), false);
        fillWithBlocks(world, sbb, 10, 1, 11, 10, 3, 15, Blocks.PLANKS.getDefaultState(), Blocks.PLANKS.getDefaultState(), false);
        fillWithBlocks(world, sbb, 14, 1, 11, 14, 3, 15, Blocks.PLANKS.getDefaultState(), Blocks.PLANKS.getDefaultState(), false);
        fillWithBlocks(world, sbb, 10, 4, 11, 14, 4, 15, Blocks.LOG.getDefaultState(), Blocks.LOG.getDefaultState(), false);
        fillWithBlocks(world, sbb, 11, 4, 12, 13, 4, 14, Blocks.PLANKS.getDefaultState(), Blocks.PLANKS.getDefaultState(), false);
        fillWithBlocks(world, sbb, 10, 1, 11, 10, 3, 11, Blocks.COBBLESTONE.getDefaultState(), Blocks.COBBLESTONE.getDefaultState(), false);
        fillWithBlocks(world, sbb, 14, 1, 11, 14, 3, 11, Blocks.COBBLESTONE.getDefaultState(), Blocks.COBBLESTONE.getDefaultState(), false);
        fillWithBlocks(world, sbb, 10, 1, 15, 10, 3, 15, Blocks.COBBLESTONE.getDefaultState(), Blocks.COBBLESTONE.getDefaultState(), false);
        fillWithBlocks(world, sbb, 14, 1, 15, 14, 3, 15, Blocks.COBBLESTONE.getDefaultState(), Blocks.COBBLESTONE.getDefaultState(), false);
        createVillageDoor(world, sbb, rand, 10, 1, 13, EnumFacing.WEST);
        generateBodypartChest(world, sbb, rand, 3, 1, 12);
        generateBodypartChest(world, sbb, rand, 3, 1, 14);
        spawnVillagers(world, sbb, 11, 3, 12, 1);
        return true;
    }

    private void generateBodypartChest(World world, StructureBoundingBox sbb, Random rand, int x, int y, int z) {
        List<NecroEntityBase> mobs = new ArrayList<>(NecroEntityRegistry.registeredEntities.values());
        if (mobs.isEmpty()) return;
        ItemStack headItem, torsoItem, armLeftItem, armRightItem, legItem;
        while (true) {
            headItem = mobs.get(rand.nextInt(mobs.size())).headItem;
            torsoItem = mobs.get(rand.nextInt(mobs.size())).torsoItem;
            armLeftItem = mobs.get(rand.nextInt(mobs.size())).armItem;
            armRightItem = mobs.get(rand.nextInt(mobs.size())).armItem;
            legItem = mobs.get(rand.nextInt(mobs.size())).legItem;
            if (ok(headItem) && ok(torsoItem) && ok(armLeftItem) && ok(armRightItem) && ok(legItem)) break;
        }
        generateChest(world, sbb, x, y, z, 12, headItem, 13, torsoItem, 14, legItem, 4, armRightItem, 22, armLeftItem);
    }

    private static boolean ok(ItemStack stack) {
        return stack != null && !stack.isEmpty();
    }

    private void generateChest(World world, StructureBoundingBox sbb, int x, int y, int z, Object... content) {
        BlockPos pos = new BlockPos(getXWithOffset(x, z), getYWithOffset(y), getZWithOffset(x, z));
        if (sbb.isVecInside(pos) && world.getBlockState(pos).getBlock() != Blocks.CHEST) {
            world.setBlockState(pos, Blocks.CHEST.getDefaultState(), 2);
            TileEntity te = world.getTileEntity(pos);
            if (te instanceof TileEntityChest chest) {
                for (int i = 0; i + 1 < content.length; i++) {
                    if (content[i] instanceof Integer slot && content[i + 1] instanceof ItemStack stack) {
                        chest.setInventorySlotContents(slot, stack.copy());
                    }
                }
            }
        }
    }

    @Override
    protected VillagerRegistry.VillagerProfession chooseForgeProfession(int count, VillagerRegistry.VillagerProfession prof) {
        return RegistryNecromancyEntities.necromancerProfession;
    }
}

package com.electrifiedded.necromancy.block;

import com.electrifiedded.archaiclib.legacy.registry.LegacyGameRegistry;
import com.electrifiedded.archaiclib.legacy.registry.LegacyRecipes;
import com.electrifiedded.necromancy.item.ItemGeneric;
import com.electrifiedded.necromancy.lib.ReferenceNecromancy;
import com.electrifiedded.necromancy.tileentity.TileEntityAltar;
import com.electrifiedded.necromancy.tileentity.TileEntitySewing;
import com.electrifiedded.necromancy.tileentity.TileEntitySkullWall;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class RegistryBlocksNecromancy {
    public static Block altar;
    public static Block altarBlock;
    public static Block sewing;
    public static Block blood;
    public static Block skullWall;
    public static Fluid fluidBlood;

    public static void initBlocks() {
        altar = new BlockAltar().setHardness(4);
        altar.setTranslationKey("Summoning Altar");
        LegacyGameRegistry.registerBlock(altar, "summoning_altar");
        GameRegistry.registerTileEntity(TileEntityAltar.class, new ResourceLocation(ReferenceNecromancy.MOD_ID, "summoning_altar"));

        altarBlock = new BlockAltarBlock().setHardness(4);
        altarBlock.setTranslationKey("Altar Building Block");
        LegacyGameRegistry.registerBlock(altarBlock, "altar_building_block");

        sewing = new BlockSewing(Material.IRON).setHardness(4);
        sewing.setTranslationKey("Sewing Machine");
        LegacyGameRegistry.registerBlock(sewing, "sewing_machine");
        GameRegistry.registerTileEntity(TileEntitySewing.class, new ResourceLocation(ReferenceNecromancy.MOD_ID, "sewing"));

        fluidBlood = FluidRegistry.getFluid("blood");
        if (fluidBlood == null) {
            fluidBlood = new Fluid("blood", new ResourceLocation("minecraft", "blocks/water_still"), new ResourceLocation("minecraft", "blocks/water_flow"))
                    .setColor(0xFFD90000);
            FluidRegistry.registerFluid(fluidBlood);
        }
        if (fluidBlood.getBlock() == null) {
            blood = new BlockBlood(fluidBlood);
            blood.setTranslationKey("FlowingBlood");
            fluidBlood.setBlock(blood);
            LegacyGameRegistry.registerBlock(blood, "flowing_blood");
        } else {
            blood = fluidBlood.getBlock();
        }

        skullWall = new BlockSkullWall();
        LegacyGameRegistry.registerBlock(skullWall, "skull_wall");
        GameRegistry.registerTileEntity(TileEntitySkullWall.class, new ResourceLocation(ReferenceNecromancy.MOD_ID, "skull_wall"));
    }

    public static void initRecipes() {
        LegacyRecipes.addRecipe(new ItemStack(sewing, 1), "III", "ISB", "III", 'I', Items.IRON_INGOT, 'S', Items.STRING, 'B',
                ItemGeneric.getItemStackFromName("Bone Needle"));
    }
}

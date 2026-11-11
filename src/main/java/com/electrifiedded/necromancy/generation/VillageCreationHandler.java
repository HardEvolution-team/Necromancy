package com.electrifiedded.necromancy.generation;

import com.electrifiedded.necromancy.generation.villagecomponent.ComponentVillageCemetery;
import com.electrifiedded.necromancy.item.ItemBodyPart;
import com.electrifiedded.necromancy.item.RegistryNecromancyItems;
import net.minecraft.entity.IMerchant;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureVillagePieces;
import net.minecraftforge.fml.common.registry.VillagerRegistry;

import java.util.List;
import java.util.Random;

public class VillageCreationHandler implements VillagerRegistry.IVillageCreationHandler {

    @Override
    public StructureVillagePieces.PieceWeight getVillagePieceWeight(Random random, int i) {
        return new StructureVillagePieces.PieceWeight(ComponentVillageCemetery.class, 5, 1);
    }

    @Override
    public Class<?> getComponentClass() {
        return ComponentVillageCemetery.class;
    }

    @Override
    public StructureVillagePieces.Village buildComponent(StructureVillagePieces.PieceWeight villagePiece, StructureVillagePieces.Start startPiece,
                                                         List<StructureComponent> pieces, Random random, int p1, int p2, int p3, EnumFacing facing, int p5) {
        return ComponentVillageCemetery.build(startPiece, pieces, random, p1, p2, p3, facing, p5);
    }

    public static class NecromancerTrades implements EntityVillager.ITradeList {
        @Override
        public void addMerchantRecipe(IMerchant merchant, MerchantRecipeList recipeList, Random random) {
            int parts = ItemBodyPart.necroEntities.size() - 1;
            recipeList.add(new MerchantRecipe(new ItemStack(Items.EMERALD, 6), new ItemStack(Items.BOOK), new ItemStack(RegistryNecromancyItems.necronomicon)));
            recipeList.add(new MerchantRecipe(new ItemStack(Items.EMERALD, 1 + random.nextInt(2)), ItemStack.EMPTY,
                    new ItemStack(RegistryNecromancyItems.bodyparts, 1, random.nextInt(parts))));
            recipeList.add(new MerchantRecipe(new ItemStack(RegistryNecromancyItems.bodyparts, 1, random.nextInt(parts)), ItemStack.EMPTY,
                    new ItemStack(Items.EMERALD, 1 + random.nextInt(2))));
            recipeList.add(new MerchantRecipe(new ItemStack(RegistryNecromancyItems.bodyparts, 1, random.nextInt(parts)), ItemStack.EMPTY,
                    new ItemStack(Items.EMERALD, 1 + random.nextInt(2))));
        }
    }
}

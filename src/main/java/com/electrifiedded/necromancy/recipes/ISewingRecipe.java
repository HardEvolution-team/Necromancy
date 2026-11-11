package com.electrifiedded.necromancy.recipes;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public interface ISewingRecipe {
    boolean matches(InventoryCrafting inv, World world);

    ItemStack getCraftingResult(InventoryCrafting inv);

    ItemStack getRecipeOutput();
}

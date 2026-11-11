package com.electrifiedded.necromancy.recipes;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ShapelessRecipes4x4 implements ISewingRecipe {
    private final ItemStack recipeOutput;
    private final List<ItemStack> recipeItems;

    public ShapelessRecipes4x4(ItemStack output, List<ItemStack> items) {
        recipeOutput = output;
        recipeItems = items;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return recipeOutput;
    }

    public List<ItemStack> getItems() {
        return recipeItems;
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting inv) {
        return recipeOutput.copy();
    }

    @Override
    public boolean matches(InventoryCrafting inv, World world) {
        List<ItemStack> remaining = new ArrayList<>(recipeItems);
        for (int column = 0; column < 4; column++) {
            for (int row = 0; row < 4; row++) {
                ItemStack stack = inv.getStackInRowAndColumn(row, column);
                if (!stack.isEmpty()) {
                    for (Iterator<ItemStack> it = remaining.iterator(); it.hasNext(); ) {
                        if (stack.isItemEqual(it.next())) {
                            it.remove();
                        }
                    }
                }
            }
        }
        return remaining.isEmpty();
    }
}

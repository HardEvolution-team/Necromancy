package com.electrifiedded.necromancy.recipes;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ShapedRecipes4x4 implements ISewingRecipe {
    private final int recipeWidth;
    private final int recipeHeight;
    private final ItemStack[] recipeItems;
    private final ItemStack recipeOutput;

    public ShapedRecipes4x4(int width, int height, ItemStack[] items, ItemStack output) {
        recipeWidth = width;
        recipeHeight = height;
        recipeItems = items;
        recipeOutput = output;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return recipeOutput;
    }

    public int getWidth() {
        return recipeWidth;
    }

    public int getHeight() {
        return recipeHeight;
    }

    public ItemStack[] getItems() {
        return recipeItems;
    }

    @Override
    public boolean matches(InventoryCrafting inv, World world) {
        for (int i = 0; i <= 4 - recipeWidth; i++) {
            for (int j = 0; j <= 4 - recipeHeight; j++) {
                if (matches(inv, i, j, true)) return true;
                if (matches(inv, i, j, false)) return true;
            }
        }
        return false;
    }

    private boolean matches(InventoryCrafting inv, int i, int j, boolean mirrored) {
        for (int row = 0; row < 4; row++) {
            for (int column = 0; column < 4; column++) {
                int columnI = row - i;
                int rowI = column - j;
                ItemStack expected = ItemStack.EMPTY;
                if (columnI >= 0 && rowI >= 0 && columnI < recipeWidth && rowI < recipeHeight) {
                    ItemStack s = mirrored ? recipeItems[recipeWidth - columnI - 1 + rowI * recipeWidth] : recipeItems[columnI + rowI * recipeWidth];
                    if (s != null) expected = s;
                }
                ItemStack actual = inv.getStackInRowAndColumn(row, column);
                if (actual.isEmpty() && expected.isEmpty()) continue;
                if (actual.isEmpty() || expected.isEmpty()) return false;
                if (!expected.isItemEqual(actual)) return false;
            }
        }
        return true;
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting inv) {
        return recipeOutput.copy();
    }
}

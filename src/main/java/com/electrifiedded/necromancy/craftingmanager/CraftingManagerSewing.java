package com.electrifiedded.necromancy.craftingmanager;

import com.electrifiedded.necromancy.api.NecroEntityBase;
import com.electrifiedded.necromancy.api.NecroEntityRegistry;
import com.electrifiedded.necromancy.entity.RegistryNecromancyEntities;
import com.electrifiedded.necromancy.item.ItemGeneric;
import com.electrifiedded.necromancy.item.RegistryNecromancyItems;
import com.electrifiedded.necromancy.recipes.ISewingRecipe;
import com.electrifiedded.necromancy.recipes.ShapedRecipes4x4;
import com.electrifiedded.necromancy.recipes.ShapelessRecipes4x4;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemMonsterPlacer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CraftingManagerSewing {
    private final List<ISewingRecipe> recipes = new ArrayList<>();

    public CraftingManagerSewing() {
        for (NecroEntityBase mob : NecroEntityRegistry.registeredEntities.values()) {
            if (mob.hasHead && mob.headRecipe != null) addRecipe(mob.headItem, mob.headRecipe);
            if (mob.hasTorso && mob.torsoRecipe != null) addRecipe(mob.torsoItem, mob.torsoRecipe);
            if (mob.hasArms && mob.armRecipe != null) addRecipe(mob.armItem, mob.armRecipe);
            if (mob.hasLegs && mob.legRecipe != null) addRecipe(mob.legItem, mob.legRecipe);
        }
        addShapelessRecipe(new ItemStack(RegistryNecromancyItems.organs, 8, 4), Items.LEATHER);
        addShapelessRecipe(new ItemStack(RegistryNecromancyItems.spawner, 1), Items.ROTTEN_FLESH, Items.ROTTEN_FLESH, Items.ROTTEN_FLESH,
                Items.ROTTEN_FLESH, Items.ROTTEN_FLESH, Items.GHAST_TEAR, Items.GHAST_TEAR,
                ItemGeneric.getItemStackFromName("Soul in a Jar").getItem(), new ItemStack(RegistryNecromancyItems.organs, 1, 1).getItem());
        ItemStack teddyEgg = new ItemStack(Items.SPAWN_EGG, 1);
        ItemMonsterPlacer.applyEntityIdToItemStack(teddyEgg, RegistryNecromancyEntities.TEDDY);
        addRecipe(teddyEgg, new Object[]{"LLLL", "LWWL", "LWWL", "LLLL", 'L', Items.LEATHER, 'W', Blocks.WOOL});
    }

    public List<ISewingRecipe> getRecipes() {
        return recipes;
    }

    private void addShapelessRecipe(ItemStack output, Object... ingredients) {
        List<ItemStack> stackList = new ArrayList<>();
        for (Object o : ingredients) {
            if (o instanceof ItemStack s) {
                stackList.add(s.copy());
            } else if (o instanceof Item i) {
                stackList.add(new ItemStack(i));
            } else if (o instanceof Block b) {
                stackList.add(new ItemStack(b));
            } else {
                throw new RuntimeException("Necromancy: Invalid shapeless recipe, offending object: " + o);
            }
        }
        recipes.add(new ShapelessRecipes4x4(output, stackList));
    }

    private void addRecipe(ItemStack output, Object[] params) {
        if (output == null || output.isEmpty()) return;
        StringBuilder s = new StringBuilder();
        int index = 0;
        int length = 0;
        int count = 0;
        if (params[index] instanceof String[] rows) {
            index++;
            for (String row : rows) {
                count++;
                length = row.length();
                s.append(row);
            }
        } else {
            while (params[index] instanceof String row) {
                index++;
                count++;
                length = row.length();
                s.append(row);
            }
        }
        Map<Character, ItemStack> map = new HashMap<>();
        for (; index < params.length; index += 2) {
            Character c = (Character) params[index];
            Object o = params[index + 1];
            ItemStack stack = ItemStack.EMPTY;
            if (o instanceof Item i) {
                stack = new ItemStack(i);
            } else if (o instanceof Block b) {
                stack = new ItemStack(b, 1, 0);
            } else if (o instanceof ItemStack is) {
                stack = is;
            }
            map.put(c, stack);
        }
        ItemStack[] items = new ItemStack[length * count];
        for (int i = 0; i < length * count; i++) {
            char c = s.charAt(i);
            ItemStack st = map.get(c);
            items[i] = st != null ? st.copy() : ItemStack.EMPTY;
        }
        recipes.add(new ShapedRecipes4x4(length, count, items, output));
    }

    public ItemStack findMatchingRecipe(InventoryCrafting inv, World world) {
        int i = 0;
        ItemStack first = ItemStack.EMPTY;
        ItemStack second = ItemStack.EMPTY;
        for (int j = 0; j < inv.getSizeInventory(); j++) {
            ItemStack stack = inv.getStackInSlot(j);
            if (stack.isEmpty()) continue;
            if (i == 0) first = stack;
            if (i == 1) second = stack;
            i++;
        }
        if (i == 2 && first.getItem() == second.getItem() && first.getCount() == 1 && second.getCount() == 1 && first.getItem().isDamageable()) {
            Item item = first.getItem();
            int l = item.getMaxDamage() - first.getItemDamage();
            int i1 = item.getMaxDamage() - second.getItemDamage();
            int j1 = l + i1 + item.getMaxDamage() * 10 / 100;
            int k1 = Math.max(0, item.getMaxDamage() - j1);
            return new ItemStack(item, 1, k1);
        }
        for (ISewingRecipe recipe : recipes) {
            if (recipe.matches(inv, world)) return recipe.getCraftingResult(inv);
        }
        return ItemStack.EMPTY;
    }
}

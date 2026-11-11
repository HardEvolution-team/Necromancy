package com.electrifiedded.necromancy.creativetab;

import com.electrifiedded.necromancy.item.RegistryNecromancyItems;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public final class CreativeTabNecro extends CreativeTabs {
    private final int display;

    public CreativeTabNecro(int index, String label, int item) {
        super(index, label);
        display = item;
    }

    @Override
    public ItemStack createIcon() {
        if (display == 1) return new ItemStack(RegistryNecromancyItems.necronomicon);
        return new ItemStack(Items.SKULL);
    }
}

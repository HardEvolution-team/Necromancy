package com.electrifiedded.necromancy.item;

import com.electrifiedded.necromancy.Necromancy;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.translation.I18n;

@SuppressWarnings("deprecation")
public class ItemOrgans extends ItemFood {
    public static final String[] names = {"Brains", "Heart", "Muscle", "Lungs", "Skin"};

    public ItemOrgans() {
        super(2, true);
        setPotionEffect(new PotionEffect(MobEffects.HUNGER, 30 * 20, 0), 0.8F);
        setHasSubtypes(true);
        setMaxDamage(0);
        setCreativeTab(Necromancy.tabNecromancy);
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        if (stack.getMetadata() > names.length) return "Invalid Item, please destroy";
        return I18n.translateToLocal("NecromancyorganItem" + stack.getMetadata());
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        if (!isInCreativeTab(tab)) return;
        for (int i = 0; i < names.length; i++) {
            items.add(new ItemStack(this, 1, i));
        }
    }
}

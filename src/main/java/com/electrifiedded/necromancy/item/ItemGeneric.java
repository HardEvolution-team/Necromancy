package com.electrifiedded.necromancy.item;

import com.electrifiedded.archaiclib.legacy.entity.LegacyInventory;
import com.electrifiedded.necromancy.Necromancy;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

@SuppressWarnings("deprecation")
public class ItemGeneric extends Item {
    public static final String[] names = {"Bone Needle", "Soul in a Jar", "Jar of Blood", "Brain on a Stick"};

    public ItemGeneric() {
        super();
        setHasSubtypes(true);
        setMaxDamage(0);
        setCreativeTab(Necromancy.tabNecromancy);
    }

    @Override
    public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity) {
        if (stack.getMetadata() == 0 && LegacyInventory.consumeInventoryItem(player.inventory, Items.GLASS_BOTTLE)) {
            stack.shrink(1);
            ItemStack blood = new ItemStack(RegistryNecromancyItems.genericItems, 1, 2);
            if (!player.inventory.addItemStackToInventory(blood)) {
                player.entityDropItem(blood, 0f);
            }
        }
        return false;
    }

    @Override
    public boolean isFull3D() {
        return true;
    }

    @Override
    public boolean shouldRotateAroundWhenRendering() {
        return true;
    }

    @Override
    public void onUpdate(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (stack.getMetadata() > names.length) {
            stack.setItemDamage(2);
        }
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        return I18n.translateToLocal("NecromancygenericItem" + stack.getMetadata());
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        if (!isInCreativeTab(tab)) return;
        for (int i = 0; i < names.length; i++) {
            items.add(new ItemStack(this, 1, i));
        }
    }

    public static String texturePath(int meta) {
        return names[meta].replace(" ", "").toLowerCase();
    }

    public static ItemStack getItemStackFromName(String name) {
        return getItemStackFromName(name, 1);
    }

    public static ItemStack getItemStackFromName(String name, int amount) {
        for (int i = 0; i < names.length; i++) {
            if (names[i].equalsIgnoreCase(name)) return new ItemStack(RegistryNecromancyItems.genericItems, amount, i);
        }
        return ItemStack.EMPTY;
    }

    public static boolean is(ItemStack stack, String name) {
        ItemStack ref = getItemStackFromName(name);
        return stack != null && !stack.isEmpty() && stack.getItem() == ref.getItem() && stack.getMetadata() == ref.getMetadata();
    }
}

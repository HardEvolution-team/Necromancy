package com.electrifiedded.necromancy.item;

import com.electrifiedded.necromancy.Necromancy;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.translation.I18n;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@SuppressWarnings("deprecation")
public class ItemBodyPart extends Item {
    public static List<String> necroEntities = new ArrayList<>();

    public ItemBodyPart() {
        super();
        setHasSubtypes(true);
        setMaxDamage(0);
        setCreativeTab(Necromancy.tabNecromancyBodyParts);
        necroEntities.clear();
        addParts("Cow", "Torso", "Head", "Arm", "Legs");
        addParts("Creeper", "Torso", "Legs");
        addParts("Enderman", "Head", "Torso", "Arm", "Legs");
        addParts("Pig", "Head", "Torso", "Arm", "Legs");
        addParts("Pigzombie", "Head", "Torso", "Arm", "Legs");
        addParts("Skeleton", "Torso", "Arm", "Legs");
        addParts("Spider", "Head", "Torso", "Legs");
        addParts("Zombie", "Torso", "Arm", "Legs");
        addParts("Chicken", "Head", "Torso", "Arm", "Legs");
        addParts("Villager", "Head", "Torso", "Arm", "Legs");
        addParts("Witch", "Head", "Torso", "Arm", "Legs");
        addParts("Squid", "Head", "Torso", "Legs");
        addParts("CaveSpider", "Head", "Torso", "Legs");
        addParts("Sheep", "Head", "Torso", "Arm", "Legs");
        addParts("IronGolem", "Head", "Torso", "Arm", "Legs");
        addParts("Wolf", "Head");
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        int meta = stack.getMetadata();
        if (meta < 0 || meta >= necroEntities.size()) return super.getItemStackDisplayName(stack);
        return I18n.translateToLocal(necroEntities.get(meta));
    }

    public void addParts(String entity, String... parts) {
        for (String part : parts) {
            necroEntities.add(entity + " " + part);
        }
    }

    public static String texturePath(int meta) {
        String path = necroEntities.get(meta).replaceAll(" ", "/").replaceAll("_", " ");
        return "bodyparts/" + path.toLowerCase(Locale.ROOT);
    }

    public static ItemStack getItemStackFromName(String name, int amount) {
        int i = necroEntities.indexOf(name);
        if (i >= 0) return new ItemStack(RegistryNecromancyItems.bodyparts, amount, i);
        Necromancy.loggerNecromancy.error("Necromancy major bug with bodyparts, " + name + " did not return a valid ItemStack");
        return ItemStack.EMPTY;
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        if (!isInCreativeTab(tab)) return;
        for (int i = 0; i < necroEntities.size(); i++) {
            items.add(new ItemStack(this, 1, i));
        }
    }
}

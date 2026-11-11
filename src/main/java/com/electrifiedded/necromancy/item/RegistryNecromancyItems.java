package com.electrifiedded.necromancy.item;

import com.electrifiedded.archaiclib.legacy.registry.LegacyGameRegistry;
import com.electrifiedded.archaiclib.legacy.registry.LegacyRecipes;
import com.electrifiedded.archaiclib.v1710.api.item.ArchaicItem;
import com.electrifiedded.necromancy.block.RegistryBlocksNecromancy;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.EnumHelper;

public class RegistryNecromancyItems {
    public static Item genericItems;
    public static Item necronomicon;
    public static Item scythe;
    public static Item scytheBone;
    public static Item bucketBlood;
    public static Item organs;
    public static Item bodyparts;
    public static Item spawner;
    public static Item isaacsHead;
    public static ArmorMaterial isaac = EnumHelper.addArmorMaterial("NECROMANCY_ISAAC", "necromancy:isaac", 0, new int[]{0, 0, 0, 0}, 0,
            SoundEvents.ITEM_ARMOR_EQUIP_GENERIC, 0F);

    public static void initItems() {
        genericItems = new ItemGeneric().setTranslationKey("ItemNecromancy");
        LegacyGameRegistry.registerItem(genericItems, "ItemNecromancy");
        for (int i = 0; i < ItemGeneric.names.length; i++) {
            ArchaicItem.registerMetaTexture(genericItems, i, ItemGeneric.texturePath(i));
        }

        necronomicon = new ItemNecronomicon().setTranslationKey("Necronomicon");
        LegacyGameRegistry.registerItem(necronomicon, "Necronomicon");

        scythe = new ItemScythe(ItemScythe.toolScythe).setTranslationKey("ItemScythe");
        LegacyGameRegistry.registerItem(scythe, "ItemScythe");

        scytheBone = new ItemScythe(ItemScythe.toolScytheBone).setTranslationKey("ItemScytheBone");
        LegacyGameRegistry.registerItem(scytheBone, "ItemScytheBone");

        bucketBlood = new ItemBucketBlood(RegistryBlocksNecromancy.blood).setTranslationKey("BucketBlood");
        LegacyGameRegistry.registerItem(bucketBlood, "BucketBlood");
        ArchaicItem.registerMetaTexture(bucketBlood, 0, "bucketblood");

        organs = new ItemOrgans().setTranslationKey("Organs");
        LegacyGameRegistry.registerItem(organs, "Organs");
        for (int i = 0; i < ItemOrgans.names.length; i++) {
            ArchaicItem.registerMetaTexture(organs, i, ItemOrgans.names[i].toLowerCase());
        }

        bodyparts = new ItemBodyPart().setTranslationKey("BodyParts");
        LegacyGameRegistry.registerItem(bodyparts, "BodyParts");
        for (int i = 0; i < ItemBodyPart.necroEntities.size(); i++) {
            ArchaicItem.registerMetaTexture(bodyparts, i, ItemBodyPart.texturePath(i));
        }

        isaacsHead = new ItemIsaacsHead(isaac, 0, EntityEquipmentSlot.HEAD);
        LegacyGameRegistry.registerItem(isaacsHead, "ItemIsaacsHead");
        ArchaicItem.registerMetaTexture(isaacsHead, 0, "isaacsseveredhead");

        spawner = new ItemSpawner().setTranslationKey("NecroSpawner");
        LegacyGameRegistry.registerItem(spawner, "NecroSpawner");
        ArchaicItem.registerMetaTexture(spawner, 0, "soulheart");
    }

    public static void initRecipes() {
        LegacyRecipes.addRecipe(new ItemStack(necronomicon, 1), "LSL", "IBF", "LNL", 'B', Items.BOOK, 'L', Items.LEATHER, 'S',
                ItemGeneric.getItemStackFromName("Jar of Blood"), 'I', new ItemStack(Items.DYE, 1, 0), 'F', Items.FEATHER, 'N', Items.NETHER_WART);
        LegacyRecipes.addRecipe(ItemGeneric.getItemStackFromName("Bone Needle"), "X", 'X', new ItemStack(Items.DYE, 1, 15));
        LegacyRecipes.addRecipe(new ItemStack(scythe, 1), "IH", " S", " B", 'I', Blocks.OBSIDIAN, 'H', Items.IRON_HOE, 'S', Items.STICK, 'B',
                ItemGeneric.getItemStackFromName("Jar of Blood"));
        LegacyRecipes.addRecipe(new ItemStack(scytheBone, 1), "IH", " S", " B", 'I', Blocks.OBSIDIAN, 'H', scythe, 'S', Items.BONE, 'B', Items.DIAMOND);
        LegacyRecipes.addRecipe(ItemGeneric.getItemStackFromName("Brain on a Stick"), "# ", " X", '#', Items.FISHING_ROD, 'X', new ItemStack(organs, 1, 0));
        LegacyRecipes.addShapelessRecipe(ItemGeneric.getItemStackFromName("Jar of Blood", 8), new ItemStack(bucketBlood), Items.GLASS_BOTTLE,
                Items.GLASS_BOTTLE, Items.GLASS_BOTTLE, Items.GLASS_BOTTLE, Items.GLASS_BOTTLE, Items.GLASS_BOTTLE, Items.GLASS_BOTTLE, Items.GLASS_BOTTLE);
        ItemStack jar = ItemGeneric.getItemStackFromName("Jar of Blood");
        LegacyRecipes.addShapelessRecipe(new ItemStack(bucketBlood), Items.BUCKET, jar, jar, jar, jar, jar, jar, jar, jar);
    }
}

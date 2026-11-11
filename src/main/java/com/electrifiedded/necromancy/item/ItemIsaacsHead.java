package com.electrifiedded.necromancy.item;

import com.electrifiedded.necromancy.Necromancy;
import net.minecraft.entity.Entity;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;

public class ItemIsaacsHead extends ItemArmor {

    public ItemIsaacsHead(ArmorMaterial material, int renderIndex, EntityEquipmentSlot slot) {
        super(material, renderIndex, slot);
        setTranslationKey("IsaacsHead");
        setCreativeTab(Necromancy.tabNecromancy);
        setMaxDamage(0);
    }

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type) {
        return "necromancy:textures/models/armor/isaacarmor.png";
    }
}

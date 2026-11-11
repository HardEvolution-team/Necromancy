package com.electrifiedded.necromancy.item;

import com.electrifiedded.archaiclib.legacy.entity.LegacyInventory;
import com.electrifiedded.necromancy.Necromancy;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraftforge.common.util.EnumHelper;

public class ItemScythe extends ItemSword {
    public static ToolMaterial toolScythe = EnumHelper.addToolMaterial("BLOODSCYTHE", 0, 666, 7F, 2, 9);
    public static ToolMaterial toolScytheBone = EnumHelper.addToolMaterial("BLOODSCYTHEBONE", 0, 666, 7F, 4, 9);

    public ItemScythe(ToolMaterial material) {
        super(material);
        setCreativeTab(Necromancy.tabNecromancy);
    }

    @Override
    public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
        stack.damageItem(1, attacker);
        if (target.getHealth() <= 0 && attacker instanceof EntityPlayer player) {
            if (LegacyInventory.consumeInventoryItem(player.inventory, Items.GLASS_BOTTLE)) {
                ItemStack soul = ItemGeneric.getItemStackFromName("Soul in a Jar");
                if (!player.inventory.addItemStackToInventory(soul)) {
                    player.entityDropItem(soul, 0f);
                }
                if (target.world.isRemote) {
                    for (int i = 0; i < 30; i++) {
                        Necromancy.proxy.spawnParticle("skull", target.posX, target.posY, target.posZ, target.getRNG().nextDouble() / 360 * 10,
                                target.getRNG().nextDouble() / 360 * 10, target.getRNG().nextDouble() / 360 * 10);
                    }
                }
                target.motionY = 10000;
            }
        }
        super.hitEntity(stack, target, attacker);
        return false;
    }
}

package com.electrifiedded.necromancy.entity.necroapi;

import net.minecraft.entity.EntityLiving;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import com.electrifiedded.necromancy.api.BodyPartLocation;
import com.electrifiedded.necromancy.api.NecroEntityBiped;
import com.electrifiedded.necromancy.item.ItemBodyPart;

public class NecroEntityZombie extends NecroEntityBiped
{

    public NecroEntityZombie()
    {
        super("Zombie");
        headItem = new ItemStack(Items.SKULL, 1, 2);
        torsoItem = ItemBodyPart.getItemStackFromName("Zombie Torso", 1);
        armItem = ItemBodyPart.getItemStackFromName("Zombie Arm", 1);
        legItem = ItemBodyPart.getItemStackFromName("Zombie Legs", 1);
        texture = new ResourceLocation("textures/entity/zombie/zombie.png");
        textureHeight = 64;
    }

    @Override
    public void initRecipes()
    {
        initDefaultRecipes(Items.ROTTEN_FLESH);
    }

    @Override
    public void setAttributes(EntityLiving minion, BodyPartLocation location)
    {
        if (location == BodyPartLocation.Head)
        {
            addAttributeMods(minion, "Head", 1D, 1D, 0D, 0D, 1D);
        }
        else if (location == BodyPartLocation.Torso)
        {
            addAttributeMods(minion, "Torso", 2D, 0D, 0D, 0D, 0D);
        }
        else if (location == BodyPartLocation.ArmLeft)
        {
            addAttributeMods(minion, "ArmL", 0.5D, 0D, 0D, 0D, 0.5D);
        }
        else if (location == BodyPartLocation.ArmRight)
        {
            addAttributeMods(minion, "ArmR", 0.5D, 0D, 0D, 0D, 0.5D);
        }
        else if (location == BodyPartLocation.Legs)
        {
            addAttributeMods(minion, "Legs", 1D, 0D, 3D, 3D, 0D);
        }
    }
}

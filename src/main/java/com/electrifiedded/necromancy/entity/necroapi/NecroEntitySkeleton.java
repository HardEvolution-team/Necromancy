package com.electrifiedded.necromancy.entity.necroapi;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.EntityLiving;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import com.electrifiedded.necromancy.api.BodyPart;
import com.electrifiedded.necromancy.api.BodyPartLocation;
import com.electrifiedded.necromancy.api.ISkull;
import com.electrifiedded.necromancy.api.NecroEntityBiped;
import com.electrifiedded.necromancy.item.ItemBodyPart;

public class NecroEntitySkeleton extends NecroEntityBiped implements ISkull
{

    public NecroEntitySkeleton()
    {
        super("Skeleton");
        headItem = new ItemStack(Items.SKULL);
        torsoItem = ItemBodyPart.getItemStackFromName("Skeleton Torso", 1);
        armItem = ItemBodyPart.getItemStackFromName("Skeleton Arm", 1);
        legItem = ItemBodyPart.getItemStackFromName("Skeleton Legs", 1);
        texture = new ResourceLocation("textures/entity/skeleton/skeleton.png");
    }

    @Override
    public void initRecipes()
    {
        initDefaultRecipes(Items.BONE);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public BodyPart[] initArmLeft(ModelBase model)
    {
        BodyPart armLeft = new BodyPart(this, model, 40, 16);
        armLeft.addBox(2.0F, 0.0F, -1.0F, 2, 12, 2, 0.0F);
        armLeft.mirror = true;
        return new BodyPart[] { armLeft };
    }

    @Override
    @SideOnly(Side.CLIENT)
    public BodyPart[] initArmRight(ModelBase model)
    {
        BodyPart armRight = new BodyPart(this, model, 40, 16);
        armRight.addBox(0.0F, 0.0F, -1.0F, 2, 12, 2, 0.0F);
        return new BodyPart[] { armRight };
    }

    @Override
    @SideOnly(Side.CLIENT)
    public BodyPart[] initLegs(ModelBase model)
    {
        float[] torsoPos = { -4F, -2F, -2F };
        BodyPart legLeft = new BodyPart(this, torsoPos, model, 0, 16);
        legLeft.addBox(-3.0F, -2.0F, -1.0F, 2, 12, 2, 0.0F);
        legLeft.setRotationPoint(0.0F, 12.0F, 0.0F);
        BodyPart legRight = new BodyPart(this, torsoPos, model, 0, 16);
        legRight.addBox(1.0F, -2.0F, -1.0F, 2, 12, 2, 0.0F);
        legRight.setRotationPoint(0.0F, 12.0F, 0.0F);
        legLeft.mirror = true;
        return new BodyPart[] { legLeft, legRight };
    }

    @Override
    public String getSkullModelTexture()
    {
        return "/mob/skeleton.png";
    }

    @Override
    public String getSkullIIconTexture()
    {
        return "skull_skeleton";
    }

    @Override
    public void setAttributes(EntityLiving minion, BodyPartLocation location)
    {
        if (location == BodyPartLocation.Head)
        {
            addAttributeMods(minion, "Head", 1D, 1D, 0D, 0D, 0D);
        }
        else if (location == BodyPartLocation.Torso)
        {
            addAttributeMods(minion, "Torso", 1.5D, 0D, 0D, 0D, 0D);
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
            addAttributeMods(minion, "Legs", 0.75D, 0D, 3D, 3D, 0D);
        }
    }
}

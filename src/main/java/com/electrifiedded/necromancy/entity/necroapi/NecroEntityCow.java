package com.electrifiedded.necromancy.entity.necroapi;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.EntityLiving;
import net.minecraft.init.Items;
import net.minecraft.util.ResourceLocation;

import com.electrifiedded.necromancy.api.BodyPart;
import com.electrifiedded.necromancy.api.BodyPartLocation;
import com.electrifiedded.necromancy.api.NecroEntityQuadruped;
import com.electrifiedded.necromancy.item.ItemBodyPart;

public class NecroEntityCow extends NecroEntityQuadruped
{

    public NecroEntityCow()
    {
        this("Cow", 12);
    }

    public NecroEntityCow(String name, int size)
    {
        super(name, size);
        headItem = ItemBodyPart.getItemStackFromName("Cow Head", 1);
        torsoItem = ItemBodyPart.getItemStackFromName("Cow Torso", 1);
        armItem = ItemBodyPart.getItemStackFromName("Cow Arm", 1);
        legItem = ItemBodyPart.getItemStackFromName("Cow Legs", 1);
        texture = new ResourceLocation("textures/entity/cow/cow.png");
    }

    @Override
    public void initRecipes()
    {
        initDefaultRecipes(Items.BEEF);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public BodyPart[] initHead(ModelBase model)
    {
        BodyPart head = new BodyPart(this, model, 0, 0);
        head.addBox(-4.0F, -4.0F, -4.0F, 8, 8, 6, 0.0F);
        head.setTextureOffset(22, 0).addBox(-5.0F, -5.0F, -4.0F, 1, 3, 1, 0.0F);
        head.setTextureOffset(22, 0).addBox(4.0F, -5.0F, -4.0F, 1, 3, 1, 0.0F);
        head.setTextureSize(textureWidth, textureHeight);
        return new BodyPart[] { head };
    }

    @Override
    @SideOnly(Side.CLIENT)
    public BodyPart[] initTorso(ModelBase model)
    {
        float[] headPos = { 4.0F, 16 - size, -14.0F };
        float[] armLeftPos = { -1.0F, 12.0F, -10.0F };
        float[] armRightPos = { 5F, 12.0F, -10.0F };
        BodyPart body = new BodyPart(this, armLeftPos, armRightPos, headPos, model, 18, 4);
        body.addBox(-2.0F, -12.0F, -12.0F, 12, 18, 10, 0.0F);
        body.setTextureOffset(52, 0).addBox(2.0F, 2.0F, -13.0F, 4, 6, 1);
        body.setTextureSize(textureWidth, textureHeight);
        return new BodyPart[] { body };
    }

    @Override
    @SideOnly(Side.CLIENT)
    public BodyPart[] initLegs(ModelBase model)
    {
        float[] torsoPos = { -4F, -2F, 0F };
        BodyPart legLeft = new BodyPart(this, torsoPos, model, 0, 16);
        legLeft.addBox(-2.0F, 0.0F, -2.0F, 4, size, 4, 0.0F);
        legLeft.setRotationPoint(-4.0F, (float) 22 - size, 2.0F);
        BodyPart legRight = new BodyPart(this, torsoPos, model, 0, 16);
        legRight.addBox(-2.0F, 0.0F, -2.0F, 4, size, 4, 0.0F);
        legRight.setRotationPoint(4.0F, (float) 22 - size, 2.0F);
        legLeft.setTextureSize(textureWidth, textureHeight);
        legRight.setTextureSize(textureWidth, textureHeight);
        legLeft.mirror = true;
        return new BodyPart[] { legLeft, legRight };
    }

    @Override
    public void setAttributes(EntityLiving minion, BodyPartLocation location)
    {
        if (location == BodyPartLocation.Head)
        {
            addAttributeMods(minion, "Head", 0.5D, 1D, 0D, 0D, 0D);
        }
        else if (location == BodyPartLocation.Torso)
        {
            addAttributeMods(minion, "Torso", 1D, 0D, 0D, 0D, 0D);
        }
        else if (location == BodyPartLocation.ArmLeft)
        {
            addAttributeMods(minion, "ArmL", 0.25D, 0D, 0D, 0D, 0.25D);
        }
        else if (location == BodyPartLocation.ArmRight)
        {
            addAttributeMods(minion, "ArmL", 0.25D, 0D, 0D, 0D, 0.25D);
        }
        else if (location == BodyPartLocation.Legs)
        {
            addAttributeMods(minion, "Legs", 0.25D, 0D, 1D, 3D, 0D);
        }
    }
}

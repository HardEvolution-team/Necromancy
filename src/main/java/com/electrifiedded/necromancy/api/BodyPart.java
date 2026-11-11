package com.electrifiedded.necromancy.api;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class BodyPart extends ModelRenderer {

    public String name;
    public NecroEntityBase entity;

    public float[] torsoPos;
    public float[] armLeftPos;
    public float[] armRightPos;
    public float[] headPos;

    public BodyPart(NecroEntityBase base, ModelBase model, int textureXOffset, int textureYOffset) {
        super(model, textureXOffset, textureYOffset);
        entity = base;
        name = base.mobName;
        textureHeight = base.textureHeight;
        textureWidth = base.textureWidth;
    }

    public BodyPart(NecroEntityBase base, float[] torsoPos, ModelBase model, int textureXOffset, int textureYOffset) {
        this(base, model, textureXOffset, textureYOffset);
        this.torsoPos = torsoPos;
    }

    public BodyPart(NecroEntityBase base, float[] armLeftPos, float[] armRightPos, float[] headPos, ModelBase model, int textureXOffset, int textureYOffset) {
        this(base, model, textureXOffset, textureYOffset);
        this.armLeftPos = armLeftPos;
        this.armRightPos = armRightPos;
        this.headPos = headPos;
    }
}

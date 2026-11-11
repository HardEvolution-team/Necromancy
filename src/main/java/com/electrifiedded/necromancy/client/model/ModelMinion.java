package com.electrifiedded.necromancy.client.model;

import com.electrifiedded.necromancy.api.BodyPart;
import com.electrifiedded.necromancy.api.BodyPartLocation;
import com.electrifiedded.necromancy.api.ISaddleAble;
import com.electrifiedded.necromancy.api.NecroEntityBase;
import com.electrifiedded.necromancy.api.NecroEntityRegistry;
import com.electrifiedded.necromancy.entity.EntityMinion;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.Entity;
import org.lwjgl.opengl.GL11;

public class ModelMinion extends ModelBase {
    private BodyPart[] head, torso, armLeft, armRight, legs;
    private float[] torsoPos = new float[3], armLeftPos = new float[3], armRightPos = new float[3], headPos = new float[3];
    private String[] names = new String[5];

    @Override
    public void render(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        EntityMinion minion = (EntityMinion) entity;
        updateModel(minion);
        setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, minion);
        if (legs != null) {
            GL11.glPushMatrix();
            NecroEntityBase mob = bindTexByPart(names[4]);
            mob.preRender(minion, legs, BodyPartLocation.Legs, this);
            for (BodyPart part : legs) part.render(scale);
            mob.postRender(minion, legs, BodyPartLocation.Legs, this);
            GL11.glPopMatrix();
        }
        if (torso != null) {
            NecroEntityBase mob = bindTexByPart(names[1]);
            GL11.glTranslatef(torsoPos[0] / 16, torsoPos[1] / 16, torsoPos[2] / 16);
            mob.preRender(minion, torso, BodyPartLocation.Torso, this);
            for (BodyPart part : torso) part.render(scale);
            if (mob instanceof ISaddleAble saddle && minion.getSaddled()) {
                Minecraft.getMinecraft().getTextureManager().bindTexture(saddle.getSaddleTex());
                for (BodyPart part : torso) part.render(scale);
            }
            mob.postRender(minion, torso, BodyPartLocation.Torso, this);
        }
        if (armLeft != null) {
            GL11.glPushMatrix();
            NecroEntityBase mob = bindTexByPart(names[2]);
            GL11.glTranslatef(armLeftPos[0] / 16, armLeftPos[1] / 16, armLeftPos[2] / 16);
            mob.preRender(minion, armLeft, BodyPartLocation.ArmLeft, this);
            for (BodyPart part : armLeft) part.render(scale);
            mob.postRender(minion, armLeft, BodyPartLocation.ArmLeft, this);
            GL11.glPopMatrix();
        }
        if (armRight != null) {
            GL11.glPushMatrix();
            GL11.glTranslatef(armRightPos[0] / 16, armRightPos[1] / 16, armRightPos[2] / 16);
            NecroEntityBase mob = bindTexByPart(names[3]);
            mob.preRender(minion, armRight, BodyPartLocation.ArmRight, this);
            for (BodyPart part : armRight) part.render(scale);
            mob.postRender(minion, armRight, BodyPartLocation.ArmRight, this);
            GL11.glPopMatrix();
        }
        if (head != null) {
            GL11.glPushMatrix();
            GL11.glTranslatef(headPos[0] / 16, headPos[1] / 16, headPos[2] / 16);
            NecroEntityBase mob = bindTexByPart(names[0]);
            mob.preRender(minion, head, BodyPartLocation.Head, this);
            for (BodyPart part : head) part.render(scale);
            mob.postRender(minion, head, BodyPartLocation.Head, this);
            GL11.glPopMatrix();
        }
    }

    private NecroEntityBase bindTexByPart(String name) {
        NecroEntityBase mob = NecroEntityRegistry.registeredEntities.get(name);
        textureHeight = mob.textureHeight;
        textureWidth = mob.textureWidth;
        Minecraft.getMinecraft().getTextureManager().bindTexture(mob.texture);
        return mob;
    }

    private BodyPart[] parts(String name, BodyPartLocation location) {
        NecroEntityBase mob = NecroEntityRegistry.registeredEntities.get(name);
        if (mob == null) return null;
        BodyPart[] p = mob.getParts(location, this);
        return p == null || p.length == 0 || p[0] == null ? null : p;
    }

    public void updateModel(EntityMinion minion) {
        names = minion.getBodyPartsNames();
        head = parts(names[0], BodyPartLocation.Head);
        torso = parts(names[1], BodyPartLocation.Torso);
        armLeft = parts(names[2], BodyPartLocation.ArmRight);
        armRight = parts(names[3], BodyPartLocation.ArmLeft);
        legs = parts(names[4], BodyPartLocation.Legs);
        torsoPos = new float[3];
        armLeftPos = new float[3];
        armRightPos = new float[3];
        headPos = new float[3];
        if (legs != null && legs[0].torsoPos != null) {
            torsoPos = legs[0].torsoPos;
        }
        if (torso != null && torso[0].armLeftPos != null) {
            armLeftPos = torso[0].armLeftPos;
            armRightPos = torso[0].armRightPos;
            headPos = torso[0].headPos;
        }
    }

    @Override
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale, Entity entity) {
        EntityMinion minion = (EntityMinion) entity;
        NecroEntityBase mob;
        if (head != null && (mob = NecroEntityRegistry.registeredEntities.get(names[0])) != null) {
            mob.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entity, head, BodyPartLocation.Head);
        }
        if (torso != null && (mob = NecroEntityRegistry.registeredEntities.get(names[1])) != null) {
            mob.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entity, torso, BodyPartLocation.Torso);
        }
        if (armLeft != null && (mob = NecroEntityRegistry.registeredEntities.get(names[2])) != null) {
            mob.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entity, armLeft, BodyPartLocation.ArmLeft);
        }
        if (armRight != null && (mob = NecroEntityRegistry.registeredEntities.get(names[3])) != null) {
            mob.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entity, armRight, BodyPartLocation.ArmRight);
        }
        if (legs != null && (mob = NecroEntityRegistry.registeredEntities.get(names[4])) != null) {
            mob.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entity, legs, BodyPartLocation.Legs);
        }
        int i = minion.getAttackTimer();
        if (i > 0) {
            float angle = -2.0F + 1.5F * calc((float) i - netHeadYaw, 10.0F);
            if (armLeft != null) for (BodyPart p : armLeft) p.rotateAngleX = angle;
            if (armRight != null) for (BodyPart p : armRight) p.rotateAngleX = angle;
        }
    }

    private float calc(float a, float b) {
        return (Math.abs(a % b - b * 0.5F) - b * 0.25F) / (b * 0.25F);
    }
}

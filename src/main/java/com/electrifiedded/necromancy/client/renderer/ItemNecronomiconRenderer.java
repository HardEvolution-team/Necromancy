package com.electrifiedded.necromancy.client.renderer;

import com.electrifiedded.archaiclib.v1710.api.client.IItemRenderer;
import com.electrifiedded.necromancy.Necromancy;
import com.electrifiedded.necromancy.client.model.ModelNecronomicon;
import com.electrifiedded.necromancy.lib.ReferenceNecromancy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;

public class ItemNecronomiconRenderer implements IItemRenderer {
    private final ModelNecronomicon modelInteractive = new ModelNecronomicon();
    private final ModelNecronomicon modelStatic = new ModelNecronomicon();
    public String[] leftPageContent = {}, rightPageContent = {};

    @Override
    public boolean handleRenderType(ItemStack item, ItemRenderType type) {
        return true;
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
        return true;
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
        switch (type) {
            case ENTITY -> renderStatic(0F, 0.2F, 0F, 180F, 1F, 1F, 0.004F);
            case EQUIPPED -> renderInteractive(0.8F, 1.3F, 0.6F, 180F, 180F, 0F, 0.009F);
            case EQUIPPED_FIRST_PERSON -> renderInteractive(0.8F, 1F, -1F, 20F, 100F, 160F, 0.01F);
            case INVENTORY -> renderStatic(-0.2F, 0.2F, 0.2F, 100F, -20F, -20F, 0.007F);
            default -> {
            }
        }
    }

    private void renderInteractive(float posX, float posY, float posZ, float rotX, float rotY, float rotZ, float scale) {
        FontRenderer font = Minecraft.getMinecraft().fontRenderer;
        modelInteractive.setRotationAngles(100, 0, 0, 0, 0, 0, null);
        GL11.glPushMatrix();
        GL11.glTranslatef(posX, posY, posZ);
        GL11.glRotatef(rotX, 1, 0, 0);
        GL11.glRotatef(rotY, 0, 1, 0);
        GL11.glRotatef(rotZ, 0, 0, 1);
        GL11.glScalef(scale, scale, scale);
        Necromancy.proxy.bindTexture(ReferenceNecromancy.TEXTURES_MODELS_NECRONOMICON);
        modelInteractive.render(null, 1, 0, 0, 0, 1, 1);
        GL11.glRotatef(-54, 0, 1, 0);
        GL11.glTranslatef(8, -20, -30);
        GL11.glScalef(0.8F, 0.8F, 0.8F);
        for (int i = 0; i < rightPageContent.length; i++) {
            font.drawString(rightPageContent[i], 10, -50 + i * 10, 7208960);
        }
        GL11.glRotatef(-69, 0, 1, 0);
        GL11.glTranslatef(-104, 0, 3);
        for (int i = 0; i < leftPageContent.length; i++) {
            font.drawString(leftPageContent[i], 10, -50 + i * 10, 7208960);
        }
        GL11.glPopMatrix();
    }

    private void renderStatic(float posX, float posY, float posZ, float rotX, float rotY, float rotZ, float scale) {
        modelInteractive.setRotationAngles(100, 0, 0, 0, 0, 0, null);
        GL11.glPushMatrix();
        GL11.glTranslatef(posX, posY, posZ);
        GL11.glRotatef(rotX, 1, 0, 0);
        GL11.glRotatef(rotY, 0, 1, 0);
        GL11.glRotatef(rotZ, 0, 0, 1);
        GL11.glScalef(scale, scale, scale);
        Necromancy.proxy.bindTexture(ReferenceNecromancy.TEXTURES_MODELS_NECRONOMICON);
        modelStatic.render(null, 0, 0, 0, 0f, 1, 1);
        GL11.glPopMatrix();
    }
}

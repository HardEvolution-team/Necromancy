package com.electrifiedded.necromancy.client.renderer.tileentity;

import com.electrifiedded.archaiclib.v1710.api.client.IItemRenderer;
import com.electrifiedded.necromancy.Necromancy;
import com.electrifiedded.necromancy.client.model.ModelSewing;
import com.electrifiedded.necromancy.lib.ReferenceNecromancy;
import com.electrifiedded.necromancy.tileentity.TileEntitySewing;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;

public class TileEntitySewingRenderer extends TileEntitySpecialRenderer<TileEntitySewing> implements IItemRenderer {
    private final ModelSewing model = new ModelSewing();

    @Override
    public void render(TileEntitySewing sewing, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        int meta = sewing.hasWorld() ? sewing.getBlockMetadata() : 0;
        Necromancy.proxy.bindTexture(ReferenceNecromancy.TEXTURES_MODELS_SEWING);
        GL11.glPushMatrix();
        GlStateManager.enableRescaleNormal();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glTranslatef((float) x, (float) y + 2.0F, (float) z + 1.0F);
        GL11.glScalef(1.0F, -1F, -1F);
        GL11.glTranslatef(0.5F, 0.5F, 0.5F);
        int j = switch (meta) {
            case 0 -> 270;
            case 2 -> 90;
            case 3 -> 180;
            default -> 0;
        };
        GL11.glRotatef(j, 0.0F, 1.0F, 0.0F);
        model.render();
        GlStateManager.disableRescaleNormal();
        GL11.glPopMatrix();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
    }

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
            case ENTITY -> renderSewing(0.0F, 3.0F, 0.0F, 0.0F, 0.0F, 180.0F, 2F);
            case INVENTORY -> renderSewing(0.0F, 1.8F, 0.0F, 0.0F, 0.0F, 180.0F, 1.5F);
            case EQUIPPED, EQUIPPED_FIRST_PERSON -> renderSewing(0.0F, 3.5F, 0.5F, 0.0F, 0.0F, 180.0F, 2.0F);
            default -> {
            }
        }
    }

    private void renderSewing(float posX, float posY, float posZ, float rotX, float rotY, float rotZ, float scale) {
        Necromancy.proxy.bindTexture(ReferenceNecromancy.TEXTURES_MODELS_SEWING);
        GL11.glPushMatrix();
        GL11.glTranslatef(posX, posY, posZ);
        GL11.glRotatef(rotX, 1, 0, 0);
        GL11.glRotatef(rotY, 0, 1, 0);
        GL11.glRotatef(rotZ, 0, 0, 1);
        GL11.glScalef(scale, scale, scale);
        model.render();
        GL11.glPopMatrix();
    }
}

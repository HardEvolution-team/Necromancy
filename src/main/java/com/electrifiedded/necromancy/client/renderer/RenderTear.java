package com.electrifiedded.necromancy.client.renderer;

import com.electrifiedded.necromancy.entity.EntityTear;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;

public class RenderTear extends Render<EntityTear> {
    private final ResourceLocation sprite;

    public RenderTear(RenderManager manager, ResourceLocation sprite) {
        super(manager);
        this.sprite = sprite;
    }

    @Override
    public void doRender(EntityTear entity, double x, double y, double z, float entityYaw, float partialTicks) {
        TextureAtlasSprite icon = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(sprite.toString());
        GlStateManager.pushMatrix();
        GlStateManager.translate((float) x, (float) y, (float) z);
        GlStateManager.enableRescaleNormal();
        GlStateManager.scale(0.5F, 0.5F, 0.5F);
        bindEntityTexture(entity);
        double minU = icon.getMinU();
        double maxU = icon.getMaxU();
        double minV = icon.getMinV();
        double maxV = icon.getMaxV();
        GlStateManager.rotate(180.0F - renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate((renderManager.options.thirdPersonView == 2 ? -1 : 1) * -renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        buffer.begin(7, DefaultVertexFormats.POSITION_TEX_NORMAL);
        buffer.pos(-0.5D, -0.25D, 0.0D).tex(minU, maxV).normal(0.0F, 1.0F, 0.0F).endVertex();
        buffer.pos(0.5D, -0.25D, 0.0D).tex(maxU, maxV).normal(0.0F, 1.0F, 0.0F).endVertex();
        buffer.pos(0.5D, 0.75D, 0.0D).tex(maxU, minV).normal(0.0F, 1.0F, 0.0F).endVertex();
        buffer.pos(-0.5D, 0.75D, 0.0D).tex(minU, minV).normal(0.0F, 1.0F, 0.0F).endVertex();
        tessellator.draw();
        GlStateManager.disableRescaleNormal();
        GlStateManager.popMatrix();
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityTear entity) {
        return TextureMap.LOCATION_BLOCKS_TEXTURE;
    }
}

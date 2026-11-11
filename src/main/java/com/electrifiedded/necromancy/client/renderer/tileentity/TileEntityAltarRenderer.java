package com.electrifiedded.necromancy.client.renderer.tileentity;

import com.electrifiedded.archaiclib.v1710.api.client.IItemRenderer;
import com.electrifiedded.necromancy.Necromancy;
import com.electrifiedded.necromancy.client.model.ModelAltar;
import com.electrifiedded.necromancy.entity.EntityMinion;
import com.electrifiedded.necromancy.lib.ReferenceNecromancy;
import com.electrifiedded.necromancy.tileentity.TileEntityAltar;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBook;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

import java.util.Random;

public class TileEntityAltarRenderer extends TileEntitySpecialRenderer<TileEntityAltar> implements IItemRenderer {
    private final ModelBook modelBook = new ModelBook();
    private final ResourceLocation book = new ResourceLocation("textures/entity/enchanting_table_book.png");
    private final ModelAltar model = new ModelAltar();
    private final Random rand = new Random();

    @Override
    public void render(TileEntityAltar altar, double d, double d1, double d2, float partialTicks, int destroyStage, float alpha) {
        int i = altar.hasWorld() ? altar.getBlockMetadata() : 0;
        renderAltar(altar, i, d, d1, d2);
        if (!altar.hasWorld()) return;
        EntityMinion entity = altar.getPreviewEntity();
        GL11.glPushMatrix();
        GL11.glTranslatef((float) d + 1.5F, (float) d1 + 1.0F, (float) d2 + 0.5F);
        switch (i) {
            case 2 -> {
                GL11.glTranslatef(-1F, 0.2F, -2.2F);
                GL11.glRotatef(180F, 0.0F, 1.0F, 0.0F);
                GL11.glRotatef(-90F, 1.0F, 0.0F, 0.0F);
                GL11.glTranslatef(0.0F, -0.4F, 0.0F);
            }
            case 3 -> {
                GL11.glTranslatef(1.2F, 0.2F, 0.0F);
                GL11.glRotatef(90F, 0.0F, 1.0F, 0.0F);
                GL11.glRotatef(-90F, 1.0F, 0.0F, 0.0F);
                GL11.glTranslatef(0.0F, -0.4F, 0.0F);
            }
            case 1 -> {
                GL11.glTranslatef(-3.2F, 0.2F, 0.0F);
                GL11.glRotatef(90F, 0.0F, 0.1F, 0.0F);
                GL11.glRotatef(180F, 0.0F, 0.1F, 0.0F);
                GL11.glRotatef(-90F, 0.1F, 0.0F, 0.0F);
                GL11.glTranslatef(0.0F, -0.4F, 0.0F);
            }
            case 0 -> {
                GL11.glTranslatef(-1F, 0.2F, 3F);
                GL11.glRotatef(-90F, 1.0F, 0.0F, 0.0F);
                GL11.glTranslatef(0.0F, 0.4F, 0.0F);
            }
            default -> {
            }
        }
        GL11.glTranslated(0, 0, 0.05D * Math.sin(0.001D * System.currentTimeMillis()) + 0.1D);
        entity.setLocationAndAngles(d, d1, d2, 0.0F, 0.0F);
        entity.prevRenderYawOffset = entity.renderYawOffset = 0.0F;
        entity.prevRotationYawHead = entity.rotationYawHead = 0.0F;
        Minecraft.getMinecraft().getRenderManager().renderEntity(entity, 0.0D, 0.0D, 0.0D, 0.0F, 0.0F, false);
        GL11.glPopMatrix();
    }

    private void renderAltar(TileEntityAltar entity, int meta, double x, double y, double z) {
        Necromancy.proxy.bindTexture(ReferenceNecromancy.TEXTURES_MODELS_ALTAR);
        GL11.glPushMatrix();
        GL11.glTranslatef((float) x, (float) y + 2.0F, (float) z + 1.0F);
        GL11.glScalef(1.0F, -1F, -1F);
        GL11.glTranslatef(0.5F, 0.5F, 0.5F);
        int j = switch (meta) {
            case 0 -> 90;
            case 1 -> 180;
            case 2 -> 270;
            default -> 0;
        };
        GL11.glRotatef(j, 0.0F, 1.0F, 0.0F);
        model.render();
        GlStateManager.enableRescaleNormal();
        GL11.glPushMatrix();
        GL11.glTranslatef(.3F, -.1F, -0.3F);
        float f1 = 0.5F;
        GL11.glScalef(-f1, -f1, f1);
        int i1 = 1000000000;
        int j1 = i1 % 65536;
        int k = i1 / 65536;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, j1, k);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        Necromancy.proxy.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        GL11.glPushMatrix();
        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
        Minecraft.getMinecraft().getBlockRendererDispatcher().renderBlockBrightness(Blocks.TORCH.getDefaultState(), 1.0F);
        GL11.glPopMatrix();
        Necromancy.proxy.bindTexture(book);
        GL11.glScalef(0.1F, 0.1F, 0.1F);
        GL11.glRotatef(90, 0, 0, 1);
        GL11.glTranslatef(-4F, -8F, 8F);
        GlStateManager.color(0.9F, 0.9F, 0.9F, 1.0F);
        modelBook.render(null, 1F, 0F, 0F, 1.22F, 0F, 1F);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        if (entity != null && entity.hasWorld() && rand.nextInt(100) == 0) {
            randomDisplayTick(entity.getWorld(), entity.getPos().getX(), entity.getPos().getY() + 1, entity.getPos().getZ(), meta);
        }
        GL11.glPopMatrix();
        GL11.glPopMatrix();
    }

    private void randomDisplayTick(World world, int x, int y, int z, int metadata) {
        double d0 = 0, d1 = 0, d2 = 0;
        switch (metadata) {
            case 0 -> { d0 = x + .2F; d1 = y + 0.7F; d2 = z + .8F; }
            case 1 -> { d0 = x + .2F; d1 = y + 0.7F; d2 = z + 0.2F; }
            case 2 -> { d0 = x + .8F; d1 = y + 0.7F; d2 = z + 0.2F; }
            case 3 -> { d0 = x + .8F; d1 = y + 0.7F; d2 = z + 0.8F; }
            default -> {
            }
        }
        world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0, d1, d2, 0.0D, 0.0D, 0.0D);
        world.spawnParticle(EnumParticleTypes.FLAME, d0, d1, d2, 0.0D, 0.0D, 0.0D);
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
            case ENTITY -> renderAltarItem(1.5F, 1F, 0.5F, 1F, 1F, 180F, 1);
            case EQUIPPED, EQUIPPED_FIRST_PERSON -> renderAltarItem(0.5F, 1F, 0.5F, 1F, 250F, 180F, 1);
            case INVENTORY -> renderAltarItem(-0.5F, 0.5F, 0, 1F, -180F, 180F, 0.55F);
            default -> {
            }
        }
    }

    private void renderAltarItem(float posX, float posY, float posZ, float rotX, float rotY, float rotZ, float scale) {
        Necromancy.proxy.bindTexture(ReferenceNecromancy.TEXTURES_MODELS_ALTAR);
        GL11.glPushMatrix();
        GL11.glTranslatef(posX, posY, posZ);
        GL11.glRotatef(rotX, 1, 0, 0);
        GL11.glRotatef(rotY, 0, 1, 0);
        GL11.glRotatef(rotZ, 0, 0, 1);
        GL11.glScalef(scale, scale, scale);
        model.render();
        GL11.glPopMatrix();
    }

    @Override
    public boolean isGlobalRenderer(TileEntityAltar te) {
        return true;
    }
}

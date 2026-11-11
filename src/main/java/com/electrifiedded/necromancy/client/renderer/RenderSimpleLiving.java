package com.electrifiedded.necromancy.client.renderer;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.ResourceLocation;

public class RenderSimpleLiving<T extends EntityLiving> extends RenderLiving<T> {
    private final ResourceLocation texture;

    public RenderSimpleLiving(RenderManager manager, ModelBase model, float shadowSize, ResourceLocation texture) {
        super(manager, model, shadowSize);
        this.texture = texture;
    }

    @Override
    protected ResourceLocation getEntityTexture(T entity) {
        return texture;
    }
}

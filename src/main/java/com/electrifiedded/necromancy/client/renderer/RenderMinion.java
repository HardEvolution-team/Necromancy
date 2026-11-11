package com.electrifiedded.necromancy.client.renderer;

import com.electrifiedded.necromancy.client.model.ModelMinion;
import com.electrifiedded.necromancy.entity.EntityMinion;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderMinion extends RenderLiving<EntityMinion> {
    private static final ResourceLocation ZOMBIE = new ResourceLocation("textures/entity/zombie/zombie.png");

    public RenderMinion(RenderManager manager) {
        super(manager, new ModelMinion(), 1.0F);
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityMinion entity) {
        return ZOMBIE;
    }
}

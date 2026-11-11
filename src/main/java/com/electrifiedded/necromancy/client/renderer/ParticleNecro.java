package com.electrifiedded.necromancy.client.renderer;

import com.electrifiedded.necromancy.lib.ReferenceNecromancy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;

public class ParticleNecro extends Particle {

    public ParticleNecro(World world, double x, double y, double z, double mx, double my, double mz, float scale) {
        super(world, x, y, z, 0.0D, 0.0D, 0.0D);
        motionX *= 0.010000000149011612D;
        motionY *= 0.010000000149011612D;
        motionZ *= 0.010000000149011612D;
        motionX += mx;
        motionY += my;
        motionZ += mz;
        particleRed = particleGreen = particleBlue = (float) (Math.random() * 0.50000001192092891D);
        particleScale *= 0.75F;
        particleScale *= scale;
        particleMaxAge = (int) (8D / (Math.random() * 0.80000000000000004D + 0.20000000000000001D) + 64D);
        particleMaxAge = (int) (particleMaxAge * scale);
        canCollide = false;
    }

    @Override
    public int getFXLayer() {
        return 3;
    }

    @Override
    public void renderParticle(BufferBuilder unused, Entity entity, float partialTicks, float rotX, float rotZ, float rotYZ, float rotXY, float rotXZ) {
        Minecraft.getMinecraft().getTextureManager().bindTexture(ReferenceNecromancy.TEXTURES_PARTICLES);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        buffer.begin(7, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);
        int light = getBrightnessForRender(partialTicks);
        int sky = light >> 16 & 65535;
        int block = light & 65535;
        float sC = 0.1F * particleScale;
        float dX = (float) (prevPosX + (posX - prevPosX) * partialTicks - interpPosX);
        float dY = (float) (prevPosY + (posY - prevPosY) * partialTicks - interpPosY);
        float dZ = (float) (prevPosZ + (posZ - prevPosZ) * partialTicks - interpPosZ);
        buffer.pos(dX - rotX * sC - rotXY * sC, dY - rotZ * sC, dZ - rotYZ * sC - rotXZ * sC).tex(32.0624375F, 32.0624375F)
                .color(particleRed, particleGreen, particleBlue, 1.0F).lightmap(sky, block).endVertex();
        buffer.pos(dX - rotX * sC + rotXY * sC, dY + rotZ * sC, dZ - rotYZ * sC + rotXZ * sC).tex(32.0624375F, 32f)
                .color(particleRed, particleGreen, particleBlue, 1.0F).lightmap(sky, block).endVertex();
        buffer.pos(dX + rotX * sC + rotXY * sC, dY + rotZ * sC, dZ + rotYZ * sC + rotXZ * sC).tex(32f, 32f)
                .color(particleRed, particleGreen, particleBlue, 1.0F).lightmap(sky, block).endVertex();
        buffer.pos(dX + rotX * sC - rotXY * sC, dY - rotZ * sC, dZ + rotYZ * sC - rotXZ * sC).tex(32f, 32.0624375F)
                .color(particleRed, particleGreen, particleBlue, 1.0F).lightmap(sky, block).endVertex();
        tessellator.draw();
    }

    @Override
    public void onUpdate() {
        prevPosX = posX;
        prevPosY = posY;
        prevPosZ = posZ;
        if (particleAge++ >= particleMaxAge) {
            setExpired();
        }
        move(motionX, motionY, motionZ);
        if (posY == prevPosY) {
            motionX *= 1.1D;
            motionZ *= 1.1D;
        }
        motionX *= 0.99999997854232792D;
        motionY *= 0.99999997854232792D;
        motionZ *= 0.99999997854232792D;
        if (onGround) {
            motionX *= 0.69999998807907104D;
            motionZ *= 0.69999998807907104D;
        }
    }
}

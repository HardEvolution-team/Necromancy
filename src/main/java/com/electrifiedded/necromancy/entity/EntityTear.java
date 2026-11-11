package com.electrifiedded.necromancy.entity;

import com.electrifiedded.archaiclib.legacy.entity.LegacyThrowable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityTear extends EntityThrowable {
    protected float damage = 3f;
    protected EnumParticleTypes particle = EnumParticleTypes.WATER_SPLASH;

    public EntityTear(World world) {
        super(world);
    }

    public EntityTear(World world, EntityLivingBase shooter) {
        super(world, shooter);
        LegacyThrowable.applyLegacyThrowerInit(this, shooter);
    }

    public EntityTear(World world, EntityLivingBase shooter, EntityLivingBase target) {
        super(world, shooter);
        LegacyThrowable.applyLegacyThrowerInit(this, shooter);
        posY = shooter.posY + shooter.getEyeHeight() - 0.1D;
        double xDiff = target.posX - shooter.posX;
        double yDiff = target.getEntityBoundingBox().minY + (target.height / 3.0F) - posY;
        double zDiff = target.posZ - shooter.posZ;
        double distEuclid = MathHelper.sqrt(xDiff * xDiff + zDiff * zDiff);
        if (distEuclid >= 1.0E-7D) {
            float rot = (float) ((Math.atan2(zDiff, xDiff) * 180.0D / Math.PI) - 90.0F);
            float pitch = (float) (-(Math.atan2(yDiff, distEuclid) * 180.0D / Math.PI));
            double extraX = xDiff / distEuclid;
            double extraZ = zDiff / distEuclid;
            setLocationAndAngles(shooter.posX + extraX, posY, shooter.posZ + extraZ, rot, pitch);
            shoot(xDiff, yDiff, zDiff, 1.0F, 2F);
        }
    }

    @Override
    protected void onImpact(RayTraceResult result) {
        if (result.entityHit != null) {
            if (result.entityHit == getThrower() && ticksExisted < 5) return;
            result.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, getThrower()), damage);
        }
        if (!world.isRemote) {
            world.setEntityState(this, (byte) 3);
            setDead();
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void handleStatusUpdate(byte id) {
        if (id == 3) {
            for (int i = 0; i < 8; ++i) {
                world.spawnParticle(particle, posX, posY, posZ, 0.0D, 0.0D, 0.0D);
            }
        } else {
            super.handleStatusUpdate(id);
        }
    }

    public boolean isBlood() {
        return false;
    }
}

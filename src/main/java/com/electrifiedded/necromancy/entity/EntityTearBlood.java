package com.electrifiedded.necromancy.entity;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;

public class EntityTearBlood extends EntityTear {

    public EntityTearBlood(World world) {
        super(world);
        doInit();
    }

    public EntityTearBlood(World world, EntityLivingBase shooter) {
        super(world, shooter);
        doInit();
    }

    public EntityTearBlood(World world, EntityLivingBase shooter, EntityLivingBase target) {
        super(world, shooter, target);
        doInit();
    }

    private void doInit() {
        damage = 6;
        particle = EnumParticleTypes.REDSTONE;
    }

    @Override
    public boolean isBlood() {
        return true;
    }
}

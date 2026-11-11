package com.electrifiedded.necromancy.entity;

import com.electrifiedded.necromancy.sound.NecroSounds;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.ai.EntityAIAttackRanged;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

public class EntityIsaacNormal extends EntityIsaacBody implements IRangedAttackMob {

    public EntityIsaacNormal(World world) {
        super(world);
    }

    @Override
    protected void initEntityAI() {
        super.initEntityAI();
        tasks.addTask(1, new EntityAIAttackRanged(this, 0.25D, 18, 50F));
    }

    @Override
    public void attackEntityWithRangedAttack(EntityLivingBase target, float distanceFactor) {
        playSound(NecroSounds.TEAR, 1.0F, 1.0F / (getRNG().nextFloat() * 0.4F + 0.8F));
        world.spawnEntity(new EntityTear(world, this, target));
    }

    @Override
    public void setSwingingArms(boolean swingingArms) {
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_GHAST_AMBIENT;
    }

    @Override
    public void onDeath(DamageSource cause) {
        super.onDeath(cause);
        if (!world.isRemote) {
            onIsaacDeath();
        }
    }

    protected void onIsaacDeath() {
        EntityIsaacBlood isaac = new EntityIsaacBlood(world);
        isaac.setLocationAndAngles(posX, posY, posZ, rotationYaw, rotationPitch);
        world.spawnEntity(isaac);
    }
}

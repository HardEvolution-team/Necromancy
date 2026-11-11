package com.electrifiedded.necromancy.entity;

import com.electrifiedded.necromancy.sound.NecroSounds;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.world.World;

public class EntityIsaacBlood extends EntityIsaacNormal {

    public EntityIsaacBlood(World world) {
        super(world);
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(75.0D);
        getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(32.0D);
        getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(0.0D);
        getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.3D);
        getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(4.0D);
    }

    @Override
    public void attackEntityWithRangedAttack(EntityLivingBase target, float distanceFactor) {
        playSound(NecroSounds.TEAR, 1.0F, 1.0F / (getRNG().nextFloat() * 0.4F + 0.8F));
        world.spawnEntity(new EntityTearBlood(world, this, target));
    }

    @Override
    protected void onIsaacDeath() {
        EntityIsaacHead head = new EntityIsaacHead(world);
        EntityIsaacBody body = new EntityIsaacBody(world);
        head.setLocationAndAngles(posX, posY + 1, posZ, rotationYaw, rotationPitch);
        body.setLocationAndAngles(posX, posY, posZ, rotationYaw, rotationPitch);
        world.spawnEntity(head);
        world.spawnEntity(body);
    }
}

package com.electrifiedded.necromancy.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIFollowOwner;
import net.minecraft.entity.ai.EntityAISit;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

import java.util.Locale;

public class EntityTeddy extends EntityTameable {

    public enum EntityState {
        WALKING, DEFENDING, SITTING
    }

    public EntityState entityState;
    private float moveSpeed;

    public EntityTeddy(World world) {
        super(world);
        setTamed(true);
        setSitting(true);
        moveSpeed = 0.3F;
        setSize(0.6F, 0.8F);
        setAIMoveSpeed(0.15F);
        entityState = EntityState.WALKING;
    }

    @Override
    protected void initEntityAI() {
        aiSit = new EntityAISit(this);
        tasks.addTask(1, new EntityAISwimming(this));
        tasks.addTask(2, aiSit);
        tasks.addTask(3, new EntityAIFollowOwner(this, 0.3F, 8F, 5F));
        tasks.addTask(4, new EntityAIWatchClosest(this, EntityPlayer.class, 10F));
        tasks.addTask(6, new EntityAIScareEntities(this, 10F, 7F, 0.3F, EntityMob.class));
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(8.0D);
        getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(32.0D);
        getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(0.0D);
        getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.3F);
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound tag) {
        super.writeEntityToNBT(tag);
        tag.setInteger("state", entityState.ordinal());
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound tag) {
        super.readEntityFromNBT(tag);
        int index = tag.getInteger("state");
        if (index >= 0 && index < EntityState.values().length) {
            entityState = EntityState.values()[index];
        }
    }

    @Override
    protected boolean canDespawn() {
        return false;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_ZOMBIE_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.ENTITY_ZOMBIE_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_ZOMBIE_DEATH;
    }

    @Override
    protected Item getDropItem() {
        return Items.LEATHER;
    }

    @Override
    public boolean attackEntityAsMob(Entity target) {
        return target.attackEntityFrom(DamageSource.causeMobDamage(this), 3);
    }

    @Override
    public boolean processInteract(EntityPlayer player, EnumHand hand) {
        if (hand != EnumHand.MAIN_HAND) return false;
        setOwnerId(player.getUniqueID());
        switch (entityState) {
            case WALKING -> {
                entityState = EntityState.DEFENDING;
                setSitting(true);
                moveSpeed = 0.1F;
            }
            case DEFENDING -> {
                entityState = EntityState.SITTING;
                setSitting(true);
            }
            case SITTING -> {
                entityState = EntityState.WALKING;
                setSitting(false);
                moveSpeed = 0.3F;
            }
        }
        aiSit.setSitting(isSitting());
        if (!world.isRemote && getOwner() instanceof EntityPlayer owner) {
            owner.sendMessage(new TextComponentString("Animated Teddy is now " + entityState.toString().toLowerCase(Locale.ROOT)));
        }
        return true;
    }

    @Override
    public EntityAgeable createChild(EntityAgeable ageable) {
        return null;
    }

    public float getMoveSpeed() {
        return moveSpeed;
    }
}

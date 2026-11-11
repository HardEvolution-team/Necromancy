package com.electrifiedded.necromancy.entity;

import com.electrifiedded.archaiclib.legacy.sound.LegacySounds;
import com.electrifiedded.necromancy.Necromancy;
import com.electrifiedded.necromancy.api.BodyPartLocation;
import com.electrifiedded.necromancy.api.ISaddleAble;
import com.electrifiedded.necromancy.api.NecroEntityBase;
import com.electrifiedded.necromancy.api.NecroEntityRegistry;
import com.electrifiedded.necromancy.item.ItemGeneric;
import com.electrifiedded.necromancy.sound.NecroSounds;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAIFollowOwner;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILeapAtTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIOwnerHurtByTarget;
import net.minecraft.entity.ai.EntityAIOwnerHurtTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITempt;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Locale;

public class EntityMinion extends EntityTameable implements IRangedAttackMob {

    public static final String UNDEFINED = "UNDEFINED";

    private static final DataParameter<String> HEAD = EntityDataManager.createKey(EntityMinion.class, DataSerializers.STRING);
    private static final DataParameter<String> TORSO = EntityDataManager.createKey(EntityMinion.class, DataSerializers.STRING);
    private static final DataParameter<String> ARM_LEFT = EntityDataManager.createKey(EntityMinion.class, DataSerializers.STRING);
    private static final DataParameter<String> ARM_RIGHT = EntityDataManager.createKey(EntityMinion.class, DataSerializers.STRING);
    private static final DataParameter<String> LEGS = EntityDataManager.createKey(EntityMinion.class, DataSerializers.STRING);
    private static final DataParameter<Byte> SADDLE = EntityDataManager.createKey(EntityMinion.class, DataSerializers.BYTE);
    @SuppressWarnings("unchecked")
    private static final DataParameter<String>[] PARTS = new DataParameter[]{HEAD, TORSO, ARM_LEFT, ARM_RIGHT, LEGS};

    private boolean isAgressive;
    private int attackTimer;
    private int rangedAttackTimer;

    public EntityMinion(World world) {
        super(world);
        setSize(0.6F, 1.8F);
        ticksExisted = 0;
        attackTimer = rangedAttackTimer = 0;
    }

    public EntityMinion(World world, String[] bodyParts, EntityPlayer owner) {
        this(world);
        setBodyPartNames(bodyParts);
        setTamed(true);
        setOwnerId(owner.getUniqueID());
    }

    @Override
    protected void initEntityAI() {
        aiSit = new net.minecraft.entity.ai.EntityAISit(this);
        tasks.addTask(1, new EntityAISwimming(this));
        tasks.addTask(2, aiSit);
        tasks.addTask(4, new EntityAILeapAtTarget(this, 0.4F));
        tasks.addTask(5, new EntityAIAttackMelee(this, 1, true));
        tasks.addTask(7, new EntityAIWander(this, 1));
        tasks.addTask(8, new EntityAITempt(this, 1, ItemGeneric.getItemStackFromName("Brain on a Stick").getItem(), false));
        tasks.addTask(9, new EntityAIFollowOwner(this, 1, 10.0F, 2.0F));
        tasks.addTask(10, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        tasks.addTask(10, new EntityAILookIdle(this));
        targetTasks.addTask(1, new EntityAIOwnerHurtByTarget(this));
        targetTasks.addTask(2, new EntityAIOwnerHurtTarget(this));
        targetTasks.addTask(3, new EntityAIHurtByTarget(this, true));
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        for (DataParameter<String> p : PARTS) {
            dataManager.register(p, UNDEFINED);
        }
        dataManager.register(SADDLE, (byte) 0);
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        getAttributeMap().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE);
    }

    @Nullable
    public NecroEntityBase getMob(BodyPartLocation location) {
        return NecroEntityRegistry.registeredEntities.get(getBodyPartsNames()[location.ordinal()]);
    }

    public void calculateAttributes() {
        getAttributeMap().getAttributeInstance(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(20D);
        getAttributeMap().getAttributeInstance(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.1D);
        getAttributeMap().getAttributeInstance(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(16D);
        getAttributeMap().getAttributeInstance(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(0.1D);
        getAttributeMap().getAttributeInstance(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(2D);

        for (BodyPartLocation location : BodyPartLocation.values()) {
            NecroEntityBase mob = getMob(location);
            if (mob != null) {
                mob.setAttributes(this, location);
            }
        }

        setHealth((float) getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).getAttributeValue());
        printMinionInfo();
    }

    private void printMinionInfo() {
        Necromancy.loggerNecromancy.info("Necromancy Minion movementSpeed:" + getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue());
        Necromancy.loggerNecromancy.info("Necromancy Minion health:" + getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).getAttributeValue());
        Necromancy.loggerNecromancy.info("Necromancy Minion damage:" + getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue());
        Necromancy.loggerNecromancy.info("Necromancy Minion followRange:" + getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).getAttributeValue());
        Necromancy.loggerNecromancy.info("Necromancy Minion knockbackres:" + getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).getAttributeValue());
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound tag) {
        super.writeEntityToNBT(tag);
        String[] names = getBodyPartsNames();
        tag.setString("head", names[0]);
        tag.setString("body", names[1]);
        tag.setString("armLeft", names[2]);
        tag.setString("armRight", names[3]);
        tag.setString("leg", names[4]);
        tag.setBoolean("Saddle", getSaddled());
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound tag) {
        super.readEntityFromNBT(tag);
        setBodyPartNames(new String[]{tag.getString("head"), tag.getString("body"), tag.getString("armLeft"), tag.getString("armRight"), tag.getString("leg")});
        setSaddled(tag.getBoolean("Saddle"));
    }

    @Override
    public boolean attackEntityAsMob(Entity target) {
        attackTimer = 10;
        world.setEntityState(this, (byte) 4);

        float damage = (float) getAttributeMap().getAttributeInstance(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue();

        for (BodyPartLocation location : BodyPartLocation.values()) {
            NecroEntityBase mob = getMob(location);
            if (mob != null) {
                mob.attackEntityAsMob(this, location, target, damage);
            }
        }

        EntityLivingBase owner = getOwner();
        if (owner instanceof EntityPlayer player) {
            return target.attackEntityFrom(DamageSource.causePlayerDamage(player), damage);
        }
        return target.attackEntityFrom(DamageSource.causeMobDamage(this), damage);
    }

    @Override
    @Nullable
    public Entity getControllingPassenger() {
        return getPassengers().isEmpty() ? null : getPassengers().get(0);
    }

    @Override
    public boolean canBeSteered() {
        Entity rider = getControllingPassenger();
        if (!(rider instanceof EntityPlayer player)) return false;
        return ItemGeneric.is(player.getHeldItemMainhand(), "Brain on a Stick") || ItemGeneric.is(player.getHeldItemOffhand(), "Brain on a Stick");
    }

    @Override
    public void travel(float strafe, float vertical, float forward) {
        Entity rider = getControllingPassenger();
        if (isBeingRidden() && canBeSteered() && rider != null) {
            rotationYaw = rider.rotationYaw;
            prevRotationYaw = rotationYaw;
            rotationPitch = rider.rotationPitch * 0.5F;
            setRotation(rotationYaw, rotationPitch);
            renderYawOffset = rotationYaw;
            rotationYawHead = rotationYaw;
            stepHeight = 1.0F;
            jumpMovementFactor = getAIMoveSpeed() * 0.1F;
            if (canPassengerSteer()) {
                setAIMoveSpeed((float) getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue() * 0.8F);
                super.travel(0.0F, 0.0F, 1.0F);
            } else {
                motionX = 0.0D;
                motionY = 0.0D;
                motionZ = 0.0D;
            }
            prevLimbSwingAmount = limbSwingAmount;
            double dx = posX - prevPosX;
            double dz = posZ - prevPosZ;
            float f = MathHelper.sqrt(dx * dx + dz * dz) * 4.0F;
            if (f > 1.0F) f = 1.0F;
            limbSwingAmount += (f - limbSwingAmount) * 0.4F;
            limbSwing += limbSwingAmount;
        } else {
            stepHeight = 0.5F;
            jumpMovementFactor = 0.02F;
            super.travel(strafe, vertical, forward);
        }
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (!world.isRemote) {
            EntityLivingBase owner = getOwner();
            if (owner != null) {
                List<EntityPlayer> list = world.getEntitiesWithinAABB(EntityPlayer.class, getEntityBoundingBox().grow(10D, 4.0D, 10D));
                NBTTagCompound nbt = owner.getEntityData();
                isAgressive = nbt.getBoolean("aggressive");
                for (EntityPlayer player : list) {
                    String relation = nbt.getString(player.getName());
                    if (relation.equals("enemy")) {
                        setAttackTarget(player);
                    } else if (relation.isEmpty() && isAgressive && player != owner) {
                        setAttackTarget(player);
                    }
                }
            }
        }
    }

    @Override
    public boolean processInteract(EntityPlayer player, EnumHand hand) {
        if (!world.isRemote) {
            ItemStack held = player.getHeldItem(hand);
            if (!getSaddled()) {
                if (!held.isEmpty() && held.getItem() == Items.SADDLE) {
                    NecroEntityBase mob = getMob(BodyPartLocation.Torso);
                    if (mob instanceof ISaddleAble) {
                        setSaddled(true);
                        if (!player.capabilities.isCreativeMode) {
                            held.shrink(1);
                        }
                        return true;
                    }
                    return false;
                }
            } else if (!isBeingRidden() && isOwner(player)) {
                player.startRiding(this);
                return true;
            }

            if (!isBeingRidden()) {
                if (isOwner(player)) {
                    boolean sit = !isSitting();
                    aiSit.setSitting(sit);
                    setSitting(sit);
                    isJumping = false;
                    navigator.clearPath();
                    setAttackTarget(null);
                    player.sendMessage(new TextComponentString("Minion is " + (sit ? "staying put." : "free to move.")));
                    printMinionInfo();
                } else {
                    EntityLivingBase owner = getOwner();
                    player.sendMessage(new TextComponentString("<Minion> I obey only " + (owner != null ? owner.getName() : "my master")));
                }
            }
        }
        return true;
    }

    @Override
    public double getMountedYOffset() {
        NecroEntityBase mob = getMob(BodyPartLocation.Torso);
        double extra = mob instanceof ISaddleAble saddle ? saddle.riderHeight() / 16.0D : 0.0D;
        return height * 0.75D + extra;
    }

    public boolean getSaddled() {
        return (dataManager.get(SADDLE) & 1) != 0;
    }

    public void setSaddled(boolean saddled) {
        dataManager.set(SADDLE, (byte) (saddled ? 1 : 0));
    }

    public void setBodyPartName(BodyPartLocation location, String name) {
        dataManager.set(PARTS[location.ordinal()], name == null || name.isEmpty() ? UNDEFINED : name);
    }

    public void setBodyPartNames(String[] names) {
        for (int i = 0; i < PARTS.length; i++) {
            String name = i < names.length ? names[i] : null;
            String value = name == null || name.isEmpty() ? UNDEFINED : name;
            if (!value.equals(dataManager.get(PARTS[i]))) {
                dataManager.set(PARTS[i], value);
            }
        }
    }

    public String[] getBodyPartsNames() {
        String[] list = new String[PARTS.length];
        for (int i = 0; i < PARTS.length; i++) {
            list[i] = dataManager.get(PARTS[i]);
        }
        return list;
    }

    @Override
    public String toString() {
        String[] n = getBodyPartsNames();
        return String.format(Locale.ROOT, "%s['%s'/%d, l='%s', x=%.1f, y=%.1f, z=%.1f, head='%s', torso='%s', armLeft='%s', armRight='%s', legs='%s']",
                getClass().getSimpleName(), getName(), getEntityId(), world == null ? "~NULL~" : world.getWorldInfo().getWorldName(),
                posX, posY, posZ, n[0], n[1], n[2], n[3], n[4]);
    }

    @Override
    public EntityAgeable createChild(EntityAgeable ageable) {
        return null;
    }

    @Nullable
    private SoundEvent headSound(String suffix) {
        return LegacySounds.get(("mob." + getBodyPartsNames()[0] + "." + suffix).toLowerCase(Locale.ROOT));
    }

    @Override
    @Nullable
    protected SoundEvent getAmbientSound() {
        return headSound("say");
    }

    @Override
    @Nullable
    protected SoundEvent getHurtSound(DamageSource source) {
        return headSound("hurt");
    }

    @Override
    @Nullable
    protected SoundEvent getDeathSound() {
        return headSound("death");
    }

    @Override
    public void onDeath(DamageSource cause) {
        super.onDeath(cause);
        EntityLivingBase owner = getOwner();
        if (owner != null && !world.isRemote) {
            owner.getEntityData().setInteger("minions", owner.getEntityData().getInteger("minions") - 1);
        }
    }

    @Override
    public void attackEntityWithRangedAttack(EntityLivingBase target, float distanceFactor) {
        if (getBodyPartsNames()[0].equals("Isaac")) {
            playSound(NecroSounds.TEAR, 1.0F, 1.0F / (getRNG().nextFloat() * 0.4F + 0.8F));
            world.spawnEntity(rand.nextInt(5) == 0 ? new EntityTearBlood(world, this, target) : new EntityTear(world, this, target));
        }
    }

    @Override
    public void setSwingingArms(boolean swingingArms) {
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();

        if (attackTimer > 0) {
            attackTimer--;
        } else if (!world.isRemote && getAttackTarget() != null && rangedAttackTimer == 0) {
            attackEntityWithRangedAttack(getAttackTarget(), 0);
            rangedAttackTimer = 60;
        }

        if (rangedAttackTimer > 0) {
            rangedAttackTimer--;
        }
    }

    @Override
    public void handleStatusUpdate(byte id) {
        if (id == 4) {
            attackTimer = 10;
        } else {
            super.handleStatusUpdate(id);
        }
    }

    public int getAttackTimer() {
        return attackTimer;
    }
}

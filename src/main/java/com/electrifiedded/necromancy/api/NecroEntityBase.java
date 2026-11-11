package com.electrifiedded.necromancy.api;

import com.electrifiedded.necromancy.entity.EntityMinion;
import com.electrifiedded.necromancy.item.RegistryNecromancyItems;
import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class NecroEntityBase {

    public String mobName;
    public ResourceLocation texture;
    public ItemStack headItem;
    public ItemStack torsoItem;
    public ItemStack armItem;
    public ItemStack legItem;
    public Object[] headRecipe;
    public Object[] torsoRecipe;
    public Object[] armRecipe;
    public Object[] legRecipe;
    public boolean hasHead;
    public boolean hasTorso;
    public boolean hasArms;
    public boolean hasLegs;
    public Item organs;
    public int textureWidth;
    public int textureHeight;
    protected boolean isNecromancyInstalled;

    @SideOnly(Side.CLIENT)
    public BodyPart[] head;
    @SideOnly(Side.CLIENT)
    public BodyPart[] torso;
    @SideOnly(Side.CLIENT)
    public BodyPart[] armLeft;
    @SideOnly(Side.CLIENT)
    public BodyPart[] armRight;
    @SideOnly(Side.CLIENT)
    public BodyPart[] legs;

    private boolean modelInit;

    public NecroEntityBase(String mobName) {
        this.mobName = mobName;
        hasHead = true;
        hasTorso = true;
        hasArms = true;
        hasLegs = true;
        textureWidth = 64;
        textureHeight = 32;
        isNecromancyInstalled = true;
        organs = RegistryNecromancyItems.organs;
        initRecipes();
        modelInit = false;
    }

    public void initRecipes() {
    }

    public void initDefaultRecipes(Object... items) {
        Object headItem, torsoItem, armItem, legItem;
        if (items.length == 1) {
            headItem = torsoItem = armItem = legItem = items[0];
        } else {
            headItem = items[0];
            torsoItem = items[1];
            armItem = items[2];
            legItem = items[3];
        }
        headRecipe = new Object[]{"SSSS", "SBFS", "SEES", 'S', new ItemStack(organs, 1, 4),
                'E', Items.SPIDER_EYE, 'F', headItem, 'B', new ItemStack(organs, 1, 0)
        };
        torsoRecipe = new Object[]{" LL ", "BHUB", "LEEL", "BLLB", 'L', new ItemStack(organs, 1, 4),
                'E', torsoItem, 'H', new ItemStack(organs, 1, 1),
                'U', new ItemStack(organs, 1, 3),
                'B', Items.BONE};
        armRecipe = new Object[]{"LLLL", "BMEB", "LLLL", 'L', new ItemStack(organs, 1, 4),
                'E', armItem, 'M', new ItemStack(organs, 1, 2),
                'B', Items.BONE};
        legRecipe = new Object[]{"LBBL", "LMML", "LEEL", "LBBL", 'L', new ItemStack(organs, 1, 4),
                'E', legItem, 'M', new ItemStack(organs, 1, 2),
                'B', Items.BONE};
    }

    @SideOnly(Side.CLIENT)
    public BodyPart[] initHead(ModelBase model) {
        return null;
    }

    @SideOnly(Side.CLIENT)
    public BodyPart[] initTorso(ModelBase model) {
        return null;
    }

    @SideOnly(Side.CLIENT)
    public BodyPart[] initLegs(ModelBase model) {
        return null;
    }

    @SideOnly(Side.CLIENT)
    public BodyPart[] initArmLeft(ModelBase model) {
        return null;
    }

    @SideOnly(Side.CLIENT)
    public BodyPart[] initArmRight(ModelBase model) {
        return null;
    }

    @SideOnly(Side.CLIENT)
    public NecroEntityBase updateParts(ModelBase model) {
        if (!modelInit) {
            head = initHead(model);
            torso = initTorso(model);
            armLeft = initArmLeft(model);
            armRight = initArmRight(model);
            legs = initLegs(model);
            modelInit = true;
        }
        return this;
    }

    @SideOnly(Side.CLIENT)
    public BodyPart[] getParts(BodyPartLocation location, ModelBase model) {
        updateParts(model);
        return switch (location) {
            case Head -> head;
            case Torso -> torso;
            case ArmLeft -> armLeft;
            case ArmRight -> armRight;
            case Legs -> legs;
        };
    }

    public void setAttributes(EntityLiving minion, BodyPartLocation location) {
        addAttributeMods(minion, "default", 2D, 1D, 0D, 2D, 2D);
    }

    protected void addAttributeMods(EntityLiving entity, String bodyPart, double health, double followRange, double knockBackResistance, double movementSpeed, double attackDamage) {
        if (health != 0D) {
            apply(entity.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH), bodyPart + "FPMod", health);
        }
        if (followRange != 0D) {
            apply(entity.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE), bodyPart + "FRMod", followRange);
        }
        if (knockBackResistance != 0D) {
            apply(entity.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE), bodyPart + "KBRMod", knockBackResistance);
        }
        if (movementSpeed != 0D) {
            apply(entity.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED), bodyPart + "MOVMod", movementSpeed);
        }
        if (attackDamage != 0D) {
            apply(entity.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE), bodyPart + "DMGMod", attackDamage);
        }
    }

    private static void apply(IAttributeInstance attribute, String name, double amount) {
        if (attribute == null) return;
        attribute.applyModifier(new AttributeModifier(name, amount, 1));
    }

    @SideOnly(Side.CLIENT)
    public void setRotationAngles(float par1, float par2, float par3, float par4, float par5, float par6, Entity entity, BodyPart[] part, BodyPartLocation location) {
    }

    @SideOnly(Side.CLIENT)
    public void preRender(Entity entity, BodyPart[] parts, BodyPartLocation location, ModelBase model) {
    }

    @SideOnly(Side.CLIENT)
    public void postRender(Entity entity, BodyPart[] parts, BodyPartLocation location, ModelBase model) {
    }

    public void attackEntityAsMob(EntityMinion minion, BodyPartLocation location, Entity target, float damage) {
    }
}

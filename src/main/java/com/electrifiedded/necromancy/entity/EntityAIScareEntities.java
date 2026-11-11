package com.electrifiedded.necromancy.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.entity.monster.IMob;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.List;

public class EntityAIScareEntities extends EntityAIBase {
    private EntityLiving target;
    private final EntityTeddy entity;
    private final float seekingRange;
    private final float scaringRange;
    private final Class<? extends Entity> targetSpecs;
    private final List<Entity> targets = new ArrayList<>();
    private final PathNavigate pathFinderSelf;

    public EntityAIScareEntities(EntityLiving entity, float seekingRange, float scaringRange, float speed, Class<? extends Entity> targetClass) {
        this.entity = (EntityTeddy) entity;
        this.seekingRange = seekingRange;
        this.scaringRange = scaringRange;
        targetSpecs = targetClass;
        pathFinderSelf = entity.getNavigator();
    }

    @Override
    public boolean shouldExecute() {
        if (entity.entityState != EntityTeddy.EntityState.DEFENDING) return false;
        targets.clear();
        for (Entity e : entity.world.getEntitiesWithinAABB(targetSpecs, entity.getEntityBoundingBox().grow(seekingRange, seekingRange, seekingRange))) {
            if (e instanceof IMob && e instanceof EntityLiving) {
                targets.add(e);
            }
        }
        return true;
    }

    @Override
    public void updateTask() {
        if ((target = getClosestEntity()) != null) {
            PathNavigate pathFinderTarget = target.getNavigator();
            pathFinderSelf.tryMoveToEntityLiving(target, entity.getAIMoveSpeed());
            if (entity.getDistance(target) < scaringRange && target instanceof EntityCreature creature) {
                Vec3d away = RandomPositionGenerator.findRandomTargetBlockAwayFrom(creature, 16, 7, new Vec3d(entity.posX, entity.posY, entity.posZ));
                if (away != null && entity.getDistanceSq(away.x, away.y, away.z) >= entity.getDistanceSq(target)) {
                    Path path = pathFinderTarget.getPathToXYZ(away.x, away.y, away.z);
                    if (path != null) {
                        pathFinderTarget.setPath(path, 0.4F);
                    }
                }
            }
        }
    }

    @Override
    public boolean shouldContinueExecuting() {
        return target != null && entity.entityState == EntityTeddy.EntityState.DEFENDING;
    }

    private EntityLiving getClosestEntity() {
        EntityLiving closest = null;
        double range = seekingRange + 1.0F;
        for (Entity e : targets) {
            if (e.isDead) continue;
            double d = entity.getDistance(e);
            if (d < range) {
                range = d;
                closest = (EntityLiving) e;
            }
        }
        return closest;
    }
}

package com.electrifiedded.necromancy.core.handler;

import com.electrifiedded.necromancy.block.RegistryBlocksNecromancy;
import com.electrifiedded.necromancy.entity.EntityIsaacNormal;
import com.electrifiedded.necromancy.entity.EntityNightCrawler;
import com.electrifiedded.necromancy.item.ItemGeneric;
import com.electrifiedded.necromancy.item.RegistryNecromancyItems;
import com.electrifiedded.necromancy.lib.ConfigurationNecromancy;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.event.entity.player.FillBucketEvent;
import net.minecraftforge.fluids.BlockFluidBase;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.ItemCraftedEvent;

public class ForgeEventHandler {

    @SubscribeEvent
    public void onPotentialSpawns(LivingSpawnEvent.SpecialSpawn event) {
        World world = event.getWorld();
        if (world.isRemote) return;
        Class<?> cls = event.getEntityLiving().getClass();
        if (cls.equals(EntityZombie.class) || cls.equals(EntitySkeleton.class)) {
            if (world.rand.nextInt(ConfigurationNecromancy.rarityNightCrawlers) == 0) {
                event.setCanceled(true);
                EntityNightCrawler enc = new EntityNightCrawler(world);
                enc.setPosition(event.getX(), event.getY(), event.getZ());
                world.spawnEntity(enc);
                event.getEntityLiving().setDead();
            } else if (world.rand.nextInt(ConfigurationNecromancy.rarityIsaacs) == 0) {
                event.setCanceled(true);
                EntityIsaacNormal ein = new EntityIsaacNormal(world);
                ein.setPosition(event.getX(), event.getY(), event.getZ());
                world.spawnEntity(ein);
                event.getEntityLiving().setDead();
            }
        }
    }

    @SubscribeEvent
    public void onLivingDeath(LivingDeathEvent evt) {
        if (evt.getEntity() instanceof EntityLiving && !evt.getEntity().world.isRemote) {
            int meta = switch (evt.getEntity().world.rand.nextInt(100)) {
                case 0 -> 0;
                case 1 -> 1;
                case 2, 3, 4, 5 -> 2;
                case 6 -> 3;
                default -> -1;
            };
            if (meta >= 0) {
                evt.getEntity().entityDropItem(new ItemStack(RegistryNecromancyItems.organs, 1, meta), 1);
            }
        }
    }

    @SubscribeEvent
    public void onCrafting(ItemCraftedEvent event) {
        ItemStack item = event.crafting;
        EntityPlayer player = event.player;
        if (item.isEmpty() || player.world.isRemote) return;
        if (ItemGeneric.is(item, "Jar of Blood")) {
            give(player, new ItemStack(Items.BUCKET));
        } else if (item.getItem() == RegistryNecromancyItems.bucketBlood) {
            give(player, new ItemStack(Items.GLASS_BOTTLE, 8));
        }
    }

    private static void give(EntityPlayer player, ItemStack stack) {
        if (!player.inventory.addItemStackToInventory(stack)) {
            player.dropItem(stack, false);
        }
    }

    @SubscribeEvent
    public void onBucketFill(FillBucketEvent event) {
        RayTraceResult target = event.getTarget();
        if (target == null || target.typeOfHit != RayTraceResult.Type.BLOCK) return;
        if (event.getEmptyBucket().getItem() != Items.BUCKET) return;
        World world = event.getWorld();
        BlockPos pos = target.getBlockPos();
        IBlockState state = world.getBlockState(pos);
        if ((state.getBlock() == RegistryBlocksNecromancy.blood || state.getBlock() == RegistryBlocksNecromancy.fluidBlood.getBlock())
                && state.getValue(BlockFluidBase.LEVEL) == 0) {
            world.setBlockToAir(pos);
            event.setFilledBucket(new ItemStack(RegistryNecromancyItems.bucketBlood));
            event.setResult(Result.ALLOW);
        }
    }
}

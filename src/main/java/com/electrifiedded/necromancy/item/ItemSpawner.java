package com.electrifiedded.necromancy.item;

import com.electrifiedded.necromancy.Necromancy;
import com.electrifiedded.necromancy.lib.ReferenceNecromancy;
import net.minecraft.block.BlockFence;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class ItemSpawner extends Item {
    private final String[] necroNames = new String[]{"IsaacNormal", "teddyNecro", "NightCrawler"};

    public ItemSpawner() {
        super();
        setCreativeTab(Necromancy.tabNecromancy);
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (world.isRemote) return EnumActionResult.SUCCESS;
        ItemStack stack = player.getHeldItem(hand);
        boolean fence = world.getBlockState(pos).getBlock() instanceof BlockFence;
        BlockPos p = pos.offset(facing);
        double d0 = facing == EnumFacing.UP && fence ? 0.5D : 0.0D;
        Entity entity = spawnCreature(world, necroNames[player.getRNG().nextInt(necroNames.length)], p.getX() + 0.5D, p.getY() + d0, p.getZ() + 0.5D);
        if (entity != null && !player.capabilities.isCreativeMode) {
            stack.shrink(1);
        }
        return EnumActionResult.SUCCESS;
    }

    private Entity spawnCreature(World world, String name, double x, double y, double z) {
        Entity entity = EntityList.createEntityByIDFromName(new ResourceLocation(ReferenceNecromancy.MOD_ID, name), world);
        if (entity instanceof EntityLiving living) {
            entity.setLocationAndAngles(x, y, z, MathHelper.wrapDegrees(world.rand.nextFloat() * 360.0F), 0.0F);
            living.rotationYawHead = living.rotationYaw;
            living.renderYawOffset = living.rotationYaw;
            living.onInitialSpawn(world.getDifficultyForLocation(new BlockPos(living)), null);
            world.spawnEntity(entity);
            living.playLivingSound();
        }
        return entity;
    }
}

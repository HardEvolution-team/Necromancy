package com.electrifiedded.necromancy.tileentity;

import com.electrifiedded.necromancy.Necromancy;
import com.electrifiedded.necromancy.api.BodyPartLocation;
import com.electrifiedded.necromancy.api.NecroEntityBase;
import com.electrifiedded.necromancy.api.NecroEntityRegistry;
import com.electrifiedded.necromancy.entity.EntityMinion;
import com.electrifiedded.necromancy.item.ItemGeneric;
import com.electrifiedded.necromancy.sound.NecroSounds;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityAltar extends TileEntity implements IInventory {
    private final NonNullList<ItemStack> altarItemStacks = NonNullList.withSize(7, ItemStack.EMPTY);

    @SideOnly(Side.CLIENT)
    private EntityMinion preview;

    @Override
    public boolean isUsableByPlayer(EntityPlayer player) {
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getRenderBoundingBox() {
        return TileEntity.INFINITE_EXTENT_AABB;
    }

    public void spawn(EntityPlayer user) {
        if (world.isRemote) return;
        if (Necromancy.instance.maxSpawn != -1 && user.getEntityData().getInteger("minions") >= Necromancy.instance.maxSpawn) {
            user.sendMessage(new TextComponentString("<Death> Mortal fool! Thou shan't never grow that strong."));
            world.addWeatherEffect(new EntityLightningBolt(world, pos.getX(), pos.getY(), pos.getZ(), false));
            return;
        }
        EntityMinion minionSpawned = new EntityMinion(world, getPartNames(), user);
        minionSpawned.setPosition(pos.getX(), pos.getY() + 1, pos.getZ());
        minionSpawned.calculateAttributes();
        world.spawnEntity(minionSpawned);
        Necromancy.loggerNecromancy.info(minionSpawned.toString());
        user.sendMessage(new TextComponentString("<Minion> Your bidding?"));
        if (!user.capabilities.isCreativeMode) {
            for (int x = 0; x < 7; x++) {
                decrStackSize(x, 1);
            }
        }
        user.getEntityData().setInteger("minions", user.getEntityData().getInteger("minions") + 1);
        minionSpawned.playSound(NecroSounds.SPAWN, 1.0F, 1.0F / (minionSpawned.getRNG().nextFloat() * 0.4F + 0.8F));
        markDirty();
    }

    public boolean canSpawn() {
        return ItemGeneric.is(getStackInSlot(0), "Jar of Blood") && ItemGeneric.is(getStackInSlot(1), "Soul in a Jar");
    }

    public String[] getPartNames() {
        return new String[]{
                getPartName(getStackInSlot(2), BodyPartLocation.Head),
                getPartName(getStackInSlot(3), BodyPartLocation.Torso),
                getPartName(getStackInSlot(6), BodyPartLocation.ArmLeft),
                getPartName(getStackInSlot(5), BodyPartLocation.ArmRight),
                getPartName(getStackInSlot(4), BodyPartLocation.Legs)};
    }

    public static String getPartName(ItemStack stack, BodyPartLocation location) {
        if (stack.isEmpty()) return EntityMinion.UNDEFINED;
        for (NecroEntityBase mob : NecroEntityRegistry.registeredEntities.values()) {
            boolean match = switch (location) {
                case Head -> mob.hasHead && matches(stack, mob.headItem);
                case Torso -> mob.hasTorso && matches(stack, mob.torsoItem);
                case ArmLeft, ArmRight -> mob.hasArms && matches(stack, mob.armItem);
                case Legs -> mob.hasLegs && matches(stack, mob.legItem);
            };
            if (match) return mob.mobName;
        }
        return EntityMinion.UNDEFINED;
    }

    private static boolean matches(ItemStack stack, ItemStack part) {
        return part != null && !part.isEmpty() && stack.isItemEqual(part);
    }

    @SideOnly(Side.CLIENT)
    public EntityMinion getPreviewEntity() {
        if (preview == null || preview.world != world) {
            preview = new EntityMinion(world);
        }
        preview.setBodyPartNames(getPartNames());
        return preview;
    }

    @Override
    public void markDirty() {
        super.markDirty();
        if (world != null && !world.isRemote) {
            IBlockState state = world.getBlockState(pos);
            world.notifyBlockUpdate(pos, state, state, 3);
        }
    }

    @Override
    public void openInventory(EntityPlayer player) {
    }

    @Override
    public void closeInventory(EntityPlayer player) {
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        altarItemStacks.clear();
        NBTTagList list = tag.getTagList("Items", 10);
        for (int i = 0; i < list.tagCount(); i++) {
            NBTTagCompound t = list.getCompoundTagAt(i);
            int slot = t.getByte("Slot");
            if (slot >= 0 && slot < altarItemStacks.size()) {
                altarItemStacks.set(slot, new ItemStack(t));
            }
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);
        NBTTagList list = new NBTTagList();
        for (int i = 0; i < altarItemStacks.size(); i++) {
            ItemStack stack = altarItemStacks.get(i);
            if (!stack.isEmpty()) {
                NBTTagCompound t = new NBTTagCompound();
                t.setByte("Slot", (byte) i);
                stack.writeToNBT(t);
                list.appendTag(t);
            }
        }
        tag.setTag("Items", list);
        return tag;
    }

    @Override
    public NBTTagCompound getUpdateTag() {
        return writeToNBT(new NBTTagCompound());
    }

    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        return new SPacketUpdateTileEntity(pos, 0, getUpdateTag());
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        readFromNBT(pkt.getNbtCompound());
    }

    @Override
    public ItemStack decrStackSize(int slot, int amount) {
        ItemStack stack = ItemStackHelper.getAndSplit(altarItemStacks, slot, amount);
        if (!stack.isEmpty()) markDirty();
        return stack;
    }

    @Override
    public ItemStack removeStackFromSlot(int slot) {
        return ItemStackHelper.getAndRemove(altarItemStacks, slot);
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack stack) {
        altarItemStacks.set(slot, stack);
        if (!stack.isEmpty() && stack.getCount() > getInventoryStackLimit()) {
            stack.setCount(getInventoryStackLimit());
        }
        markDirty();
    }

    @Override
    public String getName() {
        return "Altar";
    }

    @Override
    public boolean hasCustomName() {
        return false;
    }

    @Override
    public int getSizeInventory() {
        return altarItemStacks.size();
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack s : altarItemStacks) if (!s.isEmpty()) return false;
        return true;
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        return altarItemStacks.get(slot);
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public boolean isItemValidForSlot(int i, ItemStack itemstack) {
        return false;
    }

    @Override
    public int getField(int id) {
        return 0;
    }

    @Override
    public void setField(int id, int value) {
    }

    @Override
    public int getFieldCount() {
        return 0;
    }

    @Override
    public void clear() {
        altarItemStacks.clear();
    }
}

package com.electrifiedded.necromancy.inventory;

import com.electrifiedded.necromancy.block.RegistryBlocksNecromancy;
import com.electrifiedded.necromancy.item.ItemGeneric;
import com.electrifiedded.necromancy.tileentity.TileEntityAltar;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ContainerAltar extends Container {
    private final TileEntityAltar altar;
    private final World world;
    private final BlockPos pos;

    public ContainerAltar(InventoryPlayer inventory, TileEntityAltar tileEntityAltar) {
        world = tileEntityAltar.getWorld();
        pos = tileEntityAltar.getPos();
        altar = tileEntityAltar;
        addSlotToContainer(new Slot(altar, 0, 26, 40));
        addSlotToContainer(new Slot(altar, 1, 134, 39));
        addSlotToContainer(new Slot(altar, 2, 80, 19));
        addSlotToContainer(new Slot(altar, 3, 80, 36));
        addSlotToContainer(new Slot(altar, 4, 80, 53));
        addSlotToContainer(new Slot(altar, 5, 63, 36));
        addSlotToContainer(new Slot(altar, 6, 97, 36));
        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 9; ++col) {
                addSlotToContainer(new Slot(inventory, col + row * 9 + 9, 8 + col * 18, 84 + row * 18));
            }
        }
        for (int col = 0; col < 9; ++col) {
            addSlotToContainer(new Slot(inventory, col, 8 + col * 18, 142));
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return world.getBlockState(pos).getBlock() == RegistryBlocksNecromancy.altar
                && player.getDistanceSq(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D) <= 64D;
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int index) {
        ItemStack copy = ItemStack.EMPTY;
        Slot slot = inventorySlots.get(index);
        if (slot != null && slot.getHasStack()) {
            ItemStack stack = slot.getStack();
            copy = stack.copy();
            if (index <= 6) {
                if (!mergeItemStack(stack, 7, 43, false)) return ItemStack.EMPTY;
            } else if (ItemGeneric.is(stack, "Soul in a Jar")) {
                if (!mergeItemStack(stack, 1, 2, true)) return ItemStack.EMPTY;
            } else if (ItemGeneric.is(stack, "Jar of Blood")) {
                if (!mergeItemStack(stack, 0, 1, true)) return ItemStack.EMPTY;
            } else {
                return ItemStack.EMPTY;
            }
            if (stack.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }
            if (stack.getCount() == copy.getCount()) return ItemStack.EMPTY;
            slot.onTake(player, stack);
        }
        return copy;
    }
}

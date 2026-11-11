package com.electrifiedded.necromancy.inventory;

import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;

public class SlotSewingRequirements extends Slot {
    private final ContainerSewing machine;

    public SlotSewingRequirements(IInventory inventory, int index, int x, int y, Container container) {
        super(inventory, index, x, y);
        machine = (ContainerSewing) container;
    }

    @Override
    public void onSlotChanged() {
        super.onSlotChanged();
        machine.onInventoryChanged();
    }
}

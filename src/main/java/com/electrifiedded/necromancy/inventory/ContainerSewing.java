package com.electrifiedded.necromancy.inventory;

import com.electrifiedded.necromancy.Necromancy;
import com.electrifiedded.necromancy.block.RegistryBlocksNecromancy;
import com.electrifiedded.necromancy.item.ItemGeneric;
import com.electrifiedded.necromancy.tileentity.TileEntitySewing;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCraftResult;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.SPacketSetSlot;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ContainerSewing extends Container {
    private final TileEntitySewing sewing;
    public InventoryCrafting craftMatrix;
    public InventoryCraftResult craftResult;
    private final World world;
    private final BlockPos pos;
    private final EntityPlayer player;
    private final Slot resultSlot;

    public ContainerSewing(InventoryPlayer inventory, TileEntity sewingTile) {
        sewing = (TileEntitySewing) sewingTile;
        player = inventory.player;
        craftMatrix = new InventoryCrafting(this, 4, 4);
        craftResult = new InventoryCraftResult();
        world = sewingTile.getWorld();
        pos = sewingTile.getPos();
        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 9; ++col) {
                addSlotToContainer(new Slot(inventory, col + row * 9 + 9, 8 + col * 18, 84 + row * 18));
            }
        }
        for (int col = 0; col < 9; ++col) {
            addSlotToContainer(new Slot(inventory, col, 8 + col * 18, 142));
        }
        for (int x = 0; x < 4; x++) {
            for (int y = 0; y < 4; y++) {
                addSlotToContainer(new Slot(craftMatrix, y + x * 4, 8 + y * 18, 8 + x * 18));
            }
        }
        addSlotToContainer(new SlotSewingRequirements(sewing, 0, 95, 17, this));
        addSlotToContainer(new SlotSewingRequirements(sewing, 1, 95, 54, this));
        resultSlot = addSlotToContainer(new SlotSewing(inventory.player, craftMatrix, craftResult, sewing, this, 0, 145, 35));
        onCraftMatrixChanged(craftMatrix);
    }

    public void onInventoryChanged() {
        craftResult.setInventorySlotContents(0, ItemStack.EMPTY);
        onCraftMatrixChanged(craftMatrix);
    }

    @Override
    public void onCraftMatrixChanged(IInventory inventory) {
        super.onCraftMatrixChanged(inventory);
        ItemStack result = ItemStack.EMPTY;
        if (ItemGeneric.is(sewing.getStackInSlot(0), "Bone Needle")
                && !sewing.getStackInSlot(1).isEmpty() && sewing.getStackInSlot(1).getItem() == Items.STRING
                && Necromancy.instance.sewingRecipeHandler != null) {
            result = Necromancy.instance.sewingRecipeHandler.findMatchingRecipe(craftMatrix, world);
        }
        craftResult.setInventorySlotContents(0, result);
        if (!world.isRemote && resultSlot != null && windowId != 0 && player instanceof EntityPlayerMP mp) {
            mp.connection.sendPacket(new SPacketSetSlot(windowId, resultSlot.slotNumber, result));
        }
    }

    @Override
    public void onContainerClosed(EntityPlayer player) {
        super.onContainerClosed(player);
        if (!world.isRemote) {
            clearContainer(player, world, craftMatrix);
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return world.getBlockState(pos).getBlock() == RegistryBlocksNecromancy.sewing
                && player.getDistanceSq(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D) <= 64D;
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int index) {
        ItemStack copy = ItemStack.EMPTY;
        Slot slot = inventorySlots.get(index);
        if (slot != null && slot.getHasStack()) {
            ItemStack stack = slot.getStack();
            copy = stack.copy();
            if (index >= 36 && index <= 54) {
                if (!mergeItemStack(stack, 0, 36, index == 54)) return ItemStack.EMPTY;
                if (index == 54) slot.onSlotChange(stack, copy);
            } else if (index < 36 && !mergeItemStack(stack, 36, 52, false)) {
                return ItemStack.EMPTY;
            }
            if (stack.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }
            if (stack.getCount() == copy.getCount()) return ItemStack.EMPTY;
            ItemStack taken = slot.onTake(player, stack);
            if (index == 54) player.dropItem(taken, false);
        }
        return copy;
    }

    @Override
    public boolean canMergeSlot(ItemStack stack, Slot slot) {
        return slot.inventory != craftResult && super.canMergeSlot(stack, slot);
    }
}

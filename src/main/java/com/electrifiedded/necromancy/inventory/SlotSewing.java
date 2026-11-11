package com.electrifiedded.necromancy.inventory;

import com.electrifiedded.necromancy.tileentity.TileEntitySewing;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.SlotCrafting;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerDestroyItemEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class SlotSewing extends SlotCrafting {
    private final InventoryCrafting craftMatrix;
    private final EntityPlayer thePlayer;
    private final TileEntitySewing sewing;
    private final ContainerSewing containerSewing;

    public SlotSewing(EntityPlayer player, InventoryCrafting craftMatrix, IInventory result, TileEntitySewing sewing, ContainerSewing containerSewing, int index, int x, int y) {
        super(player, craftMatrix, result, index, x, y);
        thePlayer = player;
        this.craftMatrix = craftMatrix;
        this.sewing = sewing;
        this.containerSewing = containerSewing;
    }

    @Override
    public ItemStack onTake(EntityPlayer player, ItemStack stack) {
        FMLCommonHandler.instance().firePlayerCraftingEvent(player, stack, craftMatrix);
        onCrafting(stack);
        ForgeHooks.setCraftingPlayer(player);
        for (int i = 0; i < craftMatrix.getSizeInventory(); ++i) {
            ItemStack ingredient = craftMatrix.getStackInSlot(i);
            if (ingredient.isEmpty()) continue;
            ItemStack container = ingredient.getItem().hasContainerItem(ingredient) ? ingredient.getItem().getContainerItem(ingredient) : ItemStack.EMPTY;
            craftMatrix.decrStackSize(i, 1);
            if (!container.isEmpty()) {
                if (container.isItemStackDamageable() && container.getItemDamage() > container.getMaxDamage()) {
                    MinecraftForge.EVENT_BUS.post(new PlayerDestroyItemEvent(thePlayer, container, null));
                    continue;
                }
                if (craftMatrix.getStackInSlot(i).isEmpty()) {
                    craftMatrix.setInventorySlotContents(i, container);
                } else if (!thePlayer.inventory.addItemStackToInventory(container)) {
                    thePlayer.dropItem(container, false);
                }
            }
        }
        ForgeHooks.setCraftingPlayer(null);
        sewing.decrStackSize(0, 1);
        sewing.decrStackSize(1, 1);
        containerSewing.onCraftMatrixChanged(craftMatrix);
        return stack;
    }
}

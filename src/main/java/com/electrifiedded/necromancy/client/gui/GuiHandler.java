package com.electrifiedded.necromancy.client.gui;

import com.electrifiedded.necromancy.block.BlockAltar;
import com.electrifiedded.necromancy.block.BlockSewing;
import com.electrifiedded.necromancy.inventory.ContainerAltar;
import com.electrifiedded.necromancy.inventory.ContainerSewing;
import com.electrifiedded.necromancy.tileentity.TileEntityAltar;
import com.electrifiedded.necromancy.tileentity.TileEntitySewing;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler {

    @Override
    public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        TileEntity te = world.getTileEntity(new BlockPos(x, y, z));
        if (id == BlockAltar.guiID && te instanceof TileEntityAltar altar) return new ContainerAltar(player.inventory, altar);
        if (id == BlockSewing.guiID && te instanceof TileEntitySewing) return new ContainerSewing(player.inventory, te);
        return null;
    }

    @Override
    public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        TileEntity te = world.getTileEntity(new BlockPos(x, y, z));
        if (id == BlockAltar.guiID && te instanceof TileEntityAltar altar) return new GuiAltar(player.inventory, altar);
        if (id == BlockSewing.guiID && te instanceof TileEntitySewing sewing) return new GuiSewing(player.inventory, sewing);
        return null;
    }
}

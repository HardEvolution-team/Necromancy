package com.electrifiedded.necromancy.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

public class TileEntitySkullWall extends TileEntity {
    private String skullType = "Skeleton";
    private int blockColor = 0;

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);
        tag.setString("SkullType", skullType);
        tag.setInteger("BlockColor", blockColor);
        return tag;
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        skullType = tag.getString("SkullType");
        blockColor = tag.getInteger("BlockColor");
    }

    @Override
    public NBTTagCompound getUpdateTag() {
        return writeToNBT(new NBTTagCompound());
    }

    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        return new SPacketUpdateTileEntity(pos, 4, getUpdateTag());
    }
}

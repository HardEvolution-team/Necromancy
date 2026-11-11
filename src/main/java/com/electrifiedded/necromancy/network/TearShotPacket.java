package com.electrifiedded.necromancy.network;

import com.electrifiedded.archaiclib.v1710.api.network.AbstractPacket;
import com.electrifiedded.necromancy.entity.EntityTear;
import com.electrifiedded.necromancy.entity.EntityTearBlood;
import com.electrifiedded.necromancy.item.RegistryNecromancyItems;
import com.electrifiedded.necromancy.sound.NecroSounds;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraftforge.fml.relauncher.Side;

public class TearShotPacket extends AbstractPacket {
    private boolean blood;

    public TearShotPacket() {
    }

    public TearShotPacket(boolean blood) {
        this.blood = blood;
    }

    @Override
    public void writeTo(ByteBuf buffer, Side side) {
        buffer.writeBoolean(blood);
    }

    @Override
    public void readFrom(ByteBuf buffer, Side side) {
        blood = buffer.readBoolean();
    }

    @Override
    public void execute(Side side, EntityPlayer player) {
        if (side != Side.SERVER || player == null) return;
        ItemStack head = player.getItemStackFromSlot(EntityEquipmentSlot.HEAD);
        if (head.isEmpty() || head.getItem() != RegistryNecromancyItems.isaacsHead) return;
        if (blood) {
            if (player.getHealth() > 0.5F) {
                player.attackEntityFrom(DamageSource.STARVE, 0.5F);
            } else {
                return;
            }
        } else {
            if (player.getFoodStats().getFoodLevel() > 3) {
                player.getFoodStats().addExhaustion(3F);
            } else {
                return;
            }
        }
        EntityTear tear = blood ? new EntityTearBlood(player.world, player) : new EntityTear(player.world, player);
        player.world.spawnEntity(tear);
        tear.playSound(NecroSounds.TEAR, 1.0F, 1.0F / (player.getRNG().nextFloat() * 0.4F + 0.8F));
    }
}

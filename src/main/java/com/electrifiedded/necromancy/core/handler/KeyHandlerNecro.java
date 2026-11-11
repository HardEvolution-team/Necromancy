package com.electrifiedded.necromancy.core.handler;

import com.electrifiedded.necromancy.item.RegistryNecromancyItems;
import com.electrifiedded.necromancy.network.NecroNetwork;
import com.electrifiedded.necromancy.network.TearShotPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

@SideOnly(Side.CLIENT)
public class KeyHandlerNecro {
    private final Minecraft mc;
    public static KeyBinding tearNormal = new KeyBinding("Shoot Normal Tear", Keyboard.KEY_F, "key.categories.gameplay");
    public static KeyBinding tearBlood = new KeyBinding("Shoot Blood Tear", Keyboard.KEY_G, "key.categories.gameplay");
    public long nextShot = 0;

    public KeyHandlerNecro() {
        ClientRegistry.registerKeyBinding(tearNormal);
        ClientRegistry.registerKeyBinding(tearBlood);
        mc = Minecraft.getMinecraft();
    }

    @SubscribeEvent
    public void onTick(ClientTickEvent event) {
        EntityPlayer player = mc.player;
        if (player != null && player.isEntityAlive() && event.phase == Phase.END) {
            ItemStack helmet = player.getItemStackFromSlot(EntityEquipmentSlot.HEAD);
            if (mc.currentScreen == null && !helmet.isEmpty() && helmet.getItem() == RegistryNecromancyItems.isaacsHead) {
                if (tearNormal.isKeyDown() && System.currentTimeMillis() > nextShot) {
                    NecroNetwork.channel.sendToServer(new TearShotPacket(false));
                    nextShot = System.currentTimeMillis() + 333L;
                }
                if (tearBlood.isKeyDown() && System.currentTimeMillis() > nextShot) {
                    NecroNetwork.channel.sendToServer(new TearShotPacket(true));
                    nextShot = System.currentTimeMillis() + 333L;
                }
            }
        }
    }
}

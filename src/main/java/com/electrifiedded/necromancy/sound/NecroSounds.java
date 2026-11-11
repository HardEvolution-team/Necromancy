package com.electrifiedded.necromancy.sound;

import com.electrifiedded.necromancy.lib.ReferenceNecromancy;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public final class NecroSounds {
    public static SoundEvent NIGHTCRAWLER_HOWL;
    public static SoundEvent NIGHTCRAWLER_SCREAM;
    public static SoundEvent SPAWN;
    public static SoundEvent TEAR;

    private NecroSounds() {}

    public static void register() {
        NIGHTCRAWLER_HOWL = reg("nightcrawler.howl");
        NIGHTCRAWLER_SCREAM = reg("nightcrawler.scream");
        SPAWN = reg("spawn");
        TEAR = reg("tear");
    }

    private static SoundEvent reg(String name) {
        ResourceLocation loc = new ResourceLocation(ReferenceNecromancy.MOD_ID, name);
        SoundEvent ev = new SoundEvent(loc).setRegistryName(loc);
        ForgeRegistries.SOUND_EVENTS.register(ev);
        return ev;
    }
}

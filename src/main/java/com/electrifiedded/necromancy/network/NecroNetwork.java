package com.electrifiedded.necromancy.network;

import com.electrifiedded.archaiclib.v1710.api.network.ArchaicChannel;
import com.electrifiedded.necromancy.lib.ReferenceNecromancy;

public final class NecroNetwork {
    public static ArchaicChannel channel;

    private NecroNetwork() {}

    @SuppressWarnings("unchecked")
    public static void init() {
        channel = new ArchaicChannel(ReferenceNecromancy.MOD_NAME, TearShotPacket.class);
    }
}

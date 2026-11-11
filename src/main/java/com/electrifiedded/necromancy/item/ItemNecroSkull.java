package com.electrifiedded.necromancy.item;

import com.electrifiedded.necromancy.api.ISkull;
import com.electrifiedded.necromancy.api.NecroEntityBase;
import com.electrifiedded.necromancy.api.NecroEntityRegistry;

public final class ItemNecroSkull {
    public static String[] skullTypes = new String[0];
    public static String[] iconTextures = new String[0];
    public static String[] modelTextures = new String[0];

    private ItemNecroSkull() {}

    public static void initSkulls() {
        int n = NecroEntityRegistry.registeredSkullEntities.size();
        iconTextures = new String[n];
        modelTextures = new String[n];
        skullTypes = new String[n];
        int i = 0;
        for (ISkull mob : NecroEntityRegistry.registeredSkullEntities.values()) {
            iconTextures[i] = mob.getSkullIIconTexture();
            modelTextures[i] = mob.getSkullModelTexture();
            skullTypes[i] = ((NecroEntityBase) mob).mobName;
            i++;
        }
    }
}

package com.electrifiedded.necromancy.api;

import java.util.LinkedHashMap;

public class NecroEntityRegistry {

    public static LinkedHashMap<String, NecroEntityBase> registeredEntities = new LinkedHashMap<>();
    public static LinkedHashMap<String, ISkull> registeredSkullEntities = new LinkedHashMap<>();

    public static void registerEntity(NecroEntityBase data) {
        if (data.isNecromancyInstalled && !registeredEntities.containsKey(data.mobName)) {
            registeredEntities.put(data.mobName, data);
            if (data instanceof ISkull) {
                registeredSkullEntities.put(data.mobName, (ISkull) data);
            }
        }
    }
}

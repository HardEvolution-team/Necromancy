package com.electrifiedded.necromancy.entity;

import com.electrifiedded.necromancy.Necromancy;
import com.electrifiedded.necromancy.api.NecroEntityRegistry;
import com.electrifiedded.necromancy.entity.necroapi.*;
import com.electrifiedded.necromancy.generation.VillageCreationHandler;
import com.electrifiedded.necromancy.lib.ReferenceNecromancy;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.VillagerRegistry.VillagerCareer;
import net.minecraftforge.fml.common.registry.VillagerRegistry.VillagerProfession;

import java.awt.Color;

public class RegistryNecromancyEntities {

    public static final ResourceLocation TEDDY = new ResourceLocation(ReferenceNecromancy.MOD_ID, "teddynecro");
    public static VillagerProfession necromancerProfession;

    public static void initEntities() {
        register(EntityTeddy.class, "teddyNecro", 0, 25, new Color(99, 69, 29).getRGB(), Color.red.getRGB());
        register(EntityNightCrawler.class, "NightCrawler", 1, 25, new Color(6, 6, 6).getRGB(), new Color(13, 13, 13).getRGB());
        register(EntityIsaacNormal.class, "IsaacNormal", 2, 25, new Color(6, 6, 6).getRGB(), new Color(204, 153, 153).getRGB());
        register(EntityIsaacBlood.class, "IsaacBlood", 3, 25, new Color(16, 6, 6).getRGB(), new Color(214, 153, 153).getRGB());
        register(EntityIsaacHead.class, "IsaacHead", 4, 25, new Color(26, 6, 6).getRGB(), new Color(214, 153, 153).getRGB());
        register(EntityIsaacBody.class, "IsaacBody", 5, 25, new Color(36, 6, 6).getRGB(), new Color(214, 153, 153).getRGB());
        register(EntityMinion.class, "minionNecro", 6, 25, -1, -1);
        register(EntityTear.class, "TearNormal", 7, 32, -1, -1);
        register(EntityTearBlood.class, "TearBlood", 8, 32, -1, -1);

        necromancerProfession = new VillagerProfession(ReferenceNecromancy.MOD_ID + ":necromancer",
                ReferenceNecromancy.TEXTURES_ENTITIES_VILLAGER.toString(), ReferenceNecromancy.TEXTURES_ENTITIES_VILLAGER.toString());
        ForgeRegistries.VILLAGER_PROFESSIONS.register(necromancerProfession);
        new VillagerCareer(necromancerProfession, "necromancer").addTrade(1, new VillageCreationHandler.NecromancerTrades());

        NecroEntityRegistry.registerEntity(new NecroEntityCaveSpider());
        NecroEntityRegistry.registerEntity(new NecroEntityChicken());
        NecroEntityRegistry.registerEntity(new NecroEntityCow());
        NecroEntityRegistry.registerEntity(new NecroEntityCreeper());
        NecroEntityRegistry.registerEntity(new NecroEntityEnderman());
        NecroEntityRegistry.registerEntity(new NecroEntityIronGolem());
        NecroEntityRegistry.registerEntity(new NecroEntityIsaac());
        NecroEntityRegistry.registerEntity(new NecroEntityPig());
        NecroEntityRegistry.registerEntity(new NecroEntityPigZombie());
        NecroEntityRegistry.registerEntity(new NecroEntitySheep());
        NecroEntityRegistry.registerEntity(new NecroEntitySkeleton());
        NecroEntityRegistry.registerEntity(new NecroEntitySpider());
        NecroEntityRegistry.registerEntity(new NecroEntitySquid());
        NecroEntityRegistry.registerEntity(new NecroEntityVillager());
        NecroEntityRegistry.registerEntity(new NecroEntityWolf());
        NecroEntityRegistry.registerEntity(new NecroEntityWitch());
        NecroEntityRegistry.registerEntity(new NecroEntityZombie());
    }

    private static void register(Class<? extends Entity> cls, String name, int id, int range, int eggPrimary, int eggSecondary) {
        ResourceLocation rl = new ResourceLocation(ReferenceNecromancy.MOD_ID, name.toLowerCase());
        if (eggPrimary != -1) {
            EntityRegistry.registerModEntity(rl, cls, name, id, Necromancy.instance, range, 5, true, eggPrimary, eggSecondary);
        } else {
            EntityRegistry.registerModEntity(rl, cls, name, id, Necromancy.instance, range, 5, true);
        }
    }
}

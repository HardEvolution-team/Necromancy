package com.electrifiedded.necromancy;

import com.electrifiedded.necromancy.block.RegistryBlocksNecromancy;
import com.electrifiedded.necromancy.client.gui.GuiHandler;
import com.electrifiedded.necromancy.command.CommandMinion;
import com.electrifiedded.necromancy.core.handler.ForgeEventHandler;
import com.electrifiedded.necromancy.core.proxy.CommonProxy;
import com.electrifiedded.necromancy.craftingmanager.CraftingManagerSewing;
import com.electrifiedded.necromancy.creativetab.CreativeTabNecro;
import com.electrifiedded.necromancy.entity.RegistryNecromancyEntities;
import com.electrifiedded.necromancy.generation.VillageCreationHandler;
import com.electrifiedded.necromancy.generation.WorldGenerator;
import com.electrifiedded.necromancy.generation.villagecomponent.ComponentVillageCemetery;
import com.electrifiedded.necromancy.item.ItemNecroSkull;
import com.electrifiedded.necromancy.item.RegistryNecromancyItems;
import com.electrifiedded.necromancy.lib.ConfigurationNecromancy;
import com.electrifiedded.necromancy.lib.ReferenceNecromancy;
import com.electrifiedded.necromancy.network.NecroNetwork;
import com.electrifiedded.necromancy.sound.NecroSounds;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.server.dedicated.PropertyManager;
import net.minecraft.world.gen.structure.MapGenStructureIO;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartedEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.registry.VillagerRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Mod(modid = ReferenceNecromancy.MOD_ID, name = ReferenceNecromancy.MOD_NAME, version = Reference.VERSION, dependencies = "required-after:archaiclib@[1.1.0,)")
public class Necromancy {

    public static final CreativeTabs tabNecromancy = new CreativeTabNecro(CreativeTabs.getNextID(), ReferenceNecromancy.MOD_NAME, 1)
            .setBackgroundImageName("necro_gui.png");
    public static final CreativeTabs tabNecromancyBodyParts = new CreativeTabNecro(CreativeTabs.getNextID(), "BodyParts", 2)
            .setBackgroundImageName("necro_gui.png");

    public static final List<String> specialFolk = new ArrayList<>();

    public int maxSpawn = -1;

    public static Logger loggerNecromancy = LogManager.getLogger(ReferenceNecromancy.MOD_ID);

    public static final GuiHandler guiHandler = new GuiHandler();
    public static final ForgeEventHandler eventHandler = new ForgeEventHandler();
    public static final VillageCreationHandler villageHandler = new VillageCreationHandler();

    public CraftingManagerSewing sewingRecipeHandler;

    @SidedProxy(clientSide = "com.electrifiedded.necromancy.core.proxy.ClientProxy", serverSide = "com.electrifiedded.necromancy.core.proxy.CommonProxy")
    public static CommonProxy proxy;

    @Mod.Instance(ReferenceNecromancy.MOD_ID)
    public static Necromancy instance;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        ConfigurationNecromancy.initProperties(event);

        NecroNetwork.init();

        NetworkRegistry.INSTANCE.registerGuiHandler(this, guiHandler);
        MinecraftForge.EVENT_BUS.register(eventHandler);

        specialFolk.add("AtomicStryker");

        NecroSounds.register();
        RegistryBlocksNecromancy.initBlocks();
        RegistryNecromancyItems.initItems();
        RegistryNecromancyEntities.initEntities();

        proxy.preInit();

        MapGenStructureIO.registerStructureComponent(ComponentVillageCemetery.class, "NeViCem");
        VillagerRegistry.instance().registerVillageCreationHandler(villageHandler);
        GameRegistry.registerWorldGenerator(new WorldGenerator(), 5);
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init();
        RegistryBlocksNecromancy.initRecipes();
        RegistryNecromancyItems.initRecipes();
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        ItemNecroSkull.initSkulls();
        sewingRecipeHandler = new CraftingManagerSewing();
    }

    @EventHandler
    public void serverStarting(FMLServerStartingEvent event) {
        event.registerServerCommand(new CommandMinion());
    }

    @EventHandler
    public void serverStarted(FMLServerStartedEvent event) {
        if (new File("server.properties").exists()) {
            PropertyManager manager = new PropertyManager(new File("server.properties"));
            maxSpawn = manager.getIntProperty("max_minion_spawn", -1);
        }
    }
}

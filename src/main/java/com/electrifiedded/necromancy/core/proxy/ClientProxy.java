package com.electrifiedded.necromancy.core.proxy;

import com.electrifiedded.archaiclib.legacy.registry.LegacyGameRegistry;
import com.electrifiedded.archaiclib.v1710.api.client.ArchaicMinecraftForgeClient;
import com.electrifiedded.necromancy.block.RegistryBlocksNecromancy;
import com.electrifiedded.necromancy.client.model.ModelIsaacHead;
import com.electrifiedded.necromancy.client.model.ModelIsaacNormal;
import com.electrifiedded.necromancy.client.model.ModelIsaacSevered;
import com.electrifiedded.necromancy.client.model.ModelNightCrawler;
import com.electrifiedded.necromancy.client.model.ModelTeddy;
import com.electrifiedded.necromancy.client.renderer.ItemNecronomiconRenderer;
import com.electrifiedded.necromancy.client.renderer.ItemScytheBoneRenderer;
import com.electrifiedded.necromancy.client.renderer.ItemScytheRenderer;
import com.electrifiedded.necromancy.client.renderer.ParticleNecro;
import com.electrifiedded.necromancy.client.renderer.RenderMinion;
import com.electrifiedded.necromancy.client.renderer.RenderSimpleLiving;
import com.electrifiedded.necromancy.client.renderer.RenderTear;
import com.electrifiedded.necromancy.client.renderer.tileentity.TileEntityAltarRenderer;
import com.electrifiedded.necromancy.client.renderer.tileentity.TileEntitySewingRenderer;
import com.electrifiedded.necromancy.core.handler.KeyHandlerNecro;
import com.electrifiedded.necromancy.entity.EntityIsaacBlood;
import com.electrifiedded.necromancy.entity.EntityIsaacBody;
import com.electrifiedded.necromancy.entity.EntityIsaacHead;
import com.electrifiedded.necromancy.entity.EntityIsaacNormal;
import com.electrifiedded.necromancy.entity.EntityMinion;
import com.electrifiedded.necromancy.entity.EntityNightCrawler;
import com.electrifiedded.necromancy.entity.EntityTear;
import com.electrifiedded.necromancy.entity.EntityTearBlood;
import com.electrifiedded.necromancy.entity.EntityTeddy;
import com.electrifiedded.necromancy.item.RegistryNecromancyItems;
import com.electrifiedded.necromancy.lib.ReferenceNecromancy;
import com.electrifiedded.necromancy.tileentity.TileEntityAltar;
import com.electrifiedded.necromancy.tileentity.TileEntitySewing;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ClientProxy extends CommonProxy {
    public static Minecraft mc;

    public static final ResourceLocation TEAR_SPRITE = new ResourceLocation(ReferenceNecromancy.MOD_ID, "items/tear");
    public static final ResourceLocation TEAR_BLOOD_SPRITE = new ResourceLocation(ReferenceNecromancy.MOD_ID, "items/tearblood");

    @Override
    public void preInit() {
        mc = Minecraft.getMinecraft();
        MinecraftForge.EVENT_BUS.register(new KeyHandlerNecro());
        MinecraftForge.EVENT_BUS.register(this);

        RenderingRegistry.registerEntityRenderingHandler(EntityMinion.class, RenderMinion::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityTeddy.class, m -> new RenderSimpleLiving<>(m, new ModelTeddy(), 0.3F, ReferenceNecromancy.TEXTURES_ENTITIES_TEDDY));
        RenderingRegistry.registerEntityRenderingHandler(EntityNightCrawler.class, m -> new RenderSimpleLiving<>(m, new ModelNightCrawler(), 0.3F, ReferenceNecromancy.TEXTURES_ENTITIES_NIGHTCRAWLER));
        RenderingRegistry.registerEntityRenderingHandler(EntityIsaacNormal.class, m -> new RenderSimpleLiving<>(m, new ModelIsaacNormal(), 0.3F, ReferenceNecromancy.TEXTURES_ENTITIES_ISAAC));
        RenderingRegistry.registerEntityRenderingHandler(EntityIsaacBlood.class, m -> new RenderSimpleLiving<>(m, new ModelIsaacNormal(), 0.3F, ReferenceNecromancy.TEXTURES_ENTITIES_ISAACBLOOD));
        RenderingRegistry.registerEntityRenderingHandler(EntityIsaacBody.class, m -> new RenderSimpleLiving<>(m, new ModelIsaacSevered(), 0.3F, ReferenceNecromancy.TEXTURES_ENTITIES_ISAACBLOOD));
        RenderingRegistry.registerEntityRenderingHandler(EntityIsaacHead.class, m -> new RenderSimpleLiving<>(m, new ModelIsaacHead(), 0.3F, ReferenceNecromancy.TEXTURES_ENTITIES_ISAACBLOOD));
        RenderingRegistry.registerEntityRenderingHandler(EntityTear.class, m -> new RenderTear(m, TEAR_SPRITE));
        RenderingRegistry.registerEntityRenderingHandler(EntityTearBlood.class, m -> new RenderTear(m, TEAR_BLOOD_SPRITE));

        TileEntityAltarRenderer altarRenderer = new TileEntityAltarRenderer();
        TileEntitySewingRenderer sewingRenderer = new TileEntitySewingRenderer();
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityAltar.class, altarRenderer);
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntitySewing.class, sewingRenderer);

        registerItemRenderer(Item.getItemFromBlock(RegistryBlocksNecromancy.altar), altarRenderer);
        registerItemRenderer(Item.getItemFromBlock(RegistryBlocksNecromancy.sewing), sewingRenderer);
        registerItemRenderer(RegistryNecromancyItems.scythe, new ItemScytheRenderer());
        registerItemRenderer(RegistryNecromancyItems.scytheBone, new ItemScytheBoneRenderer());
        registerItemRenderer(RegistryNecromancyItems.necronomicon, new ItemNecronomiconRenderer());
        Item altarBlockItem = Item.getItemFromBlock(RegistryBlocksNecromancy.altarBlock);
        LegacyGameRegistry.registerItemModel(altarBlockItem, 0, altarBlockItem.getRegistryName().toString(), "inventory");

        ModelResourceLocation bloodModel = new ModelResourceLocation(new ResourceLocation(ReferenceNecromancy.MOD_ID, "fluid_blood"), "fluid");
        if (RegistryBlocksNecromancy.blood.getRegistryName() != null
                && ReferenceNecromancy.MOD_ID.equals(RegistryBlocksNecromancy.blood.getRegistryName().getNamespace())) {
            ModelLoader.setCustomStateMapper(RegistryBlocksNecromancy.blood, new StateMapperBase() {
                @Override
                protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
                    return bloodModel;
                }
            });
            Item bloodItem = Item.getItemFromBlock(RegistryBlocksNecromancy.blood);
            if (bloodItem != net.minecraft.init.Items.AIR) {
                ModelLoader.registerItemVariants(bloodItem, bloodModel);
                ModelLoader.setCustomMeshDefinition(bloodItem, stack -> bloodModel);
            }
        }
    }

    private static void registerItemRenderer(Item item, com.electrifiedded.archaiclib.v1710.api.client.IItemRenderer renderer) {
        ArchaicMinecraftForgeClient.registerItemRenderer(item, renderer);
        LegacyGameRegistry.registerItemModel(item, 0, item.getRegistryName().toString(), "inventory");
    }

    @SubscribeEvent
    public void onTextureStitch(TextureStitchEvent.Pre event) {
        event.getMap().registerSprite(TEAR_SPRITE);
        event.getMap().registerSprite(TEAR_BLOOD_SPRITE);
    }

    @Override
    public void init() {
    }

    @Override
    public void spawnParticle(String name, double posX, double posY, double posZ, double motionX, double motionY, double motionZ) {
        if (mc != null && mc.getRenderViewEntity() != null && mc.effectRenderer != null && name.equals("skull")) {
            mc.effectRenderer.addEffect(new ParticleNecro(mc.world, posX, posY, posZ, motionX, motionY, motionZ, 0.5F));
        }
    }

    @Override
    public void bindTexture(ResourceLocation location) {
        Minecraft.getMinecraft().getTextureManager().bindTexture(location);
    }
}

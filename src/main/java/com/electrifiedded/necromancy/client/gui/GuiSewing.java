package com.electrifiedded.necromancy.client.gui;

import com.electrifiedded.necromancy.inventory.ContainerSewing;
import com.electrifiedded.necromancy.lib.ReferenceNecromancy;
import com.electrifiedded.necromancy.tileentity.TileEntitySewing;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;

public class GuiSewing extends GuiContainer {

    public GuiSewing(InventoryPlayer inventory, TileEntitySewing sewing) {
        super(new ContainerSewing(inventory, sewing));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        renderHoveredToolTip(mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        fontRenderer.drawString("String", 88, 72, 1);
        fontRenderer.drawString("Needle", 88, 8, 1);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        mc.getTextureManager().bindTexture(ReferenceNecromancy.TEXTURES_GUI_SEWING);
        int x = (width - xSize) / 2;
        int y = (height - ySize) / 2;
        drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
    }
}

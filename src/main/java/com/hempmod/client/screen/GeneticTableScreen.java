package com.hempmod.client.screen;

import com.hempmod.screen.GeneticTableMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class GeneticTableScreen extends AbstractContainerScreen<GeneticTableMenu> {

    // Используем ванильную текстуру печки
    private static final ResourceLocation TEXTURE =
            new ResourceLocation("minecraft", "textures/gui/container/furnace.png");

    public GeneticTableScreen(GeneticTableMenu menu, Inventory inventory, Component component) {
        super(menu, inventory, component);
    }

    @Override
    protected void init() {
        super.init();
        this.titleLabelY = 6;
        this.inventoryLabelY = 72;
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        // Рисуем фон GUI
        guiGraphics.blit(TEXTURE, x, y, 0, 0, imageWidth, imageHeight);

        // Рисуем стрелку прогресса если идёт крафт
        if (menu.isCrafting()) {
            int progress = menu.getScaledProgress();
            // Стрелка прогресса в печке: координаты в текстуре (176, 14), размер 24x16
            // Рисуем только часть стрелки в зависимости от прогресса
            guiGraphics.blit(TEXTURE, x + 80, y + 35, 176, 14, progress + 1, 16);
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        renderBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, delta);
        renderTooltip(guiGraphics, mouseX, mouseY);
    }
}

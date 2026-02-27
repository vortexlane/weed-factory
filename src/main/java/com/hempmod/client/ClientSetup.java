package com.hempmod.client;

import com.hempmod.HempMod;
import com.hempmod.client.screen.GeneticTableScreen;
import com.hempmod.registry.ModBlocks;
import com.hempmod.screen.ModMenuTypes;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(
        modid = HempMod.MODID,
        bus = Mod.EventBusSubscriber.Bus.MOD,
        value = Dist.CLIENT
)
public class ClientSetup {

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            ItemBlockRenderTypes.setRenderLayer(
                    ModBlocks.HEMP_CROP.get(),
                    RenderType.cutout()
            );
            ItemBlockRenderTypes.setRenderLayer(
                    ModBlocks.HEMP_CROP_TOP.get(),
                    RenderType.cutout()
            );

            // Регистрация GUI
            MenuScreens.register(ModMenuTypes.GENETIC_TABLE_MENU.get(), GeneticTableScreen::new);
        });
    }
}
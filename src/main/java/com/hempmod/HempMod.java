package com.hempmod;

import com.hempmod.registry.ModBlocks;
import com.hempmod.registry.ModCreativeTabs;
import com.hempmod.registry.ModEffects;
import com.hempmod.registry.ModItems;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(HempMod.MODID)
public class HempMod {
    public static final String MODID = "hempmod";

    public HempMod() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        ModBlocks.BLOCKS.register(bus);
        ModItems.ITEMS.register(bus);
        ModEffects.EFFECTS.register(bus);
        ModCreativeTabs.CREATIVE_MODE_TABS.register(bus);
    }
}
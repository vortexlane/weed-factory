package com.hempmod.registry;

import com.hempmod.HempMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, HempMod.MODID);

    public static final RegistryObject<CreativeModeTab> WEED_FACTORY_TAB = CREATIVE_MODE_TABS.register("weed_factory",
            () -> CreativeModeTab.builder()
                    .icon(() -> new ItemStack(ModItems.HEMP_FIBER.get()))
                    .title(Component.translatable("creative_tab.hempmod.weed_factory"))
                    .displayItems((params, output) -> {
                        output.accept(ModItems.HEMP_FIBER.get());
                        output.accept(ModItems.HEMP_SEEDS.get());
                        output.accept(ModItems.BUDS.get());
                        output.accept(ModItems.JOINT.get());
                        output.accept(ModItems.HEMP_PLANKS.get());
                        output.accept(ModItems.GENETIC_TABLE.get());
                    })
                    .build());
}

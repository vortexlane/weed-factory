package com.hempmod.registry;

import com.hempmod.HempMod;
import com.hempmod.registry.ModBlocks;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {

    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, HempMod.MODID);

    public static final RegistryObject<Item> HEMP_FIBER =
            ITEMS.register("hemp_fiber", () -> new Item(new Item.Properties()));


    // Семена, которые ставят hemp_crop
    public static final RegistryObject<Item> HEMP_SEEDS =
            ITEMS.register("hemp_seeds",
                    () -> new ItemNameBlockItem(
                            ModBlocks.HEMP_CROP.get(),
                            new Item.Properties()
                    ));

    public static final RegistryObject<Item> JOINT =
            ITEMS.register("joint", () -> new com.hempmod.item.JointItem(new Item.Properties()));

    public static final RegistryObject<Item> BUDS =
            ITEMS.register("buds", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> GENETIC_TABLE =
            ITEMS.register("genetic_table",
                    () -> new net.minecraft.world.item.BlockItem(
                            ModBlocks.GENETIC_TABLE.get(),
                            new Item.Properties()
                    ));

    public static final RegistryObject<Item> HEMP_PLANKS =
            ITEMS.register("hemp_planks",
                    () -> new net.minecraft.world.item.BlockItem(
                            ModBlocks.HEMP_PLANKS.get(),
                            new Item.Properties()
                    ));
}
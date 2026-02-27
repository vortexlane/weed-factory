package com.hempmod.registry;

import com.hempmod.HempMod;
import com.hempmod.block.entity.GeneticTableBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntities {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, HempMod.MODID);

    public static final RegistryObject<BlockEntityType<GeneticTableBlockEntity>> GENETIC_TABLE =
            BLOCK_ENTITIES.register("genetic_table", () ->
                    BlockEntityType.Builder.of(GeneticTableBlockEntity::new, ModBlocks.GENETIC_TABLE.get()).build(null));
}

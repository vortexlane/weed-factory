package com.hempmod.registry;

import com.hempmod.HempMod;
import com.hempmod.block.GeneticTable;
import com.hempmod.block.HempCropBlock;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlocks {

    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, HempMod.MODID);

    public static final RegistryObject<Block> HEMP_CROP =
            BLOCKS.register("hemp_crop", HempCropBlock::new);

    public static final RegistryObject<Block> HEMP_CROP_TOP =
            BLOCKS.register("hemp_crop_top",
                    () -> new com.hempmod.block.HempCropTopBlock(
                            net.minecraft.world.level.block.state.BlockBehaviour.Properties.copy(net.minecraft.world.level.block.Blocks.WHEAT)
                                    .noCollission()
                                    .instabreak()
                    ));

    public static final RegistryObject<Block> GENETIC_TABLE =
            BLOCKS.register("genetic_table", GeneticTable::new);
}
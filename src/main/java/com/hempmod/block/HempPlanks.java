package com.hempmod.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class HempPlanks extends Block {

    public HempPlanks() {
        super(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS));
    }
}

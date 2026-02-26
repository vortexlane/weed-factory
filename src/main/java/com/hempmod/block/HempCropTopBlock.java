package com.hempmod.block;

import com.hempmod.registry.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

public class HempCropTopBlock extends Block {

    public static final IntegerProperty TOP_AGE = IntegerProperty.create("top_age", 0, 3);

    public HempCropTopBlock(Properties props) {
        super(props);
        this.registerDefaultState(this.stateDefinition.any().setValue(TOP_AGE, 0));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(TOP_AGE);
    }

    @Override
    public void playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        BlockPos below = pos.below();
        if (level.getBlockState(below).is(ModBlocks.HEMP_CROP.get())) {
            level.destroyBlock(below, false); // без дропа, дроп только с низа
        }
        super.playerWillDestroy(level, pos, state, player);
    }
}
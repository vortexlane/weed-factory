package com.hempmod.block;

import com.hempmod.registry.ModBlocks;
import com.hempmod.registry.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import com.hempmod.block.HempCropTopBlock;

public class HempCropBlock extends CropBlock {

    private static final int TOP_START_AGE = 4;

    public HempCropBlock() {
        super(BlockBehaviour.Properties.copy(Blocks.WHEAT));
    }

    @Override
    protected Item getBaseSeedId() {
        return ModItems.HEMP_SEEDS.get();
    }

    private void syncTop(ServerLevel level, BlockPos pos, BlockState state) {
        BlockPos above = pos.above();
        int age = this.getAge(state);

        if (age >= TOP_START_AGE) {
            int topAge = Math.min(3, age - TOP_START_AGE); // 4->0, 5->1, 6->2, 7->3

            BlockState topState = ModBlocks.HEMP_CROP_TOP.get()
                    .defaultBlockState()
                    .setValue(HempCropTopBlock.TOP_AGE, topAge);

            if (level.isEmptyBlock(above) || level.getBlockState(above).is(ModBlocks.HEMP_CROP_TOP.get())) {
                level.setBlock(above, topState, 3);
            }
        } else {
            if (level.getBlockState(above).is(ModBlocks.HEMP_CROP_TOP.get())) {
                level.destroyBlock(above, false);
            }
        }
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        super.randomTick(state, level, pos, random);
        syncTop(level, pos, level.getBlockState(pos)); // берём актуальный стейт после роста
    }

    @Override
    public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
        super.performBonemeal(level, random, pos, state);
        syncTop(level, pos, level.getBlockState(pos)); // после костной муки возраст мог перепрыгнуть за 4
    }

    @Override
    public void playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        // Ломаешь низ -> ломаем верх без дропа
        BlockPos above = pos.above();
        if (level.getBlockState(above).is(ModBlocks.HEMP_CROP_TOP.get())) {
            level.destroyBlock(above, false);
        }
        super.playerWillDestroy(level, pos, state, player);
    }
}
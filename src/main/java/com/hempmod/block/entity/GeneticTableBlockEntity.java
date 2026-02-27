package com.hempmod.block.entity;

import com.hempmod.registry.ModBlockEntities;
import com.hempmod.registry.ModItems;
import com.hempmod.screen.GeneticTableMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class GeneticTableBlockEntity extends BlockEntity implements MenuProvider {

    private final ItemStackHandler itemHandler = new ItemStackHandler(4) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }

        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            return switch (slot) {
                case 0 -> stack.is(ModItems.HEMP_SEEDS.get()); // Слот для семян конопли
                case 1 -> stack.is(Items.BONE_MEAL); // Слот для костной муки
                case 2 -> isWaterBottle(stack) || stack.is(Items.GLASS_BOTTLE); // Слот для бутылки воды и возврата пустой бутылки
                case 3 -> true; // Выходной слот - разрешаем для крафта, ModResultSlot блокирует ручную вставку
                default -> super.isItemValid(slot, stack);
            };
        }
    };

    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();

    protected final ContainerData data;
    private int progress = 0;
    private int maxProgress = 200; // 10 секунд = 200 тиков

    public GeneticTableBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.GENETIC_TABLE.get(), pos, state);
        this.data = new ContainerData() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> GeneticTableBlockEntity.this.progress;
                    case 1 -> GeneticTableBlockEntity.this.maxProgress;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0 -> GeneticTableBlockEntity.this.progress = value;
                    case 1 -> GeneticTableBlockEntity.this.maxProgress = value;
                }
            }

            @Override
            public int getCount() {
                return 2;
            }
        };
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.hempmod.genetic_table");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        return new GeneticTableMenu(id, inventory, this, this.data);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            return lazyItemHandler.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        lazyItemHandler = LazyOptional.of(() -> itemHandler);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyItemHandler.invalidate();
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        tag.put("inventory", itemHandler.serializeNBT());
        tag.putInt("genetic_table.progress", progress);
        super.saveAdditional(tag);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        itemHandler.deserializeNBT(tag.getCompound("inventory"));
        progress = tag.getInt("genetic_table.progress");
    }

    public void drops() {
        SimpleContainer inventory = new SimpleContainer(itemHandler.getSlots());
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            inventory.setItem(i, itemHandler.getStackInSlot(i));
        }
        Containers.dropContents(this.level, this.worldPosition, inventory);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, GeneticTableBlockEntity entity) {
        if (level.isClientSide) {
            return;
        }

        if (hasRecipe(entity)) {
            entity.progress++;
            setChanged(level, pos, state);

            if (entity.progress >= entity.maxProgress) {
                craftItem(entity);
            }
        } else {
            entity.resetProgress();
            setChanged(level, pos, state);
        }
    }

    private void resetProgress() {
        this.progress = 0;
    }

    private static void craftItem(GeneticTableBlockEntity entity) {
        Level level = entity.level;

        // Всегда выдаем семена пшеницы (100% шанс)
        ItemStack result = new ItemStack(Items.WHEAT_SEEDS);
        entity.itemHandler.insertItem(3, result, false);

        // Расходуем ингредиенты
        entity.itemHandler.extractItem(0, 1, false); // Семена конопли
        entity.itemHandler.extractItem(1, 1, false); // Костная мука
        entity.itemHandler.extractItem(2, 1, false); // Бутылка воды

        // Возвращаем пустую бутылку
        ItemStack glassBottle = new ItemStack(Items.GLASS_BOTTLE);
        ItemStack leftover = entity.itemHandler.insertItem(2, glassBottle, false);
        if (!leftover.isEmpty()) {
            // Если не поместилось в слот, дропаем в мир
            Containers.dropContents(level, entity.getBlockPos(), new SimpleContainer(leftover));
        }

        entity.resetProgress();
    }

    private static boolean hasRecipe(GeneticTableBlockEntity entity) {
        SimpleContainer inventory = new SimpleContainer(entity.itemHandler.getSlots());
        for (int i = 0; i < entity.itemHandler.getSlots(); i++) {
            inventory.setItem(i, entity.itemHandler.getStackInSlot(i));
        }

        boolean hasHempSeeds = entity.itemHandler.getStackInSlot(0).is(ModItems.HEMP_SEEDS.get());
        boolean hasBoneMeal = entity.itemHandler.getStackInSlot(1).is(Items.BONE_MEAL);
        boolean hasWaterBottle = isWaterBottle(entity.itemHandler.getStackInSlot(2));
        boolean canInsertOutput = entity.itemHandler.getStackInSlot(3).isEmpty() ||
                (entity.itemHandler.getStackInSlot(3).is(Items.WHEAT_SEEDS) &&
                 entity.itemHandler.getStackInSlot(3).getCount() < entity.itemHandler.getStackInSlot(3).getMaxStackSize());

        return hasHempSeeds && hasBoneMeal && hasWaterBottle && canInsertOutput;
    }

    public ItemStackHandler getItemHandler() {
        return itemHandler;
    }

    private static boolean isWaterBottle(ItemStack stack) {
        return stack.is(Items.POTION) && PotionUtils.getPotion(stack) == Potions.WATER;
    }
}

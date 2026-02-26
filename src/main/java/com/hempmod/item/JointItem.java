package com.hempmod.item;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import com.hempmod.registry.ModEffects;


public class JointItem extends Item {

    // сколько "покуров" у одного косяка
    private static final int MAX_PUFFS = 3;

    public JointItem(Properties properties) {
        // прочность = MAX_PUFFS (то есть maxDamage=3)
        super(properties.durability(MAX_PUFFS));
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 32; // как зелье
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.BOW; // анимация лука
    }

    @Override
    public SoundEvent getDrinkingSound() {
        return SoundEvents.GENERIC_DRINK;
    }

    @Override
    public SoundEvent getEatingSound() {
        return SoundEvents.GENERIC_DRINK;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        player.startUsingItem(hand);
        return InteractionResultHolder.consume(stack);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        if (!level.isClientSide) {

            // добавка времени: +20/+40/+60 сек
            int usedSoFar = stack.getDamageValue();
            int amplifier = Math.min(2, usedSoFar);

            int addSeconds = (usedSoFar + 1) * 20;
            int addTicks = addSeconds * 20;

// ---- ТВОЙ ЭФФЕКТ: копим время + повышаем уровень ----
            var high = ModEffects.HIGH.get();
            var existingHigh = entity.getEffect(high);

            int totalTicks = addTicks;
            int finalAmplifier = amplifier;

            if (existingHigh != null) {
                totalTicks = existingHigh.getDuration() + addTicks;
                finalAmplifier = Math.max(existingHigh.getAmplifier(), amplifier);
            }
            entity.addEffect(new MobEffectInstance(high, totalTicks, finalAmplifier));

// ---- И ПЛЮС обычная тошнота (также копим/апаем) ----
            var existingNausea = entity.getEffect(MobEffects.CONFUSION);
            int nauseaTicks = totalTicks;
            int nauseaAmp = finalAmplifier;

            if (existingNausea != null) {
                nauseaTicks = existingNausea.getDuration() + addTicks;
                nauseaAmp = Math.max(existingNausea.getAmplifier(), amplifier);
            }
            entity.addEffect(new MobEffectInstance(MobEffects.CONFUSION, nauseaTicks, nauseaAmp));

            // креатив — не ломаем
            if (entity instanceof Player player && player.getAbilities().instabuild) {
                return stack;
            }

            stack.hurtAndBreak(1, entity, (e) -> {});
        }

        return stack;
    }
}
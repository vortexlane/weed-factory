package com.hempmod.registry;

import com.hempmod.HempMod;
import com.hempmod.effect.HighEffect;
import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEffects {
    public static final DeferredRegister<MobEffect> EFFECTS =
            DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, HempMod.MODID);

    public static final RegistryObject<MobEffect> HIGH =
            EFFECTS.register("high", HighEffect::new);
}
package de.nike.extramodules2.mobeffects;

import de.nike.extramodules2.ExtraModules2;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.alchemy.Potions;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class EMMobEffects {

    public static DeferredRegister<MobEffect> MOB_EFFECTS = DeferredRegister.create(BuiltInRegistries.MOB_EFFECT, ExtraModules2.MODID);

    public static Holder<MobEffect> CHAOS_POISONING = MOB_EFFECTS.register("chaos_poisoning", ChaosPoisoning::new);

    public static void init(IEventBus eventBus) {
        MOB_EFFECTS.register(eventBus);
    }
}

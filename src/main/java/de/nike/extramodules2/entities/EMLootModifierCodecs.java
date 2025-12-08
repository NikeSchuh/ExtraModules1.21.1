package de.nike.extramodules2.entities;

import com.mojang.serialization.MapCodec;
import de.nike.extramodules2.ExtraModules2;
import de.nike.extramodules2.entities.drops.ElderGuardianLootModifier;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public class EMLootModifierCodecs {

    public static DeferredRegister<MapCodec<? extends IGlobalLootModifier>> LOOT_MODIFIERS = DeferredRegister.create(NeoForgeRegistries.GLOBAL_LOOT_MODIFIER_SERIALIZERS, ExtraModules2.MODID);

    public static final Supplier<MapCodec<ElderGuardianLootModifier>> ELDER_GUARDIAN_DROPS = LOOT_MODIFIERS.register("elder_guardian_drops", () -> ElderGuardianLootModifier.CODEC);

    public static void register(IEventBus modEventBus) {
        LOOT_MODIFIERS.register(modEventBus);
    }

}

package de.nike.extramodules2.sounds;

import de.nike.extramodules2.ExtraModules2;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class EMSounds {

    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(BuiltInRegistries.SOUND_EVENT, ExtraModules2.MODID);

    public static final Supplier<SoundEvent> DRACONIC_LIGHTNING_CHAIN_ZAP = registerSoundEvent("draconic_lightning_chain_zap");

    public static Supplier<SoundEvent> registerSoundEvent(String registryName) {
        ResourceLocation id = ResourceLocation.fromNamespaceAndPath(ExtraModules2.MODID, registryName);
        return SOUNDS.register(registryName, () -> SoundEvent.createVariableRangeEvent(id));
    }

    public static void init(IEventBus eventBus) {
        SOUNDS.register(eventBus);
    }

}

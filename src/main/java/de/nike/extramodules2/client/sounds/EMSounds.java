package de.nike.extramodules2.client.sounds;

import de.nike.extramodules2.ExtraModules2;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class EMSounds {

    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(BuiltInRegistries.SOUND_EVENT, ExtraModules2.MODID);

    public static final Supplier<SoundEvent> DRACONIC_LIGHTNING_CHAIN_ZAP = registerSoundEvent("draconic_lightning_chain_zap");
    public static final Supplier<SoundEvent> MODULAR_PISTOL_SHOOT = registerSoundEvent("modular_pistol_shoot");
    public static final Supplier<SoundEvent> DISTANT_THUNDER = registerSoundEvent("distant_thunder");



    public static Supplier<SoundEvent> registerSoundEvent(String registryName) {
        ResourceLocation id = ResourceLocation.fromNamespaceAndPath(ExtraModules2.MODID, registryName);
        return SOUNDS.register(registryName, () -> SoundEvent.createVariableRangeEvent(id));
    }

    public static void init(IEventBus eventBus) {
        SOUNDS.register(eventBus);
    }

}

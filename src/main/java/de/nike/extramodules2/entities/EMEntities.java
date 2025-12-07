package de.nike.extramodules2.entities;

import de.nike.extramodules2.ExtraModules2;
import de.nike.extramodules2.entities.projectiles.DraconicLightningChain;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class EMEntities {

    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, ExtraModules2.MODID);



    public static Supplier<EntityType<DraconicLightningChain>> DRACONIC_LIGHTNING_CHAIN =
            ENTITY_TYPES.register("draconic_lightning_chain", () -> EntityType.Builder.of(DraconicLightningChain::new, MobCategory.MISC)
                    .sized(0F, 0F)
                    .clientTrackingRange(16)
                    .noSave()
                    .updateInterval(1)
                    .build("draconic_lightning_chain")
            );

    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }

}

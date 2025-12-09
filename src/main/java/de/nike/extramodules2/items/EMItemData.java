package de.nike.extramodules2.items;

import com.mojang.serialization.Codec;
import de.nike.extramodules2.ExtraModules2;
import de.nike.extramodules2.modules.EMModules;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class EMItemData {

    public static final DeferredRegister<DataComponentType<?>> DATA = DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, ExtraModules2.MODID);

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Boolean>> EFFECT_NECKLACE_ACTIVE =
            DATA.register("effect_necklace_active", () ->
                    DataComponentType.<Boolean>builder()
                            .persistent(Codec.BOOL)
                            .networkSynchronized(ByteBufCodecs.BOOL)
                            .build()
            );

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> PISTOL_SHOOT_COOLDOWN =
            DATA.register("pistol_shoot_cooldown", () ->
                    DataComponentType.<Integer>builder()
                            .persistent(Codec.INT)
                            .networkSynchronized(ByteBufCodecs.INT)
                            .build()
            );

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> OXYGEN_STORAGE =
            DATA.register("oxygen_storage", () ->
                    DataComponentType.<Integer>builder()
                            .persistent(Codec.INT)
                            .networkSynchronized(ByteBufCodecs.INT)
                            .build()
            );

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Float>> RAGE_CHARGE =
            DATA.register("rage_charge", () ->
                    DataComponentType.<Float>builder()
                            .persistent(Codec.FLOAT)
                            .networkSynchronized(ByteBufCodecs.FLOAT)
                            .build()
            );

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> RAGE_TICKS =
            DATA.register("rage_ticks", () ->
                    DataComponentType.<Integer>builder()
                            .persistent(Codec.INT)
                            .networkSynchronized(ByteBufCodecs.INT)
                            .build()
            );



    public static void init(IEventBus modBus) {
        DATA.register(modBus);
    }

}

package de.nike.extramodules2.entities;

import de.nike.extramodules2.ExtraModules2;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class EMDamageTypes {

    public static final ResourceKey<DamageType> DRACONIC_LIGHTNING =
            ResourceKey.create(Registries.DAMAGE_TYPE, ResourceLocation.fromNamespaceAndPath(ExtraModules2.MODID, "draconic_lightning_chain"));

    public static final ResourceKey<DamageType> CHAOS_POISONING =
            ResourceKey.create(Registries.DAMAGE_TYPE, ResourceLocation.fromNamespaceAndPath(ExtraModules2.MODID, "chaos_poisoning"));

    public static DamageSource playerDraconicLightning(Level level, Player attacker, LivingEntity attacked) {
        return new DamageSource(level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DRACONIC_LIGHTNING), attacked, attacker);
    }

    public static DamageSource chaosPoisoning(Level level, LivingEntity attacked) {
        return new DamageSource(level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(CHAOS_POISONING), attacked);
    }
}

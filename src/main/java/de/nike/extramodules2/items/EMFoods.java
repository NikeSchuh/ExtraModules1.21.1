package de.nike.extramodules2.items;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;

public class EMFoods {

   public static final FoodProperties WYVERN_APPLE = (new FoodProperties.Builder())
           .nutrition(10)
           .saturationModifier(2.5F)
           .effect(new MobEffectInstance(MobEffects.REGENERATION, 2000, 2), 1.0F)
           .effect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 6000, 1), 1.0F)
           .effect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 10000, 0), 1.0F)
           .effect(new MobEffectInstance(MobEffects.ABSORPTION, 6000, 9), 1.0F)
           .effect(new MobEffectInstance(MobEffects.HEALTH_BOOST, 6000, 4), 1.0F)
           .alwaysEdible()
           .build();

   public static final FoodProperties DRACONIC_APPLE = (new FoodProperties.Builder())
           .nutrition(40)
           .saturationModifier(10.0F)
           .effect(new MobEffectInstance(MobEffects.REGENERATION, 600, 5), 1.0F)
           .effect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 10000, 3), 1.0F)
           .effect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 20000, 0), 1.0F)
           .effect(new MobEffectInstance(MobEffects.ABSORPTION, 10000, 18), 1.0F)
           .effect(new MobEffectInstance(MobEffects.HEALTH_BOOST, 6000, 9), 1.0F)
           .alwaysEdible()
           .build();


}

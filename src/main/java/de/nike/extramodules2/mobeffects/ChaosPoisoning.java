package de.nike.extramodules2.mobeffects;

import de.nike.extramodules2.entities.EMDamageTypes;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.neoforged.neoforge.common.brewing.BrewingRecipeRegistry;

import java.awt.*;

public class ChaosPoisoning extends MobEffect {

    public static final int COLOR = new Color(15, 15, 55).getRGB();

    protected ChaosPoisoning() {
        super(MobEffectCategory.HARMFUL, COLOR);
    }


    @Override
    public boolean applyEffectTick(LivingEntity livingEntity, int amplifier) {
        if(!livingEntity.level().isClientSide) {
            livingEntity.hurt(EMDamageTypes.chaosPoisoning(livingEntity.level(), livingEntity), 10 * (amplifier + 1) + (livingEntity.getHealth() / 8f));
        }
        return super.applyEffectTick(livingEntity, amplifier);
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int tick, int amplifier) {
        return tick % 20 == 0;
    }
}
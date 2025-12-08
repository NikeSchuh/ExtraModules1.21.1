package de.nike.extramodules2.items.custom.effectnecklace;

import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;

import java.util.HashMap;

public class NecklaceEffectRules {

    private static HashMap<Holder<MobEffect>, Integer> CAPS = new HashMap<>();
    private static HashMap<Holder<MobEffect>, Integer> DELAYS = new HashMap<>();


    public static void init() {
        CAPS.put(MobEffects.DAMAGE_RESISTANCE, 2);
        CAPS.put(MobEffects.REGENERATION, 5);
        CAPS.put(MobEffects.INVISIBILITY, 0);

        DELAYS.put(MobEffects.ABSORPTION, 250);
    }

    public static boolean hasCap(Holder<MobEffect> effect) {
        return CAPS.containsKey(effect);
    }

    public static int getCap(Holder<MobEffect> effect) {
        return CAPS.get(effect);
    }

    public static int getDelay(Holder<MobEffect> effect) {
        return DELAYS.getOrDefault(effect, 60);
    }

}

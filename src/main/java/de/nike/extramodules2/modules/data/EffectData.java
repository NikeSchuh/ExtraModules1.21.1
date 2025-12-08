package de.nike.extramodules2.modules.data;

import com.brandon3055.draconicevolution.api.modules.data.ModuleData;
import com.brandon3055.draconicevolution.api.modules.lib.ModuleContext;
import de.nike.extramodules2.items.custom.effectnecklace.NecklaceEffectRules;
import de.nike.extramodules2.utils.FormatUtils;
import de.nike.extramodules2.utils.TranslationUtils;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public class EffectData implements ModuleData<EffectData> {

    private final HashMap<Holder<MobEffect>, Integer> ampMap;
    private final int tickCost;

    public EffectData(Holder<MobEffect> mobEffect, int amplifier, int tickCost) {
        this.ampMap =new HashMap<>();
        ampMap.put(mobEffect, amplifier + 1);
        this.tickCost = tickCost;
    }

    public EffectData(HashMap<Holder<MobEffect>, Integer> ampMap, int tickCost) {
        this.ampMap = ampMap;
        this.tickCost = tickCost;
    }


    public int getTickCost() {
        return tickCost;
    }

    public HashMap<Holder<MobEffect>, Integer> getAmpMap() {
        return ampMap;
    }

    @Override
    public EffectData combine(EffectData other) {
        HashMap<Holder<MobEffect>, Integer> combinedMap = new HashMap<>(ampMap);
        for(Holder<MobEffect> effect : other.getAmpMap().keySet()) {
            combinedMap.put(effect, combinedMap.getOrDefault(effect, 0) + other.ampMap.get(effect));
        }
        return new EffectData(combinedMap, tickCost + other.tickCost);
    }

    @Override
    public void addInformation(Map<Component, Component> map, @Nullable ModuleContext context, boolean stack) {
        map.put(Component.translatable("module.extramodules2.effect.tickcost"), TranslationUtils.string(FormatUtils.formatE(tickCost) + " OP/t"));
        for(Holder<MobEffect> effect : ampMap.keySet()) {
            int amp = ampMap.get(effect) - 1;
            if(NecklaceEffectRules.hasCap(effect)) amp = Math.min(amp, NecklaceEffectRules.getCap(effect));
            map.put(TranslationUtils.string(FormatUtils.capitalizeString(effect.value().getDisplayName().getString())), TranslationUtils.string(FormatUtils.toRoman(amp + 1)));
        }
        if(stack) {
            ampMap.keySet().stream().findFirst().ifPresent(localEffect -> {
                if(NecklaceEffectRules.hasCap(localEffect))
                    map.put(Component.translatable("module.extramodules2.effect.levelcap"), TranslationUtils.string(FormatUtils.toRoman(NecklaceEffectRules.getCap(localEffect) + 1)));
            });
        }
    }
}
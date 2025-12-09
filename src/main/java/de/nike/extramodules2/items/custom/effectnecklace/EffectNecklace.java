package de.nike.extramodules2.items.custom.effectnecklace;

import com.brandon3055.brandonscore.api.power.IOPStorage;
import com.brandon3055.brandonscore.utils.EnergyUtils;
import com.brandon3055.draconicevolution.api.capability.DECapabilities;
import com.brandon3055.draconicevolution.api.capability.ModuleHost;
import com.brandon3055.draconicevolution.api.modules.ModuleCategory;
import com.brandon3055.draconicevolution.api.modules.lib.ModuleHostImpl;
import com.brandon3055.draconicevolution.init.TechProperties;
import de.nike.extramodules2.items.custom.ModularEnergyItem;
import de.nike.extramodules2.modules.EMModuleCategories;
import de.nike.extramodules2.modules.EMModuleTypes;
import de.nike.extramodules2.modules.data.EffectData;
import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

public class EffectNecklace extends ModularEnergyItem implements ICurioItem {

    private final int gridWidth;
    private final int gridHeight;

    private ModuleHostImpl host;

    public EffectNecklace(TechProperties properties, int gridWidth, int gridHeight) {
        super(properties);
        this.gridWidth = gridWidth;
        this.gridHeight = gridHeight;
    }

    @Override
    public @NotNull ModuleHostImpl instantiateHost(ItemStack stack) {
        ModuleHostImpl hostImpl = new ModuleHostImpl(tier, gridWidth, gridHeight, "necklace", false, EMModuleCategories.EFFECT, ModuleCategory.ENERGY);
        this.host = hostImpl;
        return hostImpl;
    }

    @Override
    public boolean canEquipFromUse(SlotContext slotContext, ItemStack stack) {
        return true;
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        ModuleHost moduleHost = DECapabilities.getHost(stack);
        IOPStorage opStorage = EnergyUtils.getStorage(stack);
        LivingEntity livingEntity = slotContext.entity();
        if (livingEntity == null) return;
        if (moduleHost == null && opStorage == null) return;
        EffectData effectData = moduleHost.getModuleData(EMModuleTypes.EFFECT);
        if (effectData == null) return;
        int opCost = effectData.getTickCost();
        if (opStorage.getOPStored() > opCost) {
            opStorage.modifyEnergyStored(-opCost);
            for (Holder<MobEffect> effect : effectData.getAmpMap().keySet()) {
                int amp = effectData.getAmpMap().get(effect) - 1;
                if (NecklaceEffectRules.hasCap(effect)) amp = Math.min(amp, NecklaceEffectRules.getCap(effect));
                int delay = NecklaceEffectRules.getDelay(effect);
                if (livingEntity.tickCount % delay == 0) {
                    livingEntity.addEffect(new MobEffectInstance(effect, delay + 1, amp));
                }
            }
        }

    }
}

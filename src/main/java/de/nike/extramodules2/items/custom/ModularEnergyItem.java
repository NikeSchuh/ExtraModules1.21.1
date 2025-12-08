package de.nike.extramodules2.items.custom;

import com.brandon3055.brandonscore.api.TechLevel;
import com.brandon3055.brandonscore.capability.MultiCapabilityProvider;
import com.brandon3055.draconicevolution.api.capability.ModuleHost;
import com.brandon3055.draconicevolution.api.modules.lib.ModularOPStorage;
import com.brandon3055.draconicevolution.api.modules.lib.ModuleHostImpl;
import com.brandon3055.draconicevolution.init.EquipCfg;
import com.brandon3055.draconicevolution.init.TechProperties;
import com.brandon3055.draconicevolution.integration.equipment.EquipmentManager;
import com.brandon3055.draconicevolution.integration.equipment.IDEEquipment;
import com.brandon3055.draconicevolution.items.equipment.IModularEnergyItem;
import com.brandon3055.draconicevolution.items.equipment.IModularItem;
import de.nike.extramodules2.items.EMItems;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Supplier;

public class ModularEnergyItem extends Item implements IModularEnergyItem, IDEEquipment {

    protected final TechLevel tier;

    public ModularEnergyItem(TechProperties properties) {
        super(properties);
        this.tier = properties.getTechLevel();
    }

    @Override
    public TechLevel getTechLevel() {
        return null;
    }

    @Override
    public @NotNull ModuleHostImpl instantiateHost(ItemStack itemStack) {
        ModuleHostImpl host = new ModuleHostImpl(tier, 1, 1, "curios", false);
        return host;
    }

    public @NotNull ModularOPStorage instantiateOPStorage(ItemStack stack, Supplier<ModuleHost> hostSupplier) {
        return new ModularOPStorage(hostSupplier, 0, 0);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        this.addModularItemInformation(stack, context, tooltipComponents, tooltipFlag);
    }

    public boolean isBarVisible(ItemStack stack) {
        return this.damageBarVisible(stack);
    }

    public int getBarWidth(ItemStack stack) {
        return this.damageBarWidth(stack);
    }

    public int getBarColor(ItemStack stack) {
        return this.damageBarColour(stack);
    }
}

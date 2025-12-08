package de.nike.extramodules2.utils;

import com.brandon3055.draconicevolution.api.capability.DECapabilities;
import com.brandon3055.draconicevolution.api.capability.ModuleHost;
import com.brandon3055.draconicevolution.api.modules.lib.ModuleEntity;
import com.brandon3055.draconicevolution.items.equipment.IModularArmor;
import de.nike.extramodules2.modules.EMModuleTypes;
import de.nike.extramodules2.modules.entities.defensebrain.DefenseBrainEntity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;

public class ModuleHostFinder {

    public static Optional<DefenseBrainEntity> findDefenseBrain(Player player) {
        ItemStack stack = IModularArmor.getArmor(player);
        if(stack == null) return Optional.empty();
        ModuleHost host = DECapabilities.getHost(stack);
        if(host == null)  return Optional.empty();
        DefenseBrainEntity brainEntity = host.getEntitiesByType(EMModuleTypes.DEFENSE_BRAIN).map((e) -> (DefenseBrainEntity)e).findAny().orElse(null);
        return Optional.ofNullable(brainEntity);
    }

    public static ModuleHost unsafeGet(ItemStack stack) {
        return DECapabilities.getHost(stack);
    }

}

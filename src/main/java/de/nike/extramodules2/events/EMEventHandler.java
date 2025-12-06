package de.nike.extramodules2.events;

import com.brandon3055.draconicevolution.api.capability.DECapabilities;
import com.brandon3055.draconicevolution.api.capability.ModuleHost;
import com.brandon3055.draconicevolution.api.modules.ModuleTypes;
import com.brandon3055.draconicevolution.api.modules.entities.ShieldControlEntity;
import com.brandon3055.draconicevolution.items.equipment.IModularArmor;
import com.brandon3055.draconicevolution.items.equipment.IModularItem;
import com.brandon3055.draconicevolution.items.equipment.ModularChestpiece;
import de.nike.extramodules2.ExtraModules2;
import de.nike.extramodules2.modules.EMModuleTypes;
import de.nike.extramodules2.modules.entities.defensebrain.DefenseBrainEntity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;

import java.util.Optional;

@EventBusSubscriber(modid = ExtraModules2.MODID)
public class EMEventHandler {

    @SubscribeEvent
    public static void playerDamaged(LivingDamageEvent.Pre event) {
        if(!(event.getEntity() instanceof ServerPlayer)) return;

        ServerPlayer serverPlayer = (ServerPlayer) event.getEntity();
        ItemStack stack = IModularArmor.getArmor(serverPlayer);
        if(stack == null) return;

        ModuleHost host = DECapabilities.getHost(stack);
        if(host == null) return;

        DefenseBrainEntity brainEntity = host.getEntitiesByType(EMModuleTypes.DEFENSE_BRAIN).map((e) -> (DefenseBrainEntity)e).findAny().orElse(null);

        if(brainEntity == null) return;
        ShieldControlEntity shieldControl = host.getEntitiesByType(ModuleTypes.SHIELD_CONTROLLER).map((e) -> (ShieldControlEntity)e).findAny().orElse(null);
        brainEntity.incomingDamage(serverPlayer, event.getNewDamage(), shieldControl);

    }

}

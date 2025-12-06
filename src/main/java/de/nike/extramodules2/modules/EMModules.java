package de.nike.extramodules2.modules;

import com.brandon3055.brandonscore.api.TechLevel;
import com.brandon3055.draconicevolution.api.modules.Module;
import com.brandon3055.draconicevolution.api.modules.ModuleTypes;
import com.brandon3055.draconicevolution.api.modules.items.ModuleItem;
import com.brandon3055.draconicevolution.api.modules.lib.BaseModule;
import com.brandon3055.draconicevolution.api.modules.lib.ModuleImpl;
import com.brandon3055.draconicevolution.init.DEModules;
import de.nike.extramodules2.ExtraModules2;
import de.nike.extramodules2.modules.data.DefenseBrainData;
import de.nike.extramodules2.modules.data.OxygenStorageData;
import de.nike.extramodules2.modules.data.PotionCurerData;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;

import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;

public class EMModules {

    public static DeferredRegister<Module<?>> MODULES = DeferredRegister.create(DEModules.MODULE_KEY, ExtraModules2.MODID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.createItems(ExtraModules2.MODID);

    public static DeferredHolder<Module<?>, Module<?>> draconicPotionCurer;

    public static DeferredHolder<Module<?>, Module<?>> wyvernOxygenStorage;
    public static DeferredHolder<Module<?>, Module<?>> draconicOxygenStorage;
    public static DeferredHolder<Module<?>, Module<?>> chaoticOxygenStorage;

    public static DeferredHolder<Module<?>, Module<?>> wyvernDefenseBrain;
    public static DeferredHolder<Module<?>, Module<?>> draconicDefenseBrain;
    public static DeferredHolder<Module<?>, Module<?>> chaoticDefenseBrain;



    public static DeferredHolder<Item, ModuleItem<?>> ITEM_DRACONIC_POTION_CURER;

    public static DeferredHolder<Item, ModuleItem<?>> ITEM_WYVERN_OXYGEN_STORAGE;
    public static DeferredHolder<Item, ModuleItem<?>> ITEM_DRACONIC_OXYGEN_STORAGE;
    public static DeferredHolder<Item, ModuleItem<?>> ITEM_CHAOTIC_OXYGEN_STORAGE;

    public static DeferredHolder<Item, ModuleItem<?>> ITEM_WYVERN_DEFENSE_BRAIN;
    public static DeferredHolder<Item, ModuleItem<?>> ITEM_DRACONIC_DEFENSE_BRAIN;
    public static DeferredHolder<Item, ModuleItem<?>> ITEM_CHAOTIC_DEFENSE_BRAIN;

    private static Function<Module<OxygenStorageData>, OxygenStorageData> oxygenStorageData(int oxygenCapacity, int oxygenRefillRate) {
        return e -> {
            return new OxygenStorageData(oxygenCapacity, oxygenRefillRate);
        };
    }

    private static Function<Module<PotionCurerData>, PotionCurerData> potionCureData() {
        return e -> {
            return new PotionCurerData();
        };
    }

    private static Function<Module<DefenseBrainData>, DefenseBrainData> defenseBrainData(int rageTicks, int opTickCost, float lightningRange, int maxLightningTargets) {
        return e -> {
            return new DefenseBrainData(rageTicks, opTickCost, lightningRange, maxLightningTargets);
        };
    }

    public static Map<BaseModule<?>, Item> moduleItemMap = new LinkedHashMap<>();

    public static void register(ModuleImpl<?> module, String name) {

    }

    public static void registerModules() {

    }


    public static void init(IEventBus modEventBus) {

        draconicPotionCurer = MODULES.register("draconic_potion_curer", () -> new ModuleImpl<>(EMModuleTypes.POTION_CURER, TechLevel.DRACONIC, potionCureData()));

        wyvernOxygenStorage = MODULES.register("wyvern_oxygen_storage", () -> new ModuleImpl<>(EMModuleTypes.OXYGEN_STORAGE, TechLevel.WYVERN, oxygenStorageData(800, 20)));
        draconicOxygenStorage = MODULES.register("draconic_oxygen_storage", () -> new ModuleImpl<>(EMModuleTypes.OXYGEN_STORAGE, TechLevel.DRACONIC, oxygenStorageData(3200, 100)));
        chaoticOxygenStorage = MODULES.register("chaotic_oxygen_storage", () -> new ModuleImpl<>(EMModuleTypes.OXYGEN_STORAGE, TechLevel.CHAOTIC, oxygenStorageData(10000, 500)));

        wyvernDefenseBrain = MODULES.register("wyvern_defense_brain", () -> new ModuleImpl<>(EMModuleTypes.DEFENSE_BRAIN, TechLevel.WYVERN, defenseBrainData(100, 5000, 6, 2)));
        draconicDefenseBrain= MODULES.register("draconic_defense_brain", () -> new ModuleImpl<>(EMModuleTypes.DEFENSE_BRAIN, TechLevel.DRACONIC, defenseBrainData(250, 10000, 12, 6)));
        chaoticDefenseBrain= MODULES.register("chaotic_defense_brain", () -> new ModuleImpl<>(EMModuleTypes.DEFENSE_BRAIN, TechLevel.CHAOTIC, defenseBrainData(400, 50000, 32, 16)));

        ITEM_DRACONIC_POTION_CURER = ITEMS.register("item_draconic_potion_curer", () -> new ModuleItem<PotionCurerData>(draconicPotionCurer));

        ITEM_WYVERN_OXYGEN_STORAGE = ITEMS.register("item_wyvern_oxygen_storage", () -> new ModuleItem<OxygenStorageData>(wyvernOxygenStorage));
        ITEM_DRACONIC_OXYGEN_STORAGE = ITEMS.register("item_draconic_oxygen_storage", () -> new ModuleItem<>(draconicOxygenStorage));
        ITEM_CHAOTIC_OXYGEN_STORAGE = ITEMS.register("item_chaotic_oxygen_storage", () -> new ModuleItem<>(chaoticOxygenStorage));

        ITEM_WYVERN_DEFENSE_BRAIN = ITEMS.register("item_wyvern_defense_brain", () -> new ModuleItem<DefenseBrainData>(wyvernDefenseBrain));
        ITEM_DRACONIC_DEFENSE_BRAIN = ITEMS.register("item_draconic_defense_brain", () -> new ModuleItem<DefenseBrainData>(draconicDefenseBrain));
        ITEM_CHAOTIC_DEFENSE_BRAIN = ITEMS.register("item_chaotic_defense_brain", () -> new ModuleItem<DefenseBrainData>(chaoticDefenseBrain));

        MODULES.register(modEventBus);
        ITEMS.register(modEventBus);
    }

}

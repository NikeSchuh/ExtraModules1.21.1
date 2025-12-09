package de.nike.extramodules2.modules;

import com.brandon3055.brandonscore.api.TechLevel;
import com.brandon3055.draconicevolution.api.modules.Module;
import com.brandon3055.draconicevolution.api.modules.items.ModuleItem;
import com.brandon3055.draconicevolution.api.modules.lib.BaseModule;
import com.brandon3055.draconicevolution.api.modules.lib.ModuleImpl;
import com.brandon3055.draconicevolution.init.DEModules;
import de.nike.extramodules2.ExtraModules2;
import de.nike.extramodules2.modules.data.*;
import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
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

    public static DeferredHolder<Module<?>, Module<?>> wyvernDefenseModule;
    public static DeferredHolder<Module<?>, Module<?>> draconicDefenseModule;
    public static DeferredHolder<Module<?>, Module<?>> chaoticDefenseModule;


    // Effect Modules
    public static DeferredHolder<Module<?>, Module<?>> wyvernRegeneration;
    public static DeferredHolder<Module<?>, Module<?>> draconicRegeneration;
    public static DeferredHolder<Module<?>, Module<?>> chaoticRegeneration;

    public static DeferredHolder<Module<?>, Module<?>> draconicResistance;
    public static DeferredHolder<Module<?>, Module<?>> chaoticResistance;

    public static DeferredHolder<Module<?>, Module<?>> draconicAbsorption;
    public static DeferredHolder<Module<?>, Module<?>> chaoticAbsorption;

    public static DeferredHolder<Module<?>, Module<?>> draconicFireResistance;
    public static DeferredHolder<Module<?>, Module<?>> draconicLuck;

    public static DeferredHolder<Module<?>, Module<?>> wyvernStrength;
    public static DeferredHolder<Module<?>, Module<?>> draconicStrength;
    public static DeferredHolder<Module<?>, Module<?>> chaoticStrength;

    public static DeferredHolder<Module<?>, Module<?>> draconicInvisibility;

    public static DeferredHolder<Module<?>, Module<?>> wyvernHaste;
    public static DeferredHolder<Module<?>, Module<?>> draconicHaste;

    // MODULES

    public static DeferredHolder<Module<?>, Module<?>> wyvernCrit;
    public static DeferredHolder<Module<?>, Module<?>> draconicCrit;
    public static DeferredHolder<Module<?>, Module<?>> chaoticCrit;

    public static DeferredHolder<Module<?>, Module<?>> wyvernCritDmg;
    public static DeferredHolder<Module<?>, Module<?>> draconicCritDmg;
    public static DeferredHolder<Module<?>, Module<?>> chaoticCritDmg;

    public static DeferredHolder<Module<?>, Module<?>> wyvernFire;
    public static DeferredHolder<Module<?>, Module<?>> draconicFire;
    public static DeferredHolder<Module<?>, Module<?>> chaoticFire;

    public static DeferredHolder<Module<?>, Module<?>> draconicHoming;
    public static DeferredHolder<Module<?>, Module<?>> chaoticHoming;

    // ITEMS

    public static DeferredHolder<Item, ModuleItem<?>> ITEM_DRACONIC_POTION_CURER;

    public static DeferredHolder<Item, ModuleItem<?>> ITEM_WYVERN_OXYGEN_STORAGE;
    public static DeferredHolder<Item, ModuleItem<?>> ITEM_DRACONIC_OXYGEN_STORAGE;
    public static DeferredHolder<Item, ModuleItem<?>> ITEM_CHAOTIC_OXYGEN_STORAGE;

    public static DeferredHolder<Item, ModuleItem<?>> ITEM_WYVERN_DEFENSE_BRAIN;
    public static DeferredHolder<Item, ModuleItem<?>> ITEM_DRACONIC_DEFENSE_BRAIN;
    public static DeferredHolder<Item, ModuleItem<?>> ITEM_CHAOTIC_DEFENSE_BRAIN;

    public static DeferredHolder<Item, ModuleItem<?>> ITEM_WYVERN_DEFENSE_MODULE;
    public static DeferredHolder<Item, ModuleItem<?>> ITEM_DRACONIC_DEFENSE_MODULE;
    public static DeferredHolder<Item, ModuleItem<?>> ITEM_CHAOTIC_DEFENSE_MODULE;

    public static DeferredHolder<Item, ModuleItem<?>> ITEM_WYVERN_REGENERATION;
    public static DeferredHolder<Item, ModuleItem<?>> ITEM_DRACONIC_REGENERATION;
    public static DeferredHolder<Item, ModuleItem<?>> ITEM_CHAOTIC_REGENERATION;

    public static DeferredHolder<Item, ModuleItem<?>> ITEM_DRACONIC_RESISTANCE;
    public static DeferredHolder<Item, ModuleItem<?>> ITEM_CHAOTIC_RESISTANCE;

    public static DeferredHolder<Item, ModuleItem<?>> ITEM_DRACONIC_ABSORPTION;
    public static DeferredHolder<Item, ModuleItem<?>> ITEM_CHAOTIC_ABSORPTION;

    public static DeferredHolder<Item, ModuleItem<?>> ITEM_DRACONIC_FIRE_RESISTANCE;
    public static DeferredHolder<Item, ModuleItem<?>> ITEM_DRACONIC_LUCK;

    public static DeferredHolder<Item, ModuleItem<?>> ITEM_WYVERN_STRENGTH;
    public static DeferredHolder<Item, ModuleItem<?>> ITEM_DRACONIC_STRENGTH;
    public static DeferredHolder<Item, ModuleItem<?>> ITEM_CHAOTIC_STRENGTH;

    public static DeferredHolder<Item, ModuleItem<?>> ITEM_DRACONIC_INVISIBILITY;

    public static DeferredHolder<Item, ModuleItem<?>> ITEM_WYVERN_HASTE;
    public static DeferredHolder<Item, ModuleItem<?>> ITEM_DRACONIC_HASTE;

    public static DeferredHolder<Item, ModuleItem<?>> ITEM_WYVERN_CRIT;
    public static DeferredHolder<Item, ModuleItem<?>> ITEM_DRACONIC_CRIT;
    public static DeferredHolder<Item, ModuleItem<?>> ITEM_CHAOTIC_CRIT;

    public static DeferredHolder<Item, ModuleItem<?>> ITEM_WYVERN_CRIT_DMG;
    public static DeferredHolder<Item, ModuleItem<?>> ITEM_DRACONIC_CRIT_DMG;
    public static DeferredHolder<Item, ModuleItem<?>> ITEM_CHAOTIC_CRIT_DMG;

    public static DeferredHolder<Item, ModuleItem<?>> ITEM_WYVERN_FIRE;
    public static DeferredHolder<Item, ModuleItem<?>> ITEM_DRACONIC_FIRE;
    public static DeferredHolder<Item, ModuleItem<?>> ITEM_CHAOTIC_FIRE;

    public static DeferredHolder<Item, ModuleItem<?>> ITEM_DRACONIC_HOMING;
    public static DeferredHolder<Item, ModuleItem<?>> ITEM_CHAOTIC_HOMING;



    private static Function<Module<PistolData>, PistolData> pistolData(float critChance, float criticalDamage, float extraBaseDamage, int fireTicks)
    {
        return e ->{
            return new PistolData(critChance, criticalDamage, extraBaseDamage, fireTicks);
        };
    }


    private static Function<Module<PistolHomingData>, PistolHomingData> pistolHomingData()
    {
        return e ->{
            return new PistolHomingData();
        };
    }

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

    private static Function<Module<DefenseData>, DefenseData> defenseData(float damage) {
        return e -> {
            return new DefenseData(damage);
        };
    }

    private static Function<Module<EffectData>, EffectData> effectData(Holder<MobEffect> effectHolder, int amp, int tickCost) {
        return  e -> {
          return new EffectData(effectHolder, amp, tickCost);
        };
    }

    public static Map<BaseModule<?>, Item> moduleItemMap = new LinkedHashMap<>();

    public static void register(ModuleImpl<?> module, String name) {

    }

    public static void registerModules() {

    }


    public static void init(IEventBus modEventBus) {
        draconicPotionCurer = MODULES.register("draconic_potion_curer", () -> new ModuleImpl<>(EMModuleTypes.POTION_CURER, TechLevel.DRACONIC, potionCureData()));

        wyvernOxygenStorage = MODULES.register("wyvern_oxygen_storage", () -> new ModuleImpl<>(EMModuleTypes.OXYGEN_STORAGE, TechLevel.WYVERN, oxygenStorageData(2500, 20)));
        draconicOxygenStorage = MODULES.register("draconic_oxygen_storage", () -> new ModuleImpl<>(EMModuleTypes.OXYGEN_STORAGE, TechLevel.DRACONIC, oxygenStorageData(10000, 100)));
        chaoticOxygenStorage = MODULES.register("chaotic_oxygen_storage", () -> new ModuleImpl<>(EMModuleTypes.OXYGEN_STORAGE, TechLevel.CHAOTIC, oxygenStorageData(40000, 500)));

        wyvernDefenseBrain = MODULES.register("wyvern_defense_brain", () -> new ModuleImpl<>(EMModuleTypes.DEFENSE_BRAIN, TechLevel.WYVERN, defenseBrainData(200, 10000, 3, 2)));
        draconicDefenseBrain= MODULES.register("draconic_defense_brain", () -> new ModuleImpl<>(EMModuleTypes.DEFENSE_BRAIN, TechLevel.DRACONIC, defenseBrainData(400, 30000, 6, 4)));
        chaoticDefenseBrain= MODULES.register("chaotic_defense_brain", () -> new ModuleImpl<>(EMModuleTypes.DEFENSE_BRAIN, TechLevel.CHAOTIC, defenseBrainData(500, 100000, 9, 16)));

        wyvernDefenseModule = MODULES.register("wyvern_defense_module", () -> new ModuleImpl<>(EMModuleTypes.DEFENSE_MODULE, TechLevel.WYVERN, defenseData(2.0F)));
        draconicDefenseModule = MODULES.register("draconic_defense_module", () -> new ModuleImpl<>(EMModuleTypes.DEFENSE_MODULE, TechLevel.DRACONIC, defenseData(8.0F)));
        chaoticDefenseModule = MODULES.register("chaotic_defense_module", () -> new ModuleImpl<>(EMModuleTypes.DEFENSE_MODULE, TechLevel.CHAOTIC, defenseData(32.0F)));

        wyvernRegeneration = MODULES.register("wyvern_regeneration",
                () -> new ModuleImpl<>(EMModuleTypes.EFFECT, TechLevel.WYVERN,
                        effectData(MobEffects.REGENERATION, 0, 250)));

        draconicRegeneration = MODULES.register("draconic_regeneration",
                () -> new ModuleImpl<>(EMModuleTypes.EFFECT, TechLevel.DRACONIC,
                        effectData(MobEffects.REGENERATION, 1, 500)));

        chaoticRegeneration = MODULES.register("chaotic_regeneration",
                () -> new ModuleImpl<>(EMModuleTypes.EFFECT, TechLevel.CHAOTIC,
                        effectData(MobEffects.REGENERATION, 2, 1000)));

        draconicResistance = MODULES.register("draconic_resistance",
                () -> new ModuleImpl<>(EMModuleTypes.EFFECT, TechLevel.DRACONIC,
                        effectData(MobEffects.DAMAGE_RESISTANCE, 0, 1000), 2, 1));

        chaoticResistance = MODULES.register("chaotic_resistance",
                () -> new ModuleImpl<>(EMModuleTypes.EFFECT, TechLevel.CHAOTIC,
                        effectData(MobEffects.DAMAGE_RESISTANCE, 2, 2000), 2, 1));

        draconicAbsorption = MODULES.register("draconic_absorption",
                () -> new ModuleImpl<>(EMModuleTypes.EFFECT, TechLevel.DRACONIC,
                        effectData(MobEffects.ABSORPTION, 1, 1000), 2, 2));

        chaoticAbsorption = MODULES.register("chaotic_absorption",
                () -> new ModuleImpl<>(EMModuleTypes.EFFECT, TechLevel.CHAOTIC,
                        effectData(MobEffects.ABSORPTION, 4, 2500), 2, 2));

        draconicFireResistance = MODULES.register("draconic_fire_resistance",
                () -> new ModuleImpl<>(EMModuleTypes.EFFECT, TechLevel.DRACONIC,
                        effectData(MobEffects.FIRE_RESISTANCE, 0, 300), 1, 1));

        draconicLuck = MODULES.register("draconic_luck",
                () -> new ModuleImpl<>(EMModuleTypes.EFFECT, TechLevel.DRACONIC,
                        effectData(MobEffects.LUCK, 0, 1000), 1, 1));

        wyvernStrength = MODULES.register("wyvern_strength",
                () -> new ModuleImpl<>(EMModuleTypes.EFFECT, TechLevel.WYVERN,
                        effectData(MobEffects.DAMAGE_BOOST, 0, 1000), 1, 1));

        draconicStrength = MODULES.register("draconic_strength",
                () -> new ModuleImpl<>(EMModuleTypes.EFFECT, TechLevel.DRACONIC,
                        effectData(MobEffects.DAMAGE_BOOST, 2, 2000), 1, 2));

        chaoticStrength = MODULES.register("chaotic_strength",
                () -> new ModuleImpl<>(EMModuleTypes.EFFECT, TechLevel.CHAOTIC,
                        effectData(MobEffects.DAMAGE_BOOST, 3, 3000), 1, 2));

        draconicInvisibility = MODULES.register("draconic_invisibility",
                () -> new ModuleImpl<>(EMModuleTypes.EFFECT, TechLevel.DRACONIC,
                        effectData(MobEffects.INVISIBILITY, 0, 2500), 2, 2));

        wyvernHaste = MODULES.register("wyvern_haste",
                () -> new ModuleImpl<>(EMModuleTypes.EFFECT, TechLevel.WYVERN,
                        effectData(MobEffects.DIG_SPEED, 0, 400), 1, 1));

        draconicHaste = MODULES.register("draconic_haste",
                () -> new ModuleImpl<>(EMModuleTypes.EFFECT, TechLevel.DRACONIC,
                        effectData(MobEffects.DIG_SPEED, 1, 1200), 1, 1));

        wyvernCrit = MODULES.register("wyvern_crit",
                () -> new ModuleImpl<>(EMModuleTypes.PISTOL, TechLevel.WYVERN,
                        pistolData(0.05f, 0.0f, 0.0f, 0), 2, 1));

        draconicCrit = MODULES.register("draconic_crit",
                () -> new ModuleImpl<>(EMModuleTypes.PISTOL, TechLevel.DRACONIC,
                        pistolData(0.1f, 0.0f, 0.0f, 0), 2, 1));

        chaoticCrit = MODULES.register("chaotic_crit",
                () -> new ModuleImpl<>(EMModuleTypes.PISTOL, TechLevel.CHAOTIC,
                        pistolData(0.2f, 0.0f, 0.0f, 0), 2, 1));


        wyvernCritDmg = MODULES.register("wyvern_crit_dmg",
                () -> new ModuleImpl<>(EMModuleTypes.PISTOL, TechLevel.WYVERN,
                        pistolData(0.0f, 0.1f, 0.5f, 0), 1, 1));

        draconicCritDmg = MODULES.register("draconic_crit_dmg",
                () -> new ModuleImpl<>(EMModuleTypes.PISTOL, TechLevel.DRACONIC,
                        pistolData(0.0f, 0.25f, 1.0f, 0), 1, 1));

        chaoticCritDmg = MODULES.register("chaotic_crit_dmg",
                () -> new ModuleImpl<>(EMModuleTypes.PISTOL, TechLevel.CHAOTIC,
                        pistolData(0.0f, 0.50f, 2.0f, 0), 1, 1));


        wyvernFire = MODULES.register("wyvern_fire",
                () -> new ModuleImpl<>(EMModuleTypes.PISTOL, TechLevel.WYVERN,
                        pistolData(0.0f, 0, 0.75f, 50), 1, 1));

        draconicFire = MODULES.register("draconic_fire",
                () -> new ModuleImpl<>(EMModuleTypes.PISTOL, TechLevel.DRACONIC,
                        pistolData(0.0f, 0, 1.25f, 100), 1, 1));

        chaoticFire = MODULES.register("chaotic_fire",
                () -> new ModuleImpl<>(EMModuleTypes.PISTOL, TechLevel.CHAOTIC,
                        pistolData(0.0f, 0, 2.25f, 200), 1, 1));


        draconicHoming = MODULES.register("draconic_homing",
                () -> new ModuleImpl<>(EMModuleTypes.PISTOL_HOMING, TechLevel.DRACONIC,
                        pistolHomingData(), 3, 3));

        chaoticHoming = MODULES.register("chaotic_homing",
                () -> new ModuleImpl<>(EMModuleTypes.PISTOL_HOMING, TechLevel.CHAOTIC,
                        pistolHomingData(), 2, 2));

        ITEM_DRACONIC_POTION_CURER = ITEMS.register("item_draconic_potion_curer", () -> new ModuleItem<PotionCurerData>(draconicPotionCurer));

        ITEM_WYVERN_OXYGEN_STORAGE = ITEMS.register("item_wyvern_oxygen_storage", () -> new ModuleItem<OxygenStorageData>(wyvernOxygenStorage));
        ITEM_DRACONIC_OXYGEN_STORAGE = ITEMS.register("item_draconic_oxygen_storage", () -> new ModuleItem<>(draconicOxygenStorage));
        ITEM_CHAOTIC_OXYGEN_STORAGE = ITEMS.register("item_chaotic_oxygen_storage", () -> new ModuleItem<>(chaoticOxygenStorage));

        ITEM_WYVERN_DEFENSE_BRAIN = ITEMS.register("item_wyvern_defense_brain", () -> new ModuleItem<DefenseBrainData>(wyvernDefenseBrain));
        ITEM_DRACONIC_DEFENSE_BRAIN = ITEMS.register("item_draconic_defense_brain", () -> new ModuleItem<DefenseBrainData>(draconicDefenseBrain));
        ITEM_CHAOTIC_DEFENSE_BRAIN = ITEMS.register("item_chaotic_defense_brain", () -> new ModuleItem<DefenseBrainData>(chaoticDefenseBrain));

        ITEM_WYVERN_DEFENSE_MODULE = ITEMS.register("item_wyvern_defense_module", () -> new ModuleItem<DefenseData>(wyvernDefenseModule));
        ITEM_DRACONIC_DEFENSE_MODULE = ITEMS.register("item_draconic_defense_module", () -> new ModuleItem<DefenseData>(draconicDefenseModule));
        ITEM_CHAOTIC_DEFENSE_MODULE = ITEMS.register("item_chaotic_defense_module", () -> new ModuleItem<DefenseData>(chaoticDefenseModule));

        ITEM_WYVERN_REGENERATION = ITEMS.register("item_wyvern_regeneration",
                () -> new ModuleItem<EffectData>(wyvernRegeneration));

        ITEM_DRACONIC_REGENERATION = ITEMS.register("item_draconic_regeneration",
                () -> new ModuleItem<EffectData>(draconicRegeneration));

        ITEM_CHAOTIC_REGENERATION = ITEMS.register("item_chaotic_regeneration",
                () -> new ModuleItem<EffectData>(chaoticRegeneration));

        ITEM_DRACONIC_RESISTANCE = ITEMS.register("item_draconic_resistance",
                () -> new ModuleItem<EffectData>(draconicResistance));

        ITEM_CHAOTIC_RESISTANCE = ITEMS.register("item_chaotic_resistance",
                () -> new ModuleItem<EffectData>(chaoticResistance));

        ITEM_DRACONIC_ABSORPTION = ITEMS.register("item_draconic_absorption",
                () -> new ModuleItem<EffectData>(draconicAbsorption));

        ITEM_CHAOTIC_ABSORPTION = ITEMS.register("item_chaotic_absorption",
                () -> new ModuleItem<EffectData>(chaoticAbsorption));

        ITEM_DRACONIC_FIRE_RESISTANCE = ITEMS.register("item_draconic_fire_resistance",
                () -> new ModuleItem<EffectData>(draconicFireResistance));

        ITEM_DRACONIC_LUCK = ITEMS.register("item_draconic_luck",
                () -> new ModuleItem<EffectData>(draconicLuck));

        ITEM_WYVERN_STRENGTH = ITEMS.register("item_wyvern_strength",
                () -> new ModuleItem<EffectData>(wyvernStrength));

        ITEM_DRACONIC_STRENGTH = ITEMS.register("item_draconic_strength",
                () -> new ModuleItem<EffectData>(draconicStrength));

        ITEM_CHAOTIC_STRENGTH = ITEMS.register("item_chaotic_strength",
                () -> new ModuleItem<EffectData>(chaoticStrength));

        ITEM_DRACONIC_INVISIBILITY = ITEMS.register("item_draconic_invisibility",
                () -> new ModuleItem<EffectData>(draconicInvisibility));

        ITEM_WYVERN_HASTE = ITEMS.register("item_wyvern_haste",
                () -> new ModuleItem<EffectData>(wyvernHaste));

        ITEM_DRACONIC_HASTE = ITEMS.register("item_draconic_haste",
                () -> new ModuleItem<EffectData>(draconicHaste));

        ITEM_WYVERN_CRIT = ITEMS.register("item_wyvern_crit",
                () -> new ModuleItem<>(wyvernCrit));

        ITEM_DRACONIC_CRIT = ITEMS.register("item_draconic_crit",
                () -> new ModuleItem<>(draconicCrit));

        ITEM_CHAOTIC_CRIT = ITEMS.register("item_chaotic_crit",
                () -> new ModuleItem<>(chaoticCrit));


        ITEM_WYVERN_CRIT_DMG = ITEMS.register("item_wyvern_crit_dmg",
                () -> new ModuleItem<>(wyvernCritDmg));

        ITEM_DRACONIC_CRIT_DMG = ITEMS.register("item_draconic_crit_dmg",
                () -> new ModuleItem<>(draconicCritDmg));

        ITEM_CHAOTIC_CRIT_DMG = ITEMS.register("item_chaotic_crit_dmg",
                () -> new ModuleItem<>(chaoticCritDmg));


        ITEM_WYVERN_FIRE = ITEMS.register("item_wyvern_fire",
                () -> new ModuleItem<>(wyvernFire));

        ITEM_DRACONIC_FIRE = ITEMS.register("item_draconic_fire",
                () -> new ModuleItem<>(draconicFire));

        ITEM_CHAOTIC_FIRE = ITEMS.register("item_chaotic_fire",
                () -> new ModuleItem<>(chaoticFire));


        ITEM_DRACONIC_HOMING = ITEMS.register("item_draconic_homing",
                () -> new ModuleItem<>(draconicHoming));

        ITEM_CHAOTIC_HOMING = ITEMS.register("item_chaotic_homing",
                () -> new ModuleItem<>(chaoticHoming));



        MODULES.register(modEventBus);
        ITEMS.register(modEventBus);
    }

}

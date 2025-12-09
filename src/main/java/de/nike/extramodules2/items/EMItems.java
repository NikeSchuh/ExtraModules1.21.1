package de.nike.extramodules2.items;

import com.brandon3055.brandonscore.api.TechLevel;
import com.brandon3055.draconicevolution.init.TechProperties;
import de.nike.extramodules2.ExtraModules2;
import de.nike.extramodules2.items.custom.AntiPotionItem;
import de.nike.extramodules2.items.custom.ModularPistol;
import de.nike.extramodules2.items.custom.effectnecklace.EffectNecklace;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class EMItems {

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(ExtraModules2.MODID);

    public static final DeferredItem<Item> WYVERN_APPLE = ITEMS.register("wyvern_apple", () -> new Item((new Item.Properties())
            .rarity(Rarity.EPIC)
            .food(EMFoods.WYVERN_APPLE)
            .component(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true)));

    public static final DeferredItem<Item> ANTI_POTION = ITEMS.register("anti_potion", () -> new AntiPotionItem(new Item.Properties().stacksTo(1)));

    public static final DeferredItem<Item> ADVANCED_MODULE_CORE = ITEMS.register("advanced_module_core", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> SUPERIOR_MODULE_CORE = ITEMS.register("superior_module_core", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> MODULE_CONTROLLER = ITEMS.register("module_controller", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> ELDER_GUARDIAN_BRAIN = ITEMS.register("elder_guardian_brain", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> ELDER_GUARDIAN_PARTS = ITEMS.register("elder_guardian_parts", () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> WYVERN_EFFECT_NECKLACE = ITEMS.register("wyvern_effect_necklace", () -> new EffectNecklace((TechProperties) new TechProperties(TechLevel.WYVERN).stacksTo(1), 3, 3));
    public static final DeferredItem<Item> DRACONIC_EFFECT_NECKLACE = ITEMS.register("draconic_effect_necklace", () -> new EffectNecklace((TechProperties) new TechProperties(TechLevel.DRACONIC).stacksTo(1), 4, 4));
    public static final DeferredItem<Item> CHAOTIC_EFFECT_NECKLACE = ITEMS.register("chaotic_effect_necklace", () -> new EffectNecklace((TechProperties) new TechProperties(TechLevel.CHAOTIC).stacksTo(1), 5, 5));

    public static final DeferredItem<Item> WYVERN_PISTOL = ITEMS.register("wyvern_pistol", () -> new ModularPistol((TechProperties) new TechProperties(TechLevel.WYVERN).stacksTo(1), 6, 4, 10f));
    public static final DeferredItem<Item> DRACONIC_PISTOL = ITEMS.register("draconic_pistol", () -> new ModularPistol((TechProperties) new TechProperties(TechLevel.DRACONIC).stacksTo(1), 9, 4, 15f));
    public static final DeferredItem<Item> CHAOTIC_PISTOL = ITEMS.register("chaotic_pistol", () -> new ModularPistol((TechProperties) new TechProperties(TechLevel.CHAOTIC).stacksTo(1), 11, 5, 20f));


    public static void init(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }

}

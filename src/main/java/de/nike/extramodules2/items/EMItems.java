package de.nike.extramodules2.items;

import com.brandon3055.draconicevolution.api.modules.items.ModuleItem;
import de.nike.extramodules2.ExtraModules2;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.food.Foods;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.Map;

public class EMItems {

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(ExtraModules2.MODID);

    public static final DeferredItem<Item> WYVERN_APPLE = ITEMS.register("wyvern_apple", () -> new Item((new Item.Properties())
            .rarity(Rarity.EPIC)
            .food(EMFoods.WYVERN_APPLE)
            .component(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true)));

    public static final DeferredItem<Item> DRACONIC_APPLE = ITEMS.register("draconic_apple", () -> new Item((new Item.Properties())
            .rarity(Rarity.EPIC)
            .food(EMFoods.DRACONIC_APPLE)
            .component(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true)));

    public static void init(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }

}

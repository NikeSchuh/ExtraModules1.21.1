package de.nike.extramodules2;

import com.brandon3055.brandonscore.capability.CapabilityOP;
import com.brandon3055.draconicevolution.api.DataComponentAccessor;
import com.brandon3055.draconicevolution.api.DraconicAPI;
import com.brandon3055.draconicevolution.api.capability.DECapabilities;
import com.brandon3055.draconicevolution.api.capability.ModuleProvider;
import com.brandon3055.draconicevolution.api.modules.lib.ModularOPStorage;
import com.brandon3055.draconicevolution.api.modules.lib.ModuleHostImpl;
import com.brandon3055.draconicevolution.integration.equipment.CurioWrapper;
import com.brandon3055.draconicevolution.integration.equipment.EquipmentManager;
import com.brandon3055.draconicevolution.integration.equipment.IDEEquipment;
import com.brandon3055.draconicevolution.items.equipment.IModularEnergyItem;
import com.brandon3055.draconicevolution.items.equipment.IModularItem;
import de.nike.extramodules2.entities.EMEntities;
import de.nike.extramodules2.entities.EMLootModifierCodecs;
import de.nike.extramodules2.items.EMItemData;
import de.nike.extramodules2.items.EMItems;
import de.nike.extramodules2.items.custom.effectnecklace.NecklaceEffectRules;
import de.nike.extramodules2.mobeffects.EMMobEffects;
import de.nike.extramodules2.mobeffects.EMPotions;
import de.nike.extramodules2.modules.EMModules;
import de.nike.extramodules2.network.ModuleNetwork;
import de.nike.extramodules2.sounds.EMSounds;
import net.minecraft.world.item.*;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.registries.*;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import top.theillusivec4.curios.api.CuriosCapability;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(ExtraModules2.MODID)
public class ExtraModules2 {

    public static final String MODID = "extramodules2";
    public static final Logger LOGGER = LogUtils.getLogger();



    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);


    // Creates a creative tab with the id "extramodules2:example_tab" for the example item, that is placed after the combat tab
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> EXAMPLE_TAB = CREATIVE_MODE_TABS.register("extramodules2.modules", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.extramodules2")) //The language key for the title of your CreativeModeTab
            .withTabsBefore(CreativeModeTabs.COMBAT)
            .icon(() -> EMModules.ITEM_DRACONIC_OXYGEN_STORAGE.get().getDefaultInstance())
            .displayItems((parameters, output) -> {
                EMModules.ITEMS.getEntries().forEach(itemDeferredHolder -> {
                    output.accept(itemDeferredHolder.get());
                }); // Add the example item to the tab. For your own tabs, this method is preferred over the event

                EMItems.ITEMS.getEntries().forEach(itemDeferredHolder -> {
                    output.accept(itemDeferredHolder.get());
                }); //
            }).build());


    // The constructor for the mod class is the first code that is run when your mod is loaded.
    // FML will recognize some parameter types like IEventBus or ModContainer and pass them in automatically.
    public ExtraModules2(IEventBus modEventBus, ModContainer modContainer) {
        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        DraconicAPI.addModuleProvider(MODID);

        EMItemData.init(modEventBus);
        EMModules.init(modEventBus);
        EMEntities.register(modEventBus);
        EMItems.init(modEventBus);
        EMMobEffects.init(modEventBus);
        EMSounds.init(modEventBus);
        EMPotions.init(modEventBus);
        ModuleNetwork.init(modEventBus);
        EMLootModifierCodecs.register(modEventBus);
        NecklaceEffectRules.init();




        CREATIVE_MODE_TABS.register(modEventBus);

        // Register ourselves for server and other game events we are interested in.
        // Note that this is necessary if and only if we want *this* class (ExtraModules2) to respond directly to events.
        // Do not add this line if there are no @SubscribeEvent-annotated functions in this class, like onServerStarting() below.
        NeoForge.EVENT_BUS.register(this);

        // Register the item to a creative tab
        modEventBus.addListener(this::addCreative);
        modEventBus.addListener(this::registerCaps);

        // Register our mod's ModConfigSpec so that FML can create and load the config file for us
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);

    }

    private static ModuleHostImpl getItemHostCap(ItemStack stack) {
        Item host = stack.getItem();
        if (host instanceof IModularItem item) {
            ModuleHostImpl var3 = item.createHostCapForRegistration(stack);

            assert var3 != null;

            var3.updateDataAccess(DataComponentAccessor.itemStack(stack));
            return var3;
        } else {
            throw new IllegalStateException("ITEM_HOST_DATA can only be used on an ItemStack who's item implements IModularItem!");
        }
    }

    private static ModularOPStorage getEnergyCap(ItemStack stack) {
        Item storage = stack.getItem();
        if (storage instanceof IModularEnergyItem item) {
            ModularOPStorage var3 = item.createOPCapForRegistration(stack);

            assert var3 != null;

            var3.updateDataAccess(DataComponentAccessor.itemStack(stack));
            return var3;
        } else {
            throw new IllegalStateException("ITEM_HOST_DATA can only be used on an ItemStack who's item implements IModularEnergyItem!");
        }
    }

    private void registerCaps(RegisterCapabilitiesEvent event) {
        EMModules.ITEMS.getEntries().forEach((holder) -> {
            Item item = holder.get();

            if (item instanceof ModuleProvider<?> provider) {
                event.registerItem(DECapabilities.Module.ITEM, (stack, context) -> provider, new ItemLike[]{item});
            }

        });
        EMItems.ITEMS.getEntries().forEach(holder -> {
            Item item = holder.get();

            if (item instanceof IModularItem modularItem) {
                LOGGER.info("Registered modular item " + item);
                event.registerItem(DECapabilities.Host.ITEM, (stack, v) -> getItemHostCap(stack), new ItemLike[]{item});
                if (item instanceof IModularEnergyItem modularEnergyItem) {
                    event.registerItem(CapabilityOP.ITEM, (stack, v) -> getEnergyCap(stack), new ItemLike[]{item});
                    event.registerItem(Capabilities.EnergyStorage.ITEM, (stack, v) -> getEnergyCap(stack), new ItemLike[]{item});
                }
            }

            if (item instanceof IDEEquipment) {
                EquipmentManager.registerCapability(event, item);
            }

            if(item instanceof ICurioItem) {
                event.registerItem(CuriosCapability.ITEM, (stack, context) -> new CurioWrapper(stack), new ItemLike[]{item});
            }
        });
    }

    private void commonSetup(FMLCommonSetupEvent event) {

    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {

    }


    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {

    }

    public void test() {

    }

}




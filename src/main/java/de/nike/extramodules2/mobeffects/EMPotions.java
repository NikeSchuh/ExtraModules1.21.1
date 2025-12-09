package de.nike.extramodules2.mobeffects;

import com.brandon3055.draconicevolution.init.DEContent;
import de.nike.extramodules2.ExtraModules2;
import de.nike.extramodules2.utils.NikesMath;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.alchemy.Potions;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.event.brewing.RegisterBrewingRecipesEvent;
import net.neoforged.neoforge.registries.DeferredRegister;

public class EMPotions {

    public static DeferredRegister<Potion> POTIONS = DeferredRegister.create(BuiltInRegistries.POTION, ExtraModules2.MODID);

    public static final Holder<Potion> HEALTH_BOOST =
            POTIONS.register("health_boost",
                    () -> new Potion(new MobEffectInstance(MobEffects.HEALTH_BOOST, NikesMath.minutesToTicks(8f), 1)));
    public static final Holder<Potion> HEALTH_BOOST_STRONG =
            POTIONS.register("strong_health_boost",
                    () -> new Potion(new MobEffectInstance(MobEffects.HEALTH_BOOST, NikesMath.minutesToTicks(8f), 4)));

    public static final Holder<Potion> WITHER =
            POTIONS.register("wither",
                    () -> new Potion(new MobEffectInstance(MobEffects.WITHER, NikesMath.minutesToTicks(0.5f), 0)));
    public static final Holder<Potion> WITHER_STRONG =
            POTIONS.register("strong_wither",
                    () -> new Potion(new MobEffectInstance(MobEffects.WITHER, NikesMath.minutesToTicks(0.5f), 1)));

    public static final Holder<Potion> BAD_OMEN =
            POTIONS.register("bad_omen",
                    () -> new Potion(new MobEffectInstance(MobEffects.BAD_OMEN, NikesMath.minutesToTicks(10f), 0)));

    public static final Holder<Potion> HASTE =
            POTIONS.register("haste",
                    () -> new Potion(new MobEffectInstance(MobEffects.DIG_SPEED, NikesMath.minutesToTicks(2f), 0)));

    public static final Holder<Potion> STRONG_HASTE =
            POTIONS.register("strong_haste",
                    () -> new Potion(new MobEffectInstance(MobEffects.DIG_SPEED, NikesMath.minutesToTicks(2f), 1)));

    public static final Holder<Potion> CHAOS =
            POTIONS.register("chaos_poisoning",
                    () -> new Potion(new MobEffectInstance(EMMobEffects.CHAOS_POISONING, 10 * 20, 0)));

    public static void init(IEventBus eventBus) {
        POTIONS.register(eventBus);
    }

    public static void registerBrewingRecipes(RegisterBrewingRecipesEvent event) {

        PotionBrewing.Builder builder = event.getBuilder();
        builder.addMix(Potions.AWKWARD, Items.TOTEM_OF_UNDYING, BAD_OMEN);
        builder.addMix(Potions.AWKWARD, Items.WITHER_ROSE, WITHER);
        builder.addMix(WITHER, Items.GLOWSTONE_DUST, WITHER_STRONG);
        builder.addMix(Potions.THICK, Items.HONEYCOMB, HEALTH_BOOST);
        builder.addMix(HEALTH_BOOST, Items.GLOWSTONE_DUST, HEALTH_BOOST_STRONG);
        builder.addMix(Potions.STRONG_SWIFTNESS, Items.SUGAR, HASTE);
        builder.addMix(HASTE, Items.GLOWSTONE_DUST, STRONG_HASTE);
        builder.addMix(WITHER_STRONG,DEContent.CHAOS_FRAG_MEDIUM.get(), CHAOS);
    }

}

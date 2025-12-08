package de.nike.extramodules2.modules.entities;

import com.brandon3055.brandonscore.api.BCStreamCodec;
import com.brandon3055.brandonscore.api.power.IOPStorage;
import com.brandon3055.draconicevolution.api.config.BooleanProperty;
import com.brandon3055.draconicevolution.api.config.ConfigProperty;
import com.brandon3055.draconicevolution.api.modules.Module;
import com.brandon3055.draconicevolution.api.modules.lib.ModuleContext;
import com.brandon3055.draconicevolution.api.modules.lib.ModuleEntity;
import com.brandon3055.draconicevolution.api.modules.lib.StackModuleContext;
import com.brandon3055.draconicevolution.init.DEModules;
import com.brandon3055.draconicevolution.init.ItemData;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import de.nike.extramodules2.ExtraModules2;
import de.nike.extramodules2.utils.FormatUtils;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.fml.util.thread.EffectiveSide;

import java.util.List;
import java.util.Objects;

public class PotionCurerEntity extends ModuleEntity {

    private BooleanProperty curePotions;

    public static final Codec<PotionCurerEntity> CODEC =
            RecordCodecBuilder.create(builder ->
                    builder.group(
                            DEModules.codec().fieldOf("module").forGetter(ModuleEntity::getModule),
                            Codec.INT.fieldOf("gridx").forGetter(ModuleEntity::getGridX),
                            Codec.INT.fieldOf("gridy").forGetter(ModuleEntity::getGridY),
                            BooleanProperty.CODEC.fieldOf("curePotions").forGetter(e -> e.curePotions)
                    ).apply(builder, PotionCurerEntity::new)
            );

    public static final StreamCodec<RegistryFriendlyByteBuf, PotionCurerEntity> STREAM_CODEC =
            BCStreamCodec.composite(
                    DEModules.streamCodec(), ModuleEntity::getModule,
                    ByteBufCodecs.INT, ModuleEntity::getGridX,
                    ByteBufCodecs.INT, ModuleEntity::getGridY,
                    BooleanProperty.STREAM_CODEC, e -> e.curePotions,
                    PotionCurerEntity::new
            );

    public PotionCurerEntity(Module<?> module) {
        super(module);
        this.curePotions = new BooleanProperty("potion_cure_mod.cure_potions", true)
                .setFormatter(ConfigProperty.BooleanFormatter.YES_NO);
    }

    public PotionCurerEntity(Module<?> module, int gridX, int gridY, BooleanProperty curePotions) {
        super(module, gridX, gridY);
        this.curePotions = curePotions.copy();
    }

    @Override
    public void getEntityProperties(List properties) {
        super.getEntityProperties(properties);
        properties.add(curePotions);
    }

    @Override
    public void tick(ModuleContext context) {
        if (!(context instanceof StackModuleContext moduleContext)) return;
        if (!moduleContext.isEquipped()) return;
        if (!curePotions.getValue()) return;

        IOPStorage energy = moduleContext.getOpStorage();
        if (energy == null && EffectiveSide.get().isClient()) return;

        LivingEntity entity = moduleContext.getEntity();
        if (entity instanceof ServerPlayer player && entity.tickCount % 10 == 0) {

            int removed = 0;
            int spent = 0;

            for (MobEffectInstance inst : player.getActiveEffects().stream().toList()) {
                Holder<MobEffect> effect = inst.getEffect();
                if (!effect.value().isBeneficial()) {

                    int level = inst.getAmplifier() + 1;
                    int ticks = inst.getDuration();
                    int cost = (int) (Math.pow(level, 3) * Math.pow(ticks, 1.5f));

                    if (energy.getOPStored() >= cost) {
                        player.removeEffect(effect);
                        energy.modifyEnergyStored(-cost);
                        removed++;
                        spent += cost;
                    }
                }
            }

            if (removed > 0) {
                player.displayClientMessage(
                        Component.literal("Removed " + removed + " bad effect(s) for " + FormatUtils.formatE(spent)),
                        true
                );
            }
        }
    }

    @Override
    public void saveEntityToStack(ItemStack stack, ModuleContext context) {
        stack.set(ItemData.BOOL_ITEM_PROP_1, curePotions.copy());
    }

    @Override
    public void loadEntityFromStack(ItemStack stack, ModuleContext context) {
        this.curePotions = stack.getOrDefault(ItemData.BOOL_ITEM_PROP_1, new BooleanProperty("potion_cure_mod.cure_potions", true)
                .setFormatter(ConfigProperty.BooleanFormatter.YES_NO)).copy();
    }

    @Override
    public ModuleEntity<?> copy() {
        return new PotionCurerEntity(module, getGridX(), getGridY(), curePotions);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof PotionCurerEntity other)) return false;
        return super.equals(o) &&
                Objects.equals(curePotions.getValue(), other.curePotions.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), curePotions.getValue());
    }
}

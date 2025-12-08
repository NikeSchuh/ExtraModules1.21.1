package de.nike.extramodules2.entities.drops;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.common.loot.LootModifier;

import java.util.Arrays;
import java.util.Random;

public class ElderGuardianLootModifier extends LootModifier {

    public static final MapCodec<ElderGuardianLootModifier> CODEC = RecordCodecBuilder.mapCodec(inst ->
            LootModifier.codecStart(inst)
                    .and(
                            Codec.list(DropItem.DROP_ITEM_CODEC.codec())
                                    .fieldOf("items")
                                    .forGetter(mod -> Arrays.asList(mod.items))
                    )
                    .apply(inst, (conditions, items) -> new ElderGuardianLootModifier(
                            conditions,
                            items.toArray(new DropItem[0])
                    ))
    );


    private final DropItem[] items;

    public ElderGuardianLootModifier(LootItemCondition[] conditionsIn, DropItem[] items) {
        super(conditionsIn);
        this.items = items;
    }

    @Override
    public MapCodec<? extends IGlobalLootModifier> codec() {
        return CODEC;
    }

    @Override
    protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        var entity = context.getParamOrNull(LootContextParams.THIS_ENTITY);
        if (entity == null || entity.getType() != EntityType.ELDER_GUARDIAN) return generatedLoot;

        int lootingLevel = 0;
        RandomSource random = context.getRandom();

        for (DropItem drop : items) {
            if (random.nextFloat() > drop.chance) continue;

            int amount = drop.min + random.nextInt(drop.max - drop.min + 1);
            amount += Math.round(drop.lootingMultiplier * lootingLevel);

            if (amount > 0) {
                Item item = BuiltInRegistries.ITEM.get(drop.id);
                if (item != null) {
                    generatedLoot.add(new ItemStack(item, amount));
                }
            }
        }

        return generatedLoot;
    }

    public static class DropItem {

        public static final MapCodec<DropItem> DROP_ITEM_CODEC = RecordCodecBuilder.mapCodec(inst ->
                inst.group(
                        Codec.STRING.fieldOf("id").forGetter(drop -> drop.id.toString()),
                        Codec.INT.optionalFieldOf("min", 1).forGetter(drop -> drop.min),
                        Codec.INT.optionalFieldOf("max", 1).forGetter(drop -> drop.max),
                        Codec.FLOAT.optionalFieldOf("looting_multiplier", 0f).forGetter(drop -> drop.lootingMultiplier),
                        Codec.FLOAT.optionalFieldOf("chance", 1f).forGetter(drop -> drop.chance)
                ).apply(inst, (idStr, min, max, lootingMultiplier, chance) -> {
                    ResourceLocation id = ResourceLocation.tryParse(idStr);
                    if (id == null) throw new IllegalArgumentException("Invalid ResourceLocation: " + idStr);
                    return new DropItem(id, min, max, lootingMultiplier, chance);
                })
        );

        public final ResourceLocation id;
        public final int min;
        public final int max;
        public final float lootingMultiplier;
        public final float chance;

        public DropItem(ResourceLocation id, int min, int max, float lootingMultiplier, float chance) {
            this.id = id;
            this.min = min;
            this.max = max;
            this.lootingMultiplier = lootingMultiplier;
            this.chance = chance;
        }
    }
}

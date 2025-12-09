package de.nike.extramodules2.modules.entities.defensebrain;

import codechicken.lib.gui.modular.elements.GuiElement;
import codechicken.lib.gui.modular.lib.GuiRender;
import com.brandon3055.brandonscore.api.BCStreamCodec;
import com.brandon3055.brandonscore.api.TechLevel;
import com.brandon3055.brandonscore.api.power.IOPStorage;
import com.brandon3055.draconicevolution.api.config.BooleanProperty;
import com.brandon3055.draconicevolution.api.config.ConfigProperty;
import com.brandon3055.draconicevolution.api.modules.Module;
import com.brandon3055.draconicevolution.api.modules.entities.ShieldControlEntity;
import com.brandon3055.draconicevolution.api.modules.lib.ModuleContext;
import com.brandon3055.draconicevolution.api.modules.lib.ModuleEntity;
import com.brandon3055.draconicevolution.api.modules.lib.StackModuleContext;
import com.brandon3055.draconicevolution.init.DEModules;
import com.brandon3055.draconicevolution.init.ItemData;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import de.nike.extramodules2.entities.EMDamageTypes;
import de.nike.extramodules2.entities.EMEntities;
import de.nike.extramodules2.entities.projectiles.DraconicLightningChain;
import de.nike.extramodules2.items.EMItemData;
import de.nike.extramodules2.modules.EMModuleTypes;
import de.nike.extramodules2.modules.data.DefenseBrainData;
import de.nike.extramodules2.modules.data.DefenseData;
import de.nike.extramodules2.network.ModuleNetwork;
import de.nike.extramodules2.client.sounds.EMSounds;
import de.nike.extramodules2.utils.*;
import net.minecraft.ChatFormatting;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.fml.util.thread.EffectiveSide;
import org.joml.Vector2f;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.*;
import java.util.List;

public class DefenseBrainEntity extends ModuleEntity {

    public static final Codec<DefenseBrainEntity> CODEC = RecordCodecBuilder.create((builder) ->
            builder.group(
                    DEModules.codec().fieldOf("module").forGetter(ModuleEntity::getModule),
                    Codec.INT.fieldOf("gridx").forGetter(ModuleEntity::getGridX),
                    Codec.INT.fieldOf("gridy").forGetter(ModuleEntity::getGridY),
                    BooleanProperty.CODEC.fieldOf("defense_brain_mod.defend_player").forGetter(e -> e.defendPlayer),
                    Codec.FLOAT.fieldOf("rageProgress").forGetter(e -> e.rageProgress),
                    Codec.INT.fieldOf("rageTicks").forGetter(e -> e.rageTicks)
            ).apply(builder, DefenseBrainEntity::new)
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, DefenseBrainEntity> STREAM_CODEC = BCStreamCodec.composite(
            DEModules.streamCodec(), ModuleEntity::getModule,
            ByteBufCodecs.INT, ModuleEntity::getGridX,
            ByteBufCodecs.INT, ModuleEntity::getGridY,
            BooleanProperty.STREAM_CODEC, e -> e.defendPlayer,
            ByteBufCodecs.FLOAT, e -> e.rageProgress,
            ByteBufCodecs.INT, e -> e.rageTicks,
            DefenseBrainEntity::new
    );
    public static final float EYE_SPEED = 0.01f;
    public static final int EYE_COLOR = new Color(174, 167, 164).getRGB();
    public static final int RAGE_COLOR_EYE = new Color(255, 0, 0, 50).getRGB();
    public static final int RAGE_COLOR = new Color(255, 0, 0, 65).getRGB();
    private static final int rectWidth = 8;
    private static final int rectHeight = 6;
    private static final int rectOffsetX = 12;
    private static final int rectOffsetY = 14;
    private static final int EYE_RAGE_CHANGE_DELAY = 5;
    // Eye Size
    private static final int eyeWidth = 4;
    private static final int eyeHeight = 4;
    // Wow i have to do this help me god
    private static final HashMap<ServerPlayer, Float> rageIncreaseMap = new HashMap<>();
    private static int currentEyeChangeDelay = EYE_RAGE_CHANGE_DELAY;
    private static float clientLastRageProgress = 0F;

    // Client Stuff
    private static int lastRectX = 0;
    private static int lastRectY = 0;
    private static Vector2f currentPosition;

    //


    // Rage Mode
    //private final int EYE_POSITION_CHANGE_DELAY = 5;
    //private final float RAGE_CHARGE_LOSE = 0.005f;
    // Eye Rect
    private static final Vector2f currentTarget = new Vector2f();
    private static final EyeMode gurdianEyeMode = EyeMode.CHASING;
    private static float currentPitch = 0.5f;
    private static boolean rising = true;
    // SYNCED VALUES
    private int rageTicks = 0;
    private float rageProgress = 0.5f;
    private BooleanProperty defendPlayer;


    public DefenseBrainEntity(Module module) {
        super(module);
        defendPlayer = new BooleanProperty("defense_brain_mod.defend_player", true).setFormatter(ConfigProperty.BooleanFormatter.YES_NO);
    }

    DefenseBrainEntity(Module<?> module, int gridX, int gridY, BooleanProperty defendPlayer, float rageProgress, int rageTicks) {
        super(module, gridX, gridY);
        this.defendPlayer = defendPlayer.copy();
        this.rageProgress = rageProgress;
        this.rageTicks = rageTicks;
    }

    public void getEntityProperties(List properties) {
        properties.add(defendPlayer);
    }

    @Override
    public void tick(ModuleContext context) {
        if (!defendPlayer.getValue()) return;
        if (context instanceof StackModuleContext moduleContext) {
            if (EffectiveSide.get().isServer()) {
                DefenseBrainData brainData = (DefenseBrainData) module.getData();
                if (moduleContext.getEntity() instanceof ServerPlayer serverPlayer) {
                    IOPStorage energyStorage = context.getOpStorage();

                    int opCost = brainData.getOpTickCost();

                    if (rageIncreaseMap.containsKey(serverPlayer)) {
                        if (!isInRageMode())
                            rageProgress = NikesMath.saveTotalPercentage(rageProgress += rageIncreaseMap.get(serverPlayer));
                        rageIncreaseMap.remove(serverPlayer);
                    }

                    if (rageProgress > 0F && rageProgress < 1.0F) {
                        rageProgress = Math.max(0.0F, rageProgress - 0.0025F);
                    }

                    if (rageProgress >= 1.0F && !isInRageMode()) {
                        rageTicks = brainData.getRageTicks();
                        rageProgress = 0F;
                        serverPlayer.playNotifySound(SoundEvents.GUARDIAN_DEATH, SoundSource.MASTER, 3f, 0.5f);
                        serverPlayer.playNotifySound(SoundEvents.ELDER_GUARDIAN_CURSE, SoundSource.MASTER, 1f, (float) (Math.random() + Math.random()));
                        serverPlayer.playNotifySound(SoundEvents.ELDER_GUARDIAN_AMBIENT, SoundSource.MASTER, 1f, (float) (Math.random() * 0.6F));

                    }

                    if (rageTicks > 0) {
                        if (energyStorage.getOPStored() < opCost) {
                            rageTicks = 0;
                        } else energyStorage.modifyEnergyStored(-opCost);
                        rageTick(serverPlayer, brainData, moduleContext);
                        rageTicks--;
                    }

                }
            } else {
                ClientLevel clientWorld = (ClientLevel) moduleContext.getEntity().level();
                if (moduleContext.getEntity() instanceof LocalPlayer localPlayer) {
                    if (isInRageMode()) {

                        if (localPlayer.tickCount % 5 == 0) {
                            localPlayer.playSound(SoundEvents.GUARDIAN_AMBIENT, 0.5f, currentPitch);
                            localPlayer.playSound(SoundEvents.ELDER_GUARDIAN_AMBIENT, 0.5f, currentPitch);

                            if (rising) {
                                currentPitch += 0.2f;
                            } else currentPitch -= 0.2f;

                            if (currentPitch > 2) {
                                rising = false;
                            } else if (currentPitch < 0.5) rising = true;
                        }

                        if (currentEyeChangeDelay == 0) {
                            setEyeTarget(clientWorld.random.nextInt(10000) - 5000, clientWorld.random.nextInt(10000) - 5000, lastRectX, lastRectY);
                            currentEyeChangeDelay = EYE_RAGE_CHANGE_DELAY;
                        } else currentEyeChangeDelay--;
                    }
                }

            }
        }
    }

    public void chainLightning(ServerPlayer damager, LivingEntity origin, ServerLevel serverLevel, final float range, float damage, int remainingStrikes, List<LivingEntity> blackList) {
        remainingStrikes--;
        if(remainingStrikes == 0) return;

        blackList.add(origin);

        AABB axisAlignedBB = new AABB(origin.position().add(range, range, range), origin.position().subtract(range, range, range));

        List<LivingEntity> livingEntities = serverLevel.getEntitiesOfClass(LivingEntity.class, axisAlignedBB);
        livingEntities.sort(Comparator.comparing(l -> l.blockPosition().distSqr(origin.blockPosition())));

        livingEntities.removeAll(blackList);

        if(livingEntities.isEmpty()) return;

        for(LivingEntity scannedEntity : livingEntities) {
            if (scannedEntity.invulnerableTime > 0
                    || scannedEntity instanceof Villager
                    || scannedEntity.isInvulnerable()
                    || scannedEntity.isDeadOrDying()
                    || !scannedEntity.isAlive()
                    || !scannedEntity.isAttackable()
                    || !damager.canAttack(scannedEntity)) {
                blackList.add(scannedEntity);
                continue;
            }

            LivingEntity target = livingEntities.getFirst();

            DraconicLightningChain chain = new DraconicLightningChain(EMEntities.DRACONIC_LIGHTNING_CHAIN.get(), serverLevel);

            chain.setPos(origin.position().x, origin.position().y, origin.position().z);
            chain.setStartEntity(origin);
            chain.setEndEntity(target);
            chain.setLightningColor(getDefaultLightningColor(module.getModuleTechLevel()));

            ModuleNetwork.sendLightningChain(chain);

            serverLevel.playSound(null, target.blockPosition(), EMSounds.DRACONIC_LIGHTNING_CHAIN_ZAP.get(), SoundSource.HOSTILE, 0.5F, (float) (0.5f + Math.random()));
            target.hurt(EMDamageTypes.playerDraconicLightning(serverLevel, damager, target), damage);
            chainLightning(damager, target, serverLevel, range, damage, remainingStrikes, blackList);
        }


    }

    public void rageTick(ServerPlayer playerEntity, DefenseBrainData data, StackModuleContext moduleContext) {
        if(playerEntity.tickCount % 20 == 0) {
            ServerLevel serverLevel = playerEntity.serverLevel();
            float range = data.getLightningJumpRange();
            AABB axisAlignedBB = new AABB(playerEntity.position().add(range, range / 2, range), playerEntity.position().subtract(range, range / 2, range));

            List<LivingEntity> livingEntities = serverLevel.getEntitiesOfClass(LivingEntity.class, axisAlignedBB);
            livingEntities.sort(Comparator.comparing(l -> l.blockPosition().distSqr(playerEntity.blockPosition())));

            float damage = ModuleHostFinder.unsafeGet(moduleContext.getStack()).getModuleData(EMModuleTypes.DEFENSE_MODULE, new DefenseData(1.0F)).getDamage();

            chainLightning(playerEntity, playerEntity, serverLevel, range, damage, data.getMaximumJumpTargets(), new ArrayList<>());
        }
    }

    private static int getDefaultLightningColor(TechLevel techLevel) {
        int var10000;
        switch (techLevel) {
            case DRACONIUM -> var10000 = 32972;
            case WYVERN -> var10000 = 9175205;
            case DRACONIC -> var10000 = 16748544;
            case CHAOTIC -> var10000 = 12520460;
            default -> throw new MatchException((String)null, (Throwable)null);
        }

        return var10000;
    }

    public boolean isInRageMode() {
        return rageTicks > 0;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void renderModule(GuiElement parent, GuiRender render, int x, int y, int width, int height, double mouseX, double mouseY, boolean stackRender, float partialTicks) {
        super.renderModule(parent, render, x, y, width, height, mouseX, mouseY, stackRender, partialTicks);
        if (stackRender) return;
        if (!defendPlayer.getValue()) return;

        // Eye Bounds / Position
        int rectX = lastRectX = x + rectOffsetX;
        int rectY = lastRectY = y + rectOffsetY;

        if (!isInRageMode()) {
            setEyeTarget(mouseX, mouseY, rectX, rectY);
        }

        if (currentPosition == null) {
            currentPosition = new Vector2f(rectX, rectY);
        }

        // Draw Eye
        currentPosition.lerp(currentTarget, 0.05F);

        float eyeX = Math.max(Math.min(x - eyeWidth / 2, currentPosition.x + rectWidth - eyeWidth), currentPosition.x);
        float eyeY = Math.max(Math.min(y - eyeHeight / 2, currentPosition.y + rectHeight - eyeHeight), currentPosition.y);

        if (isInRageMode()) {
            render.rect(x, y, width, height, RAGE_COLOR);
            render.rect(rectX - 2, rectY, rectWidth + 4, rectHeight, RAGE_COLOR_EYE);

        }

        render.rect(eyeX, eyeY, eyeWidth, eyeHeight, EYE_COLOR);

        if (!isInRageMode() && (rageProgress > 0F || clientLastRageProgress > 0.01)) {
            clientLastRageProgress = NikesMath.lerp(clientLastRageProgress, rageProgress, 0.15F);
            render.rect(x, (y + height) - (height * clientLastRageProgress), width, height * NikesMath.saveTotalPercentage(clientLastRageProgress), RAGE_COLOR);
        }
    }

    public void incomingDamage(ServerPlayer damagedPlayer, double damageAmount, @Nullable ShieldControlEntity shieldControl) {
        if (!defendPlayer.getValue()) return;
        float rageTolerance = shieldControl == null ? (damagedPlayer.getHealth()) : (float) Math.min(Math.max(10, (shieldControl.getShieldPoints() + 1.0F) / 2F), 100);
        float rageIncrease = (float) (damageAmount / rageTolerance);

        rageIncreaseMap.put(damagedPlayer, rageIncreaseMap.getOrDefault(damagedPlayer, 0.0F) + rageIncrease);
    }

    public void setRageProgress(float newRageProgress) {
        rageProgress = NikesMath.saveTotalPercentage(newRageProgress);
        this.markDirty();
    }

    @OnlyIn(Dist.CLIENT)
    public void setEyeTarget(double x, double y, int rectX, int rectY) {
        float targetX = (float) Math.max(Math.min(x - eyeWidth / 2f, rectX + rectWidth - eyeWidth), rectX);
        float targetY = (float) Math.max(Math.min(y - eyeHeight / 2f, rectY + rectHeight - eyeHeight), rectY);
        currentTarget.set(targetX, targetY);
    }

    @Override
    public void onInstalled(ModuleContext context) {
        super.onInstalled(context);
        currentPosition = null;
        if (context instanceof StackModuleContext moduleContext) {
            if (moduleContext.getEntity() instanceof LocalPlayer player) {
                player.playSound(SoundEvents.ELDER_GUARDIAN_DEATH, 1f, 0.5f);
            }
        }
    }

    @Override
    public void saveEntityToStack(ItemStack stack, ModuleContext context) {
        super.saveEntityToStack(stack, context);
        stack.set(EMItemData.RAGE_CHARGE, rageProgress);
        stack.set(EMItemData.RAGE_TICKS, rageTicks);
        stack.set(ItemData.BOOL_ITEM_PROP_1, defendPlayer);
    }

    @Override
    public void loadEntityFromStack(ItemStack stack, ModuleContext context) {
        super.loadEntityFromStack(stack, context);
        this.rageProgress = stack.getOrDefault(EMItemData.RAGE_CHARGE, 0F);
        this.rageTicks = stack.getOrDefault(EMItemData.RAGE_TICKS, 0);
        this.defendPlayer = stack.getOrDefault(ItemData.BOOL_ITEM_PROP_1, this.defendPlayer).copy();
    }

    @Override
    public void addToolTip(List list) {
        super.addToolTip(list);
        DefenseBrainData brainData = (DefenseBrainData) module.getData();
        list.add(TranslationUtils.string(ChatFormatting.GRAY + TranslationUtils.getTranslation("module.extramodules2.defense_brain.rage") + ": " + ChatFormatting.GREEN + String.format("%.0f", rageProgress * 100F) + "%"));
        list.add(TranslationUtils.string(ChatFormatting.GRAY + TranslationUtils.getTranslation("module.extramodules2.defense_brain.tick_cost") + ": " + ChatFormatting.GREEN + FormatUtils.formatE(brainData.getOpTickCost()) + " OP/t"));
        list.add(TranslationUtils.string(ChatFormatting.GRAY + TranslationUtils.getTranslation("module.extramodules2.defense_brain.spread_range")+ ": " + ChatFormatting.GREEN + brainData.getLightningJumpRange() + " blocks"));
        list.add(TranslationUtils.string(ChatFormatting.GRAY + TranslationUtils.getTranslation("module.extramodules2.defense_brain.max_spreads")+ ": " + ChatFormatting.GREEN + brainData.getMaximumJumpTargets() + " mobs"));
        if(isInRageMode()) list.add(TranslationUtils.string(ChatFormatting.GRAY + TranslationUtils.getTranslation("module.extramodules2.defense_brain.rage_duration") + ": " + ChatFormatting.GREEN + String.format("%.2f", rageTicks / 20F) + "s"));
        else list.add(TranslationUtils.string(ChatFormatting.GRAY + TranslationUtils.getTranslation("module.extramodules2.defense_brain.rage_duration") + ": " + ChatFormatting.GREEN + String.format("%.2f", brainData.getRageTicks() / 20F) + "s"));
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof DefenseBrainEntity that)) return false;
        if (!super.equals(o)) return false;
        return rageTicks == that.rageTicks && Float.compare(rageProgress, that.rageProgress) == 0 && Objects.equals(defendPlayer, that.defendPlayer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), rageTicks, rageProgress, defendPlayer);
    }

    @Override
    public ModuleEntity<?> copy() {
        return new DefenseBrainEntity(
                this.module,
                this.getGridX(),
                this.getGridY(),
                this.defendPlayer,
                this.rageProgress,
                this.rageTicks
        );
    }


}

package de.nike.extramodules2.modules.entities.defensebrain;

import codechicken.lib.gui.modular.elements.GuiElement;
import codechicken.lib.gui.modular.lib.GuiRender;
import com.brandon3055.brandonscore.api.BCStreamCodec;
import com.brandon3055.draconicevolution.api.config.BooleanProperty;
import com.brandon3055.draconicevolution.api.config.ConfigProperty;
import com.brandon3055.draconicevolution.api.modules.Module;
import com.brandon3055.draconicevolution.api.modules.entities.AutoFeedEntity;
import com.brandon3055.draconicevolution.api.modules.entities.ShieldControlEntity;
import com.brandon3055.draconicevolution.api.modules.lib.ModuleContext;
import com.brandon3055.draconicevolution.api.modules.lib.ModuleEntity;
import com.brandon3055.draconicevolution.api.modules.lib.StackModuleContext;
import com.brandon3055.draconicevolution.init.DEModules;
import com.brandon3055.draconicevolution.init.ItemData;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import de.nike.extramodules2.ExtraModules2;
import de.nike.extramodules2.items.EMItemData;
import de.nike.extramodules2.modules.EMModules;
import de.nike.extramodules2.modules.data.DefenseBrainData;
import de.nike.extramodules2.modules.data.OxygenStorageData;
import de.nike.extramodules2.network.ModuleNetwork;
import de.nike.extramodules2.utils.NikesMath;
import de.nike.extramodules2.utils.TranslationUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.fml.util.thread.EffectiveSide;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import org.joml.Vector2f;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.List;
import java.util.Objects;

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
    public static final int RAGE_COLOR = new Color(255, 0, 0, 45).getRGB();
    public static final int RAGE_COLOR_EYE = new Color(255, 0, 0, 50).getRGB();

    private static final int rectWidth = 8;
    private static final int rectHeight = 6;
    private static final int rectOffsetX = 12;
    private static final int rectOffsetY = 14;

    // Client Stuff

    private static Vector2f currentPosition;
    private static Vector2f currentTarget = new Vector2f();
    private EyeMode gurdianEyeMode = EyeMode.CHASING;

    //
    private static int lastRectX = 0;
    private static int lastRectY = 0;

    // Rage Mode
    //private final int EYE_POSITION_CHANGE_DELAY = 5;
    //private final float RAGE_CHARGE_LOSE = 0.005f;
    // Eye Rect

    // Eye Size
    private static final int eyeWidth = 4;
    private static final int eyeHeight = 4;

    // SYNCED VALUES
    private int rageTicks = 0;
    private float rageProgress = 0.5f;

    private static float clientLastRageProgress = 0F;

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
        if(EffectiveSide.get().isServer()) {
            if(context instanceof StackModuleContext moduleContext) {
                if(rageProgress > 0F) {
                    rageProgress = Math.max(0.0F, rageProgress-0.0025F);
                }


                if(moduleContext.getEntity().tickCount % 500 == 0) {
                    rageProgress += 0.65F;
                }
            }
        }
    }

    public boolean isInRageMode() {
        return rageTicks > 0;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void renderModule(GuiElement parent, GuiRender render, int x, int y, int width, int height, double mouseX, double mouseY, boolean stackRender, float partialTicks) {
        super.renderModule(parent, render, x, y, width, height, mouseX, mouseY, stackRender, partialTicks);
        if(stackRender) return;

        // Eye Bounds / Position
        int rectX = lastRectX = x + rectOffsetX;
        int rectY = lastRectY = y + rectOffsetY;

        if (gurdianEyeMode == EyeMode.CHASING) {
            setEyeTarget(mouseX, mouseY, rectX, rectY);
        }

        if (currentPosition == null) {
            currentPosition = new Vector2f(rectX, rectY);
        }

        // Draw Eye
        currentPosition.lerp(currentTarget, 0.05F);

        float eyeX = (float) Math.max(Math.min(x - eyeWidth / 2, currentPosition.x + rectWidth - eyeWidth), currentPosition.x);
        float eyeY = (float) Math.max(Math.min(y - eyeHeight / 2, currentPosition.y + rectHeight - eyeHeight), currentPosition.y);

        render.rect(eyeX, eyeY, eyeWidth, eyeHeight, EYE_COLOR);

        if(!isInRageMode() && (rageProgress > 0F || clientLastRageProgress > 0.01)) {
            clientLastRageProgress = NikesMath.lerp(clientLastRageProgress, rageProgress, 0.15F);
            render.rect(x, (y + height) - (height * clientLastRageProgress), width, height * NikesMath.saveTotalPercentage(clientLastRageProgress), RAGE_COLOR);
        }
    }

    public void incomingDamage(ServerPlayer damagedPlayer, double damageAmount, @Nullable ShieldControlEntity shieldControl) {
        float rageTolerance = shieldControl == null ? damagedPlayer.getMaxHealth() : Math.min(Math.max(30, shieldControl.getShieldCapacity() / 2F), 100);
        float rageIncrease = (float) (damageAmount / rageTolerance);
        setRageProgress(rageProgress + rageIncrease);
    }

    public void setRageProgress(float newRageProgress) {
        rageProgress = NikesMath.saveTotalPercentage(newRageProgress);
        this.markDirty();
    }

    @OnlyIn(Dist.CLIENT)
    public void setEyeTarget(double x, double y, int rectX, int rectY) {
        float targetX = (float) Math.max(Math.min(x - eyeWidth / 2, rectX + rectWidth - eyeWidth), rectX);
        float targetY = (float) Math.max(Math.min(y - eyeHeight / 2, rectY + rectHeight - eyeHeight), rectY);
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
        this.defendPlayer = ((BooleanProperty)stack.getOrDefault(ItemData.BOOL_ITEM_PROP_1, this.defendPlayer)).copy();
    }

    @Override
    public void addToolTip(List list) {
        super.addToolTip(list);
        list.add(TranslationUtils.string(ChatFormatting.GRAY +  TranslationUtils.getTranslation("module.extramodules2.defense_brain.rage") + ": " + ChatFormatting.GREEN + String.format("%.2f", rageProgress * 100F)));
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

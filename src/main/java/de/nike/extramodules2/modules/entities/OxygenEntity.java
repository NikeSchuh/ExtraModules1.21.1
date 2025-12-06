package de.nike.extramodules2.modules.entities;

import codechicken.lib.gui.modular.elements.GuiElement;
import codechicken.lib.gui.modular.lib.GuiRender;
import com.brandon3055.brandonscore.api.BCStreamCodec;
import com.brandon3055.draconicevolution.api.config.BooleanProperty;
import com.brandon3055.draconicevolution.api.config.ConfigProperty;
import com.brandon3055.draconicevolution.api.modules.Module;
import com.brandon3055.draconicevolution.api.modules.entities.AutoFeedEntity;
import com.brandon3055.draconicevolution.api.modules.lib.ModuleContext;
import com.brandon3055.draconicevolution.api.modules.lib.ModuleEntity;
import com.brandon3055.draconicevolution.api.modules.lib.StackModuleContext;
import com.brandon3055.draconicevolution.init.DEModules;
import com.brandon3055.draconicevolution.init.ItemData;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import de.nike.extramodules2.ExtraModules2;
import de.nike.extramodules2.items.EMItemData;
import de.nike.extramodules2.modules.data.OxygenStorageData;
import de.nike.extramodules2.utils.ColorUtil;
import de.nike.extramodules2.utils.NikesMath;
import de.nike.extramodules2.utils.NikesRendering;
import de.nike.extramodules2.utils.TranslationUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.fml.Logging;
import net.neoforged.fml.util.thread.EffectiveSide;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Objects;

public class OxygenEntity extends ModuleEntity {


    private static final DecimalFormat toolTipFormat = new DecimalFormat("#.##");
    public static final Codec<OxygenEntity> CODEC = RecordCodecBuilder.create((builder) -> builder.group(DEModules.codec().fieldOf("module").forGetter(ModuleEntity::getModule), Codec.INT.fieldOf("gridx").forGetter(ModuleEntity::getGridX), Codec.INT.fieldOf("gridy").forGetter(ModuleEntity::getGridY), Codec.INT.fieldOf("oxygen_stored").forGetter((e) -> e.oxygenStored), BooleanProperty.CODEC.fieldOf("oxygen_mod.consume_oxygen").forGetter((e) -> e.consumeOxygen)).apply(builder, OxygenEntity::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, OxygenEntity> STREAM_CODEC;

    private BooleanProperty consumeOxygen;

    private int oxygenStored;

    public OxygenEntity(Module<OxygenStorageData> module) {
        super(module);
        consumeOxygen = new BooleanProperty("oxygen_mod.consume_oxygen", true).setFormatter(ConfigProperty.BooleanFormatter.YES_NO);
    }

    OxygenEntity(Module<?> module, int gridX, int gridY, int oxygenStored, BooleanProperty consumeFood) {
        super(module, gridX, gridY);
        this.consumeOxygen = (new BooleanProperty("oxygen_mod.consume_oxygen", true)).setFormatter(ConfigProperty.BooleanFormatter.YES_NO);
        this.oxygenStored = oxygenStored;
        consumeOxygen.setValue(consumeFood.getValue());
    }



    public void getEntityProperties(List properties) {
        properties.add(consumeOxygen);
    }

    private static double progress = 0;

    @Override
    public void onInstalled(ModuleContext context) {
        super.onInstalled(context);
        if(EffectiveSide.get().isClient()) {
            progress = 0;
        }
    }

    @Override
    public ModuleEntity<?> copy() {
        return new OxygenEntity(
                this.module,
                this.getGridX(),
                this.getGridY(),
                this.oxygenStored,
                this.consumeOxygen
        );
    }

    @Override
    public void tick(ModuleContext context) {
        OxygenStorageData oxygenStorageData = (OxygenStorageData) module.getData();
        if (context instanceof StackModuleContext) {
            StackModuleContext moduleContext = (StackModuleContext) context;
            LivingEntity entity = moduleContext.getEntity();
            if (entity instanceof ServerPlayer && entity.tickCount % 10 == 0 && ((StackModuleContext) context).isEquipped()) {
                ServerPlayer playerEntity = (ServerPlayer) entity;
                int maxStorage = oxygenStorageData.getOxygenCapacity();
                if (playerEntity.isUnderWater()) {
                    int missingAir = playerEntity.getMaxAirSupply() - playerEntity.getAirSupply();
                    if (oxygenStored == 0) return;
                    if (oxygenStored > missingAir) {
                        playerEntity.setAirSupply(playerEntity.getMaxAirSupply());
                        oxygenStored -= missingAir;
                      //  this.markDirty();
                    }
                    else {
                        playerEntity.setAirSupply(playerEntity.getAirSupply() + oxygenStored);
                        oxygenStored = 0;
                        playerEntity.playSound(SoundEvents.BUBBLE_COLUMN_BUBBLE_POP, 1f, 1f);
                    }
                }
                else {
                    if (oxygenStored < maxStorage) {
                        oxygenStored += Math.min(oxygenStorageData.getOxygenRefillRate(), maxStorage - oxygenStored);
                     //   this.markDirty();
                    }
                }
            }
        }
    }

    public void setOxygenStored(int oxygenStored) {
        this.oxygenStored = oxygenStored;
    }

    @Override
    public void addToolTip(List list) {
        super.addToolTip(list);
        OxygenStorageData storageData = (OxygenStorageData) module.getData();
        list.add(TranslationUtils.string(ChatFormatting.GRAY +  TranslationUtils.getTranslation("module.extramodules2.oxygen_storage.oxygen_storage") + ": " + ChatFormatting.GREEN + (storageData.getOxygenCapacity() / 10 / 2)));
        list.add(TranslationUtils.string(ChatFormatting.GRAY +  TranslationUtils.getTranslation("module.extramodules2.oxygen_storage.refill_rate") + ": " + ChatFormatting.GREEN + (storageData.getOxygenRefillRate() / 10 / 2)));
        if(oxygenStored == 0) return;
        list.add(TranslationUtils.string(ChatFormatting.GRAY +  TranslationUtils.getTranslation("module.extramodules2.oxygen_storage.oxygen") + ": " + ChatFormatting.GREEN + (oxygenStored / 10 / 2) + ChatFormatting.BLUE + " (" + toolTipFormat.format(((oxygenStored / (double) storageData.getOxygenCapacity())) * 100D) + "%)"));
    }


    @Override
    public void saveEntityToStack(ItemStack stack, ModuleContext context) {
        stack.set(EMItemData.OXYGEN_STORAGE, oxygenStored);
        stack.set(ItemData.BOOL_ITEM_PROP_1, this.consumeOxygen.copy());
    }

    public void loadEntityFromStack(ItemStack stack, ModuleContext context) {
        this.setOxygenStored((Integer)stack.getOrDefault(EMItemData.OXYGEN_STORAGE, 0));
        this.consumeOxygen = ((BooleanProperty)stack.getOrDefault(ItemData.BOOL_ITEM_PROP_1, this.consumeOxygen)).copy();
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void renderModule(GuiElement parent, GuiRender render, int x, int y, int width, int height, double mouseX, double mouseY, boolean renderStack, float partialTicks) {
        super.renderModule(parent, render, x, y, width, height, mouseX, mouseY, renderStack, partialTicks);
        OxygenStorageData storageData =(OxygenStorageData) module.getData();
        double currentProgress = oxygenStored / Math.max(1D, storageData.getOxygenCapacity());
        progress = NikesMath.lerp(progress, currentProgress, 0.070f);
        if(progress >= 0.999D) return;
        NikesRendering.drawChargeProgress(render, x, y, width, height, progress, ColorUtil.generateColor(0, 0, 0, 0));
    }


    @Override
    public boolean equals(Object o) {
        if (!(o instanceof OxygenEntity that)) return false;
        if (!super.equals(o)) return false;
        return oxygenStored == that.oxygenStored;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), this.oxygenStored, this.consumeOxygen.getValue());
    }

    static {
        STREAM_CODEC = BCStreamCodec.composite(DEModules.streamCodec(), ModuleEntity::getModule, ByteBufCodecs.INT, ModuleEntity::getGridX, ByteBufCodecs.INT, ModuleEntity::getGridY, ByteBufCodecs.INT, (e) -> e.oxygenStored, BooleanProperty.STREAM_CODEC, (e) -> e.consumeOxygen, OxygenEntity::new);
    }

}

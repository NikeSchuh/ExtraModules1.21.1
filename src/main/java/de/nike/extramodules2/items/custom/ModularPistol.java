package de.nike.extramodules2.items.custom;

import com.brandon3055.brandonscore.api.TechLevel;
import com.brandon3055.brandonscore.api.power.IOPStorage;
import com.brandon3055.brandonscore.utils.EnergyUtils;
import com.brandon3055.draconicevolution.api.capability.DECapabilities;
import com.brandon3055.draconicevolution.api.capability.ModuleHost;
import com.brandon3055.draconicevolution.api.modules.ModuleCategory;
import com.brandon3055.draconicevolution.api.modules.ModuleTypes;
import com.brandon3055.draconicevolution.api.modules.data.ProjectileData;
import com.brandon3055.draconicevolution.api.modules.data.SpeedData;
import com.brandon3055.draconicevolution.api.modules.lib.ModuleHostImpl;
import com.brandon3055.draconicevolution.init.TechProperties;
import de.nike.extramodules2.client.sounds.EMSounds;
import de.nike.extramodules2.entities.EMEntities;
import de.nike.extramodules2.entities.projectiles.DraconicBulletEntity;
import de.nike.extramodules2.items.EMItemData;
import de.nike.extramodules2.modules.EMModuleCategories;
import de.nike.extramodules2.modules.EMModuleTypes;
import de.nike.extramodules2.modules.data.PistolData;
import de.nike.extramodules2.utils.EntityHitUtils;
import de.nike.extramodules2.utils.FormatUtils;
import de.nike.extramodules2.utils.TranslationUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.text.DecimalFormat;
import java.util.List;
import java.util.function.Predicate;

public class ModularPistol extends ModularEnergyItem {

    private static final DecimalFormat numberFormat = new DecimalFormat("#.#");
    private final int gridWidth, gridHeight;
    private TechLevel level;
    private final float defaultDamage;

    public ModularPistol(TechProperties properties, int gridWidth, int gridHeight, float defaultDamage) {
        super(properties);
        this.gridWidth = gridWidth;
        this.gridHeight = gridHeight;
        this.level = properties.getTechLevel();
        this.defaultDamage = defaultDamage;
    }

    @Override
    public @NotNull ModuleHostImpl instantiateHost(ItemStack itemStack) {
        ModuleHostImpl host = new ModuleHostImpl(tier, gridWidth, gridHeight, "pistol", false);
        host.addCategories(ModuleCategory.ENERGY, ModuleCategory.RANGED_WEAPON, EMModuleCategories.PISTOL);
        return host;
    }

    public static int getShootCooldown(ItemStack stack) {
        return stack.getOrDefault(EMItemData.PISTOL_SHOOT_COOLDOWN, 0);
    }

    public static void setShootCooldown(ItemStack stack, int cooldown) {
        stack.set(EMItemData.PISTOL_SHOOT_COOLDOWN, cooldown);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        super.inventoryTick(stack, level, entity, slotId, isSelected);
        int shootCooldown = getShootCooldown(stack);
        if(shootCooldown > 0) {
            setShootCooldown(stack, shootCooldown-1);
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        ItemStack stack = player.getItemInHand(usedHand);

        if(getShootCooldown(stack) > 0) return super.use(level, player, usedHand);
        if(level.isClientSide) return super.use(level, player, usedHand);

        ServerLevel serverLevel = (ServerLevel) level;
        ServerPlayer serverPlayer = (ServerPlayer) player;

        ModuleHost moduleHost = DECapabilities.getHost(stack);
        IOPStorage opStorage = EnergyUtils.getStorage(stack);

        if(moduleHost == null || opStorage == null) return super.use(level, player, usedHand);
        if(opStorage.getMaxOPStored() <= 0) return super.use(level, player, usedHand);

        ProjectileData projectileData = moduleHost.getModuleData(ModuleTypes.PROJ_MODIFIER, new ProjectileData(0, 0, 0, 0, 0));
        SpeedData speedData = moduleHost.getModuleData(ModuleTypes.SPEED, new SpeedData(0));
        PistolData pistolData = moduleHost.getModuleData(EMModuleTypes.PISTOL, new PistolData(0f, 0, 0, 0));

        boolean ignoreCancellation = moduleHost.getModuleData(ModuleTypes.PROJ_ANTI_IMMUNE) != null;
        boolean homing = moduleHost.getModuleData(EMModuleTypes.PISTOL_HOMING) != null;

        float velocity = (float) (((0.5F + (techLevelToBaseVel(getTechLevel()) / 4F))) + (0.5 * projectileData.velocity()));
        float baseDamage = calculateDamage(defaultDamage, pistolData.getExtraBaseDamage(), projectileData.damage(), projectileData.velocity());
        int opCost = calculateCostPerShot(baseDamage, pistolData.getCriticalChance(), pistolData.getCriticalDamage(), 0.0F, velocity);

        if(opStorage.getOPStored() < opCost) return super.use(level, player, usedHand);

        opStorage.modifyEnergyStored(-opCost);

        int cooldown = (int) ((100) - (30 * speedData.speedMultiplier()));
        player.getCooldowns().addCooldown(stack.getItem(), cooldown);
        setShootCooldown(stack, cooldown);

        serverLevel.playSound(null, player.blockPosition(), EMSounds.MODULAR_PISTOL_SHOOT.get(), SoundSource.AMBIENT, 0.35f, 1.0f + (serverLevel.random.nextFloat() / 2));

        DraconicBulletEntity bulletEntity = createProjectile(serverLevel, serverPlayer, pistolData, projectileData, stack, baseDamage, velocity, ignoreCancellation);



        bulletEntity.setPos(player.getEyePosition().add(0, -0.25, 0));
        bulletEntity.shootFromRotation(player, player.getXRot(), player.getYRot(), 1.0F, Math.min(velocity, 4.0f), 0.1f + (-1 * projectileData.accuracy()));


        if(homing) {
            EntityHitResult result = EntityHitUtils.getFirstEntityHit(serverLevel, bulletEntity, bulletEntity.position(), player.getLookAngle().normalize(), 100, new AABB(1, 1, 1, -1, -1, -1), (Predicate<Entity>) entity -> !(entity.equals(player)) && !(entity instanceof DraconicBulletEntity) && entity.isAlive() && entity.isAttackable());
            if(result != null) bulletEntity.setTargetEntity(result.getEntity());
            bulletEntity.setSpeedMult(projectileData.velocity() + 1);
        }

        serverLevel.addFreshEntity(bulletEntity);

        return  super.use(level, player, usedHand);
    }

    private DraconicBulletEntity createProjectile(ServerLevel level, ServerPlayer shooter, PistolData pistolData, ProjectileData projectileData, ItemStack weapon, float baseDamage, float velocity, boolean ignoreCancellation) {
        DraconicBulletEntity draconicBulletEntity = new DraconicBulletEntity(EMEntities.DRACONIC_BULLET.get(), level);

        draconicBulletEntity.setOwner(shooter);
        draconicBulletEntity.setNoGravity(true);

        draconicBulletEntity.setColor(getProjAndLightningColor(tier));

        draconicBulletEntity.setBaseDamage(baseDamage);
        draconicBulletEntity.setCritChance(pistolData.getCriticalChance());
        draconicBulletEntity.setCritDamage(2 + pistolData.getCriticalDamage());
        draconicBulletEntity.setPenetration(projectileData.penetration());
        draconicBulletEntity.setTechLevel(getTechLevel());

        draconicBulletEntity.setFireTicks(pistolData.getFireTicks());
        draconicBulletEntity.setIgnoreImmunityCancellation(ignoreCancellation);

        return draconicBulletEntity;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltip, tooltipFlag);

        ModuleHost moduleHost = DECapabilities.getHost(stack);
        IOPStorage opStorage = EnergyUtils.getStorage(stack);

        if(moduleHost == null || opStorage == null) return;

        ProjectileData projectileData = moduleHost.getModuleData(ModuleTypes.PROJ_MODIFIER, new ProjectileData(0, 0, 0, 0, 0));
        PistolData pistolData = moduleHost.getModuleData(EMModuleTypes.PISTOL, new PistolData(0f, 0, 0, 0));

        float velocity = (float) (((0.5F + (techLevelToBaseVel(getTechLevel()) / 4F))) + (0.5 * projectileData.velocity()));
        float baseDamage = calculateDamage(defaultDamage, pistolData.getExtraBaseDamage(), projectileData.damage(), projectileData.velocity());
        int opCost = calculateCostPerShot(baseDamage, pistolData.getCriticalChance(), pistolData.getCriticalDamage(), 0.0F, velocity);
        float maximumPossible = baseDamage * (2 + pistolData.getCriticalDamage());


        tooltip.add(TranslationUtils.string(ChatFormatting.DARK_GREEN +""+ numberFormat.format(baseDamage) + " Damage"));
        tooltip.add(TranslationUtils.string(ChatFormatting.DARK_GREEN +""+ FormatUtils.formatE(opCost) + " OP/shot"));
        if(Screen.hasShiftDown()) {
            tooltip.add(TranslationUtils.string(ChatFormatting.DARK_GREEN +""+ numberFormat.format(maximumPossible) + " Max Damage"));
        }

    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return slotChanged;
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 72000;
    }

    public static float calculateDamage(float pistolDefaultDamage, float pistolExtraBaseDamage, float damageMultiplier, float velocityMultiplier) {
        float baseDamage = pistolDefaultDamage + pistolExtraBaseDamage;
        baseDamage += (baseDamage * (velocityMultiplier / 6));
        return baseDamage + (baseDamage * damageMultiplier);
    }


    public static int calculateCostPerShot(double damage, float criticalChance, float criticalDamage, float lightningChance, float velocity) {
        return Math.max(10, Math.min(2000000000,(int) (((Math.pow(damage, 1.5f) - Math.pow(damage, 1.6f)) * (1 + ((criticalChance * criticalDamage) / 2))) * (1 + (velocity / 8)) + (Math.pow(damage, 1.9f) * (lightningChance * 20)))));
    }

    public static int techLevelToBaseVel(TechLevel level) {
        switch (level) {
            case WYVERN:
                return 1;
            case DRACONIC:
                return 2;
            case CHAOTIC:
                return 3;
            default:
                return 0;
        }
    }

    public static int getProjAndLightningColor(TechLevel level) {
        switch (level) {
            case WYVERN:
                return Color.magenta.getRGB();
            case DRACONIC:
                return 0xff8c00;
            case CHAOTIC:
                return Color.red.getRGB();
            default:
                return Color.WHITE.getRGB();
        }
    }
}

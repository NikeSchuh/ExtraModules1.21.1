package de.nike.extramodules2.entities.projectiles;

import com.brandon3055.brandonscore.api.TechLevel;
import com.brandon3055.brandonscore.network.BCoreNetwork;
import com.brandon3055.draconicevolution.DEConfig;
import com.brandon3055.draconicevolution.entity.projectile.DraconicArrowEntity;
import com.brandon3055.draconicevolution.init.DEContent;
import com.brandon3055.draconicevolution.init.DEDamage;
import de.nike.extramodules2.entities.EMEntities;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.fml.util.thread.EffectiveSide;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class DraconicBulletEntity extends AbstractArrow {

    private static final EntityDataAccessor<Integer> COLOR = SynchedEntityData.defineId(DraconicBulletEntity.class, EntityDataSerializers.INT);;
    private static final EntityDataAccessor<Float> CRIT_CHANCE = SynchedEntityData.defineId(DraconicBulletEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> CRIT_DAMAGE = SynchedEntityData.defineId(DraconicBulletEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Boolean> IGNORE_CANCELLATION = SynchedEntityData.defineId(DraconicBulletEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> FIRE_TICKS = SynchedEntityData.defineId(DraconicBulletEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Float> SPEED_MULT = SynchedEntityData.defineId(DraconicBulletEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Integer> TARGET_ENTITY = SynchedEntityData.defineId(DraconicBulletEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Float> PENETRATION = SynchedEntityData.defineId(DraconicBulletEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Byte> TECH_LEVEL = SynchedEntityData.defineId(DraconicBulletEntity.class, EntityDataSerializers.BYTE);

    private List<Entity> piercedEntities = new ArrayList<>();
    private boolean targetLost = false;

    public DraconicBulletEntity(EntityType<? extends DraconicBulletEntity> entityType, Level level) {
        super(entityType, level);
        this.noCulling = true;
    }

    public void tick() {
        super.tick();
        if(tickCount > 200 && EffectiveSide.get().isServer()) remove(RemovalReason.DISCARDED);
        if(inGroundTime > 60 && EffectiveSide.get().isServer()) remove(RemovalReason.DISCARDED);
        if(!targetLost) {
            Entity target = getTarget();
            if(target == null) {
                targetLost = true;
                return;
            }
            if(target.distanceTo(this) < 0.1f) {
                targetLost = true;
            }
            // Vector3d dir = target.getEyePosition(target.getEyeHeight()).subtract(position()).normalize();
            Vec3 dir = target.position().add(0, target.getBoundingBox().getYsize() / 2.0F, 0).subtract(position()).normalize();
            float speedMult = getSpeedMult() / 2;
            setDeltaMovement(dir.multiply(speedMult, speedMult, speedMult));
        }
    }

    private DamageSource getDamageSource(Entity target) {
        Entity owner = this.getOwner();
        TechLevel techLevel = TechLevel.byIndex((Byte)this.entityData.get(TECH_LEVEL));
        boolean bypassImmune = this.ignoresImmunityCancellation() && DEConfig.projectileAntiImmuneEntities.contains(BuiltInRegistries.ENTITY_TYPE.getKey(target.getType()).toString());
        return DEDamage.draconicArrow(this.level(), this, owner, techLevel, bypassImmune);
    }


    @Override
    protected void onHitEntity(EntityHitResult result) {
        Entity entity = result.getEntity();
        if(entity.level().isClientSide) return;

        ServerLevel level = (ServerLevel) level();

        setTargetEntity(null);

        if(!piercedEntities.contains(entity) && level.random.nextFloat() < getPenetration()) {
            piercedEntities.add(result.getEntity());
            setPenetration(getPenetration()-0.25f);
        } else remove(RemovalReason.DISCARDED);

        DamageSource source = getDamageSource(entity);

        boolean crit = level.random.nextFloat() < getCritChance();
        double damage = getBaseDamage();
        if(crit) damage += getCritDamage();

        if(getFireTicks() > 0) {
            entity.setRemainingFireTicks(getFireTicks() / 20);
        }

        damage(entity, source, (float) damage);
    }

    private void damage(Entity entity, DamageSource source, float amount) {
        entity.hurt(source, amount);
    }

    public Entity getTarget() {
        if(entityData.get(TARGET_ENTITY) == -1) return null;
        return level().getEntity(entityData.get(TARGET_ENTITY));
    }

    public void setColor(int value) {
        entityData.set(COLOR, value);
    }

    public int getColor() {
        return entityData.get(COLOR);
    }

    public void setCritChance(float value) {
        entityData.set(CRIT_CHANCE, value);
    }

    public float getCritChance() {
        return entityData.get(CRIT_CHANCE);
    }

    public void setCritDamage(float value) {
        entityData.set(CRIT_DAMAGE, value);
    }

    public float getCritDamage() {
        return entityData.get(CRIT_DAMAGE);
    }

    public void setIgnoreImmunityCancellation(boolean value) {
        entityData.set(IGNORE_CANCELLATION, value);
    }

    public boolean ignoresImmunityCancellation() {
        return entityData.get(IGNORE_CANCELLATION);
    }

    public void setFireTicks(int value) {
        entityData.set(FIRE_TICKS, value);
    }

    public int getFireTicks() {
        return entityData.get(FIRE_TICKS);
    }

    public void setSpeedMult(float value) {
        entityData.set(SPEED_MULT, value);
    }

    public float getSpeedMult() {
        return entityData.get(SPEED_MULT);
    }

    public void setTargetEntity(Entity entity) {
        if(entity == null)  entityData.set(TARGET_ENTITY, -1);
        else entityData.set(TARGET_ENTITY, entity.getId());
    }

    public Entity getTargetEntity() {
        return level().getEntity(entityData.get(TARGET_ENTITY));
    }


    public void setPenetration(float value) {
        entityData.set(PENETRATION, value);
    }

    public float getPenetration() {
        return entityData.get(PENETRATION);
    }

    public void setTechLevel(TechLevel value) {
        entityData.set(TECH_LEVEL, (byte) value.index);
    }

    public byte getTechLevel() {
        return entityData.get(TECH_LEVEL);
    }




    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
      super.defineSynchedData(builder);
        builder.define(COLOR, Color.WHITE.getRGB());
        builder.define(CRIT_CHANCE, 0.0F);
        builder.define(CRIT_DAMAGE, 1.0F);
        builder.define(IGNORE_CANCELLATION, false);
        builder.define(FIRE_TICKS, 0);
        builder.define(SPEED_MULT, 1.0F);
        builder.define(TARGET_ENTITY, -1);
        builder.define(PENETRATION, 0.0F);
        builder.define(TECH_LEVEL, (byte) TechLevel.WYVERN.index);
    }

    public Packet<ClientGamePacketListener> getAddEntityPacket(ServerEntity serverEntity) {
        Entity entity = this.getOwner();
        return (Packet<ClientGamePacketListener>) (BCoreNetwork.getEntitySpawnPacket(this, serverEntity, entity == null ? 0 : entity.getId()));
    }

    @Override
    protected ItemStack getDefaultPickupItem() {
        return ItemStack.EMPTY;
    }

}

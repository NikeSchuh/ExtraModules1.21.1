package de.nike.extramodules2.entities.projectiles;

import codechicken.lib.packet.PacketCustom;
import de.nike.extramodules2.ExtraModules2;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.awt.*;
import java.util.concurrent.ThreadLocalRandom;

public class DraconicLightningChain extends Entity {

    private static final EntityDataAccessor<Integer> LIGHTNING_COLOR = SynchedEntityData.defineId(DraconicLightningChain.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> LIGHTNING_SIZE = SynchedEntityData.defineId(DraconicLightningChain.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> LIGHTNING_SEGMENTS = SynchedEntityData.defineId(DraconicLightningChain.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Integer> START_ENTITY = SynchedEntityData.defineId(DraconicLightningChain.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> END_ENTITY = SynchedEntityData.defineId(DraconicLightningChain.class, EntityDataSerializers.INT);



    private long lightningSeed = ThreadLocalRandom.current().nextInt(0, 10000000);

    private int aliveTicks = 80;

    public DraconicLightningChain(EntityType<?> entityType, Level level) {
        super(entityType, level);
        this.noCulling = true;
    }

    public void setStartEntity(Entity startEntity) {
        entityData.set(START_ENTITY, startEntity.getId());

    }

    public void setEndEntity(Entity endEntity) {
        entityData.set(END_ENTITY, endEntity.getId());
    }


    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(LIGHTNING_COLOR, Color.RED.getRGB());
        builder.define(LIGHTNING_SEGMENTS, 6);
        builder.define(LIGHTNING_SIZE, 8);
        builder.define(START_ENTITY, -1);
        builder.define(END_ENTITY, -1);
    }

    public long getLightningSeed() {
        return lightningSeed;
    }

    @Override
    public boolean shouldRender(double x, double y, double z) {
        return true;
    }

    public boolean shouldRenderAtSqrDistance(double distance) {
        double d0 = (double)64.0F * getViewScale();
        return distance < d0 * d0;
    }

    public Entity getStartEntity() {
        return level().getEntity(entityData.get(START_ENTITY));
    }

    public Entity getEndEntity() {
        return level().getEntity(entityData.get(END_ENTITY));
    }

    @Override
    public void tick() {
        super.tick();
        Level level = level();
        if(level().isClientSide) {
            if(tickCount % 2 == 0) {
                lightningSeed++;
            }
            Entity start = level.getEntity(entityData.get(START_ENTITY));
            Entity end = level.getEntity(entityData.get(END_ENTITY));

            if(start == null || end == null || start.isRemoved() ||end.isRemoved()) {
                ExtraModules2.LOGGER.info("Removing draconic chain due to invalid start or end entity.");
                remove(RemovalReason.DISCARDED);
                return;
            }
        } else {
            if(aliveTicks == 0) {
                remove(RemovalReason.DISCARDED);
                return;
            } else aliveTicks--;
        }
    }

    public void setLightningColor(int color) {
        entityData.set(LIGHTNING_COLOR, color);
    }

    public void setLightningSize(int size) {
        entityData.set(LIGHTNING_SIZE, size);
    }

    public void setLightningSegments(int segments) {
        entityData.set(LIGHTNING_SEGMENTS, segments);
    }

    public int getLightningColor() {
        return entityData.get(LIGHTNING_COLOR);
    }

    public int getLightningSize() {
        return entityData.get(LIGHTNING_SIZE);
    }

    public int getLightningSegments() {
        return entityData.get(LIGHTNING_SEGMENTS);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compoundTag) {

    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compoundTag) {

    }

}

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

    private long lightningSeed = ThreadLocalRandom.current().nextInt(0, 10000000);
    private int aliveTicks = 2;

    private Entity start;
    private Entity end;
    private int startId;
    private int endId;
    private int color;

    public DraconicLightningChain(EntityType<?> entityType, Level level) {
        super(entityType, level);
        this.noCulling = true;
    }

    public int getStartId() {
        return startId;
    }

    public int getEndId() {
        return endId;
    }

    public void setStartEntity(Entity startEntity) {
        this.start = startEntity;
        this.startId = startEntity.getId();
    }

    public void setEndEntity(Entity endEntity) {
        this.end = endEntity;
        this.endId = endEntity.getId();
    }

    public Entity getStartEntity() {
        return start;
    }

    public Entity getEndEntity() {
        return end;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {

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


    @Override
    public void tick() {
        super.tick();
        if(level().isClientSide) {
            if(tickCount % 2 == 0) {
                lightningSeed++;
            }

            ExtraModules2.LOGGER.info(getId() + " " +start.position());

            if(start == null || end == null || start.isRemoved() ||end.isRemoved()) {
                ExtraModules2.LOGGER.info("Removing draconic chain due to invalid start or end entity.");
                remove(RemovalReason.DISCARDED);
                return;
            }

            if(aliveTicks == 0) {
                remove(RemovalReason.DISCARDED);
                return;
            } else aliveTicks--;
        }
    }

    public void setLightningColor(int color) {
        this.color = color;
    }

    public int getLightningColor() {
        return color;
    }


    @Override
    protected void readAdditionalSaveData(CompoundTag compoundTag) {

    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compoundTag) {

    }

}

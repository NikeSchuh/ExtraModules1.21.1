package de.nike.extramodules2.utils;

import codechicken.lib.vec.Vector3;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

import java.util.function.Predicate;

public class EntityHitUtils {

    public static EntityHitResult getFirstEntityHit(Level level, Entity origin, Vec3 start, Vec3 direction, int samples, AABB box, Predicate<Entity> filter) {
        Vector3 current = new Vector3(start.x, start.y, start.z);
        for(int i = 0; i < samples; i++) {
            current = current.add(direction);
            for(Entity entity : level.getEntities(origin, new AABB(current.x + box.maxX, current.y + box.maxY, current.z + box.maxZ, current.x + box.minX, current.y + box.minY, current.z + box.minZ))) {
                if(filter.test(entity)) {
                    return new EntityHitResult(entity);
                }
            }
        }
        return null;

    }

}

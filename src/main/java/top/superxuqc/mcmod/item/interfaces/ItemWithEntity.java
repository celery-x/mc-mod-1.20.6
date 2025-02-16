package top.superxuqc.mcmod.item.interfaces;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;

import java.util.List;

public interface ItemWithEntity {

    default void setEntityVelocity(Vec3d entitiesVelocity) {
    }

    default Entity getEntityOfItem() {
        return null;
    }

    ;

    default List<Entity> getEntitesOfItem() {
        return null;
    }

    ;

    default void setEntityOfItem(Entity entityOfItem) {
    }

    ;

    default void setEntitiesOfItem(List<Entity> entityOfItem) {
    }

    ;

    default void addEntitiesOfItem(Entity entityOfItem) {
    }

    ;
}

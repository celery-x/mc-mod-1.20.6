package top.superxuqc.mcmod.item.interfaces;

import net.minecraft.entity.Entity;

import java.util.List;

public interface ItemWithEntity {
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

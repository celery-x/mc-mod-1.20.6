package top.superxuqc.mcmod.item.interfaces;

import net.minecraft.entity.Entity;

public interface ItemWithEntity {
    Entity getEntityOfItem();

    void setEntityOfItem(Entity entityOfItem);
}

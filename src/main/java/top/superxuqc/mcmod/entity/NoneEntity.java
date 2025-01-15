package top.superxuqc.mcmod.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;

public class NoneEntity extends Entity {
    public NoneEntity(EntityType<?> type, World world) {
        super(type, world);
    }

    @Override
    public int getId() {
        return -1;
    }

    public NoneEntity(World world) {
        super(EntityType.ARROW, world);

    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {

    }

    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {

    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {

    }
}

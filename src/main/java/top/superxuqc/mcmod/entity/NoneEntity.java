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
        return -998;
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

//    @Override
//    public boolean equals(Object o) {
//        if (o instanceof NoneEntity e) {
//            return
//                    (int)e.getX() == (int) this.getX() &&
//                    (int)e.getY() == (int) this.getY() &&
//                    (int)e.getZ() == (int) this.getZ();
//        } else {
//            return false;
//        }
//    }
//
//    @Override
//    public int hashCode() {
//        int dx = (int) this.getX();
//        int dy = (int) this.getY();
//        int dz = (int) this.getZ();
//        return Integer.hashCode(dx + dy + dz);
//    }
}

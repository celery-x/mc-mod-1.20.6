package top.superxuqc.mcmod.common;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

public class TargetEntity extends Entity {
    public TargetEntity(EntityType<?> type, World world) {
        super(type, world);
        this.setBoundingBox(new Box(0.0, 0.0, 0.0, 1.0, 1.0, 1.0));
    }

    public TargetEntity(int x, int y, int z) {
        this(null, null);
        this.setPos(x,y,z);
        this.setBoundingBox(new Box(0.0, 0.0, 0.0, 1.0, 1.0, 1.0));
    }

    @Override
    public double getEyeY() {
        return 0.5;
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

package top.superxuqc.mcmod.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import top.superxuqc.mcmod.register.ModEntryTypes;

import java.util.BitSet;

public class RepulsiveForceEntity extends Entity {

    public int step = 0;

    public int level;

    public RepulsiveForceEntity(EntityType<?> type, World world) {
        super(type, world);
    }

    public RepulsiveForceEntity(World world, int level) {
        this(ModEntryTypes.NoneViewEntity, world);
        setLevel(level);
    }

    private int ageStep = 0;


    @Override
    public void tick() {
        ageStep++;
        if (ageStep % 4 != 0 ){
            return;
        }
        System.out.println(step);
        if (step > level) {
            discard();
            return;
        }
        int distanceEffect = step;
        BlockPos pos = this.getBlockPos();

        int x1 = pos.getX();
        int y1 = pos.getY();
        int z1 = pos.getZ();

        int x,y,z;

        // 左面
        x = x1 - step;
        for (y = y1 - step; y <= y1 + step; y++) {
            for (z = z1 - step; z <= z1 + step; z++) {
                breakTheBlock(x, y, z, pos);
            }
        }


        // 右边
        x = x1 + step;
        for (y = y1 - step; y <= y1 + step; y++) {
            for (z = z1 - step; z <= z1 + step; z++) {
                breakTheBlock(x, y, z, pos);
            }
        }

        //上面
        y = y1 + step;
        for (x = x1 - step; x <= x1 + step; x++) {
            for (z = z1 - step; z <= z1 + step; z++) {
                breakTheBlock(x, y, z, pos);
            }
        }

        //下面
        y = y1 - step;
        for (x = x1 - step; x <= x1 + step; x++) {
            for (z = z1 - step; z <= z1 + step; z++) {
                breakTheBlock(x, y, z, pos);
            }
        }

        //前面
        z = z1 + step;
        for (x = x1 - step; x <= x1 + step; x++) {
            for (y = y1 - step; y <= y1 + step; y++) {
                breakTheBlock(x, y, z, pos);
            }
        }

        //后面
        z = z1 - step;
        for (x = x1 - step; x <= x1 + step; x++) {
            for (y = y1 - step; y <= y1 + step; y++) {
                breakTheBlock(x, y, z, pos);
            }
        }
        step++;
    }

    private void breakTheBlock(int x, int y, int z, BlockPos pos) {
        BlockPos pos1 = new BlockPos(x, y, z);
        BlockState blockState = getWorld().getBlockState(pos1);
        if (!blockState.isAir() && !pos.add(0, -1, 0).equals(pos1) && calculateDistance(pos, pos1) < level) {
            if (blockState.isOf(Blocks.WATER)) {
                getWorld().setBlockState(pos1, Blocks.AIR.getDefaultState());
                return;
            }
            float blastResistance = blockState.getBlock().getBlastResistance();
            float h = 4;
            h -= (blastResistance + 0.3F) * 0.3F;
            if (h > 0.0F) {
                getWorld().breakBlock(pos1, false);
            }
        }
    }

    private double calculateDistance(BlockPos pos1, BlockPos pos2) {
        int x = pos1.getX() - pos2.getX();
        int y = pos1.getY() - pos2.getY();
        int z = pos1.getZ() - pos2.getZ();
        return Math.sqrt(x * x + y * y + z * z);
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

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }
}

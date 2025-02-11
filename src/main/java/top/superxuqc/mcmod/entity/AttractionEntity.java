package top.superxuqc.mcmod.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.*;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.joml.Vector3f;
import top.superxuqc.mcmod.common.BooleanHelper;
import top.superxuqc.mcmod.common.VelocityUtils;
import top.superxuqc.mcmod.block.interfaces.WithTag;
import top.superxuqc.mcmod.register.ModEntryTypes;
import top.superxuqc.mcmod.register.ModItemRegister;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class AttractionEntity extends ThrownItemEntity {

    private int step = 0;

    private int level;

    private BooleanHelper using;

    private List<BlockPos> blockList = new CopyOnWriteArrayList<>();

    private List<FallingBlockEntity> transformBlocks = new CopyOnWriteArrayList<>();

    public AttractionEntity(EntityType<? extends ThrownItemEntity> entityType, World world) {
        super(ModEntryTypes.ATTRACTION_ENTITY_TYPE, world);
    }

    public AttractionEntity(World world, int level, BooleanHelper using) {
        this(ModEntryTypes.ATTRACTION_ENTITY_TYPE, world);
        setLevel(level);
        this.using = using;
    }

    private int ageStep = 0;

    @Override
    public void tick() {
        ageStep++;
        super.tick();
        if (!getWorld().isClient()) {
            if (using == null) {
                discard();
            }
            if (ageStep > 20 * 5) {
                using.setFalse();
            }
            if (ageStep < 10) {
                // 先飞10个tick
                super.tick();
            } else if (ageStep == 10) {
                blockList.add(new BlockPos(getBlockPos()));
                this.setVelocity(Vec3d.ZERO);
                this.setNoGravity(true);
            } else {
                if (using != null && !getWorld().isClient() && !using.isB()) {
                    if (transformBlocks.isEmpty()) {
                        discard();
                    }
                }
                if (ageStep % 4 != 0) {
                    return;
                }


                if (getWorld() instanceof ServerWorld serverWorld) {
                    effectEntity(this, serverWorld, level);
                    effectBlock();
                    effectTransformBlocks();
                }
                if (step <= level) {
//                discard();
//                return;
                    step++;
                }

            }
        }
    }


    private void transformBlock2Entity(int x, int y, int z) {
        BlockPos targetPos = new BlockPos(x, y, z);
        World world = getWorld();
        BlockState blockState = world.getBlockState(targetPos);
        if (blockState.isAir() || calculateDistance(targetPos, this.getBlockPos()) > level) {
            return;
        }
        if (blockState.isOf(Blocks.WATER) || blockState.isOf(Blocks.LAVA)) {
            //流体直接破坏
            world.setBlockState(targetPos, Blocks.AIR.getDefaultState());
            return;
        }
        BlockEntity blockEntity = world.getBlockEntity(targetPos);

        CanMoveBlockEntity fallingBlockEntity = new CanMoveBlockEntity(world, targetPos.getX(), targetPos.getY(), targetPos.getZ(), blockState, blockList, using, transformBlocks);
        Vec3d p = fallingBlockEntity.getPos().add(0.5d, 1.1d, 0.5d);
        fallingBlockEntity.setPos(p.x, p.y, p.z);
        fallingBlockEntity.setNoGravity(true);
        Vector3f calculate = VelocityUtils.calculate(this, fallingBlockEntity);
        fallingBlockEntity.setVelocity(new Vec3d(calculate).multiply(-0.1));
        if (blockEntity != null) {
            NbtCompound nbt = blockEntity.createNbt(this.getRegistryManager());
            if (!nbt.isEmpty()) {
                fallingBlockEntity.blockEntityData = nbt;
            }
        }
        transformBlocks.add(fallingBlockEntity);
        world.breakBlock(targetPos, false);
        world.spawnEntity(fallingBlockEntity);
    }

    private void effectTransformBlocks() {
        this.transformBlocks.forEach(v -> {
            Vector3f calculate = VelocityUtils.calculate(this, v);
            v.setVelocity(new Vec3d(calculate).multiply(-0.1).addRandom(getWorld().getRandom(), 0.03F * level));
        });
    }

    private void effectBlock() {
        BlockPos pos = this.getBlockPos();

        int x1 = pos.getX();
        int y1 = pos.getY();
        int z1 = pos.getZ();

        int x, y, z;

        // 左面
        x = x1 - step;
        for (y = y1 - step; y <= y1 + step; y++) {
            for (z = z1 - step; z <= z1 + step; z++) {
                transformBlock2Entity(x, y, z);
            }
        }


        // 右边
        x = x1 + step;
        for (y = y1 - step; y <= y1 + step; y++) {
            for (z = z1 - step; z <= z1 + step; z++) {
                transformBlock2Entity(x, y, z);
            }
        }

        //上面
        y = y1 + step;
        for (x = x1 - step; x <= x1 + step; x++) {
            for (z = z1 - step; z <= z1 + step; z++) {
                transformBlock2Entity(x, y, z);
            }
        }

        //下面
        y = y1 - step;
        for (x = x1 - step; x <= x1 + step; x++) {
            for (z = z1 - step; z <= z1 + step; z++) {
                transformBlock2Entity(x, y, z);
            }
        }

        //前面
        z = z1 + step;
        for (x = x1 - step; x <= x1 + step; x++) {
            for (y = y1 - step; y <= y1 + step; y++) {
                transformBlock2Entity(x, y, z);
            }
        }

        //后面
        z = z1 - step;
        for (x = x1 - step; x <= x1 + step; x++) {
            for (y = y1 - step; y <= y1 + step; y++) {
                transformBlock2Entity(x, y, z);
            }
        }
    }

    private void effectEntity(Entity center, ServerWorld serverWorld, int level) {
        List<LivingEntity> effectEntities = serverWorld.getEntitiesByClass(LivingEntity.class,
                center.getBoundingBox().expand(level),
                livingEntity -> calculateDistance(center, livingEntity) <= level);
        effectEntities.forEach(v -> {
            Vector3f calculate = VelocityUtils.calculate(center, v);
            v.setVelocity(new Vec3d(calculate).multiply(-1.5));
        });
    }

    private double calculateDistance(Entity self, Entity target) {
        double x = self.getX() - target.getX();
        double y = self.getY() - target.getY();
        double z = self.getZ() - target.getZ();
        return Math.sqrt(x * x + z * z + y * y);
    }

    private double calculateDistance(BlockPos pos1, BlockPos pos2) {
        int x = pos1.getX() - pos2.getX();
        int y = pos1.getY() - pos2.getY();
        int z = pos1.getZ() - pos2.getZ();
        return Math.sqrt(x * x + y * y + z * z);
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    @Override
    protected Item getDefaultItem() {
        return ModItemRegister.ATTRACTION_ITEM;
    }
}

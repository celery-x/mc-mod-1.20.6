package top.superxuqc.mcmod.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.*;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Unique;
import top.superxuqc.mcmod.common.VelocityUtils;
import top.superxuqc.mcmod.common.particle.BigBoomArg;
import top.superxuqc.mcmod.network.handler.ServerHitCheckPayloadHandler;
import top.superxuqc.mcmod.particle.BoomParticleEffect;
import top.superxuqc.mcmod.particle.PaintParticleEffect;
import top.superxuqc.mcmod.register.ModEntryTypes;

import java.util.HashSet;

public class LightArrowEntity extends PersistentProjectileEntity {

    private static final TrackedData<Boolean> BOOM = DataTracker.registerData(LightArrowEntity.class, TrackedDataHandlerRegistry.BOOLEAN);


    public LightArrowEntity(EntityType<? extends PersistentProjectileEntity> entityType, World world) {
        super(ModEntryTypes.LIGHT_ARROW, world);
        setNoGravity(true);
    }


    protected LightArrowEntity(double x, double y, double z, World world, ItemStack stack) {
        super(ModEntryTypes.LIGHT_ARROW, x, y, z, world, stack);
        setNoGravity(true);
    }

    public LightArrowEntity(LivingEntity owner, World world, ItemStack stack) {
        super(ModEntryTypes.LIGHT_ARROW, owner, world, stack);
        setNoGravity(true);
    }

    public LightArrowEntity(LivingEntity owner, World world, ItemStack stack, boolean boom) {
        super(ModEntryTypes.LIGHT_ARROW, owner, world, stack);
        setNoGravity(true);
        setBoom(boom);
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
        builder.add(BOOM, false);
    }

    public void setBoom(Boolean boom) {
        this.dataTracker.set(BOOM, boom);
    }

    public Boolean getBoom() {
        return this.dataTracker.get(BOOM);
    }

    @Override
    public boolean isFireImmune() {
        return true;
    }


    private int willDead = -99;

    @Override
    protected void onCollision(HitResult hitResult) {
        if (!(hitResult.getType() == HitResult.Type.MISS) && !getBoom()) {
            willDead = 10;
        }
    }

    @Override
    public void tick() {
        if (getBoom()) {

            renderBoom(BigBoomArg.ARG, getWorld());

            return;
        }
        super.tick();
        if (willDead > 0) {
            willDead --;
            if (willDead <= 0) {
                discard();
            }
        }

    }
    private void renderBoom(float[][] args, World world) {
        Vec3d vec3d = getVelocity().normalize();
        NoneEntity noneEntity = new NoneEntity(world);
        noneEntity.setPitch(getPitch());
        noneEntity.setYaw(getYaw());
        int i = age * 5;
        for (int j = (age - 1) * 5; j <= age * 5; j++) {
            Vec3d add = getPos().add(vec3d.multiply(j));
            noneEntity.setPos(add.x, add.y, add.z);
            if (add.y < -64 || add.y > 400) {
                discard();
                return;
            }
            for (float[] arg : args) {
                addParticle(this, noneEntity, arg);
            }
            if (i % 20 == 0 && j == age * 5) {
                addCircle(noneEntity, 5);
            }
        }
    }

    @Unique
    private void addParticle(Entity user, Entity entity, float[] args) {
        if (random.nextBoolean()) {
            return;
        }
        Vec3d realPos = VelocityUtils.toRealPos(entity, args[3], args[4], args[5]);
        int bb = entity.getWorld().getRandom().nextInt(10);
        float r = bb > 9 ? 1 : args[0];
        float g = bb > 9 ? 1 : args[1];
        float b = bb > 9 ? 1 : args[2];

        double x = realPos.x + random.nextGaussian();
        double y = realPos.y + random.nextGaussian();
        double z = realPos.z + random.nextGaussian();
        entity.getWorld().addParticle(
                new BoomParticleEffect(new Vector3f(r, g, b), 20 * 5 - age, 1f),
                x, y, z, 0, 0, 0);
        if (!user.getWorld().isClient()) {
            ServerHitCheckPayloadHandler.hitAndBreak(new BlockPos((int) realPos.x, (int) realPos.y, (int) realPos.z), 100, (LivingEntity) getOwner(), (ServerWorld) user.getWorld());
            for (int i = -5; i <= 5; i++) {
                if (i == 0) {
                    continue;
                }
                Vec3i passed = passed(realPos.x + i, realPos.y, realPos.z);
                if (passed != null) {
                    ServerHitCheckPayloadHandler.hitAndBreak(new BlockPos(passed.getX(), passed.getY(), passed.getZ()), 100, (LivingEntity) getOwner(), (ServerWorld) user.getWorld());
                }
                passed = passed((int) realPos.x, (int) realPos.y + i, (int) realPos.z);
                if (passed != null) {
                    ServerHitCheckPayloadHandler.hitAndBreak(new BlockPos(passed.getX(), passed.getY(), passed.getZ()), 100, (LivingEntity) getOwner(), (ServerWorld) user.getWorld());
                }
                passed = passed((int) realPos.x, (int) realPos.y, (int) realPos.z + i);
                if (passed != null) {
                    ServerHitCheckPayloadHandler.hitAndBreak(new BlockPos(passed.getX(), passed.getY(), passed.getZ()), 100, (LivingEntity) getOwner(), (ServerWorld) user.getWorld());
                }
                passed = passed((int) realPos.x + i, (int) realPos.y + i, (int) realPos.z);
                if (passed != null) {
                    ServerHitCheckPayloadHandler.hitAndBreak(new BlockPos(passed.getX(), passed.getY(), passed.getZ()), 100, (LivingEntity) getOwner(), (ServerWorld) user.getWorld());
                }
                passed = passed((int) realPos.x + i, (int) realPos.y, (int) realPos.z + i);
                if (passed != null) {
                    ServerHitCheckPayloadHandler.hitAndBreak(new BlockPos(passed.getX(), passed.getY(), passed.getZ()), 100, (LivingEntity) getOwner(), (ServerWorld) user.getWorld());
                }
                passed = passed((int) realPos.x, (int) realPos.y + i, (int) realPos.z + i);
                if (passed != null) {
                    ServerHitCheckPayloadHandler.hitAndBreak(new BlockPos(passed.getX(), passed.getY(), passed.getZ()), 100, (LivingEntity) getOwner(), (ServerWorld) user.getWorld());
                }
                passed = passed((int) realPos.x + i, (int) realPos.y + i, (int) realPos.z + i);
                if (passed != null) {
                    ServerHitCheckPayloadHandler.hitAndBreak(new BlockPos(passed.getX(), passed.getY(), passed.getZ()), 100, (LivingEntity) getOwner(), (ServerWorld) user.getWorld());
                }
            }
        }
    }

    private void addCircle(Entity entity, int r) {
        for (float x = -r; x <= r; x += 0.25f) {
            float y = (float) Math.sqrt(r * r - x * x);
            float z = 0;
            Vec3d realPos = VelocityUtils.toRealPos(entity, x, y, z);
            Vec3d realPos1 = VelocityUtils.toRealPos(entity, x, -y, z);
            Vec3d v = entity.getPos().subtract(realPos).normalize().multiply(-0.15);
            Vec3d v1 = entity.getPos().subtract(realPos1).normalize().multiply(-0.15);
            entity.getWorld().addParticle(
                    new PaintParticleEffect(new Vector3f(1, 1, 1), 20 * 2, 1f),
                    realPos.x, realPos.y, realPos.z, v.x, v.y, v.z);
            entity.getWorld().addParticle(
                    new PaintParticleEffect(new Vector3f(1, 1, 1), 20 * 2, 1f),
                    realPos1.x, realPos1.y, realPos1.z, v1.x, v1.y, v1.z);
        }
    }

    private HashSet<Vec3i> passedList = new HashSet<Vec3i>();

    public Vec3i passed(double x, double y, double z) {
        Vec3i index = new Vec3i((int) x, (int) y, (int) z);
        if (passedList.contains(index)) {
            return null;
        } else {
            passedList.add(index);
            return index;
        }
    }

    @Override
    public void setYaw(float yaw) {
        if (getVelocity().equals(Vec3d.ZERO) && yaw == 0) {
            return;
        }
        super.setYaw(yaw);
    }

    @Override
    public void setPitch(float pitch) {
        if (getVelocity().equals(Vec3d.ZERO) && pitch == 0) {
            return;
        }
        super.setPitch(pitch);
    }

    @Override
    public void onPlayerCollision(PlayerEntity player) {

    }

    @Override
    public double getDamage() {
        return 4;
    }

    @Override
    protected ItemStack getDefaultItemStack() {
        return Items.ARROW.getDefaultStack();
    }
}

package top.superxuqc.mcmod.entity;

import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.EndRodBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.EndGatewayBlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.FlyingItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageSources;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.FlyingEntity;
import net.minecraft.entity.passive.BatEntity;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ExplosiveProjectileEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.entity.projectile.thrown.ThrownEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.*;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import top.superxuqc.mcmod.common.*;
import top.superxuqc.mcmod.particle.JianQiParticleEffect;
import top.superxuqc.mcmod.register.ModEntryTypes;
import top.superxuqc.mcmod.register.ModItemRegister;
import top.superxuqc.mcmod.register.ParticleRegister;

import java.util.HashSet;
import java.util.List;

public class SwordQiEntity extends ProjectileEntity implements FlyingItemEntity{

    private int age = 100;

    private static final TrackedData<Boolean> SIZE = DataTracker.registerData(SwordQiEntity.class, TrackedDataHandlerRegistry.BOOLEAN);


    private HashSet<Vec3i> passedList = new HashSet<Vec3i>();

    public SwordQiEntity(EntityType<? extends SwordQiEntity> entityType, World world) {
        super(entityType, world);
    }

    public SwordQiEntity(EntityType<? extends SwordQiEntity> entityType, double x, double y, double z, World world) {
        this(entityType, world);
        this.setPosition(x, y, z);
    }

    public SwordQiEntity(EntityType<? extends SwordQiEntity> type, LivingEntity owner, World world) {
        this(type, owner.getX(), owner.getEyeY() - 0.1F, owner.getZ(), world);
    }

    public boolean passed(double x, double y, double z) {
        Vec3i index = new Vec3i((int) x, (int) y, (int) z);
        if (!passedList.contains(index)) {
            return true;
        }else {
            passedList.add(index);
            return false;
        }
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        builder.add(SIZE, false);
    }

    public void setSize(Boolean size) {
        this.dataTracker.set(SIZE, size);
    }

    public Boolean getSize() {
        return this.dataTracker.get(SIZE);
    }

    @Override
    protected void onBlockHit(BlockHitResult blockHitResult) {
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
    }



    @Override
    public void tick() {
        super.tick();
        if (age <= 0) {
            this.discard();
        }

        this.checkBlockCollision();
        finalRender();
        age--;
    }


    private void finalRender() {
        Vec3d vec3d = this.getVelocity();
        double d = this.getX() + vec3d.x;
        double e = this.getY() + vec3d.y;
        double f = this.getZ() + vec3d.z;
        this.updateRotation();
//        float h;
//        if (this.isTouchingWater()) {
//            for (int i = 0; i < 4; i++) {
//                float g = 0.25F;
//                this.getWorld().addParticle(ParticleTypes.BUBBLE, d - vec3d.x * 0.25, e - vec3d.y * 0.25, f - vec3d.z * 0.25, vec3d.x, vec3d.y, vec3d.z);
//            }
//
//            h = 0.8F;
//        } else {
//            h = 0.99F;
//        }
//
//        this.setVelocity(vec3d.multiply((double)h));
        //this.applyGravity();
//        hitBox();
        this.setPosition(d, e, f);
    }


//    private void hitBox() {
//        //System.out.println();
//        float t = this.prevYaw - 90;
//
//        t = t * MathHelper.PI / 180;
//
//        for (float d = 1; d <= 3; d += 0.5) {
//            // x = 3/25z^2
//            float cos = MathHelper.abs(MathHelper.cos(getAngle()));
//            for (float i = -5 * cos; i <= 5 * cos; i += 0.5) {
//                double tmpz = i;
//                double tmpx = -MathHelper.square(i) * 3.0 / 25.0 ;
//                double tmpy = Math.tan(-1 * getAngle() * (float) (Math.PI / 180.0)) * i;
//
//
//                double realz = tmpz * MathHelper.cos(t) - tmpx * MathHelper.sin(t) + this.getZ() + d;
//                double realx = tmpz * MathHelper.sin(t) + tmpx * MathHelper.cos(t) + this.getX();
//                double realy = tmpy + this.getY();
//
//                if (d < 2) {
//                    hitParticle(realx, realy, realz);
//                }
//                hitEveryThing(realx, realy, realz);
//            }
//        }
//    }


//    public void hitParticle(double x, double y, double z) {
//        this.getWorld().addParticle(new DustParticleEffect(Vec3d.unpackRgb(11154228).toVector3f(), 0.5F), x, y, z , 0.0, 0.0, 0.0);
//    }
//
    public void hitEveryThing(double x, double y, double z) {
        World world = this.getWorld();
        BlockPos blockPos = new BlockPos((int) x, (int) y, (int) z);
        BlockState blockState = world.getBlockState(blockPos);
        if (!blockState.isAir()) {
            if (!this.getSize()) {
                this.age = 5;
            }
            world.breakBlock(blockPos, false, this.getOwner());
        }
        List<Entity> entities = world.getOtherEntities(null, this.getBoundingBox(), entity -> entity instanceof LivingEntity && !(entity instanceof PlayerEntity));
        for (Entity entity : entities) {
            if (entity.getBlockPos().equals(blockPos)) {
                entity.damage(this.getDamageSources().mobProjectile(this, (LivingEntity) this.getOwner()), 15);
            }
        }
    }

//    @Override
//    public boolean isCollidable() {
//        return true;
//    }

    @Override
    public ItemStack getStack() {
        //此处改渲染
        return ModItemRegister.Sword_Qi.getDefaultStack();
    }


}

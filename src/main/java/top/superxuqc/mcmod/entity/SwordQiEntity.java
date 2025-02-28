package top.superxuqc.mcmod.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.FlyingItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import top.superxuqc.mcmod.common.particle.*;
import top.superxuqc.mcmod.register.ModItemRegister;

import java.util.HashSet;

public class SwordQiEntity extends ProjectileEntity implements FlyingItemEntity{

    private int age = 100;

    private final int amount = 15;

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

    @Override
    public boolean isFireImmune() {
        return true;
    }

    public int getAmount() {
        return amount;
    }

    public Vec3i passed(double x, double y, double z) {
        Vec3i index = new Vec3i((int) x, (int) y, (int) z);
        if (passedList.contains(index)) {
            return null;
        }else {
            passedList.add(index);
            return index;
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
    public void tick() {
        super.tick();
//        if(getSize()) {
//            for (float[] args : JianqiArgs0.ARG) {
//                tryHit( args);
//            }
//
//
//            for (float[] args : JianqiArgs1.ARG) {
//                tryHit( args);
//            }
//
//
//            for (float[] args : JianqiArgs2.ARG) {
//                tryHit( args);
//            }
//            for (float[] args : JianqiArgs3.ARG) {
//                tryHit( args);
//            }
//        }else {
//            for (float[] args : JianqiArgMini.ARG) {
//                tryHit( args);
//            }
//        }
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



    private void tryHit(float[] args) {
        Vec3d realPos = toRealPos(args[3], args[4], args[5]);
        Vec3i passed = this.passed(realPos.x, realPos.y, realPos.z);
        if (passed != null) {
            this.hitEveryThing(passed);
        }

    }

    private Vec3d toRealPos(float dx, float dy, float dz) {
        Vec2f vec2f = this.getRotationClient();
        vec2f = new Vec2f(-vec2f.x, -vec2f.y);
        Vec3d vec3d = this.getPos();
        float f = MathHelper.cos((vec2f.y + 90.0F) * (float) (Math.PI / 180.0));
        float g = MathHelper.sin((vec2f.y + 90.0F) * (float) (Math.PI / 180.0));
        float h = MathHelper.cos(-vec2f.x * (float) (Math.PI / 180.0));
        float i = MathHelper.sin(-vec2f.x * (float) (Math.PI / 180.0));
        float j = MathHelper.cos((-vec2f.x + 90.0F) * (float) (Math.PI / 180.0));
        float k = MathHelper.sin((-vec2f.x + 90.0F) * (float) (Math.PI / 180.0));
        Vec3d vec3d2 = new Vec3d((double)(f * h), (double)i, (double)(g * h));
        Vec3d vec3d3 = new Vec3d((double)(f * j), (double)k, (double)(g * j));
        Vec3d vec3d4 = vec3d2.crossProduct(vec3d3).multiply(-1.0);
        double d = vec3d2.x * dz + vec3d3.x * dy + vec3d4.x * dx;
        double e = vec3d2.y * dz + vec3d3.y * dy + vec3d4.y * dx;
        double l = vec3d2.z * dz + vec3d3.z * dy + vec3d4.z * dx;
        return new Vec3d(vec3d.x + d, vec3d.y + e, vec3d.z + l);
//        ParticleUtils.genParticle(this.getWorld(), (float) this.getX(), (float) this.getY(), (float) this.getZ());

    }

    public void hitEveryThing(Vec3i vec3i) {
        World world = this.getWorld();
        BlockPos blockPos = new BlockPos(vec3i.getX(), vec3i.getY(), vec3i.getZ());
        world.breakBlock(blockPos, false);
        if (!world.isClient()){
            try {
                ((ServerWorld) world).iterateEntities().forEach(v -> {
                    if (v != null && v.getBlockPos().equals(blockPos)) {
                        v.damage(this.getDamageSources().mobProjectile(this, (LivingEntity) this.getOwner()), 15);
                    }
                    BlockPos pos2 = new BlockPos(blockPos.getX(), blockPos.getY() - 1, blockPos.getZ());
                    if (v != null && v.getBlockPos().equals(pos2)) {
                        v.damage(this.getDamageSources().mobProjectile(this, (LivingEntity) this.getOwner()), 15);
                    }
                    BlockPos pos3 = new BlockPos(blockPos.getX(), blockPos.getY() - 2, blockPos.getZ());
                    if (v != null && v.getBlockPos().equals(pos3)) {
                        v.damage(this.getDamageSources().mobProjectile(this, (LivingEntity) this.getOwner()), 15);
                    }
                });
            }catch (ArrayIndexOutOfBoundsException e) {
                //ignore
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

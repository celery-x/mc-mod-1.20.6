package top.superxuqc.mcmod.entity;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.FlyingItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.GiantEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.thrown.EnderPearlEntity;
import net.minecraft.entity.projectile.thrown.ThrownEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import org.joml.Vector3f;
import top.superxuqc.mcmod.common.SpawnLivingEntityUtils;
import top.superxuqc.mcmod.common.VelocityUtils;
import top.superxuqc.mcmod.enchantment.FollowProjectileEnchantment;
import top.superxuqc.mcmod.register.ModEnchantmentRegister;
import top.superxuqc.mcmod.register.ModEntryTypes;

import java.util.HashSet;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class XianJianEntity extends PersistentProjectileEntity implements FlyingItemEntity {

    private static final TrackedData<Integer> AMOUNT = DataTracker.registerData(XianJianEntity.class, TrackedDataHandlerRegistry.INTEGER);

    private static final TrackedData<Boolean> TIAN_ZAI = DataTracker.registerData(XianJianEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Integer> MAX_AGE = DataTracker.registerData(XianJianEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Vector3f> CENTER = DataTracker.registerData(XianJianEntity.class, TrackedDataHandlerRegistry.VECTOR3F);

    private ItemStack stack = this.getDefaultItemStack();

    public NoneEntity targetEntity;

    public XianJianEntity(EntityType<XianJianEntity> xianJianEntityEntityType, World world) {
        super(ModEntryTypes.XIAN_JIAN_TYPE, world);
    }

    public int getAmount() {
        return this.dataTracker.get(AMOUNT);
    }

    public void setAmount(int amount) {

        this.dataTracker.set(AMOUNT, amount);
    }

    public boolean isTianZai() {
        return this.dataTracker.get(TIAN_ZAI);
    }

    public void setTianZai(boolean tianZai) {
        this.tianZai = tianZai;
        this.dataTracker.set(TIAN_ZAI, tianZai);
    }

    public int getMaxAge() {
        return this.dataTracker.get(MAX_AGE);
    }

    public void setMaxAge(int max) {
        this.dataTracker.set(MAX_AGE, max);
        this.maxAge = max;
    }

    public Vector3f getCenter() {
        return this.dataTracker.get(CENTER);
    }

    public void setCenter(Vec3d center) {
        this.dataTracker.set(CENTER, new Vector3f((float) center.x, (float) center.y, (float) center.z));
    }

    private boolean follow = false;

    private boolean folloWed = false;

    public boolean isFolloWed() {
        return folloWed;
    }

    public void setFolloWed(boolean folloWed) {
        this.folloWed = folloWed;
    }

    public boolean isFollow() {
        return follow;
    }

    public void setFollow(boolean follow) {
        this.follow = follow;
    }

    private boolean tianZai = false;

    public XianJianEntity(LivingEntity owner, World world, ItemStack itemStack, int amount, boolean follow, boolean tianZai) {
        super(ModEntryTypes.XIAN_JIAN_TYPE,owner, world, itemStack);
        this.follow = follow;
        setTianZai(tianZai);
        this.stack = itemStack.copy();
        resetPos();

        setAmount(amount);

    }

    @Override
    protected float getDragInWater() {
        return 1;
    }

    private void resetPos() {
        double x = this.getX();
        double y = this.getY();
        double z = this.getZ();
        setNoGravity(true);
        if (!tianZai) {
            x = this.getX() + getWorld().random.nextInt(+16) - 8;
            y = this.getY() + getWorld().random.nextInt(16) - 8;
            z = this.getZ() + getWorld().random.nextInt(16) - 8;
            int Max = 10;
            int times = 0;
            while (!getWorld().getBlockState(new BlockPos((int) x, (int) y, (int) z)).isAir()) {
                x = this.getX() + getWorld().random.nextInt(+16) - 8;
                y = this.getY() + getWorld().random.nextInt(16) - 8;
                z = this.getZ() + getWorld().random.nextInt(16) - 8;
                times++;
                if (times > Max) {
                    break;
                }
            }
        }
        setPosition(x, y, z);
        resetPosition();
        this.prevYaw = 0;
        center = calculateCircleZone();
        setCenter(center);
        calculateCircleStartR();
    }

    private int maxAge = Integer.MAX_VALUE;

    private void calculateCircleStartR() {
        double x = this.getX();
        double z = this.getZ();
        double dx = x - center.x;
        double dz = z - center.z;
        double dr = Math.atan2(dx, dz) + Math.PI;
        double v = speed / (Math.PI * 2 * r) * 2 * Math.PI;
        age = (int) (dr / v);
        double lifeTime = Math.PI * 2 / v;
        setMaxAge((int) (age + lifeTime) + 1);
    }

    @Override
    protected void onCollision(HitResult hitResult) {
        //super.onCollision(hitResult);
        if (!this.getWorld().isClient() && !tianZai && hitResult.getType() != HitResult.Type.MISS) {
            discard();
        }
    }

    public ItemStack getDefaultItemStack() {
        return Items.DIAMOND_SWORD.getDefaultStack();
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
        builder.add(AMOUNT, 5);
        builder.add(TIAN_ZAI, false);
        builder.add(MAX_AGE, 20 * 360);
        builder.add(CENTER, new Vector3f());
    }

    int speed = 2;

    int r = 20;

    private List<Vec3d> entitySpawnPos = new CopyOnWriteArrayList<>();

    Vec3d center = null;

    @Override
    protected void age() {

    }

    @Override
    public void tick() {
        super.tick();
        if (!this.getWorld().isClient() && tianZai) {
            Vec3d vec3d = calculateCircleIndex(age);
            if (age < maxAge && age % 4 == 0) {
                entitySpawnPos.add(vec3d);
            }
            if (age >= maxAge && age % 10 == 0) {
                generateRandomEntity();
            }
            setVelocity(vec3d.subtract(this.getPos()));
        }
        if (age > maxAge * 11) {
            discard();
        }
    }

    public void generateRandomEntity() {
        for (Vec3d pos : entitySpawnPos) {
            int times = 1;
            List<Entity> entityModIS = SpawnLivingEntityUtils.spawnHostileByPlayerTimes(this.getWorld(), this.getBlockPos(), this.getOwner().getUuid(), times);
            for (Entity entityModI : entityModIS) {
                if (entityModI instanceof GiantEntity) {
                    continue;
                }
                double newx = pos.getX() + this.getWorld().random.nextInt(4) - 4;
                double newy = pos.getY() + this.getWorld().random.nextInt(2);
                double newz = pos.getZ() + this.getWorld().random.nextInt(4) - 4;
                System.out.println("Tian zai shengcheng");
                entityModI.setPosition(newx, newy, newz);
                this.getWorld().spawnEntity(entityModI);
                SpawnLivingEntityUtils.addClearableEntity(entityModI);
            }
        }
    }

    private Vec3d calculateCircleZone() {
        double newZ = Math.cos(prevYaw) * r + prevZ;
        double newX = Math.sin(prevYaw) * r + prevX;
        double newY = prevY;
        return new Vec3d(newX, newY, newZ);
    }

    private Vec3d calculateCircleIndex(double d) {
        if (center == null) {
            return new Vec3d(0, 0, 0);
        }
        double newZ = Math.cos(d) * r + center.z;
        double newX = Math.sin(d) * r + center.x;
        double newY = center.y;
        return new Vec3d(newX, newY, newZ);
    }

    private Vec3d calculateCircleIndex(int age) {
        double d = (speed / (Math.PI * r * 2)) * 2 * Math.PI;
        return calculateCircleIndex(d * age);
    }

    private HashSet<Vec3i> passedList = new HashSet<Vec3i>();

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
    public ItemStack getStack() {
        return stack;
    }
}

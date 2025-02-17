package top.superxuqc.mcmod.entity;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.FlyingItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.thrown.ThrownEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import org.joml.Vector3f;
import top.superxuqc.mcmod.common.VelocityUtils;
import top.superxuqc.mcmod.enchantment.FollowProjectileEnchantment;
import top.superxuqc.mcmod.register.ModEnchantmentRegister;
import top.superxuqc.mcmod.register.ModEntryTypes;

import java.util.HashSet;

public class XianJianEntity extends PersistentProjectileEntity implements FlyingItemEntity {

    private static final TrackedData<Integer> AMOUNT = DataTracker.registerData(XianJianEntity.class, TrackedDataHandlerRegistry.INTEGER);

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


    public XianJianEntity(LivingEntity owner, World world, ItemStack itemStack, int amount) {
        super(ModEntryTypes.XIAN_JIAN_TYPE,owner, world, itemStack);
        this.stack = itemStack.copy();
        resetPos();

        setAmount(amount);
    }

    @Override
    protected float getDragInWater() {
        return 1;
    }

    private void resetPos() {
        double x = this.getX() + getWorld().random.nextInt( + 16) - 8;
        double y = this.getY() + getWorld().random.nextInt(16) - 8;
        double z = this.getZ() + getWorld().random.nextInt(16) - 8;
        int Max = 10;
        int times = 0;
        while (!getWorld().getBlockState(new BlockPos((int) x, (int) y, (int) z)).isAir()) {
            x = this.getX() + getWorld().random.nextInt( + 16) - 8;
            y = this.getY() + getWorld().random.nextInt(16) - 8;
            z = this.getZ() + getWorld().random.nextInt(16) - 8;
            times++;
            if (times > Max) {
                break;
            }
        }
        setPosition(x, y, z);
        setNoGravity(true);
        resetPosition();
        center = calculateCircleZone();
    }

//    private void lookupTarget() {
//        raycast()
//    }

    @Override
    protected void onCollision(HitResult hitResult) {
        super.onCollision(hitResult);
        if (hitResult.getType() != HitResult.Type.MISS) {
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
    }

    int speed = 2;

    int r = 20;

    Vec3d center = null;
    @Override
    public void tick() {
        super.tick();
        if (!this.getWorld().isClient()) {
            Vec3d vec3d = calculateCircleIndex(age);
            setVelocity(vec3d.subtract(this.getPos()));
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

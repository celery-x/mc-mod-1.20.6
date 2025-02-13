package top.superxuqc.mcmod.entity;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.FlyingItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.thrown.ThrownEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import org.joml.Vector3f;
import top.superxuqc.mcmod.common.VelocityUtils;
import top.superxuqc.mcmod.enchantment.FollowProjectileEnchantment;
import top.superxuqc.mcmod.register.ModEnchantmentRegister;
import top.superxuqc.mcmod.register.ModEntryTypes;

import java.util.HashSet;

public class XianJianEntity extends ThrownEntity implements FlyingItemEntity {

    private int amount;

    private ItemStack stack = this.getDefaultItemStack();

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public XianJianEntity(EntityType<? extends ThrownEntity> entityType, World world) {
        super(ModEntryTypes.XIAN_JIAN_TYPE, world);
    }

    public XianJianEntity(double x, double y, double z, World world) {
        super(ModEntryTypes.XIAN_JIAN_TYPE, x, y, z, world);
        resetPos();
    }

    public XianJianEntity(LivingEntity owner, World world, ItemStack itemStack) {
        this(owner.getX(), owner.getY(), owner.getZ(), world);
        this.stack = itemStack.copy();
    }

    private void resetPos() {
        double x = this.getX() + getWorld().random.nextInt( + 16) - 8;
        double y = this.getY() + getWorld().random.nextInt(16) - 8;
        double z = this.getZ() + getWorld().random.nextInt(16) - 8;
        setPosition(x, y, z);
        setNoGravity(true);
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {

    }

    public ItemStack getDefaultItemStack() {
        return Items.DIAMOND_SWORD.getDefaultStack();
    }

    @Override
    public void tick() {
        super.tick();
        if (!getWorld().isClient()) {
            int level = EnchantmentHelper.getLevel(ModEnchantmentRegister.FOLLOW_PROJECTILE, this.getStack());
            if (level > 0) {
                if (!FollowProjectileEnchantment.TARGET.isAlive()) {
                    //entity.setVelocity(0, 0, 0);
                } else {
                    Vector3f calculate = VelocityUtils.calculate(this, FollowProjectileEnchantment.TARGET);
                    this.setVelocity(new Vec3d(calculate));
                }
            }
        }
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

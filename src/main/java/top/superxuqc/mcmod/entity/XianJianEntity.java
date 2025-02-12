package top.superxuqc.mcmod.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.FlyingItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.thrown.ThrownEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import top.superxuqc.mcmod.register.ModEntryTypes;

import java.util.HashSet;

public class XianJianEntity extends ThrownEntity implements FlyingItemEntity {

    private int amount;

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

    public XianJianEntity(LivingEntity owner, World world) {
        this(owner.getX(), owner.getY(), owner.getZ(), world);
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
        return Items.DIAMOND_SWORD.getDefaultStack();
    }
}

package top.superxuqc.mcmod.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import top.superxuqc.mcmod.register.ModEntryTypes;

import java.util.HashSet;

public class LightArrowEntity extends PersistentProjectileEntity {
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

    @Override
    public boolean isFireImmune() {
        return true;
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
    public double getDamage() {
        return 4;
    }

    @Override
    protected ItemStack getDefaultItemStack() {
        return Items.ARROW.getDefaultStack();
    }
}

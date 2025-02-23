package top.superxuqc.mcmod.item.interfaces;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public interface TickAble {
    default void tick(){};

    default void tick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks){};
}

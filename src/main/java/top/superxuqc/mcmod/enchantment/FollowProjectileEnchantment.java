package top.superxuqc.mcmod.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;

public class FollowProjectileEnchantment extends Enchantment {

    // TODO 通信
    public static Entity TARGET;

    public FollowProjectileEnchantment(Properties properties) {
        super(properties);
    }
}

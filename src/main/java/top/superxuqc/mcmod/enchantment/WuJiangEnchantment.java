package top.superxuqc.mcmod.enchantment;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.ProtectionEnchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import top.superxuqc.mcmod.register.ModEnchantmentRegister;

public class WuJiangEnchantment extends ProtectionEnchantment {
    public WuJiangEnchantment(Properties properties, Type protectionType) {
        super(properties, protectionType);
    }

    @Override
    public int getProtectionAmount(int level, DamageSource source) {
        Entity entity = source.getSource();
        ThrownItemEntity pe;
        if (entity instanceof ThrownItemEntity) {
            pe = (ThrownItemEntity) entity;
            return EnchantmentHelper.getLevel(ModEnchantmentRegister.SHUTTLECOC_KKICKING, pe.getStack()) > 0 ? 100 : 0;
        }
        return 0;
    }


}

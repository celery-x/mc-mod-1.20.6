package top.superxuqc.mcmod.common;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import top.superxuqc.mcmod.enchantment.FeiLeiEnchantment;

import java.util.Set;

public class EnchantmentHandle {
    public static boolean isEnchantmentHandle(ItemStack stack, Enchantment target) {
        Set<RegistryEntry<Enchantment>> enchantments = EnchantmentHelper.getEnchantments(stack).getEnchantments();
        boolean is = false;
        for (RegistryEntry<Enchantment> enchantment : enchantments) {
            System.out.println("enchantment :: " + enchantment.getIdAsString());
            is = enchantment.value().getTranslationKey().equals(target.getTranslationKey()) ;
            if (is) {
                break;
            }
        }
        return is;
    }
}

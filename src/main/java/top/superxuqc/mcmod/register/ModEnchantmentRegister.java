package top.superxuqc.mcmod.register;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.InfinityEnchantment;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import top.superxuqc.mcmod.MyModInitializer;
import top.superxuqc.mcmod.enchantment.FeiLeiEnchantment;
import top.superxuqc.mcmod.enchantment.HuChengEnchantment;
import top.superxuqc.mcmod.enchantment.ScareSelfEnchantment;

public class ModEnchantmentRegister {
    public static Enchantment HUCHENG = new HuChengEnchantment(
            Enchantment.properties(
                    ModTarKeys.MOD_ITEM_ENCHANT_TAG,
                    1, 9,
                    Enchantment.leveledCost(15, 9),
                    Enchantment.leveledCost(65, 9),
                    4, EquipmentSlot.MAINHAND)
    );

    public static Enchantment FeiLei = new FeiLeiEnchantment(
            Enchantment.properties(
                    ModTarKeys.MOD_ITEM_ENCHANT_TAG,
                    1, 1,
                    Enchantment.leveledCost(15, 9),
                    Enchantment.leveledCost(65, 9),
                    4, EquipmentSlot.MAINHAND)
    );

    public static Enchantment BAN_KAI = new FeiLeiEnchantment(
            Enchantment.properties(
                    ModTarKeys.SWORD_QI_TAG,
                    1, 1,
                    Enchantment.leveledCost(15, 9),
                    Enchantment.leveledCost(65, 9),
                    4, EquipmentSlot.MAINHAND)
    );

    public static Enchantment SCARE_SELF = new ScareSelfEnchantment(
            Enchantment.properties(
//                    ItemTags.BOW_ENCHANTABLE,
                    ModTarKeys.SCARE_SELF_ENCHANT_TAG,
                    1, 9,
                    Enchantment.leveledCost(15, 9),
                    Enchantment.leveledCost(65, 9),
                    4, EquipmentSlot.MAINHAND)
    );

    public static void init() {
        Registry.register(Registries.ENCHANTMENT, new Identifier(MyModInitializer.MOD_ID, "hucheng"),HUCHENG);
        Registry.register(Registries.ENCHANTMENT, new Identifier(MyModInitializer.MOD_ID, "feilei"),FeiLei);
        Registry.register(Registries.ENCHANTMENT, new Identifier(MyModInitializer.MOD_ID, "scare_self"),SCARE_SELF);
        Registry.register(Registries.ENCHANTMENT, new Identifier(MyModInitializer.MOD_ID, "bankai"),BAN_KAI);
    }

}

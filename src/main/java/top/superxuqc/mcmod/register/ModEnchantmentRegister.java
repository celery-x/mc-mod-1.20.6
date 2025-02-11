package top.superxuqc.mcmod.register;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.InfinityEnchantment;
import net.minecraft.enchantment.ProtectionEnchantment;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import top.superxuqc.mcmod.MyModInitializer;
import top.superxuqc.mcmod.enchantment.*;

public class ModEnchantmentRegister {

    private static final EquipmentSlot[] ALL_ARMOR = new EquipmentSlot[]{EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET};
    private static final EquipmentSlot[] ALL_SLOT = new EquipmentSlot[]{EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND, EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET};
    public static Enchantment HUCHENG = new HuChengEnchantment(
            Enchantment.properties(
                    ModTarKeys.MOD_ITEM_ENCHANT_TAG,
                    1, 9,
                    Enchantment.leveledCost(15, 9),
                    Enchantment.leveledCost(65, 9),
                    4, EquipmentSlot.MAINHAND)
    );

    public static final Enchantment CHENG_JIAN =
            new Enchantment(
            Enchantment.properties(ModTarKeys.CAN_HIT_ENTITY_ITEMS,
                    1, 10,
                    Enchantment.constantCost(25),
                    Enchantment.constantCost(50),
                    8, EquipmentSlot.MAINHAND));

    public static final Enchantment AMPLIFY =
            new Enchantment(
                    Enchantment.properties(ModTarKeys.CAN_HIT_ENTITY_ITEMS,
                            1, 10,
                            Enchantment.constantCost(25),
                            Enchantment.constantCost(50),
                            8, ALL_SLOT)
            );

    public static Enchantment FeiLei = new FeiLeiEnchantment(
            Enchantment.properties(
                    ModTarKeys.MOD_ITEM_ENCHANT_TAG,
                    1, 1,
                    Enchantment.leveledCost(15, 9),
                    Enchantment.leveledCost(65, 9),
                    4, EquipmentSlot.MAINHAND)
    );

    public static Enchantment BAN_KAI = new BanKaiEnchantment(
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

    public static Enchantment FOLLOW_PROJECTILE = new FollowProjectileEnchantment(
            Enchantment.properties(
//                    ItemTags.BOW_ENCHANTABLE,
                    ModTarKeys.MOD_ITEM_ENCHANT_TAG,
                    1, 1,
                    Enchantment.leveledCost(15, 9),
                    Enchantment.leveledCost(65, 9),
                    4, EquipmentSlot.MAINHAND)
    );

    public static Enchantment TIAN_ZAI = new ScareSelfEnchantment(
            Enchantment.properties(
//                    ItemTags.BOW_ENCHANTABLE,
                    ModTarKeys.MOD_ITEM_ENCHANT_TAG,
                    1, 10,
                    Enchantment.leveledCost(15, 9),
                    Enchantment.leveledCost(65, 9),
                    4, EquipmentSlot.MAINHAND)
    );

    public static Enchantment SHUTTLECOC_KKICKING = new ShuttlecockKickingEnchantment(
            Enchantment.properties(
//                    ItemTags.BOW_ENCHANTABLE,
                    ModTarKeys.CAN_HIT_ENTITY_ITEMS,
                    1, 1,
                    Enchantment.leveledCost(15, 9),
                    Enchantment.leveledCost(65, 9),
                    4, EquipmentSlot.MAINHAND)
    );

    public static Enchantment WU_JIANG_PROTECTION = new WuJiangEnchantment(
            Enchantment.properties(ItemTags.ARMOR_ENCHANTABLE, 5, 1, Enchantment.leveledCost(3, 6), Enchantment.leveledCost(9, 6), 2, EquipmentSlot.CHEST),
            ProtectionEnchantment.Type.PROJECTILE
    );

    public static Enchantment CHENG_JIAN_PROTECTION = new ProtectionEnchantment(
            Enchantment.properties(ItemTags.ARMOR_ENCHANTABLE, 5, 4, Enchantment.leveledCost(3, 6), Enchantment.leveledCost(9, 6), 2, EquipmentSlot.CHEST),
            ProtectionEnchantment.Type.ALL
    ) {
        @Override
        public boolean isCursed() {
            return true;
        }
    };

    public static void init() {
        Registry.register(Registries.ENCHANTMENT, new Identifier(MyModInitializer.MOD_ID, "hucheng"),HUCHENG);
        Registry.register(Registries.ENCHANTMENT, new Identifier(MyModInitializer.MOD_ID, "feilei"),FeiLei);
        Registry.register(Registries.ENCHANTMENT, new Identifier(MyModInitializer.MOD_ID, "scare_self"),SCARE_SELF);
        Registry.register(Registries.ENCHANTMENT, new Identifier(MyModInitializer.MOD_ID, "bankai"),BAN_KAI);
        Registry.register(Registries.ENCHANTMENT, new Identifier(MyModInitializer.MOD_ID, "follow_projectile"),FOLLOW_PROJECTILE);
        Registry.register(Registries.ENCHANTMENT, new Identifier(MyModInitializer.MOD_ID, "tianzai"), TIAN_ZAI);
        Registry.register(Registries.ENCHANTMENT, new Identifier(MyModInitializer.MOD_ID, "cheng_jian"), CHENG_JIAN);
        Registry.register(Registries.ENCHANTMENT, new Identifier(MyModInitializer.MOD_ID, "shuttlecock_kicking"), SHUTTLECOC_KKICKING);
        Registry.register(Registries.ENCHANTMENT, new Identifier(MyModInitializer.MOD_ID, "wu_jiang_protection"), WU_JIANG_PROTECTION);
        Registry.register(Registries.ENCHANTMENT, new Identifier(MyModInitializer.MOD_ID, "cheng_jian_protection"), CHENG_JIAN_PROTECTION);
        Registry.register(Registries.ENCHANTMENT, new Identifier(MyModInitializer.MOD_ID, "amplify"), AMPLIFY);
    }

}

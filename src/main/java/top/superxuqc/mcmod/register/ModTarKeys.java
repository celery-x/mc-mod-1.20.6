package top.superxuqc.mcmod.register;

import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import top.superxuqc.mcmod.MyModInitializer;

public class ModTarKeys {

    public static TagKey<Item> SCARE_SELF_ENCHANT_TAG = TagKey.of(RegistryKeys.ITEM, new Identifier(MyModInitializer.MOD_ID, "scare_self_items"));
    public static TagKey<Item> MOD_ITEM_ENCHANT_TAG = TagKey.of(RegistryKeys.ITEM, new Identifier(MyModInitializer.MOD_ID, "mod_items"));

    public static TagKey<Item> SWORD_QI_TAG = TagKey.of(RegistryKeys.ITEM, new Identifier(MyModInitializer.MOD_ID, "sword_qi_item"));



}

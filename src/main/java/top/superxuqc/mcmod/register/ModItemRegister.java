package top.superxuqc.mcmod.register;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterials;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import top.superxuqc.mcmod.MyModInitializer;
import top.superxuqc.mcmod.item.HuchengTnTItem;
import top.superxuqc.mcmod.item.QiSwordItem;
import top.superxuqc.mcmod.item.SwordQIItem;

import static top.superxuqc.mcmod.register.ModBlocksRegister.HUCHENG_TNT_BLOCK;

public class ModItemRegister {

    public static Item HUCHENG_TNT_ITEM = registerBlockItem("hucheng_tnt", HUCHENG_TNT_BLOCK);

    public static Item KUWU = Registry.register(Registries.ITEM, new Identifier(MyModInitializer.MOD_ID, "kuwu"),
            new SwordItem(ToolMaterials.DIAMOND, new Item.Settings().attributeModifiers(SwordItem.createAttributeModifiers(ToolMaterials.DIAMOND, 3, -2.4F))));

    public static Item ZHAN_YUE = Registry.register(Registries.ITEM, new Identifier(MyModInitializer.MOD_ID, "zhan_yue"),
            new QiSwordItem(new Item.Settings().attributeModifiers(SwordItem.createAttributeModifiers(ToolMaterials.DIAMOND, 3, -2.4F))));

    public static Item Sword_Qi = Registry.register(Registries.ITEM, new Identifier(MyModInitializer.MOD_ID, "sword_qi"),
            new SwordQIItem(new Item.Settings().attributeModifiers(SwordItem.createAttributeModifiers(ToolMaterials.DIAMOND, 3, -2.4F))));


    private static Item registerBlockItem(String name, Block block) {
        return Registry.register(Registries.ITEM, new Identifier(MyModInitializer.MOD_ID, name),
                new HuchengTnTItem(block, new Item.Settings()));
    }

    public static void init(){

    }
}

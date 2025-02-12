package top.superxuqc.mcmod.register;

import net.minecraft.block.Block;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import top.superxuqc.mcmod.MyModInitializer;
import top.superxuqc.mcmod.item.*;

import static top.superxuqc.mcmod.register.ModBlocksRegister.HUCHENG_TNT_BLOCK;

public class ModItemRegister {

    public static Item HUCHENG_TNT_ITEM = registerBlockItem("hucheng_tnt", HUCHENG_TNT_BLOCK);

    public static Item FALL_STONE = registerBlockItem("fall_stone", ModBlocksRegister.FALL_STONE);

    public static Item KUWU = Registry.register(Registries.ITEM, new Identifier(MyModInitializer.MOD_ID, "kuwu"),
            new SwordItem(ToolMaterials.DIAMOND, new Item.Settings().attributeModifiers(SwordItem.createAttributeModifiers(ToolMaterials.DIAMOND, 3, -2.4F))));

    public static Item ZHAN_YUE = Registry.register(Registries.ITEM, new Identifier(MyModInitializer.MOD_ID, "zhan_yue"),
            new QiSwordItem(new Item.Settings().attributeModifiers(SwordItem.createAttributeModifiers(ToolMaterials.DIAMOND, 3, -2.4F))));

    public static Item Sword_Qi = Registry.register(Registries.ITEM, new Identifier(MyModInitializer.MOD_ID, "sword_qi"),
            new SwordQIItem(new Item.Settings().attributeModifiers(SwordItem.createAttributeModifiers(ToolMaterials.DIAMOND, 3, -2.4F))));

    // 携带TNT的箭, 可分裂
    public static Item ARROW_TNT = Registry.register(Registries.ITEM, new Identifier(MyModInitializer.MOD_ID, "arrow_tnt"),
            new ArrowItem(new Item.Settings()));

    // 携带TNT的箭, 不可分裂,但爆炸可分裂
    public static Item TNT_ARROW = Registry.register(Registries.ITEM, new Identifier(MyModInitializer.MOD_ID, "tnt_arrow"),
            new ArrowItem(new Item.Settings()));

    public static Item TEMP = Registry.register(Registries.ITEM, new Identifier(MyModInitializer.MOD_ID, "temp"),
            new TempItem(new Item.Settings()));

    public static Item REPULSIVE_FORCE_ITEM =
            Registry.register(Registries.ITEM, new Identifier(MyModInitializer.MOD_ID, "repulsive_force_ball"),
                    new RepulsiveForceItem(new Item.Settings().rarity(Rarity.EPIC)));

    public static Item ATTRACTION_ITEM =
            Registry.register(Registries.ITEM, new Identifier(MyModInitializer.MOD_ID, "attraction_ball"),
                    new AttractionItem(new Item.Settings().rarity(Rarity.EPIC)));

    private static Item registerBlockItem(String name, Block block) {
        return Registry.register(Registries.ITEM, new Identifier(MyModInitializer.MOD_ID, name),
                new HuchengTnTItem(block, new Item.Settings()));
    }

    public static void init(){

    }
}

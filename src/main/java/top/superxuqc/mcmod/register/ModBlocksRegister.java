package top.superxuqc.mcmod.register;

import net.minecraft.block.*;
import net.minecraft.block.enums.Instrument;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.ColorCode;
import net.minecraft.util.Identifier;
import top.superxuqc.mcmod.MyModInitializer;
import top.superxuqc.mcmod.block.FallingWithDamageBlock;
import top.superxuqc.mcmod.block.HuChengTnTBlock;
import top.superxuqc.mcmod.enchantment.ChengJianEnchantment;

import java.util.HashMap;
import java.util.Map;

public class ModBlocksRegister {

    public static Block HUCHENG_TNT_BLOCK = registerBlock("hucheng_tnt", new HuChengTnTBlock(AbstractBlock.Settings.copy(Blocks.TNT)));

    public static Map<Integer, Block> FALLING_DAMAGE_BLOCKS = new HashMap<>();

    //创建伤害不同的落石
    static {
        for (int i = 0; i < ChengJianEnchantment.MAX_LEVE; i++) {
            FALLING_DAMAGE_BLOCKS.put(i, registerBlock("falling_damage_blocks_" + i,
                    new FallingWithDamageBlock(
                            2 + i,
                            AbstractBlock.Settings.create().mapColor(MapColor.PALE_YELLOW).instrument(Instrument.SNARE).strength(0.5F).sounds(BlockSoundGroup.SAND)
                    )));
        }
    }

    public static final Block FALL_STONE = registerBlock(
            "fall_stone",
            new FallingWithDamageBlock(
                    2,
                    AbstractBlock.Settings.create().mapColor(MapColor.PALE_YELLOW).instrument(Instrument.SNARE).strength(0.5F).sounds(BlockSoundGroup.SAND)
            )
    );

    public static void register() {

    }

    private static Block registerBlock(String name, Block block) {
        return Registry.register(Registries.BLOCK, new Identifier(MyModInitializer.MOD_ID, name), block);
    }

    public static void init(){

    }
}

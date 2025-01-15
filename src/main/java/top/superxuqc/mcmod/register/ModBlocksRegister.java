package top.superxuqc.mcmod.register;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import top.superxuqc.mcmod.MyModInitializer;
import top.superxuqc.mcmod.block.HuChengTnTBlock;

public class ModBlocksRegister {

    public static Block HUCHENG_TNT_BLOCK = registerBlock("hucheng_tnt", new HuChengTnTBlock(AbstractBlock.Settings.copy(Blocks.TNT)));


    public static void register() {

    }

    private static Block registerBlock(String name, Block block) {
        return Registry.register(Registries.BLOCK, new Identifier(MyModInitializer.MOD_ID, name), block);
    }

    public static void init(){

    }
}

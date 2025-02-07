package top.superxuqc.mcmod.enchantment;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Unique;
import top.superxuqc.mcmod.register.ModBlocksRegister;

import java.util.List;

public class ChengJianEnchantment {

    public static int MAX_LEVE = 10;

    public static void generateMountain(World world, BlockPos center, List<BlockPos> fallStoneList, int level) {
        int a = center.getX();
        int b = center.getZ();
        int oh = 13;
        int h = 14 + level * 2;
        int r = 5 + level;

        for (int i = -r; i < r; i++) {
            int x = a + i;
            for (int j = -r; j < r; j++) {
                int z = b + j;
                if (i * i + j * j <= r * r) {
                    fallStoneList.add(new BlockPos(x, oh + center.getY() , z));
                }
            }
        }
        for (int k = 1; k < h; k++) {
            for (int i = -r; i < r; i++) {
                int x = a + i;
                for (int j = -r; j < r; j++) {
                    int z = b + j;
                    if (i * i + j * j <= r * r) {

                        int y = k + oh + center.getY();
                        BlockPos pos = new BlockPos(x, y - 1, z);
                        if (fallStoneList.contains(pos)) {

                            if (isEdge(pos, fallStoneList)) {
                                //有一个是空的,就触发随机判定
                                int check = world.getRandom().nextBetween(0, 100);
                                // 60%概率短一节
                                if (check > 60) {
                                    fallStoneList.add(new BlockPos(x, y , z));
                                }
                            } else {
                                //否则直接防止stone
                                fallStoneList.add(new BlockPos(x, y , z));
                            }
                        }
                    }
                }
            }
        }

        fallStoneList.forEach(v -> {
            world.setBlockState(v, ModBlocksRegister.FALLING_DAMAGE_BLOCKS.get(level).getDefaultState());
        });
        fallStoneList.clear();


    }

    private static boolean isEdge(BlockPos pos, List<BlockPos> fallStoneList) {
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();
        BlockPos b1 = new BlockPos(x - 1, y, z);
        BlockPos b2 = new BlockPos(x + 1, y, z);
        BlockPos b3 = new BlockPos(x, y, z - 1);
        BlockPos b4 = new BlockPos(x, y, z + 1);
        if (!fallStoneList.contains(b1) ||
                !fallStoneList.contains(b2) ||
                !fallStoneList.contains(b3) ||
                !fallStoneList.contains(b4)
        ) {
            return true;
        }
        return false;
    }

}

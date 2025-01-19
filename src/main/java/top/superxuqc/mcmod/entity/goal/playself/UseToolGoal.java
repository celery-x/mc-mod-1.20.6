package top.superxuqc.mcmod.entity.goal.playself;

import net.minecraft.block.Block;
import net.minecraft.block.pattern.CachedBlockPosition;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.pathing.EntityNavigation;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

import net.minecraft.world.World;
import top.superxuqc.mcmod.register.ModTarKeys;

import java.util.List;
import java.util.function.Function;

public class UseToolGoal extends Goal {

    public LivingEntity entity;

    public ItemStack handItemStack;

    public World world;

    private final EntityNavigation navigation;

    public BlockPos pos;

    public BlockPos target;

    private int width;

    public UseToolGoal(TameableEntity entity, World world, int width) {
        this.entity = entity;
        this.world = world;
        this.width = width;
        this.pos = entity.getBlockPos();
        this.navigation = entity.getNavigation();
    }

    @Override
    public boolean canStart() {
        if (handItemStack == null) {
            handItemStack = entity.getMainHandStack();
        }
        if (handItemStack.isIn(ModTarKeys.SCARE_SELF_ENCHANT_TAG)) {
            for (int i = -width; i <= width; i++) {
                for (int j = -width; j < width; j++) {
                    for (int k = -width; k < width; k++) {
                        BlockPos t = new BlockPos(pos.getX() + i, pos.getY() + j, pos.getY() + k);
                        boolean b = handItemStack.canBreak(new CachedBlockPosition(world, t, false));
                        List<ItemStack> droppedStacks = Block.getDroppedStacks(world.getBlockState(pos), (ServerWorld) world, pos, null, entity, handItemStack);
                        if (droppedStacks != null && !droppedStacks.isEmpty()) {
                            System.out.println("发现: " + t);
                            System.out.println("内容: " + world.getBlockState(t));
                            target = t;
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
}

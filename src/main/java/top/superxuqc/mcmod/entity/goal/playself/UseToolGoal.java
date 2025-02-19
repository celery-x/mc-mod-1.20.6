package top.superxuqc.mcmod.entity.goal.playself;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.pattern.CachedBlockPosition;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.pathing.EntityNavigation;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

import net.minecraft.world.World;
import top.superxuqc.mcmod.common.TargetEntity;
import top.superxuqc.mcmod.register.ModTarKeys;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

/**
 *
 */
public class UseToolGoal extends Goal {

    public TameableEntity entity;

    public ItemStack handItemStack;

    public World world;

    private EntityNavigation navigation;

    public BlockPos pos;

    public BlockPos target;

    public int breakTicks = 100;

    public static ConcurrentHashMap<Integer, BlockPos> workingMap = new ConcurrentHashMap<>();

    private int width;

    public UseToolGoal(TameableEntity entity, World world, int width) {
        this.entity = entity;
        this.world = world;
        this.width = width;

    }

    public UseToolGoal(TameableEntity entity, World world, int width, int breakTicks) {
        this.entity = entity;
        this.world = world;
        this.width = width;
        this.breakTicks = breakTicks;
    }

    @Override
    public boolean canStart() {
        if (target != null) {
            return true;
        }
        if (handItemStack == null) {
            handItemStack = entity.getMainHandStack();
        }
        if (navigation == null) {
            navigation = entity.getNavigation();
        }
        pos = entity.getBlockPos();
        if (handItemStack.isIn(ModTarKeys.SCARE_SELF_ENCHANT_TAG)) {
            double dis = Double.MAX_VALUE;
//            world.getBlockCollisions()
            for (int i = -width; i < width; i++) {
                for (int j = -width; j < width; j++) {
                    for (int k = -width; k < width; k++) {
                        if (testAttack(pos.add(i, j, k)) && pos.getSquaredDistance(pos.add(i, j ,k)) < dis) {
                            dis = pos.getSquaredDistance(pos.add(i, j ,k));
                            target = pos.add(i, j , k);
                        }
                    }
                }
            }
            if (dis != Double.MAX_VALUE) {
                workingMap.put(entity.getId(), target);
                return true;
            }
        }
        return false;
    }

    private boolean testAttack(BlockPos t) {
        if (workingMap.contains(t)) {
            return false;
        }
        BlockState blockState = world.getBlockState(t);
        if (blockState.isAir()) {
            return false;
        }
         boolean in = blockState.isIn(ModTarKeys.SELF_BLACK_LIST);
        if (in) {
            return false;
        }
        if (handItemStack.getComponents().get(DataComponentTypes.TOOL).isCorrectForDrops(blockState)) {
            if (navigation.findPathTo(t, 1) == null) {
                return false;
            }
            System.out.println("发现: " + t);
            System.out.println("内容: " + blockState);
            return true;
        }
        return false;
    }


    private int thisTicks = 0;

    @Override
    public void tick() {
        super.tick();
        System.out.println("当前位置：" + entity.getPos() + "  目标位置: " + target);
        if (target != null && entity.getPos().squaredDistanceTo(target.getX(), target.getY(), target.getZ()) > 2 * 2) {
            boolean b = navigation.startMovingTo(target.getX(), target.getY(), target.getZ(), 5);
            if (!b) {
                workingMap.put(-1, target);
                target = null;
            }
        } else if (target != null) {
            System.out.println("see it !");
            entity.lookAtEntity(new TargetEntity(pos.getX(), pos.getY(), pos.getZ()), 90, 90);
            entity.setAttacking(true);
            thisTicks++;
            if (thisTicks > breakTicks) {
                thisTicks = 0;
                world.breakBlock(target, true);
                target = null;
                workingMap.remove(entity.getId());
            }
        }
    }
}

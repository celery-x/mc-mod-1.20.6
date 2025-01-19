package top.superxuqc.mcmod.entity.goal.playself;

import net.minecraft.block.Block;
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
import java.util.function.Function;

public class UseToolGoal extends Goal {

    public TameableEntity entity;

    public ItemStack handItemStack;

    public World world;

    private EntityNavigation navigation;

    public BlockPos pos;

    public BlockPos target;

    private int width;

    public UseToolGoal(TameableEntity entity, World world, int width) {
        this.entity = entity;
        this.world = world;
        this.width = width;

    }

    @Override
    public boolean canStart() {
        if (target != null) {
            return true;
        }
        if (handItemStack == null) {
            handItemStack = entity.getMainHandStack();
        }
        pos = entity.getBlockPos();
        if (navigation == null) {
            navigation = entity.getNavigation();
        }
        if (handItemStack.isIn(ModTarKeys.SCARE_SELF_ENCHANT_TAG)) {
            for (int i = -width; i <= width; i++) {
                for (int j = -width; j < width; j++) {
                    for (int k = -width; k < width; k++) {
                        BlockPos t = new BlockPos(pos.getX() + i, pos.getY() + j, pos.getZ() + k);
                        if (navigation.findPathTo(t, 10) == null) {
                            continue;
                        }
                        for (int l = 1; l <= 3; l++) {

                            if (testAttack(t.add(0, l, 1))) return true;

                            if (testAttack(t.add(0, l, -1))) return true;

                            if (testAttack(t.add(1, l, 0))) return true;

                            if (testAttack(t.add(-1, l, 0))) return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    private boolean testAttack(BlockPos t) {
        if (handItemStack.getComponents().get(DataComponentTypes.TOOL).isCorrectForDrops(world.getBlockState(t))){
            System.out.println("发现: " + t);
            System.out.println("内容: " + world.getBlockState(t));
            target = t;
            return true;
        }
        return false;
    }

    @Override
    public void tick() {
        super.tick();
        if (target != null && !entity.getPos().equals(target)) {
            boolean b = navigation.startMovingTo(target.getX(), target.getY(), target.getZ(),5);
        } else if (target != null) {

            entity.lookAtEntity(new TargetEntity(pos.getX(), pos.getY(), pos.getZ()), 90, 90);
        }
    }
}

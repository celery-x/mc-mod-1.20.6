package top.superxuqc.mcmod.enchantment;

import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.block.*;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldEvents;
import net.minecraft.world.event.GameEvent;

public class FireWalkerEnchantment extends Enchantment {
    public FireWalkerEnchantment(Properties properties) {
        super(properties);
    }

    @Override
    public boolean isTreasure() {
        return true;
    }

    @Override
    public boolean isCursed() {
        return true;
    }

    public static void evaporateWater(LivingEntity entity, World world, BlockPos blockPos, int level) {
        if (entity.isOnGround()) {
            int i = Math.min(16, 2 + level);
            BlockPos.Mutable mutable = new BlockPos.Mutable();

            // blockPos2 是要替换的位置 blockstate2 是要替换的位置的state
            for (BlockPos blockPos2 : BlockPos.iterate(blockPos.add(-i, -i, -i), blockPos.add(i, i, i))) {
                if (blockPos2.isWithinDistance(entity.getPos(), (double) i)) {
                    mutable.set(blockPos2.getX(), blockPos2.getY(), blockPos2.getZ());
                    BlockState blockState2 = world.getBlockState(mutable);

                    if (blockState2.isOf(Blocks.WATER) || blockState2.isOf(Blocks.SNOW)) {
                        BlockState blockState = Blocks.AIR.getDefaultState();
                        if (blockState.canPlaceAt(world, blockPos2)
                                && world.canPlace(blockState, blockPos2, ShapeContext.absent())) {
                            world.playSound(
                                    entity, blockPos2, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 0.5F, 2.6F + (world.random.nextFloat() - world.random.nextFloat()) * 0.8F
                            );
                            if (blockState2.isOf(Blocks.WATER)) {
                                world.syncWorldEvent(WorldEvents.CRAFTER_SHOOTS, blockPos2, Direction.UP.getId());
                            }
                            world.setBlockState(blockPos2, blockState);
                        }
                    } else if (blockState2.isIn(BlockTags.ICE)) {
                        BlockState blockState = Blocks.WATER.getDefaultState();
                        if (blockState.canPlaceAt(world, blockPos2)
                                && world.canPlace(blockState, blockPos2, ShapeContext.absent())) {
                            world.setBlockState(blockPos2, blockState);
                            world.updateNeighbor(blockPos2, Blocks.WATER, blockPos2);
                        }
                    } else {
                        tryFire(entity, world, blockPos2, blockState2);
                    }
                }
            }
        }
    }

    private static void tryFire(LivingEntity entity, World world, BlockPos blockPos, BlockState blockState) {
        if (!CampfireBlock.canBeLit(blockState) && !CandleBlock.canBeLit(blockState) && !CandleCakeBlock.canBeLit(blockState)) {
            if (AbstractFireBlock.canPlaceAt(world, blockPos, entity.getFacing())) {
                BlockState blockState2 = AbstractFireBlock.getState(world, blockPos);
                world.setBlockState(blockPos, blockState2, Block.NOTIFY_ALL_AND_REDRAW);
                world.emitGameEvent(entity, GameEvent.BLOCK_PLACE, blockPos);
            }
        } else {
            world.setBlockState(blockPos, blockState.with(net.minecraft.state.property.Properties.LIT, Boolean.valueOf(true)), Block.NOTIFY_ALL_AND_REDRAW);
            world.emitGameEvent(entity, GameEvent.BLOCK_CHANGE, blockPos);
        }
    }

    @Override
    public boolean canAccept(Enchantment other) {
        return super.canAccept(other) && other != Enchantments.DEPTH_STRIDER;
    }
}

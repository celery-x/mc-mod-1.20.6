package top.superxuqc.mcmod.entity;

import com.mojang.logging.LogUtils;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.FallingBlockEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.BlockUpdateS2CPacket;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import org.slf4j.Logger;
import top.superxuqc.mcmod.common.BooleanHelper;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

public class CanMoveBlockEntity extends FallingBlockEntity {

    private static final Logger LOGGER = LogUtils.getLogger();

    private List<BlockPos> blockList;

    private BooleanHelper using;

    private BlockState mBlock;

    private List<FallingBlockEntity> transformBlocks = new CopyOnWriteArrayList<>();

    public CanMoveBlockEntity(EntityType<? extends FallingBlockEntity> entityType, World world) {
        super(entityType, world);
    }

    public CanMoveBlockEntity(World world, double x, double y, double z, BlockState block, List<BlockPos> blockList, BooleanHelper using, List<FallingBlockEntity> transformBlocks) {
        this(EntityType.FALLING_BLOCK, world);
        accessBlock(block);
        mBlock = block;
        this.intersectionChecked = true;
        this.setPosition(x, y, z);
        this.setVelocity(Vec3d.ZERO);
        this.prevX = x;
        this.prevY = y;
        this.prevZ = z;
        this.setFallingBlockPos(this.getBlockPos());
        this.blockList = blockList;
        this.using = using;
        this.transformBlocks = transformBlocks;
    }

    @Override
    public BlockState getBlockState() {
        return mBlock;
    }

    @Override
    protected double getGravity() {
        return 0.001;
    }


    private void accessBlock(BlockState block) {
        try {
            Field field = FallingBlockEntity.class.getDeclaredField("block");
            field.setAccessible(true); // 解除Java语言的访问检查
            field.set(this, block); // 修改value的值
        } catch (NoSuchFieldException | IllegalAccessException e) {
            LOGGER.error(e.toString());
        }
    }

    @Override
    public void tick() {
        if (getBlockState().isAir()) {
            this.discard();
            this.kill();
            transformBlocks.remove(this);
        } else {
            Block block = getBlockState().getBlock();
            this.timeFalling++;
            this.applyGravity();
            this.move(MovementType.SELF, this.getVelocity());
            if (!this.getWorld().isClient) {
                BlockPos blockPos = this.getBlockPos();

//                if (!using.isB() && this.isCanTransform2Block()) {  //取消using限制，尽早方块化，减少实体卡顿
                if (age > 60 && this.isCanTransform2Block()) {
                    BlockState blockState = this.getWorld().getBlockState(blockPos);
                    this.setVelocity(this.getVelocity().multiply(0.7, 0.7, 0.7));
                    if (!blockState.isOf(Blocks.MOVING_PISTON)) {
                        if (this.getWorld().setBlockState(blockPos, getBlockState(), Block.NOTIFY_ALL)) {
                            blockList.add(blockPos);
                            ((ServerWorld)this.getWorld())
                                    .getChunkManager()
                                    .threadedAnvilChunkStorage
                                    .sendToOtherNearbyPlayers(this, new BlockUpdateS2CPacket(blockPos, this.getWorld().getBlockState(blockPos)));
                            this.discard();
                            this.kill();
                            transformBlocks.remove(this);
                            if (block instanceof LandingBlock) {
                                ((LandingBlock)block).onLanding(this.getWorld(), blockPos, getBlockState(), blockState, this);
                            }

                            if (this.blockEntityData != null && getBlockState().hasBlockEntity()) {
                                BlockEntity blockEntity = this.getWorld().getBlockEntity(blockPos);
                                if (blockEntity != null) {
                                    NbtCompound nbtCompound = blockEntity.createNbt(this.getWorld().getRegistryManager());

                                    for (String string : this.blockEntityData.getKeys()) {
                                        nbtCompound.put(string, this.blockEntityData.get(string).copy());
                                    }

                                    try {
                                        blockEntity.read(nbtCompound, this.getWorld().getRegistryManager());
                                    } catch (Exception var15) {
                                        LOGGER.error("Failed to load block entity from falling block", (Throwable)var15);
                                    }

                                    blockEntity.markDirty();
                                }
                            }
                        } else if (this.dropItem && this.getWorld().getGameRules().getBoolean(GameRules.DO_ENTITY_DROPS)) {
                            this.discard();
                            this.kill();
                            transformBlocks.remove(this);
                            this.onDestroyedOnLanding(block, blockPos);
                            this.dropItem(block);
                        }
                    }
                } else if (!this.getWorld().isClient
                        && (this.timeFalling > 72000 && (blockPos.getY() <= this.getWorld().getBottomY() || blockPos.getY() > this.getWorld().getTopY()) || this.timeFalling > 72000)) {
                    if (this.dropItem && this.getWorld().getGameRules().getBoolean(GameRules.DO_ENTITY_DROPS)) {
                        this.dropItem(block);
                    }

                    this.discard();
                    this.kill();
                    transformBlocks.remove(this);
                }
            }

            this.setVelocity(this.getVelocity().multiply(0.98));
        }
    }

    private boolean isCanTransform2Block() {
        World world = this.getWorld();
        if (world.isClient()) {
            return false;
        }
        return blockList.contains(getBlockPos().down()) ||
                blockList.contains(getBlockPos().up()) ||
                blockList.contains(getBlockPos().south()) ||
                blockList.contains(getBlockPos().north()) ||
                blockList.contains(getBlockPos().west()) ||
                blockList.contains(getBlockPos().east());
    }

}

package top.superxuqc.mcmod.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.EndermiteEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.thrown.EnderPearlEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import top.superxuqc.mcmod.common.PositionDouble;

public class FeiLeiShenEntity extends ThrownItemEntity {

    public static PositionDouble[] POS = new PositionDouble[9];

    //protected static final TrackedData<NbtCompound> POS = DataTracker.registerData(FeiLeiShenEntity.class, TrackedDataHandlerRegistry.NBT_COMPOUND);
    public static Entity player;

    public static BlockPos[] ORI_BLOCK_POS = new BlockPos[9];


    public static BlockState[] ORI_BLOCK_STATE = new BlockState[9];

    public static World w;

    public static Entity TARGET;

    public FeiLeiShenEntity(EntityType<? extends EnderPearlEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        super.onEntityHit(entityHitResult);
        TARGET = entityHitResult.getEntity();
        //entityHitResult.getEntity().damage(this.getDamageSources().thrown(this, this.getOwner()), 0.0F);
    }



    public FeiLeiShenEntity(World world, LivingEntity owner) {
        super(EntityType.ENDER_PEARL, owner, world);

    }

    @Override
    protected void onBlockHit(BlockHitResult blockHitResult) {
        super.onBlockHit(blockHitResult);
        BlockState blockState = this.getWorld().getBlockState(blockHitResult.getBlockPos());
        w = this.getWorld();
        BlockPos blockPos = blockHitResult.getBlockPos();
        this.getWorld().setBlockState(blockPos, Blocks.TARGET.getDefaultState());
        for (int i = 0; i < 9; i++) {
            if (ORI_BLOCK_POS[i] == null) {
                ORI_BLOCK_POS[i] = new BlockPos(blockPos.getX(), blockPos.getY(), blockPos.getZ());
                ORI_BLOCK_STATE[i] = blockState;
                break;
            }
        }
            //this.discard();
    }

    @Override
    protected void onCollision(HitResult hitResult) {
        super.onCollision(hitResult);
        for (int i = 0; i < 32; i++) {
            this.getWorld()
                    .addParticle(
                            ParticleTypes.PORTAL, this.getX(), this.getY() + this.random.nextDouble() * 2.0, this.getZ(), this.random.nextGaussian(), 0.0, this.random.nextGaussian()
                    );
        }

        if (!this.getWorld().isClient && !this.isRemoved()) {
            Entity entity = this.getOwner();
            if (entity instanceof ServerPlayerEntity serverPlayerEntity) {
                if (serverPlayerEntity.networkHandler.isConnectionOpen() && serverPlayerEntity.getWorld() == this.getWorld() && !serverPlayerEntity.isSleeping()) {
                    if (this.random.nextFloat() < 0.05F && this.getWorld().getGameRules().getBoolean(GameRules.DO_MOB_SPAWNING)) {
                        EndermiteEntity endermiteEntity = EntityType.ENDERMITE.create(this.getWorld());
                        if (endermiteEntity != null) {
                            endermiteEntity.refreshPositionAndAngles(entity.getX(), entity.getY(), entity.getZ(), entity.getYaw(), entity.getPitch());
                            this.getWorld().spawnEntity(endermiteEntity);
                        }
                    }
                    player = this.getOwner();
                    for (int i = 0; i < 9; i++) {
                        if (POS[i] == null) {
                           POS[i] = new PositionDouble(this.getX(), this.getY(), this.getZ());
                           break;
                        }
                    }
//                    if (entity.hasVehicle()) {
//                        serverPlayerEntity.requestTeleportAndDismount(this.getX(), this.getY(), this.getZ());
//                    } else {
//                        entity.requestTeleport(this.getX(), this.getY(), this.getZ());
//                    }

                    entity.onLanding();
                    //entity.damage(this.getDamageSources().fall(), 5.0F);
                    this.getWorld().playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.ENTITY_PLAYER_TELEPORT, SoundCategory.PLAYERS);
                }
            } else if (entity != null) {
                entity.requestTeleport(this.getX(), this.getY(), this.getZ());
                entity.onLanding();
            }

            this.discard();
        }

    }

//    @Override
//    public void onPlayerCollision(PlayerEntity player) {
//        super.onPlayerCollision(player);
//        player.sendPickup(this, 1);
//        this.discard();
//    }

    @Override
    protected Item getDefaultItem() {
        return Items.ENDER_PEARL;
    }
}

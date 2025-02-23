package top.superxuqc.mcmod.network.handler;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import top.superxuqc.mcmod.entity.PlayerSelfEntity;
import top.superxuqc.mcmod.entity.XianJianEntity;
import top.superxuqc.mcmod.network.payload.HitCheckPayload;

public class ServerHitCheckPayloadHandler implements ServerPlayNetworking.PlayPayloadHandler<HitCheckPayload> {


    @Override
    public void receive(HitCheckPayload hitCheckPayload, ServerPlayNetworking.Context context) {
        context.server().execute(() -> {
            ServerWorld world = context.server().getWorld(context.player().getSpawnPointDimension());
            hitAndBreak(hitCheckPayload.blockPos(), hitCheckPayload.amount(), context.player(), world);
        });
    }

    public static void hitAndBreak(BlockPos blockPos, int amount , LivingEntity entity, ServerWorld world) {
        float blastResistance = world.getBlockState(blockPos).getBlock().getBlastResistance();
        float h = 4;
        h -= (blastResistance + 0.3F) * 0.3F;
        if (h > 0.0F ) {
            world.breakBlock(blockPos, false);
        }
        if (!world.isClient()) {
            try {
                world.iterateEntities().forEach(v -> {
                    if (v != null && !(v instanceof PlayerSelfEntity) && v.getBlockPos().equals(blockPos) && !(v instanceof XianJianEntity) && !entity.equals(v)) {
                        v.damage(entity.getDamageSources().mobProjectile(entity, entity), amount);
                    }
                });
            } catch (ArrayIndexOutOfBoundsException e) {
                //ignore
            }
        }
    }
}

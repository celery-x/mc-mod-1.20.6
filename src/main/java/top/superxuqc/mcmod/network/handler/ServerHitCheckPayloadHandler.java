package top.superxuqc.mcmod.network.handler;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.world.ServerWorld;
import top.superxuqc.mcmod.entity.PlayerSelfEntity;
import top.superxuqc.mcmod.network.payload.HitCheckPayload;

public class ServerHitCheckPayloadHandler implements ServerPlayNetworking.PlayPayloadHandler<HitCheckPayload> {


    @Override
    public void receive(HitCheckPayload hitCheckPayload, ServerPlayNetworking.Context context) {
        context.server().execute(() -> {
            ServerWorld world = context.server().getWorld(context.player().getSpawnPointDimension());
            float blastResistance = world.getBlockState(hitCheckPayload.blockPos()).getBlock().getBlastResistance();
            float h = 4;
            h -= (blastResistance + 0.3F) * 0.3F;
            if (h > 0.0F ) {
                world.breakBlock(hitCheckPayload.blockPos(), false);
            }
            if (!world.isClient()) {
                try {
                    world.iterateEntities().forEach(v -> {
                        if (v != null && !(v instanceof PlayerSelfEntity) && v.getBlockPos().equals(hitCheckPayload.blockPos())) {
                            v.damage(context.player().getDamageSources().mobProjectile(context.player(), context.player()), hitCheckPayload.amount());
                        }
                    });
                } catch (ArrayIndexOutOfBoundsException e) {
                    //ignore
                }
            }
        });
    }
}

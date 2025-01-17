package top.superxuqc.mcmod.network.handler;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import top.superxuqc.mcmod.network.payload.HitCheckPayload;

public class SeverHitCheckPayloadHandler implements ServerPlayNetworking.PlayPayloadHandler<HitCheckPayload> {


    @Override
    public void receive(HitCheckPayload hitCheckPayload, ServerPlayNetworking.Context context) {
        context.server().execute(() -> {
            ServerWorld world = context.server().getWorld(context.player().getSpawnPointDimension());
            world.breakBlock(hitCheckPayload.blockPos(), false);
            if (!world.isClient()) {
                try {
                    world.iterateEntities().forEach(v -> {
                        if (v != null && v.getBlockPos().equals(hitCheckPayload.blockPos())) {
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

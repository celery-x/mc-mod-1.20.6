package top.superxuqc.mcmod.network.handler;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerWorld;
import top.superxuqc.mcmod.enchantment.FollowProjectileEnchantment;
import top.superxuqc.mcmod.network.payload.FollowProjectilePayload;
import top.superxuqc.mcmod.network.payload.HitCheckPayload;

public class SeverFollowProjectilePayloadHandler implements ServerPlayNetworking.PlayPayloadHandler<FollowProjectilePayload> {


    @Override
    public void receive(FollowProjectilePayload followProjectilePayload, ServerPlayNetworking.Context context) {
        context.server().execute(() -> {
            ServerWorld world = context.server().getWorld(context.player().getSpawnPointDimension());
            Integer entityID = followProjectilePayload.entityID();
            Entity entityById = world.getEntityById(entityID);
            if (!world.isClient()) {
                FollowProjectileEnchantment.TARGET = entityById;
            }
        });
    }
}

package top.superxuqc.mcmod.network.handler;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerWorld;
import top.superxuqc.mcmod.network.payload.EntityVelocityChangePayload;

public class ServerEntityVelocityChangePayloadHandler implements ServerPlayNetworking.PlayPayloadHandler<EntityVelocityChangePayload> {
    @Override
    public void receive(EntityVelocityChangePayload entityVelocityChangePayload, ServerPlayNetworking.Context context) {
        context.server().execute(() -> {
            ServerWorld world = context.server().getWorld(context.player().getSpawnPointDimension());
            Entity entityById = world.getEntityById(entityVelocityChangePayload.entityID());
            entityById.setVelocity(entityVelocityChangePayload.vec3d());
        });
    }
}

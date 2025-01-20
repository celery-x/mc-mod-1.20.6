package top.superxuqc.mcmod.network.handler;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import top.superxuqc.mcmod.entity.PlayerSelfEntity;
import top.superxuqc.mcmod.network.payload.HitCheckPayload;
import top.superxuqc.mcmod.network.payload.PlayerSelfSpawnPayload;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class SeverPlayerSelfSpawnPayloadHandler implements ServerPlayNetworking.PlayPayloadHandler<PlayerSelfSpawnPayload> {

    private ConcurrentHashMap<UUID, List<Integer>> entityByOwner = new ConcurrentHashMap<>();

    @Override
    public void receive(PlayerSelfSpawnPayload playerSelfSpawnPayload, ServerPlayNetworking.Context context) {
        context.server().execute(() -> {
            ServerWorld world = context.server().getWorld(context.player().getSpawnPointDimension());
            UUID fatherUuid = playerSelfSpawnPayload.father();
            List<Integer> ids = entityByOwner.get(fatherUuid);
            if (ids != null && !ids.isEmpty()) {
                ids.forEach(v -> {
                    world.getEntityById(v).discard();
                });
                entityByOwner.remove(fatherUuid);
                return;
            }
            if (!world.isClient()) {
                if (fatherUuid != null) {
                    ServerPlayerEntity player = context.server().getPlayerManager().getPlayer(fatherUuid);
                    spawnFenShen(world, fatherUuid, player);

                }
            }
        });
    }

    private static void spawnFenShen(ServerWorld world, UUID fatherUuid, ServerPlayerEntity player) {
        PlayerSelfEntity entity = new PlayerSelfEntity(null, world,
                fatherUuid, player.getMainHandStack(), player.getOffHandStack(), (float) player.getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE));
        entity.setPosition(player.getPos().add(2, 2, 2));
        world.spawnEntity(entity);
    }
}

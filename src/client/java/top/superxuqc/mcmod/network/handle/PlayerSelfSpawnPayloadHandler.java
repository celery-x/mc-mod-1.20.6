package top.superxuqc.mcmod.network.handle;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import top.superxuqc.mcmod.entity.PlayerSelfEntity;
import top.superxuqc.mcmod.network.payload.PlayerSelfSpawnPayload;

import java.util.UUID;

public class PlayerSelfSpawnPayloadHandler implements ClientPlayNetworking.PlayPayloadHandler<PlayerSelfSpawnPayload> {

    @Override
    public void receive(PlayerSelfSpawnPayload payload, ClientPlayNetworking.Context context) {
        UUID father = payload.father();
        context.client().world.getEntities().forEach(v -> {
            if (v instanceof PlayerSelfEntity && ((PlayerSelfEntity) v).getOwnerUuid().equals(father)) {
                ((PlayerSelfEntity) v).setTickTime(0);
            }
        });
    }
}

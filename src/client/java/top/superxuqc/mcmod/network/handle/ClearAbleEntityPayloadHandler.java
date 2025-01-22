package top.superxuqc.mcmod.network.handle;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import top.superxuqc.mcmod.entity.PlayerSelfEntity;
import top.superxuqc.mcmod.network.payload.ClearAbleEntityPayload;
import top.superxuqc.mcmod.network.payload.PlayerSelfSpawnPayload;

import java.util.UUID;

public class ClearAbleEntityPayloadHandler implements ClientPlayNetworking.PlayPayloadHandler<ClearAbleEntityPayload> {

    @Override
    public void receive(ClearAbleEntityPayload payload, ClientPlayNetworking.Context context) {
        Integer id = payload.entityID();
    }
}
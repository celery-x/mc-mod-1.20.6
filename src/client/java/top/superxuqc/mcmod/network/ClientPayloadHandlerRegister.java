package top.superxuqc.mcmod.network;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import top.superxuqc.mcmod.network.handle.ClearAbleEntityPayloadHandler;
import top.superxuqc.mcmod.network.handle.PlayerSelfSpawnPayloadHandler;
import top.superxuqc.mcmod.network.payload.ClearAbleEntityPayload;
import top.superxuqc.mcmod.network.payload.HitCheckPayload;
import top.superxuqc.mcmod.network.payload.PlayerSelfSpawnPayload;

public class ClientPayloadHandlerRegister {

    public static void init() {
        ClientPlayNetworking.registerGlobalReceiver(PlayerSelfSpawnPayload.ID, new PlayerSelfSpawnPayloadHandler());
        ClientPlayNetworking.registerGlobalReceiver(ClearAbleEntityPayload.ID, new ClearAbleEntityPayloadHandler());
    }
}

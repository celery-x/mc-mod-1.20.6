package top.superxuqc.mcmod.network;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import top.superxuqc.mcmod.network.handle.ClientHitCheckPayloadHandler;
import top.superxuqc.mcmod.network.payload.HitCheckPayload;

public class ClientPayloadHandlerRegister {

    public static void init() {
        ClientPlayNetworking.registerGlobalReceiver(HitCheckPayload.ID, new ClientHitCheckPayloadHandler());
    }
}

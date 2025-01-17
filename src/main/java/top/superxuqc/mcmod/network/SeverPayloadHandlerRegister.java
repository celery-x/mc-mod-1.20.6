package top.superxuqc.mcmod.network;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import top.superxuqc.mcmod.network.handler.SeverHitCheckPayloadHandler;
import top.superxuqc.mcmod.network.payload.HitCheckPayload;

public class SeverPayloadHandlerRegister {

    public static void init() {
        ServerPlayNetworking.registerGlobalReceiver(HitCheckPayload.ID, new SeverHitCheckPayloadHandler());
    }
}

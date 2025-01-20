package top.superxuqc.mcmod.network;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import top.superxuqc.mcmod.network.handler.SeverFollowProjectilePayloadHandler;
import top.superxuqc.mcmod.network.handler.SeverHitCheckPayloadHandler;
import top.superxuqc.mcmod.network.handler.SeverPlayerSelfSpawnPayloadHandler;
import top.superxuqc.mcmod.network.payload.FollowProjectilePayload;
import top.superxuqc.mcmod.network.payload.HitCheckPayload;
import top.superxuqc.mcmod.network.payload.PlayerSelfSpawnPayload;

public class SeverPayloadHandlerRegister {

    public static void init() {
        ServerPlayNetworking.registerGlobalReceiver(HitCheckPayload.ID, new SeverHitCheckPayloadHandler());
        ServerPlayNetworking.registerGlobalReceiver(FollowProjectilePayload.ID, new SeverFollowProjectilePayloadHandler());
        ServerPlayNetworking.registerGlobalReceiver(PlayerSelfSpawnPayload.ID, new SeverPlayerSelfSpawnPayloadHandler());
    }
}

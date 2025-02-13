package top.superxuqc.mcmod.network;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import top.superxuqc.mcmod.network.handler.ServerEntityVelocityChangePayloadHandler;
import top.superxuqc.mcmod.network.handler.ServerFollowProjectilePayloadHandler;
import top.superxuqc.mcmod.network.handler.ServerHitCheckPayloadHandler;
import top.superxuqc.mcmod.network.handler.ServerPlayerSelfSpawnPayloadHandler;
import top.superxuqc.mcmod.network.payload.EntityVelocityChangePayload;
import top.superxuqc.mcmod.network.payload.FollowProjectilePayload;
import top.superxuqc.mcmod.network.payload.HitCheckPayload;
import top.superxuqc.mcmod.network.payload.PlayerSelfSpawnPayload;

public class SeverPayloadHandlerRegister {

    public static void init() {
        ServerPlayNetworking.registerGlobalReceiver(HitCheckPayload.ID, new ServerHitCheckPayloadHandler());
        ServerPlayNetworking.registerGlobalReceiver(FollowProjectilePayload.ID, new ServerFollowProjectilePayloadHandler());
        ServerPlayNetworking.registerGlobalReceiver(PlayerSelfSpawnPayload.ID, new ServerPlayerSelfSpawnPayloadHandler());
        ServerPlayNetworking.registerGlobalReceiver(EntityVelocityChangePayload.ID, new ServerEntityVelocityChangePayloadHandler());
    }
}

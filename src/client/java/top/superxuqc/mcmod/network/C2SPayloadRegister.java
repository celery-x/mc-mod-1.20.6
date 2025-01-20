package top.superxuqc.mcmod.network;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import top.superxuqc.mcmod.network.payload.FollowProjectilePayload;
import top.superxuqc.mcmod.network.payload.HitCheckPayload;
import top.superxuqc.mcmod.network.payload.PlayerSelfSpawnPayload;

public class C2SPayloadRegister {
    public static void init() {
        PayloadTypeRegistry.playC2S().register(HitCheckPayload.ID, HitCheckPayload.CODEC);
        PayloadTypeRegistry.playC2S().register(FollowProjectilePayload.ID, FollowProjectilePayload.CODEC);
        PayloadTypeRegistry.playC2S().register(PlayerSelfSpawnPayload.ID, PlayerSelfSpawnPayload.CODEC);
    }
}

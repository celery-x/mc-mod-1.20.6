package top.superxuqc.mcmod.network;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import top.superxuqc.mcmod.network.payload.*;

public class S2CPayloadRegister {
    public static void init() {
        PayloadTypeRegistry.playS2C().register(HitCheckPayload.ID, HitCheckPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(FollowProjectilePayload.ID, FollowProjectilePayload.CODEC);
        PayloadTypeRegistry.playS2C().register(PlayerSelfSpawnPayload.ID, PlayerSelfSpawnPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(ClearAbleEntityPayload.ID, ClearAbleEntityPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(EntityVelocityChangePayload.ID, EntityVelocityChangePayload.CODEC);
    }
}

package top.superxuqc.mcmod.network;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import top.superxuqc.mcmod.network.payload.HitCheckPayload;

public class S2CPayloadRegister {
    public static void init() {
        PayloadTypeRegistry.playS2C().register(HitCheckPayload.ID, HitCheckPayload.CODEC);
    }
}

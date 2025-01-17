package top.superxuqc.mcmod.network;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import top.superxuqc.mcmod.network.payload.HitCheckPayload;

public class C2SPayloadRegister {
    public static void init() {
        PayloadTypeRegistry.playC2S().register(HitCheckPayload.ID, HitCheckPayload.CODEC);
    }
}

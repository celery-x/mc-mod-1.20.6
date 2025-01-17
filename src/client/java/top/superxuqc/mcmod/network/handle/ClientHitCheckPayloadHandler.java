package top.superxuqc.mcmod.network.handle;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import top.superxuqc.mcmod.network.payload.HitCheckPayload;

public class ClientHitCheckPayloadHandler implements ClientPlayNetworking.PlayPayloadHandler<HitCheckPayload> {
    @Override
    public void receive(HitCheckPayload hitCheckPayload, ClientPlayNetworking.Context context) {
        context.client().execute(() -> {
            //ignore 客户端不处理碰撞 此class为实例
        });
    }
}

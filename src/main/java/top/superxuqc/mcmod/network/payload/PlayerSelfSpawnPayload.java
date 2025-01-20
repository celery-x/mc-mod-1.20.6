package top.superxuqc.mcmod.network.payload;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import top.superxuqc.mcmod.MyModInitializer;

import java.util.UUID;

public record PlayerSelfSpawnPayload(UUID father) implements CustomPayload {
    public static final CustomPayload.Id<PlayerSelfSpawnPayload> ID = new CustomPayload.Id<>(Identifier.of(MyModInitializer.MOD_ID, "player_self_spawn_payload"));

    // buf是负责信息传递
    // 第一个匿名函数 是发消息构造buf
    // 第二个匿名函数 是收消息用buf构造obj
    // value 是PacketCodec<T, F> 中的F的实例。
    public static final PacketCodec<PacketByteBuf, PlayerSelfSpawnPayload> CODEC = PacketCodec.of((value, buf) -> {
                buf.writeUuid(value.father);
            },
            buf -> new PlayerSelfSpawnPayload(buf.readUuid())
    );

    @Override
    public CustomPayload.Id<? extends CustomPayload> getId() {
        return ID;
    }
}

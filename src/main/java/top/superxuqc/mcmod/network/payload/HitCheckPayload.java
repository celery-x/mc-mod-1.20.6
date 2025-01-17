package top.superxuqc.mcmod.network.payload;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import top.superxuqc.mcmod.MyModInitializer;

import java.nio.ByteBuffer;

public record HitCheckPayload(BlockPos blockPos, Integer amount) implements CustomPayload {
    public static final Id<HitCheckPayload> ID = new CustomPayload.Id<>(Identifier.of(MyModInitializer.MOD_ID, "hit_check_payload"));

    // buf是负责信息传递
    // 第一个匿名函数 是发消息构造buf
    // 第二个匿名函数 是收消息用buf构造obj
    // value 是PacketCodec<T, F> 中的F的实例。
    public static final PacketCodec<PacketByteBuf, HitCheckPayload> CODEC = PacketCodec.of((value, buf) -> {
                buf.writeBlockPos(value.blockPos);
                buf.writeInt(value.amount);
            },
            buf -> new HitCheckPayload(buf.readBlockPos(), buf.readInt())
    );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }


}

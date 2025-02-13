package top.superxuqc.mcmod.network.payload;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import top.superxuqc.mcmod.MyModInitializer;


public record EntityVelocityChangePayload(Vec3d vec3d, Integer entityID) implements CustomPayload {
    public static final Id<EntityVelocityChangePayload> ID = new CustomPayload.Id<>(Identifier.of(MyModInitializer.MOD_ID, "entity_velocity_change_payload"));

    // buf是负责信息传递
    // 第一个匿名函数 是发消息构造buf
    // 第二个匿名函数 是收消息用buf构造obj
    // value 是PacketCodec<T, F> 中的F的实例。
    public static final PacketCodec<PacketByteBuf, EntityVelocityChangePayload> CODEC = PacketCodec.of((value, buf) -> {
                buf.writeVec3d(value.vec3d);
                buf.writeInt(value.entityID);
            },
            buf -> new EntityVelocityChangePayload(buf.readVec3d(), buf.readInt())
    );


    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}

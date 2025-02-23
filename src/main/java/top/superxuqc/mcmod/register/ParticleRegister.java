package top.superxuqc.mcmod.register;

import com.mojang.serialization.MapCodec;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import top.superxuqc.mcmod.MyModInitializer;
import top.superxuqc.mcmod.particle.BoomParticleEffect;
import top.superxuqc.mcmod.particle.JianQiParticleEffect;
import top.superxuqc.mcmod.particle.PaintParticleEffect;

import java.util.function.Function;

public class ParticleRegister {
    public static ParticleType<JianQiParticleEffect> JIANQI = register("dust", true, type -> JianQiParticleEffect.CODEC, type -> JianQiParticleEffect.PACKET_CODEC);

    public static ParticleType<PaintParticleEffect> PAINT = register("dripping_obsidian_tear", true, type -> PaintParticleEffect.CODEC, type -> PaintParticleEffect.PACKET_CODEC);
    public static ParticleType<BoomParticleEffect> BOOM = register("snowflake", true, type -> BoomParticleEffect.CODEC, type -> BoomParticleEffect.PACKET_CODEC);

//    public static ParticleType<BoomParticleEffect> BOOM = register("boom", true, type -> JianQiParticleEffect.CODEC, type -> JianQiParticleEffect.PACKET_CODEC);

    public static void init() {
        Registry.register(Registries.PARTICLE_TYPE, new Identifier(MyModInitializer.MOD_ID, "jianqi"), JIANQI);
        Registry.register(Registries.PARTICLE_TYPE, new Identifier(MyModInitializer.MOD_ID, "dripping_obsidian_tear"), PAINT);
        Registry.register(Registries.PARTICLE_TYPE, new Identifier(MyModInitializer.MOD_ID, "snowflake"), BOOM);
    }
    private static <T extends ParticleEffect> ParticleType<T> register(
            String name,
            boolean alwaysShow,
            Function<ParticleType<T>, MapCodec<T>> codecGetter,
            Function<ParticleType<T>, PacketCodec<? super RegistryByteBuf, T>> packetCodecGetter
    ) {
        return Registry.register(Registries.PARTICLE_TYPE, name, new ParticleType<T>(alwaysShow) {
            @Override
            public MapCodec<T> getCodec() {
                return (MapCodec<T>)codecGetter.apply(this);
            }

            @Override
            public PacketCodec<? super RegistryByteBuf, T> getPacketCodec() {
                return (PacketCodec<? super RegistryByteBuf, T>)packetCodecGetter.apply(this);
            }
        });
    }
}

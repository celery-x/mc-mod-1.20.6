package top.superxuqc.mcmod.particle;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;
import net.minecraft.util.dynamic.Codecs;
import org.joml.Vector3f;
import top.superxuqc.mcmod.register.ParticleRegister;

public class BoomParticleEffect implements ParticleEffect {
    protected static final Codec<Float> SCALE_CODEC = Codec.FLOAT
            .validate(scale -> scale >= 0.01F && scale <= 4.0F ? DataResult.success(scale) : DataResult.error(() -> "Value must be within range [0.01;4.0]: " + scale));

    public static final MapCodec<BoomParticleEffect> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                            Codecs.VECTOR_3F.fieldOf("color").forGetter(effect -> effect.color),
                            Codecs.POSITIVE_INT.fieldOf("age").forGetter(effect -> effect.age),
                            Codecs.POSITIVE_FLOAT.fieldOf("scale").forGetter(effect -> effect.scale)
                    )
                    .apply(instance, BoomParticleEffect::new)
    );
    public static final PacketCodec<RegistryByteBuf, BoomParticleEffect> PACKET_CODEC = PacketCodec.of((value, buf) -> {
                buf.writeVector3f(value.getColor());
                buf.writeInt(value.getAge());
                buf.writeFloat(value.getScale());
            },
            buf -> new BoomParticleEffect(buf.readVector3f(), buf.readInt(), buf.readFloat())
    );
    private final Vector3f color;

    private final int age;

    private final float scale;

    public BoomParticleEffect(Vector3f color, int age, float scale) {
        this.color = color;
        this.age = age;
        this.scale = scale;
    }

    public BoomParticleEffect(Vector3f color, int age) {
        this.color = color;
        this.age = age;
        this.scale = 0.75f;
    }

    public BoomParticleEffect(Vector3f color) {
        this.color = color;
        this.age = 3;
        this.scale = 0.75f;
    }

    public Vector3f getColor() {
        return color;
    }

    public int getAge() {
        return age;
    }

    public float getScale() {
        return scale;
    }

    @Override
    public ParticleType<?> getType() {
        return ParticleRegister.BOOM;
    }
}

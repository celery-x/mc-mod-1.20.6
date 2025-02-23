package top.superxuqc.mcmod.particle;

import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

public class PaintParticleFactory implements ParticleFactory<PaintParticleEffect> {
    private final SpriteProvider spriteProvider;

    public PaintParticleFactory(SpriteProvider spriteProvider) {
        this.spriteProvider = spriteProvider;
    }

    @Nullable
    @Override
    public Particle createParticle(PaintParticleEffect parameters, ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
//        BlockLeakParticle.Dripping dripping = new BlockLeakParticle(world, ).createDrippingObsidianTear(world, x, y, z, Fluids.EMPTY, ParticleTypes.FALLING_OBSIDIAN_TEAR);
//        SpriteBillboardParticle particle = BlockLeakParticle.createDrippingObsidianTear(ParticleTypes.DRIPPING_OBSIDIAN_TEAR, world, x, y, z, velocityX, velocityY, velocityZ);
        LightParticle particle =
                new LightParticle(world, x, y, z, velocityX, velocityY, velocityZ, this.spriteProvider);
        particle.setSprite(this.spriteProvider);
        Vector3f color = parameters.getColor();
        particle.setColor(color.x, color.y, color.z);
        particle.setScale(parameters.getScale());
        particle.setMaxAge(parameters.getAge());
//        System.out.println("init");
        return particle;
    }
}

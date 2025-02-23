package top.superxuqc.mcmod.particle;

import net.minecraft.client.particle.ParticleTextureSheet;
import net.minecraft.client.particle.SpriteBillboardParticle;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.world.ClientWorld;

public class LightParticle extends SpriteBillboardParticle {
    private final SpriteProvider spriteProvider;

    public LightParticle(ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, SpriteProvider spriteProvider) {
        super(world, x, y, z);
        this.gravityStrength = 0F;
        this.velocityMultiplier = 1.0F;
        this.spriteProvider = spriteProvider;
        this.velocityX = velocityX;
        this.velocityY = velocityY;
        this.velocityZ = velocityZ;
        this.setSpriteForAge(spriteProvider);
    }

    public void setScale(float scale) {
        this.scale = scale;
    }
    @Override
    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_OPAQUE;
    }

    @Override
    public void tick() {
        super.tick();
        this.setSpriteForAge(this.spriteProvider);
    }

    @Override
    public int getBrightness(float tint) {
        return 15728880;
    }
}

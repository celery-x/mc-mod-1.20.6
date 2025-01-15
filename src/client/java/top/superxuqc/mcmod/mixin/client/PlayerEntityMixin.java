package top.superxuqc.mcmod.mixin.client;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.superxuqc.mcmod.keymouse.KeyBindRegister;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity{


    private final int MAX_SHOW_TKCK = 2;

    @Unique
    private int showTick = MAX_SHOW_TKCK;

    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(at = @At("TAIL"), method = "Lnet/minecraft/entity/player/PlayerEntity;tick()V")
    public void tickMixin(CallbackInfo ci) {
        if (KeyBindRegister.show && showTick <= 0) {
            showTick = MAX_SHOW_TKCK;
        }
        if (showTick > 0) {
            Entity entity = this;
            entity.getWorld()
                    .addParticle(
                            ParticleTypes.EXPLOSION,
                            entity.getX(),
                            entity.getY(),
                            entity.getZ(),
                            0,
                            0,
                            0
                    );
            showTick--;
            //System.out.println("showTick : " + showTick);
            //System.out.println(KeyBindRegister.show);
            if (showTick <= 0) {
                KeyBindRegister.show = false;
            }
        }
    }

}

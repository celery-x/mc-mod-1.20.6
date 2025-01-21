package top.superxuqc.mcmod.mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.mob.MobEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.superxuqc.mcmod.common.EntityModI;

import java.util.UUID;

@Mixin(ActiveTargetGoal.class)
public class ActiveTargetGoalMixin extends ActiveTargetGoal {


    @Shadow
    protected TargetPredicate targetPredicate;

    public ActiveTargetGoalMixin(MobEntity mob, Class targetClass, boolean checkVisibility) {
        super(mob, targetClass, checkVisibility);
    }


    @Inject(method = "Lnet/minecraft/entity/ai/goal/ActiveTargetGoal;findClosestTarget()V",
            at = @At("HEAD"), cancellable = true
    )
    public void findClosestTarget(CallbackInfo ci) {
        UUID byPlayer = ((EntityModI) this.mob).getByPlayer();
        if (byPlayer != null) {
            this.targetPredicate.setPredicate(livingEntity ->
                    !(((EntityModI) livingEntity).getByPlayer().equals(byPlayer) || livingEntity.getUuid().equals(byPlayer)));
            this.targetEntity = this.mob
                    .getWorld()
                    .getClosestEntity(
                            this.mob.getWorld().getEntitiesByClass(LivingEntity.class, this.getSearchBox(this.getFollowRange()), livingEntity -> true),
                            this.targetPredicate,
                            this.mob,
                            this.mob.getX(),
                            this.mob.getEyeY(),
                            this.mob.getZ()
                    );
            ci.cancel();
        }
    }

}

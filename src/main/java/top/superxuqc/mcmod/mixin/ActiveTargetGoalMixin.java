package top.superxuqc.mcmod.mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.SlimeEntity;
import net.minecraft.entity.passive.CatEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Box;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.superxuqc.mcmod.common.EntityModI;

import java.util.UUID;
import java.util.function.Predicate;

@Mixin(ActiveTargetGoal.class)
public abstract class ActiveTargetGoalMixin {



    @Shadow
    protected TargetPredicate targetPredicate;

    protected MobEntity myMob;

    @Shadow
    protected LivingEntity targetEntity;
    @Shadow
    protected abstract Box getSearchBox(double distance);


    @Inject(method = "Lnet/minecraft/entity/ai/goal/ActiveTargetGoal;<init>(Lnet/minecraft/entity/mob/MobEntity;Ljava/lang/Class;IZZLjava/util/function/Predicate;)V", at = @At("TAIL"))
    public void initMixin(MobEntity mob,
                          Class targetClass,
                          int reciprocalChance,
                          boolean checkVisibility,
                          boolean checkCanNavigate,
                          @Nullable Predicate<LivingEntity> targetPredicate,CallbackInfo ci) {
        myMob = mob;
    }

    @Inject(method = "Lnet/minecraft/entity/ai/goal/ActiveTargetGoal;findClosestTarget()V",
            at = @At("HEAD"), cancellable = true
    )
    public void findClosestTarget(CallbackInfo ci) {
        if (this.myMob instanceof EntityModI) {
        UUID byPlayer = ((EntityModI) this.myMob).getByPlayer();
        if (byPlayer != null) {
            PlayerEntity player = myMob.getWorld().getPlayerByUuid(byPlayer);
            if (player != null && player.getAttacker() != null) {
                this.targetEntity = player.getAttacker();
                ci.cancel();
                return;
            }
            if (player != null && player.getAttacking() != null) {
                this.targetEntity = player.getAttacking();
                ci.cancel();
                return;
            }
            this.targetPredicate.setPredicate(livingEntity ->
                    !livingEntity.getUuid().equals(byPlayer) && ( ((EntityModI) livingEntity).getByPlayer() == null || !(((EntityModI) livingEntity).getByPlayer().equals(byPlayer)))
            );
            this.targetEntity = this.myMob
                    .getWorld()
                    .getClosestEntity(
                            this.myMob.getWorld().getEntitiesByClass(LivingEntity.class, this.getSearchBox(this.myGetFollowRange()), livingEntity -> true),
                            this.targetPredicate,
                            this.myMob,
                            this.myMob.getX(),
                            this.myMob.getEyeY(),
                            this.myMob.getZ()
                    );
            ci.cancel();
        }
        }
    }

    private double myGetFollowRange() {
        return this.myMob.getAttributeValue(EntityAttributes.GENERIC_FOLLOW_RANGE);
    }

}

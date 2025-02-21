package top.superxuqc.mcmod.mixin;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import top.superxuqc.mcmod.common.LoadParticleConfigUtil;
import top.superxuqc.mcmod.common.VelocityUtils;
import top.superxuqc.mcmod.common.particle.lightArrow.LargeArg;
import top.superxuqc.mcmod.common.particle.lightArrow.MediumArg;
import top.superxuqc.mcmod.common.particle.lightArrow.SmallArg;
import top.superxuqc.mcmod.entity.LightArrowEntity;
import top.superxuqc.mcmod.entity.NoneEntity;
import top.superxuqc.mcmod.particle.JianQiParticleEffect;
import top.superxuqc.mcmod.register.ModEnchantmentRegister;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Mixin(BowItem.class)
public class BowItemMixin {

    @Inject(method = "Lnet/minecraft/item/BowItem;onStoppedUsing(Lnet/minecraft/item/ItemStack;Lnet/minecraft/world/World;Lnet/minecraft/entity/LivingEntity;I)V",
            at = @At("HEAD"), cancellable = true)
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks, CallbackInfo ci) {
        if (isYuXuGong(stack)) {
            if (user instanceof PlayerEntity playerEntity) {
                LightArrowEntity entity = new LightArrowEntity(playerEntity, world, stack);
                entity.setVelocity(playerEntity, playerEntity.getPitch(), playerEntity.getYaw(), 0.0F, 2, 1);
                world.spawnEntity(entity);
                if (!existLightArrow.isEmpty()) {
                    existLightArrow.forEach(v -> v.setVelocity(playerEntity, playerEntity.getPitch(), playerEntity.getYaw(), 0.0F, 2, 1));
                    existLightArrow.clear();
                }
            }
            ci.cancel();
        }
    }

    @Inject(method = "Lnet/minecraft/item/BowItem;use(Lnet/minecraft/world/World;Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/util/Hand;)Lnet/minecraft/util/TypedActionResult;",
            at = @At("HEAD"), cancellable = true)
    public void use(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable cr) {
        sneakingTime = 0;
        lager = null;
        medium = null;
        small = null;
    }


    private List<LightArrowEntity> existLightArrow = new CopyOnWriteArrayList<>();

    private int boomNeedTime = 5 * 20;


    private Vec3d sneakingVec3d;
    private float sneakingPitch;
    private float sneakingYaw;



    private List<Float[]> lager;
    private List<Float[]> medium;
    private List<Float[]> small;

    int sneakingTime = 0;
    @Unique
    public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
        if (isYuXuGong(stack)) {
            boolean sneaky = user.isSneaky();
            if (sneaky) {
                if (LoadParticleConfigUtil.lagerList == null
                        || LoadParticleConfigUtil.mediumList == null
                        || LoadParticleConfigUtil.smallList == null) {
                    return;
                }
                int i = sneakingTime / 10;
                System.out.println(i);
                if (i < LoadParticleConfigUtil.lagerList.size()) {
                    System.out.println("lage:" + i);
                    lager = LoadParticleConfigUtil.lagerList.get(i);
                } else if (i < LoadParticleConfigUtil.lagerList.size() + LoadParticleConfigUtil.mediumList.size()) {
                    System.out.println("med:" + i);
                    medium = LoadParticleConfigUtil.mediumList.get(i - LoadParticleConfigUtil.lagerList.size());
                } else if (i < LoadParticleConfigUtil.lagerList.size() + LoadParticleConfigUtil.mediumList.size() + LoadParticleConfigUtil.smallList.size()){
                    System.out.println("small:" + i);
                    small = LoadParticleConfigUtil.smallList
                            .get(i - LoadParticleConfigUtil.lagerList.size() - LoadParticleConfigUtil.mediumList.size());
                }
                if (lager != null) renderFaZhen(lager, world, user, 1);
                if (medium != null) renderFaZhen(medium, world, user, 2);
                if (small != null) renderFaZhen(small, world, user, 3);


                sneakingTime++;
            } else {
                BowItem bowItem = (BowItem) (Object) this;
                int i = bowItem.getMaxUseTime(stack) - remainingUseTicks;
                if (i > 20 && i % 10 == 0) {
                    if (user instanceof PlayerEntity playerEntity) {
                        LightArrowEntity entity = new LightArrowEntity(playerEntity, world, stack);
                        entity.setVelocity(playerEntity, playerEntity.getPitch(), playerEntity.getYaw(), 0.0F, 0f, 1);
                        entity.setPitch(user.getPitch() * -1);
                        entity.setYaw(user.getYaw() * -1);
                        resetPos(user, entity);
                        world.spawnEntity(entity);
                        existLightArrow.add(entity);
                        if (!existLightArrow.isEmpty()) {
                            existLightArrow.forEach(v -> v.setVelocity(playerEntity, playerEntity.getPitch(), playerEntity.getYaw(), 0.0F, 0.00001f, 1));
                        }
                    }
//                    LightArrowEntity entity = new LightArrowEntity(user, world, stack);
//                    entity.setVelocity(user, user.getPitch(), user.getYaw(), 0.0F, 0, 1);
//                    resetPos(user, entity);
//                    world.spawnEntity(entity);

                }
            }
        }
    }

    @NotNull
    private void renderFaZhen(List<Float[]> args, World world, LivingEntity user, int distence) {
        Vec3d vec3d = calVelocity(90 - user.getPitch(), user.getYaw() - 180).multiply(-1.65);
        calVelocity(user.getPitch(), user.getYaw());
        NoneEntity noneEntity = new NoneEntity(world);
        noneEntity.setPos(user.getX() + sneakingVec3d.x * distence + vec3d.x,
                user.getY() + sneakingVec3d.y * distence + vec3d.y,
                user.getZ() + sneakingVec3d.z * distence + vec3d.z);
        noneEntity.setPitch(sneakingPitch);
        noneEntity.setYaw(sneakingYaw);
        for (Float[] arg : args) {
            addParticle(noneEntity, arg);
        }
    }


    private void addParticle(Entity entity, Float[] args) {
        Vec3d realPos = VelocityUtils.toRealPos(entity, args[3], args[4], args[5]);
        Vec3d velocity = entity.getVelocity();
        entity.getWorld().addParticle(
//                ParticleTypes.END_ROD,
                new JianQiParticleEffect(new Vector3f(args[0], args[1], args[2]), 2),
                realPos.x, realPos.y, realPos.z, velocity.x, velocity.y, velocity.z);
    }

    public Vec3d calVelocity(float pitch, float yaw) {
        float f = -MathHelper.sin(yaw * (float) (Math.PI / 180.0)) * MathHelper.cos(pitch * (float) (Math.PI / 180.0));
        float g = -MathHelper.sin((pitch) * (float) (Math.PI / 180.0));
        float h = MathHelper.cos(yaw * (float) (Math.PI / 180.0)) * MathHelper.cos(pitch * (float) (Math.PI / 180.0));
        Vec3d vec3d = new Vec3d(f, g, h);
        double d = vec3d.horizontalLength();
        float calYaw = (float) (MathHelper.atan2(vec3d.x, vec3d.z) * 180.0F / (float) Math.PI);
        float calPitch = (float) (MathHelper.atan2(vec3d.y, d) * 180.0F / (float) Math.PI);
        this.sneakingVec3d = vec3d;
        this.sneakingYaw = calYaw;
        this.sneakingPitch = calPitch;
        return vec3d;
    }


    private void resetPos(Entity user, Entity entity) {
        double x = entity.getWorld().random.nextInt(4) - 2;
        double y = entity.getWorld().random.nextInt(4);
        double z = 1;

        Vec3d realPos = VelocityUtils.toRealPos(user, (float) x, (float) y, (float) z);
        entity.setPos(realPos.x, realPos.y, realPos.z);
    }

    private boolean isYuXuGong(ItemStack stack) {
        int level = EnchantmentHelper.getLevel(ModEnchantmentRegister.YU_XU_GONG_ARSENAL, stack);
        if (level > 0) {
            return true;
        }
        return false;
    }
}

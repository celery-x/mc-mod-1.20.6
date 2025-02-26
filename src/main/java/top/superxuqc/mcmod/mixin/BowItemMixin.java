package top.superxuqc.mcmod.mixin;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BowItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.DustColorTransitionParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import top.superxuqc.mcmod.common.LoadParticleConfigUtil;
import top.superxuqc.mcmod.common.VelocityUtils;
import top.superxuqc.mcmod.common.particle.BigBoomArg;
import top.superxuqc.mcmod.common.particle.lightArrow.LargeArg;
import top.superxuqc.mcmod.common.particle.lightArrow.MediumArg;
import top.superxuqc.mcmod.common.particle.lightArrow.SmallArg;
import top.superxuqc.mcmod.entity.LightArrowEntity;
import top.superxuqc.mcmod.entity.NoneEntity;
import top.superxuqc.mcmod.item.interfaces.TickAble;
import top.superxuqc.mcmod.network.handler.ServerHitCheckPayloadHandler;
import top.superxuqc.mcmod.particle.BoomParticleEffect;
import top.superxuqc.mcmod.particle.JianQiParticleEffect;
import top.superxuqc.mcmod.particle.PaintParticleEffect;
import top.superxuqc.mcmod.register.ModEnchantmentRegister;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Mixin(BowItem.class)
public class BowItemMixin implements TickAble {

    @Inject(method = "Lnet/minecraft/item/BowItem;onStoppedUsing(Lnet/minecraft/item/ItemStack;Lnet/minecraft/world/World;Lnet/minecraft/entity/LivingEntity;I)V",
            at = @At("HEAD"), cancellable = true)
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks, CallbackInfo ci) {
        if (isYuXuGong(stack)) {
            if (user instanceof PlayerEntity playerEntity) {
                if (sneakingType == true) {

                    if (canBoom) {
                        LightArrowEntity entity = new LightArrowEntity(playerEntity, world, stack, true);
                        entity.setOwner(user);
                        entity.setVelocity(playerEntity, playerEntity.getPitch(), playerEntity.getYaw(), 0.0F, 1, 1);
                        world.spawnEntity(entity);
                    }
                } else {
                    LightArrowEntity entity = new LightArrowEntity(playerEntity, world, stack);
                    entity.setVelocity(playerEntity, playerEntity.getPitch(), playerEntity.getYaw(), 0.0F, 2, 1);
                    world.spawnEntity(entity);
                    if (!existLightArrow.isEmpty()) {
                        existLightArrow.forEach(v -> v.setVelocity(playerEntity, playerEntity.getPitch(), playerEntity.getYaw(), 0.0F, 2, 1));
                        existLightArrow.clear();
                    }
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
        if (user.isSneaky()) {
            sneakingType = true;
        } else {
            sneakingType = false;
        }
    }


    private List<LightArrowEntity> existLightArrow = new CopyOnWriteArrayList<>();

    private boolean sneakingType = false;


    private Vec3d sneakingVec3d;
    private float sneakingPitch;
    private float sneakingYaw;



    private List<Float[]> lager;
    private List<Float[]> medium;
    private List<Float[]> small;

    private boolean canBoom = false;

    int sneakingTime = 0;


    @Override
    public void project$tick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {

        if (isYuXuGong(stack)) {
            boolean sneaky = user.isSneaky();
            if (sneaky && sneakingType) {
                if (LoadParticleConfigUtil.lagerList == null
                        || LoadParticleConfigUtil.mediumList == null
                        || LoadParticleConfigUtil.smallList == null) {
                    return;
                }
                int i = sneakingTime / 10;
                if (i < LoadParticleConfigUtil.lagerList.size()) {
                    lager = LoadParticleConfigUtil.lagerList.get(i);
                } else if (i < LoadParticleConfigUtil.lagerList.size() + LoadParticleConfigUtil.mediumList.size()) {
                    medium = LoadParticleConfigUtil.mediumList.get(i - LoadParticleConfigUtil.lagerList.size());
                } else if (i < LoadParticleConfigUtil.lagerList.size() + LoadParticleConfigUtil.mediumList.size() + LoadParticleConfigUtil.smallList.size()){
                    small = LoadParticleConfigUtil.smallList
                            .get(i - LoadParticleConfigUtil.lagerList.size() - LoadParticleConfigUtil.mediumList.size());
                }
                if (i >= LoadParticleConfigUtil.lagerList.size() + LoadParticleConfigUtil.mediumList.size() + LoadParticleConfigUtil.smallList.size() - 1) {
                    canBoom = true;
                }
                if (lager != null) renderFaZhen(lager, world, user, 1);
                if (medium != null) renderFaZhen(medium, world, user, 2);
                if (small != null) renderFaZhen(small, world, user, 3);

                sneakingTime++;
            } else if (!sneakingType){
                BowItem bowItem = (BowItem) (Object) this;
                int i = bowItem.getMaxUseTime(stack) - remainingUseTicks;
                if (i > 20 && i % 10 == 0) {
                    if (user instanceof PlayerEntity playerEntity) {
                        for (int j = 0; j < i / 20; j++) {
                            LightArrowEntity entity = new LightArrowEntity(playerEntity, world, stack);
                            entity.setVelocity(playerEntity, playerEntity.getPitch(), playerEntity.getYaw(), 0.0F, 0f, 1);
                            entity.setPitch(user.getPitch() * -1);
                            entity.setYaw(user.getYaw() * -1);
                            resetPos(user, entity);
                            world.spawnEntity(entity);
                            existLightArrow.add(entity);
                        }
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
        // 上移到视野中间
        Vec3d vec3d = calVelocity(90 - user.getPitch(), user.getYaw() - 180).multiply(-1.65);

        //向前推进
        calVelocity(user.getPitch(), user.getYaw());

        NoneEntity noneEntity = new NoneEntity(world);
        noneEntity.setPos(user.getX() + sneakingVec3d.x * distence + vec3d.x,
                user.getY() + sneakingVec3d.y * distence + vec3d.y,
                user.getZ() + sneakingVec3d.z * distence + vec3d.z);
        noneEntity.setPitch(sneakingPitch);
        noneEntity.setYaw(sneakingYaw);
        for (Float[] arg : args) {
            addParticle(noneEntity, arg, (float) (0.17 + 0.03 * (3 - distence)));
        }
    }

    @Unique
    @NotNull
    private void renderBoom(float[][] args, World world, LivingEntity user) {
        Vec3d vec3d = calVelocity(user.getPitch(), user.getYaw()).normalize();
        NoneEntity noneEntity = new NoneEntity(world);
        noneEntity.setPos(user.getX(),
                user.getY() ,
                user.getZ());
        noneEntity.setPitch(sneakingPitch);
        noneEntity.setYaw(sneakingYaw);
        for (int i = 0; i < 256; i++) {
            for (float[] arg : args) {
                addParticle(user, noneEntity, arg);
            }
            Vec3d add = noneEntity.getPos().add(vec3d);
            noneEntity.setPos(add.x, add.y, add.z);
            if (add.y < -64) {
                break;
            }
            if (i % 10 == 0) {
                addCircle(noneEntity, 5);
            }
        }

    }

    @Unique
    private void addParticle(LivingEntity user, Entity entity, float[] args) {
        Vec3d realPos = VelocityUtils.toRealPos(entity, args[3], args[4], args[5]);
        int bb = entity.getWorld().getRandom().nextInt(10);
        float r = bb > 9 ? 1 : args[0];
        float g = bb > 9 ? 1 : args[1];
        float b = bb > 9 ? 1 : args[2];
        entity.getWorld().addParticle(
                new BoomParticleEffect(new Vector3f(r, g, b), 20, 1f),
                realPos.x, realPos.y, realPos.z, 0, 0, 0);
        if (!user.getWorld().isClient()) {
            ServerHitCheckPayloadHandler.hitAndBreak(new BlockPos((int) realPos.x, (int) realPos.y, (int) realPos.z),
                    100, user, (ServerWorld) user.getWorld());
        }
    }

    private void addCircle(Entity entity, int r) {
        for (float x = -r; x <= r; x += 0.5f) {
            float y = (float) Math.sqrt(r * r - x * x);
            float z = 0;
            Vec3d realPos = VelocityUtils.toRealPos(entity, x, y, z);
            Vec3d realPos1 = VelocityUtils.toRealPos(entity, x, -y, z);
            Vec3d v = entity.getPos().subtract(realPos).normalize().multiply(-0.5);
            Vec3d v1 = entity.getPos().subtract(realPos1).normalize().multiply(-0.5);
            entity.getWorld().addParticle(
                    new PaintParticleEffect(new Vector3f(1, 1, 0.55f), 20 * 3, 1f),
                    realPos.x, realPos.y, realPos.z, v.x, v.y, v.z);
            entity.getWorld().addParticle(
                    new PaintParticleEffect(new Vector3f(1, 1, 0.55f), 20 * 3, 1f),
                    realPos1.x, realPos1.y, realPos1.z, v1.x, v1.y, v1.z);
        }
    }


    private void addParticle(Entity entity, Float[] args, float scale) {
        Vec3d realPos = VelocityUtils.toRealPos(entity, args[3], args[4], args[5]);
        Vec3d velocity = entity.getVelocity();
        int bb = entity.getWorld().getRandom().nextInt(10);
        float r = bb > 9 ? 1 : args[0];
        float g = bb > 9 ? 1 : args[1];
        float b = bb > 9 ? 1 : args[2];
        entity.getWorld().addParticle(
                new PaintParticleEffect(new Vector3f(r, g, b), 1, scale),
                realPos.x, realPos.y, realPos.z, 0, 0, 0);
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
        double x = entity.getWorld().random.nextTriangular(0, 2);
        double y = entity.getWorld().random.nextTriangular(2, 2);
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

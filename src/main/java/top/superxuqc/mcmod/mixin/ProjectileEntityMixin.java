package top.superxuqc.mcmod.mixin;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.thrown.SnowballEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;
import top.superxuqc.mcmod.common.SpawnLivingEntityUtils;
import top.superxuqc.mcmod.common.VelocityUtils;
import top.superxuqc.mcmod.enchantment.ChengJianEnchantment;
import top.superxuqc.mcmod.enchantment.FollowProjectileEnchantment;
import top.superxuqc.mcmod.entity.CanHuChengEntity;
import top.superxuqc.mcmod.entity.ModArrowEntity;
import top.superxuqc.mcmod.register.ModEnchantmentRegister;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Mixin(ProjectileEntity.class)
public class ProjectileEntityMixin implements CanHuChengEntity {





    @Unique
    List<BlockPos> fallStoneList = new ArrayList<>();

    @Inject(at = @At("TAIL"),
            method = "Lnet/minecraft/entity/projectile/ProjectileEntity;onEntityHit(Lnet/minecraft/util/hit/EntityHitResult;)V")
    public void onEntityHitMixin(EntityHitResult entityHitResult, CallbackInfo ci) {
        ThrownItemEntity entity;

        if ((Object)this instanceof ThrownItemEntity) {
            entity = (ThrownItemEntity) (Object) this;
        } else {
            return;
        }
        ItemStack stack = entity.getStack();
        int level = EnchantmentHelper.getLevel(ModEnchantmentRegister.CHENG_JIAN, stack);
        if (level > 0) {
            ChengJianEnchantment.generateMountain(entityHitResult.getEntity().getWorld(), entityHitResult.getEntity().getBlockPos(), fallStoneList, level);
            //entityHitResult.getEntity().damage(entityHitResult.getEntity().getDamageSources().cramming(), level * 10);
        }
        int level1 = EnchantmentHelper.getLevel(ModEnchantmentRegister.SHUTTLECOC_KKICKING, stack);
        if (level1 > 0) {
            //ChengJianEnchantment.generateMountain(entityHitResult.getEntity().getWorld(), entityHitResult.getEntity().getBlockPos(), fallStoneList, level1);
            Entity target = entityHitResult.getEntity();
            boolean wuJiang = false;
            if (target instanceof LivingEntity) {
                ItemStack equippedStack = ((LivingEntity) target).getEquippedStack(EquipmentSlot.CHEST);
                wuJiang = equippedStack.getEnchantments().getLevel(ModEnchantmentRegister.WU_JIANG_PROTECTION) > 0;
            }
            if (wuJiang) {
                target.damage(entity.getDamageSources().thrown(entity, entity.getOwner()), 0);
                Vec3d vec3d = entity.getVelocity();
                target.setVelocity(target.getVelocity().add(entity.calculateVelocity(vec3d.x, vec3d.y, vec3d.z, 13F, 0)));
            }else {
                target.damage(entity.getDamageSources().thrown(entity, entity.getOwner()), 20);
            }
        }
    }

    @ModifyArgs(method = "Lnet/minecraft/entity/projectile/ProjectileEntity;setVelocity(Lnet/minecraft/entity/Entity;FFFFF)V",
            at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/entity/projectile/ProjectileEntity;setVelocity(DDDFF)V"
    ))
    public void setVelocityMixinSetVelocity(Args args) {
        ThrownItemEntity entity;

        if ((Object)this instanceof ThrownItemEntity) {
            entity = (ThrownItemEntity) (Object) this;
        } else {
            return;
        }
        int level1 = EnchantmentHelper.getLevel(ModEnchantmentRegister.SHUTTLECOC_KKICKING, entity.getStack());
        if (level1 > 0) {
            args.set(3, 3f);
            args.set(4, 0f);
        }
    }


    private int myAge = 0;

    public int step = 1;
    public int preStep = 1;

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public int getPreStep() {
        return preStep;
    }

    public void setPreStep(int preStep) {
        this.preStep = preStep;
    }

    @Inject(method = "Lnet/minecraft/entity/projectile/ProjectileEntity;tick()V",
            at = @At("HEAD"))
    public void tickMixin(CallbackInfo ci) {
        myAge++;
        if (myAge == 5 && step > 0) {
            ThrownItemEntity entity;

            if ((Object)this instanceof ThrownItemEntity) {
                entity = (ThrownItemEntity) (Object) this;
            } else {
                return;
            }
            int level = EnchantmentHelper.getLevel(ModEnchantmentRegister.HUCHENG, entity.getStack());
            if (level > 0) {
                if (entity instanceof SnowballEntity) {
                    Entity owner = entity.getOwner();
                    if (owner instanceof LivingEntity) {
                        if (step == preStep && step == 1) {
                            step = level;
                            preStep = level;
                        }
                        for (int i = 0; i < preStep; i++) {
                            double nwex = entity.getX() + entity.getWorld().random.nextInt( + 16) - 8;
                            double nwey = entity.getY() + entity.getWorld().random.nextInt(16) - 8;
                            double nwez = entity.getZ() + entity.getWorld().random.nextInt(16) - 8;
                            SnowballEntity snowballEntity = new SnowballEntity(entity.getWorld(), (LivingEntity) owner);
                            snowballEntity.setItem(entity.getStack());
                            snowballEntity.setPosition(nwex, nwey, nwez);
                            if (snowballEntity instanceof CanHuChengEntity) {
                                ((CanHuChengEntity)snowballEntity).setPreStep(preStep);
                                ((CanHuChengEntity)snowballEntity).setStep(step - 1);
                            }
                            snowballEntity.setVelocity(entity.getVelocity());
                            entity.getWorld().spawnEntity(snowballEntity);
                        }
                    }
                }
            }
        }
    }


    @Inject(method = "Lnet/minecraft/entity/projectile/ProjectileEntity;onCollision(Lnet/minecraft/util/hit/HitResult;)V"
            , at = @At("HEAD")
    )
    public void onCollisionMixin(HitResult hitResult, CallbackInfo ci) {
        HitResult.Type type = hitResult.getType();
        if (type != HitResult.Type.MISS && (Object) this instanceof PersistentProjectileEntity) {
            PersistentProjectileEntity entity = (PersistentProjectileEntity) (Object) this;
            int level = EnchantmentHelper.getLevel(ModEnchantmentRegister.TIAN_ZAI, entity.getItemStack());
            if (level > 0) {
                generateRandomEntity(entity, level);
                entity.discard();
            }
        }
        // TODO 二向箔
//        if ((Object) this instanceof ThrownItemEntity entity) {
//            ItemStack stack = entity.getStack();
//            int level2 = EnchantmentHelper.getLevel(ModEnchantmentRegister.TOW_WAY_FOIL, stack);
//            if (level2 > 0) {
//                if (hitResult instanceof EntityHitResult entityHitResult) {
//                    Entity entity1 = entityHitResult.getEntity();
//                    if (entity1 instanceof LivingEntity livingEntity) {
//
//                    }
//                }
//            }
//        }
    }

    public void generateRandomEntity(PersistentProjectileEntity father, int times) {
        if (father.getOwner() == null) {
            return;
        }
        List<Entity> entityModIS = SpawnLivingEntityUtils.spawnHostileByPlayerTimes(father.getWorld(), father.getBlockPos(), father.getOwner().getUuid(), times);
        for (Entity entityModI : entityModIS) {
            double newx = father.getX() + father.getWorld().random.nextInt(4) -4;
            double newy = father.getY() + father.getWorld().random.nextInt(2);
            double newz = father.getZ() + father.getWorld().random.nextInt(4) - 4;
            System.out.println("Tian zai shengcheng");
            entityModI.setPosition(newx, newy, newz);
            father.getWorld().spawnEntity(entityModI);
            SpawnLivingEntityUtils.addClearableEntity(entityModI);
        }
    }

}

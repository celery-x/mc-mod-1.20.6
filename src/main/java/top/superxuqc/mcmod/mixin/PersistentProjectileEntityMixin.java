package top.superxuqc.mcmod.mixin;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.superxuqc.mcmod.common.EnchantmentHandle;
import top.superxuqc.mcmod.common.EntityModI;
import top.superxuqc.mcmod.common.SpawnLivingEntityUtils;
import top.superxuqc.mcmod.common.VelocityUtils;
import top.superxuqc.mcmod.enchantment.BanKaiEnchantment;
import top.superxuqc.mcmod.enchantment.ChengJianEnchantment;
import top.superxuqc.mcmod.enchantment.FollowProjectileEnchantment;
import top.superxuqc.mcmod.entity.ModArrowEntity;
import top.superxuqc.mcmod.entity.SwordQiEntity;
import top.superxuqc.mcmod.item.QiSwordItem;
import top.superxuqc.mcmod.register.ModEnchantmentRegister;
import top.superxuqc.mcmod.register.ModItemRegister;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

@Mixin(PersistentProjectileEntity.class)
public class PersistentProjectileEntityMixin {

    //

//    @ModifyArg(method = "net.minecraft.entity.projectile.PersistentProjectileEntity.tick",
//            at = @At(
//                    value = "INVOKE",
//                    target = "Lnet/minecraft/entity/projectile/PersistentProjectileEntity;setVelocity(Lnet/minecraft/util/math/Vec3d;)V"
//            ),
//            index = 0)

    @Unique
    List<BlockPos> fallStoneList = new ArrayList<>();

    @Inject(at = @At("TAIL"),
            method = "Lnet/minecraft/entity/projectile/PersistentProjectileEntity;onHit(Lnet/minecraft/entity/LivingEntity;)V")
    public void onHitMixin(LivingEntity target, CallbackInfo ci) {
        ItemStack stack;
        PersistentProjectileEntity entity;
        if ((Object)this instanceof PersistentProjectileEntity) {
            entity = ((PersistentProjectileEntity)(Object) this);
            stack = entity.getItemStack();
        } else {
            return;
        }
        int level = EnchantmentHelper.getLevel(ModEnchantmentRegister.CHENG_JIAN, stack);
        if (level > 0) {
            ChengJianEnchantment.generateMountain(target.getWorld(), target.getBlockPos(), fallStoneList, level);
            //target.damage(target.getDamageSources().cramming(), level * 10);
        }
        int level1 = EnchantmentHelper.getLevel(ModEnchantmentRegister.SHUTTLECOC_KKICKING, stack);
        if (level1 > 0) {
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



    private int myAge = 0;
    private Vec3d lastPos = new Vec3d(0, 0 ,0);
    @Inject(method = "Lnet/minecraft/entity/projectile/PersistentProjectileEntity;tick()V",
            at = @At("HEAD"))
    public void tickMixin(CallbackInfo ci) {
        myAge++;
        if (myAge > 10) {
            PersistentProjectileEntity entity = (PersistentProjectileEntity) (Object) this;
            if (FollowProjectileEnchantment.TARGET != null) {
                Set<RegistryEntry<Enchantment>> enchantments = EnchantmentHelper.getEnchantments(entity.getItemStack()).getEnchantments();
                boolean isFollowProjectile = false;
                for (RegistryEntry<Enchantment> enchantment : enchantments) {
                    isFollowProjectile = enchantment.value() instanceof FollowProjectileEnchantment;
                    if (isFollowProjectile) {
                        break;
                    }
                }
                if(!isFollowProjectile || !FollowProjectileEnchantment.TARGET.isAlive() ) {
                    //entity.setVelocity(0, 0, 0);
                } else {
                    System.out.println(entity.getPos().squaredDistanceTo(lastPos));
                    if (entity.getPos().squaredDistanceTo(lastPos) < 0.1 && entity instanceof ModArrowEntity) {
                        ((ModArrowEntity) entity).onHit((LivingEntity) FollowProjectileEnchantment.TARGET);
                    }else {
                        Vector3f calculate = VelocityUtils.calculate(entity, FollowProjectileEnchantment.TARGET);
                        entity.setVelocity(new Vec3d(calculate));
                    }
                    lastPos = entity.getPos();
                }
            }
        }
    }



}

package top.superxuqc.mcmod.mixin;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.superxuqc.mcmod.common.VelocityUtils;
import top.superxuqc.mcmod.enchantment.BanKaiEnchantment;
import top.superxuqc.mcmod.enchantment.FollowProjectileEnchantment;
import top.superxuqc.mcmod.entity.SwordQiEntity;
import top.superxuqc.mcmod.item.QiSwordItem;

import java.util.Iterator;
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

    private int myAge = 0;
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
                if(!isFollowProjectile || !FollowProjectileEnchantment.TARGET.isAlive()) {
                    //entity.setVelocity(0, 0, 0);
                } else {
                    Vector3f calculate = VelocityUtils.calculate(entity, FollowProjectileEnchantment.TARGET);
                    entity.setVelocity(new Vec3d(calculate));
                }
            }
        }
    }

}

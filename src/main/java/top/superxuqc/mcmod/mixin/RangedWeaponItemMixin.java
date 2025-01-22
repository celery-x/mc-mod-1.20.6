package top.superxuqc.mcmod.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.RangedWeaponItem;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import top.superxuqc.mcmod.common.EnchantmentHandle;
import top.superxuqc.mcmod.register.ModEnchantmentRegister;

import java.util.List;
import java.util.function.Predicate;

//@Mixin(RangedWeaponItem.class)
public class RangedWeaponItemMixin{


    //    @Inject(method = "Lnet/minecraft/item/RangedWeaponItem;shootAll(Lnet/minecraft/world/World;Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/util/Hand;Lnet/minecraft/item/ItemStack;Ljava/util/List;FFZLnet/minecraft/entity/LivingEntity;)V",
//            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;spawnEntity(Lnet/minecraft/entity/Entity;)Z"))
    public void shootAllMixin(World world,
                              LivingEntity shooter,
                              Hand hand,
                              ItemStack stack,
                              List<ItemStack> projectiles,
                              float speed,
                              float divergence,
                              boolean critical,
                              @Nullable LivingEntity target,
                              CallbackInfo ci, @Local ProjectileEntity projectileEntity, @Local(ordinal = 1) ItemStack projectileItem) {

    }

}

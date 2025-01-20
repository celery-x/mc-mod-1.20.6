package top.superxuqc.mcmod.mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import top.superxuqc.mcmod.entity.ModArrowEntity;
import top.superxuqc.mcmod.register.ModItemRegister;

@Mixin(ArrowItem.class)
public class ArrowItemMixin {

    @Inject(method = "Lnet/minecraft/item/ArrowItem;createArrow(Lnet/minecraft/world/World;Lnet/minecraft/item/ItemStack;Lnet/minecraft/entity/LivingEntity;)Lnet/minecraft/entity/projectile/PersistentProjectileEntity;",
            at = @At(value = "RETURN"), cancellable = true)
    public void createArrowMixin(World world, ItemStack stack, LivingEntity shooter, CallbackInfoReturnable<PersistentProjectileEntity> cr) {
        if (stack.getItem().getTranslationKey().equals(ModItemRegister.ARROW_TNT.getTranslationKey())) {
            cr.setReturnValue(new ModArrowEntity(world, shooter.getX(), shooter.getEyeY() - 0.1F, shooter.getZ(),
                    stack.copyWithCount(1), 0, 0));
        } else if (stack.getItem().getTranslationKey().equals(ModItemRegister.TNT_ARROW.getTranslationKey())) {
            cr.setReturnValue(new ModArrowEntity(world, shooter.getX(), shooter.getEyeY() - 0.1F, shooter.getZ(),
                    stack.copyWithCount(1), 0, -1));
        }
        else {
            cr.setReturnValue(new ArrowEntity(world, shooter, stack.copyWithCount(1)));
        }
    }
}

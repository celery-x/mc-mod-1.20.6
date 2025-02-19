package top.superxuqc.mcmod.mixin;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.enchantment.FrostWalkerEnchantment;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import top.superxuqc.mcmod.enchantment.FireWalkerEnchantment;
import top.superxuqc.mcmod.register.ModEnchantmentRegister;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {

    @Inject(method = "Lnet/minecraft/entity/LivingEntity;applyMovementEffects(Lnet/minecraft/util/math/BlockPos;)V",
            at = @At("HEAD")
    )
    public void applyMovementEffectsMixin(BlockPos pos, CallbackInfo ci) {
        LivingEntity entity = (LivingEntity) (Object) this;
        int j = EnchantmentHelper.getEquipmentLevel(ModEnchantmentRegister.FIRE_WALKER, entity);
        if (j > 0) {
            FireWalkerEnchantment.evaporateWater(entity, entity.getWorld(), pos, j);
        }
    }

    @Inject(method = "Lnet/minecraft/entity/LivingEntity;onEquipStack(Lnet/minecraft/entity/EquipmentSlot;Lnet/minecraft/item/ItemStack;Lnet/minecraft/item/ItemStack;)V",
            at = @At("HEAD")
    )
    public void onEquipStackMixin(EquipmentSlot slot, ItemStack oldStack, ItemStack newStack, CallbackInfo ci) {
        LivingEntity entity = (LivingEntity) (Object) this;
        int leveln = EnchantmentHelper.getLevel(ModEnchantmentRegister.FIRE_WALKER, newStack);
        int levelo = EnchantmentHelper.getLevel(ModEnchantmentRegister.FIRE_WALKER, oldStack);

        if (leveln > 0) {
            entity.addStatusEffect(new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE));
        }
        if (levelo > 0 && leveln <= 0) {
            entity.removeStatusEffect(StatusEffects.FIRE_RESISTANCE);
        }

    }


    @Inject(method = "Lnet/minecraft/entity/LivingEntity;canWalkOnFluid(Lnet/minecraft/fluid/FluidState;)Z",
            at = @At("HEAD"),
            cancellable = true
    )
    public void canWalkOnFluidMixin(FluidState state, CallbackInfoReturnable cr) {
        LivingEntity entity = (LivingEntity) (Object) this;
        int j = EnchantmentHelper.getEquipmentLevel(ModEnchantmentRegister.FIRE_WALKER, entity);
        if (j > 0) {
            cr.setReturnValue(true);
            cr.cancel();
        }
    }
}

package top.superxuqc.mcmod.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.superxuqc.mcmod.common.EnchantmentHandle;
import top.superxuqc.mcmod.entity.ModArrowEntity;
import top.superxuqc.mcmod.register.ModEnchantmentRegister;
import top.superxuqc.mcmod.register.ModEntryTypes;
import top.superxuqc.mcmod.register.ModItemRegister;

@Mixin(ArrowEntity.class)
public abstract class ArrowEntityMixin {

    public int modAge = 0;

    @Inject(method = "Lnet/minecraft/entity/projectile/ArrowEntity;tick()V",
            at = @At(value = "HEAD"))
    public void ticketMixin(CallbackInfo ci) {
        modAge ++;
        ArrowEntity entity = (ArrowEntity)(Object) this;

        if (modAge == 10 &&
                ( !(entity instanceof ModArrowEntity) || (entity instanceof ModArrowEntity && ((ModArrowEntity) entity).preStep == 0))
        ) {

            ItemStack itemStack = entity.getItemStack();
            boolean enchantmentHandle = EnchantmentHandle.isEnchantmentHandle(itemStack, ModEnchantmentRegister.HUCHENG);
            boolean tianZai = EnchantmentHandle.isEnchantmentHandle(itemStack, ModEnchantmentRegister.TIAN_ZAI);

            if (enchantmentHandle) {
                int level = EnchantmentHelper.getLevel(ModEnchantmentRegister.HUCHENG, itemStack);
                ModArrowEntity arrow = new ModArrowEntity(entity.getWorld(), entity.getX(),
                        entity.getY(), entity.getZ(),
                        entity.getItemStack().copyWithCount(1), level, level);
                arrow.setTianZai(tianZai);
                arrow.setVelocity(entity.getVelocity());
                entity.getWorld().spawnEntity(arrow);
            }
        }

    }
    @ModifyArg(method = "Lnet/minecraft/entity/projectile/ArrowEntity;<init>(Lnet/minecraft/world/World;DDDLnet/minecraft/item/ItemStack;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/projectile/PersistentProjectileEntity;<init>(Lnet/minecraft/entity/EntityType;DDDLnet/minecraft/world/World;Lnet/minecraft/item/ItemStack;)V"
            ),
            index = 0)
    private static EntityType<? extends ArrowEntity> arrowEntityMixin(EntityType<? extends ArrowEntity> entityType, @Local ItemStack stack) {
        if (stack.getItem().getTranslationKey().equals(ModItemRegister.ARROW_TNT.getTranslationKey())) {
            System.out.println("替换ARROW_TNT");
            return ModEntryTypes.ARROW_TNT;
        }
        if (stack.getItem().getTranslationKey().equals(ModItemRegister.ARROW_TNT.getTranslationKey())) {
            System.out.println("替换TNT_ARROW");
            return ModEntryTypes.TNT_ARROW;
        }
        return entityType;
    }

}

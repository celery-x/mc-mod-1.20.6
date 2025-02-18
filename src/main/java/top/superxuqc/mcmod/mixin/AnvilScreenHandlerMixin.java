package top.superxuqc.mcmod.mixin;

import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.AnvilScreenHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.superxuqc.mcmod.register.ModEnchantmentRegister;


@Mixin(AnvilScreenHandler.class)
public class AnvilScreenHandlerMixin {


    @ModifyArg(method = "Lnet/minecraft/screen/AnvilScreenHandler;updateResult()V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/inventory/CraftingResultInventory;setStack(ILnet/minecraft/item/ItemStack;)V"),
            index = 1)
    public ItemStack updateResultMixin(ItemStack stack) {
        int xian = EnchantmentHelper.getLevel(ModEnchantmentRegister.XIAN_JIAN, stack);
        if (xian > 0) {
            ItemEnchantmentsComponent.Builder builder = new ItemEnchantmentsComponent.Builder(EnchantmentHelper.getEnchantments(stack));
            builder.set(ModEnchantmentRegister.XIAN_JIAN, 0);
            int follow = EnchantmentHelper.getLevel(ModEnchantmentRegister.FOLLOW_PROJECTILE, stack);
            int dao = EnchantmentHelper.getLevel(ModEnchantmentRegister.SWORD_DANCE, stack);
            if (follow > 0) {
                builder.set(ModEnchantmentRegister.FOLLOW_PROJECTILE, 0);
                builder.set(ModEnchantmentRegister.SWORD_DANCE, 1);
            }
            int tian = EnchantmentHelper.getLevel(ModEnchantmentRegister.TIAN_ZAI, stack);
            if (tian > 0 && (follow > 0 || dao > 0)) {
                builder.set(ModEnchantmentRegister.TIAN_ZAI, 0);
                builder.set(ModEnchantmentRegister.AIR_CLAW, 1);
            }
            EnchantmentHelper.set(stack, builder.build());
        }
        return stack;
    }

}

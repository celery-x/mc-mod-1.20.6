package top.superxuqc.mcmod.mixin;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageSources;
import net.minecraft.item.ItemStack;
import net.minecraft.item.MiningToolItem;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import top.superxuqc.mcmod.enchantment.ChengJianEnchantment;
import top.superxuqc.mcmod.register.ModEnchantmentRegister;

import java.util.ArrayList;
import java.util.List;

@Mixin(MiningToolItem.class)
public class MiningToolItemMixin {

    @Unique
    List<BlockPos> fallStoneList = new ArrayList<>();

    @Inject(at = @At("TAIL"),
            method = "Lnet/minecraft/item/MiningToolItem;postHit(Lnet/minecraft/item/ItemStack;Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/entity/LivingEntity;)Z")
    public void postHitMixin(ItemStack stack, LivingEntity target, LivingEntity attacker, CallbackInfoReturnable ci) {
        int level = EnchantmentHelper.getLevel(ModEnchantmentRegister.CHENG_JIAN, stack);
        if (level > 0) {
            ChengJianEnchantment.generateMountain(target.getWorld(), target.getBlockPos(), fallStoneList, level);
            //target.damage(attacker.getDamageSources().cramming(), level * 10);
        }
    }
}

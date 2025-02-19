package top.superxuqc.mcmod.mixin;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import top.superxuqc.mcmod.enchantment.BanKaiEnchantment;
import top.superxuqc.mcmod.enchantment.ChengJianEnchantment;
import top.superxuqc.mcmod.entity.SwordQiEntity;
import top.superxuqc.mcmod.register.ModEnchantmentRegister;
import top.superxuqc.mcmod.register.ModEntryTypes;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Mixin(SwordItem.class)
public class SwordItemMixin {

    @Unique
    List<BlockPos> fallStoneList = new ArrayList<>();

    @Inject(at = @At("TAIL"),
            method = "Lnet/minecraft/item/SwordItem;postHit(Lnet/minecraft/item/ItemStack;Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/entity/LivingEntity;)Z")
    public void postHitMixin(ItemStack stack, LivingEntity target, LivingEntity attacker, CallbackInfoReturnable ci) {
        int level = EnchantmentHelper.getLevel(ModEnchantmentRegister.CHENG_JIAN, stack);
        if (level > 0) {
            ChengJianEnchantment.generateMountain(target.getWorld(), target.getBlockPos(), fallStoneList, level);
            //target.damage(target.getDamageSources().cramming(), level * 10);
        }
    }
}

package top.superxuqc.mcmod.mixin;

import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.thrown.EnderPearlEntity;
import net.minecraft.item.EnderPearlItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import top.superxuqc.mcmod.enchantment.FeiLeiEnchantment;
import top.superxuqc.mcmod.entity.FeiLeiShenEntity;
import top.superxuqc.mcmod.register.ModEnchantmentRegister;
import top.superxuqc.mcmod.register.ModItemRegister;

import java.util.Set;

@Mixin(EnderPearlItem.class)
public class EnderPearItemMixin {

    //@Inject(at = @At("FIELD"), method = "Lnet/minecraft/world/ModifiableWorld;spawnEntity(Lnet/minecraft/entity/Entity;)Z")

    @ModifyArg(method = "Lnet/minecraft/item/EnderPearlItem;use(Lnet/minecraft/world/World;Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/util/Hand;)Lnet/minecraft/util/TypedActionResult;",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/World;spawnEntity(Lnet/minecraft/entity/Entity;)Z"
            ),
            index = 0)
    private Entity spawnEntityMixin(Entity enderPearlEntity){
        Set<RegistryEntry<Enchantment>> enchantments = EnchantmentHelper.getEnchantments(((EnderPearlEntity) enderPearlEntity).getStack()).getEnchantments();
        //System.out.println(enchantments);
        boolean isFeiLei = false;
        for (RegistryEntry<Enchantment> enchantment : enchantments) {
            isFeiLei = enchantment.value() instanceof FeiLeiEnchantment;
            if (isFeiLei) {
                break;
            }
        }
        //System.out.println("ininin");
        LivingEntity user = (LivingEntity) ((EnderPearlEntity) enderPearlEntity).getOwner();
        FeiLeiShenEntity entity = new FeiLeiShenEntity(enderPearlEntity.getWorld(), user);
        entity.setItem(((EnderPearlEntity) enderPearlEntity).getStack());
        entity.setVelocity(user, user.getPitch(), user.getYaw(), 0.0F, 1.5F, 1.0F);
        if(isFeiLei) {
            //System.out.println("newnewn");
            return entity;
        }
        return enderPearlEntity;


    }
}

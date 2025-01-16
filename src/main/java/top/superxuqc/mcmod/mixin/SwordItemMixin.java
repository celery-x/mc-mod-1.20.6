package top.superxuqc.mcmod.mixin;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
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
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import top.superxuqc.mcmod.enchantment.BanKaiEnchantment;
import top.superxuqc.mcmod.entity.SwordQiEntity;
import top.superxuqc.mcmod.register.ModEntryTypes;

import java.util.Set;

@Mixin(Item.class)
public class SwordItemMixin {


    @Inject(at = @At("HEAD"), cancellable = true, method = "Lnet/minecraft/item/Item;use(Lnet/minecraft/world/World;Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/util/Hand;)Lnet/minecraft/util/TypedActionResult;")
    public void useMixin(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable cr){
        Item item = (Item) ((Object)this);
        if ( item instanceof SwordItem) {
            ItemStack stackInHand = user.getStackInHand(hand);
            Set<RegistryEntry<Enchantment>> enchantments = EnchantmentHelper.getEnchantments(stackInHand).getEnchantments();
            boolean isBanKai = false;
            for (RegistryEntry<Enchantment> enchantment : enchantments) {
                isBanKai = enchantment.value() instanceof BanKaiEnchantment;
                if (isBanKai) {
                    break;
                }
            }
            if (isBanKai) {
                world.playSound(
                        null,
                        user.getX(),
                        user.getY(),
                        user.getZ(),
                        SoundEvents.ENTITY_ENDER_PEARL_THROW,
                        SoundCategory.NEUTRAL,
                        0.5F,
                        0.4F / (world.getRandom().nextFloat() * 0.4F + 0.8F)
                );
                user.getItemCooldownManager().set(item, 20);
                if (!world.isClient) {
                    SwordQiEntity qiEntity = new SwordQiEntity(ModEntryTypes.SWORD_QI_TYPE, user, world);
                    //qiEntity.setItem(itemStack);
//            qiEntity.setVelocity(user, user.getPitch(), user.getYaw(), 0.0F, 1.5F, 1.0F);
                    qiEntity.setVelocity(user, user.getPitch(), user.getYaw(), 0F, 1F, 1.0F);
                    qiEntity.setSize(true);
                    //Vec3d velocity = qiEntity.getVelocity();
                    //qiEntity.setVelocity(velocity.x * 10 , velocity.y * 10 , velocity.z * 10);
                    world.spawnEntity(qiEntity);
                }
            }
            ItemStack itemStack = user.getStackInHand(hand);
            cr.setReturnValue(TypedActionResult.success(itemStack, world.isClient()));
        }
    }
}

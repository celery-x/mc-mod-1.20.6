package top.superxuqc.mcmod.item;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import top.superxuqc.mcmod.enchantment.BanKaiEnchantment;
import top.superxuqc.mcmod.entity.SwordQiEntity;
import top.superxuqc.mcmod.register.ModEntryTypes;

import java.util.Set;

public class SwordWithQiItem extends SwordItem {
    public SwordWithQiItem(ToolMaterial toolMaterial, Settings settings) {
        super(toolMaterial, settings);
    }


    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (this instanceof SwordItem) {
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
                user.getItemCooldownManager().set(this, 20);
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
        }
        return super.use(world, user, hand);
    }
}
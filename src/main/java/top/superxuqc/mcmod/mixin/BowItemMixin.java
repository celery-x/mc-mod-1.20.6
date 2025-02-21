package top.superxuqc.mcmod.mixin;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.EntityEffectParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import top.superxuqc.mcmod.entity.LightArrowEntity;
import top.superxuqc.mcmod.register.ModEnchantmentRegister;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Mixin(BowItem.class)
public class BowItemMixin {

    @Inject(method = "Lnet/minecraft/item/BowItem;onStoppedUsing(Lnet/minecraft/item/ItemStack;Lnet/minecraft/world/World;Lnet/minecraft/entity/LivingEntity;I)V",
            at = @At("HEAD"), cancellable = true)
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks, CallbackInfo ci) {
        if (isYuXuGong(stack)) {
            if (user instanceof PlayerEntity playerEntity) {
                LightArrowEntity entity = new LightArrowEntity(playerEntity, world, stack);
                entity.setVelocity(playerEntity, playerEntity.getPitch(), playerEntity.getYaw(), 0.0F, 2, 1);
                world.spawnEntity(entity);
                if (!existLightArrow.isEmpty()) {
                    existLightArrow.forEach(v -> v.setVelocity(playerEntity, playerEntity.getPitch(), playerEntity.getYaw(), 0.0F, 2, 1));
                    existLightArrow.clear();
                }
            }
            ci.cancel();
        }
    }

    @Inject(method = "Lnet/minecraft/item/BowItem;use(Lnet/minecraft/world/World;Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/util/Hand;)Lnet/minecraft/util/TypedActionResult;",
            at = @At("HEAD"), cancellable = true)
    public void use(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable cr) {

    }


    private List<LightArrowEntity> existLightArrow = new CopyOnWriteArrayList<>();

    private int boomNeedTime = 5 * 20;


    @Unique
    public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
        if (isYuXuGong(stack)) {
            boolean sneaky = user.isSneaky();
            if (sneaky) {

            } else {
                BowItem bowItem = (BowItem) (Object) this;
                int i = bowItem.getMaxUseTime(stack) - remainingUseTicks;
                if (i % 10 == 0) {
                    LightArrowEntity entity = new LightArrowEntity(user, world, stack);
                    entity.setVelocity(user, user.getPitch(), user.getYaw(), 0.0F, 0, 1);
                    world.spawnEntity(entity);
                    resetPos(entity);
                    existLightArrow.add(entity);
                }
            }
        }
    }

    private void resetPos(Entity entity) {
        double x = entity.getX() + entity.getWorld().random.nextInt(4) - 4;
        double y = entity.getY() + entity.getWorld().random.nextInt(4) - 4;
        double z = entity.getZ() + entity.getWorld().random.nextInt(4) - 4;
        entity.setPos(x, y, z);
    }

    private boolean isYuXuGong(ItemStack stack) {
        int level = EnchantmentHelper.getLevel(ModEnchantmentRegister.YU_XU_GONG_ARSENAL, stack);
        if (level > 0) {
            return true;
        }
        return false;
    }
}

package top.superxuqc.mcmod.mixin;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.thrown.EnderPearlEntity;
import net.minecraft.util.hit.HitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.superxuqc.mcmod.common.SpawnLivingEntityUtils;
import top.superxuqc.mcmod.register.ModEnchantmentRegister;

import java.util.List;

@Mixin(EnderPearlEntity.class)
public class EnderPearlEntityMixin {

    @Inject(method = "Lnet/minecraft/entity/projectile/thrown/EnderPearlEntity;onCollision(Lnet/minecraft/util/hit/HitResult;)V",
            at = @At("HEAD"), cancellable = true)
    public void onCollisionMixin(HitResult hitResult, CallbackInfo ci) {
        HitResult.Type type = hitResult.getType();
        if (type != HitResult.Type.MISS && (Object) this instanceof EnderPearlEntity) {
            EnderPearlEntity entity = (EnderPearlEntity) (Object) this;
            int level = EnchantmentHelper.getLevel(ModEnchantmentRegister.TIAN_ZAI, entity.getStack());
            if (level > 0) {
                generateRandomEntity(entity, level);
                entity.discard();
                ci.cancel();
            }
        }
    }

    public void generateRandomEntity(EnderPearlEntity father, int times) {
        if (father.getOwner() == null) {
            return;
        }
        List<Entity> entityModIS = SpawnLivingEntityUtils.spawnHostileByPlayerTimes(father.getWorld(), father.getBlockPos(), father.getOwner().getUuid(), times);
        for (Entity entityModI : entityModIS) {
            double newx = father.getX() + father.getWorld().random.nextInt(4) - 4;
            double newy = father.getY() + father.getWorld().random.nextInt(2);
            double newz = father.getZ() + father.getWorld().random.nextInt(4) - 4;
            System.out.println("Tian zai shengcheng");
            entityModI.setPosition(newx, newy, newz);
            father.getWorld().spawnEntity(entityModI);
            SpawnLivingEntityUtils.addClearableEntity(entityModI);
        }
    }
}

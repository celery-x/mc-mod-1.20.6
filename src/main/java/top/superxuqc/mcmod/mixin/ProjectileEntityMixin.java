package top.superxuqc.mcmod.mixin;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import top.superxuqc.mcmod.common.SpawnLivingEntityUtils;
import top.superxuqc.mcmod.enchantment.ChengJianEnchantment;
import top.superxuqc.mcmod.register.ModEnchantmentRegister;

import java.util.ArrayList;
import java.util.List;

@Mixin(ProjectileEntity.class)
public class ProjectileEntityMixin {




    @Unique
    List<BlockPos> fallStoneList = new ArrayList<>();

    @Inject(at = @At("TAIL"),
            method = "Lnet/minecraft/entity/projectile/ProjectileEntity;onEntityHit(Lnet/minecraft/util/hit/EntityHitResult;)V")
    public void postHitMixin(EntityHitResult entityHitResult, CallbackInfo ci) {
        ItemStack stack;
        if ((Object)this instanceof ThrownItemEntity) {
            stack = ((ThrownItemEntity)(Object) this).getStack();
        } else {
            return;
        }
        int level = EnchantmentHelper.getLevel(ModEnchantmentRegister.CHENG_JIAN, stack);
        if (level > 0) {
            ChengJianEnchantment.generateMountain(entityHitResult.getEntity().getWorld(), entityHitResult.getEntity().getBlockPos(), fallStoneList, level);
            //entityHitResult.getEntity().damage(entityHitResult.getEntity().getDamageSources().cramming(), level * 10);
        }
    }


    @Inject(method = "Lnet/minecraft/entity/projectile/ProjectileEntity;onCollision(Lnet/minecraft/util/hit/HitResult;)V"
            , at = @At("HEAD")
    )
    public void onCollisionMixin(HitResult hitResult, CallbackInfo ci) {
        HitResult.Type type = hitResult.getType();
        if (type != HitResult.Type.MISS && (Object) this instanceof PersistentProjectileEntity) {
            PersistentProjectileEntity entity = (PersistentProjectileEntity) (Object) this;
            int level = EnchantmentHelper.getLevel(ModEnchantmentRegister.TIAN_ZAI, entity.getItemStack());
            if (level > 0) {
                generateRandomEntity(entity, level);
                entity.discard();
            }
        }
    }

    public void generateRandomEntity(PersistentProjectileEntity father, int times) {
        if (father.getOwner() == null) {
            return;
        }
        List<Entity> entityModIS = SpawnLivingEntityUtils.spawnHostileByPlayerTimes(father.getWorld(), father.getBlockPos(), father.getOwner().getUuid(), times);
        for (Entity entityModI : entityModIS) {
            double newx = father.getX() + father.getWorld().random.nextInt(4) -4;
            double newy = father.getY() + father.getWorld().random.nextInt(2);
            double newz = father.getZ() + father.getWorld().random.nextInt(4) - 4;
            System.out.println("Tian zai shengcheng");
            entityModI.setPosition(newx, newy, newz);
            father.getWorld().spawnEntity(entityModI);
            SpawnLivingEntityUtils.addClearableEntity(entityModI);
        }
    }

}

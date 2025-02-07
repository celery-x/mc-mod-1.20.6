//package top.superxuqc.mcmod.mixin;
//
//import net.minecraft.block.BlockState;
//import net.minecraft.enchantment.Enchantment;
//import net.minecraft.enchantment.EnchantmentHelper;
//import net.minecraft.entity.Entity;
//import net.minecraft.entity.player.PlayerEntity;
//import net.minecraft.entity.projectile.thrown.SnowballEntity;
//import net.minecraft.item.Item;
//import net.minecraft.item.ItemStack;
//import net.minecraft.item.SwordItem;
//import net.minecraft.registry.entry.RegistryEntry;
//import net.minecraft.sound.SoundCategory;
//import net.minecraft.sound.SoundEvents;
//import net.minecraft.util.Hand;
//import net.minecraft.util.TypedActionResult;
//import net.minecraft.util.hit.EntityHitResult;
//import net.minecraft.util.math.BlockPos;
//import net.minecraft.util.math.Box;
//import net.minecraft.util.math.MathHelper;
//import net.minecraft.util.math.Vec3d;
//import net.minecraft.world.World;
//import org.spongepowered.asm.mixin.Mixin;
//import org.spongepowered.asm.mixin.Unique;
//import org.spongepowered.asm.mixin.injection.At;
//import org.spongepowered.asm.mixin.injection.Inject;
//import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
//import top.superxuqc.mcmod.enchantment.BanKaiEnchantment;
//import top.superxuqc.mcmod.enchantment.ChengJianEnchantment;
//import top.superxuqc.mcmod.entity.ChuanXinZhouEntity;
//import top.superxuqc.mcmod.entity.SwordQiEntity;
//import top.superxuqc.mcmod.register.ModBlocksRegister;
//import top.superxuqc.mcmod.register.ModEntryTypes;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Random;
//import java.util.Set;
//
//@Mixin(SnowballEntity.class)
//public class SnowballEntityMixin {
//
//
//    @Unique
//    private List<BlockPos> fallStoneList = new ArrayList<>();
//
//    @Inject(at = @At("HEAD")
//            , method = "Lnet/minecraft/entity/projectile/thrown/SnowballEntity;onEntityHit(Lnet/minecraft/util/hit/EntityHitResult;)V")
//    public void onEntityHitMixin(EntityHitResult entityHitResult, CallbackInfo ci){
//        BlockPos center = entityHitResult.getEntity().getBlockPos();
//        World world = entityHitResult.getEntity().getWorld();
//        ChengJianEnchantment.generateMountain(world, center, fallStoneList, 0);
//    }
//
//
//}

package top.superxuqc.mcmod.item;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import top.superxuqc.mcmod.common.BooleanHelper;
import top.superxuqc.mcmod.entity.AttractionEntity;
import top.superxuqc.mcmod.entity.RepulsiveForceEntity;
import top.superxuqc.mcmod.register.ModEnchantmentRegister;

import javax.sound.sampled.BooleanControl;
import java.util.List;
import java.util.function.Predicate;

public class AttractionItem extends Item {

    private AttractionEntity controlEntity;

    public AttractionItem(Settings settings) {
        super(settings);
    }

//    @Override
//    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
//        System.out.println(1);
//        ItemStack itemStack = user.getStackInHand(hand);
//        if (world instanceof ServerWorld) {
//            int level = EnchantmentHelper.getLevel(ModEnchantmentRegister.AMPLIFY, itemStack) + 5;
//            AttractionEntity attractionEntity = new AttractionEntity(world, level, new BooleanHelper(true));
//            attractionEntity.setPosition(user.getX(), user.getY(), user.getZ());
//            attractionEntity.setVelocity(user, user.getPitch(), user.getYaw(), 0.0F, 1.5F, 1.0F);
//            world.spawnEntity(attractionEntity);
//        }
//        user.incrementStat(Stats.USED.getOrCreateStat(this));
////        itemStack.decrementUnlessCreative(1, user);
//        return TypedActionResult.success(itemStack, world.isClient());
//    }

    public Vec3d calculateVelocity(float pitch, float yaw, float roll, float speed) {
        float f = -MathHelper.sin(yaw * (float) (Math.PI / 180.0)) * MathHelper.cos(pitch * (float) (Math.PI / 180.0));
        float g = -MathHelper.sin((pitch + roll) * (float) (Math.PI / 180.0));
        float h = MathHelper.cos(yaw * (float) (Math.PI / 180.0)) * MathHelper.cos(pitch * (float) (Math.PI / 180.0));
        return new Vec3d(f, g, h).multiply(speed);
    }

    @Override
    public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
        super.usageTick(world, user, stack, remainingUseTicks);
        if (controlEntity != null && controlEntity.getUsing()) {
            controlEntity.setVelocity(calculateVelocity(user.getPitch(), user.getYaw(), 0F, 0.1F));
        }
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        if (controlEntity != null && controlEntity.hasUsing()) {
            controlEntity.setUsing(false);
            controlEntity = null;
        }
    }

    @Override
    public int getMaxUseTime(ItemStack stack) {
        return 72000;
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.BOW;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        if (controlEntity == null) {
            if (world instanceof ServerWorld) {
                int level = EnchantmentHelper.getLevel(ModEnchantmentRegister.AMPLIFY, itemStack) + 5;
                AttractionEntity attractionEntity = new AttractionEntity(world, level, new BooleanHelper(true));
                attractionEntity.setPosition(user.getX(), user.getY(), user.getZ());
                attractionEntity.setNoGravity(true);
                attractionEntity.setVelocity(user, user.getPitch(), user.getYaw(), 0.0F, 0.5F, 1F);
                world.spawnEntity(attractionEntity);
                this.controlEntity = attractionEntity;
            }
        }
        user.setCurrentHand(hand);
        return TypedActionResult.consume(itemStack);
    }

}

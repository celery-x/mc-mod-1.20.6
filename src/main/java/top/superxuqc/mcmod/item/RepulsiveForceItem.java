package top.superxuqc.mcmod.item;

import net.minecraft.client.item.TooltipType;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.*;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;
import top.superxuqc.mcmod.common.VelocityUtils;
import top.superxuqc.mcmod.entity.RepulsiveForceEntity;
import top.superxuqc.mcmod.register.ModEnchantmentRegister;

import java.util.List;
import java.util.function.Predicate;


/**
 * 斥力球
 */
public class RepulsiveForceItem extends Item {
    public RepulsiveForceItem(Settings settings) {
        super(settings);
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        tooltip.add(Text.translatable("item.superx.repulsive_force_ball.tip").formatted(Formatting.YELLOW));
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        if (world instanceof ServerWorld) {
            int level = EnchantmentHelper.getLevel(ModEnchantmentRegister.AMPLIFY, itemStack) + 5;
            RepulsiveForceEntity repulsiveForceEntity = new RepulsiveForceEntity(world, level);
            repulsiveForceEntity.setPosition(user.getX(), user.getY(), user.getZ());
            world.spawnEntity(repulsiveForceEntity);
        }
        return ItemUsage.consumeHeldItem(world, user, hand);
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.DRINK;
    }

}

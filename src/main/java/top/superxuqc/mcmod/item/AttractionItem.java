package top.superxuqc.mcmod.item;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import top.superxuqc.mcmod.entity.AttractionEntity;
import top.superxuqc.mcmod.entity.RepulsiveForceEntity;
import top.superxuqc.mcmod.register.ModEnchantmentRegister;

public class AttractionItem extends Item {
    public AttractionItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        if (world instanceof ServerWorld) {
            int level = EnchantmentHelper.getLevel(ModEnchantmentRegister.AMPLIFY, itemStack) + 5;
            AttractionEntity attractionEntity = new AttractionEntity(world, level);
            attractionEntity.setPosition(user.getX(), user.getY(), user.getZ());
            attractionEntity.setVelocity(user, user.getPitch(), user.getYaw(), 0.0F, 1.5F, 1.0F);
            world.spawnEntity(attractionEntity);
        }
        user.incrementStat(Stats.USED.getOrCreateStat(this));
        return TypedActionResult.success(itemStack, world.isClient());
    }
}

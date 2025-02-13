package top.superxuqc.mcmod.item;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.World;
import top.superxuqc.mcmod.entity.RepulsiveForceEntity;
import top.superxuqc.mcmod.entity.XianJianEntity;
import top.superxuqc.mcmod.register.ModEnchantmentRegister;

/**
 * 测试使用的临时物品
 */
public class TempItem extends Item {

    public TempItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        System.out.println("use");
        if (world instanceof ServerWorld) {
            XianJianEntity entity = new XianJianEntity(user, world, user.getStackInHand(hand));
            entity.setVelocity(user, user.getPitch(), user.getYaw(), 0F, 1F, 1.0F);
            world.spawnEntity(entity);
        }
        return ItemUsage.consumeHeldItem(world, user, hand);
    }

    @Override
    public int getMaxUseTime(ItemStack stack) {
        return 72000;
    }


    private final int interval = 4;

    @Override
    public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
        System.out.println("tick");
//        if (world.isClient()) {
//            return;
//        }
//        if (remainingUseTicks % interval != 0){
//            return;
//        }
//        int distanceEffect = 255;
//        RepulsiveForceEntity repulsiveForceEntity = new RepulsiveForceEntity(world, distanceEffect);
//        repulsiveForceEntity.setPos(user.getX(), user.getY(), user.getZ());
//        world.spawnEntity(repulsiveForceEntity);
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.BOW;
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        System.out.println("finish");
        return super.finishUsing(stack, world, user);
    }
}

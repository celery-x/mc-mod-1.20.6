package top.superxuqc.mcmod.item;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.thrown.EnderPearlEntity;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.*;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.screen.slot.Slot;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.ClickType;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import top.superxuqc.mcmod.entity.SwordQiEntity;
import top.superxuqc.mcmod.register.ModEntryTypes;
import top.superxuqc.mcmod.register.ModTarKeys;

import java.util.List;
import java.util.function.Predicate;

import static net.minecraft.util.BlockRotation.CLOCKWISE_180;

public class QiSwordItem extends SwordItem {
    public QiSwordItem(Settings settings) {
        super(ToolMaterials.DIAMOND, settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
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

        //user.incrementStat(Stats.USED.getOrCreateStat(this));
        //itemStack.decrementUnlessCreative(1, user);
        return TypedActionResult.success(itemStack, world.isClient());
    }
}

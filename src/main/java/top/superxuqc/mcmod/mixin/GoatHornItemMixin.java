package top.superxuqc.mcmod.mixin;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.GoatHornItem;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Heightmap;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import top.superxuqc.mcmod.item.interfaces.TickAble;
import top.superxuqc.mcmod.register.ModEnchantmentRegister;

@Mixin(GoatHornItem.class)
public class GoatHornItemMixin implements TickAble {

    @Override
    public void project$tick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
        int level = EnchantmentHelper.getLevel(ModEnchantmentRegister.MORE_CHANNELING, stack);
        if (level > 0) {
            if (world instanceof ServerWorld) {
                for (int i = 0; i < level; i++) {
                    BlockPos blockPos = world.getTopPosition(Heightmap.Type.MOTION_BLOCKING, world.getRandomPosInChunk(user.getBlockX(), user.getBlockY(), user.getBlockZ(), 15));
                    if (world.isSkyVisible(blockPos)) {
                        LightningEntity lightningEntity = EntityType.LIGHTNING_BOLT.create(world);
                        if (lightningEntity != null) {
                            lightningEntity.refreshPositionAfterTeleport(Vec3d.ofBottomCenter(blockPos));
                            lightningEntity.setChanneler(user instanceof ServerPlayerEntity ? (ServerPlayerEntity) user : null);
                            world.spawnEntity(lightningEntity);
                        }
                    }
                }
            }
        }
    }
}

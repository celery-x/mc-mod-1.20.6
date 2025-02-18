package top.superxuqc.mcmod.item;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.World;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Unique;
import top.superxuqc.mcmod.common.RaycastHitUtils;
import top.superxuqc.mcmod.common.VelocityUtils;
import top.superxuqc.mcmod.entity.NoneEntity;
import top.superxuqc.mcmod.entity.RepulsiveForceEntity;
import top.superxuqc.mcmod.entity.XianJianEntity;
import top.superxuqc.mcmod.item.interfaces.ItemWithEntity;
import top.superxuqc.mcmod.register.ModEnchantmentRegister;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 测试使用的临时物品
 */
public class TempItem extends Item implements ItemWithEntity {

    public TempItem(Settings settings) {
        super(settings);
    }


    @Unique
    private List<Entity> entityOfItem;


    @Override
    @Unique
    public List<Entity> getEntitesOfItem() {
        return entityOfItem;
    }

    @Override
    @Unique
    public void setEntitiesOfItem(List<Entity> entityOfItem) {
        this.entityOfItem = entityOfItem;
    }

    @Override
    @Unique
    public void addEntitiesOfItem(Entity entityOfItem) {
        if (this.entityOfItem != null) {
            this.entityOfItem.add(entityOfItem);
        } else {
            this.entityOfItem = new CopyOnWriteArrayList<>();
            this.entityOfItem.add(entityOfItem);
        }
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        System.out.println("use");
        if (world instanceof ServerWorld) {
            if (this.entityOfItem != null && !this.entityOfItem.isEmpty()) {
                boolean isEmpty = true;
                for (Entity entity : this.entityOfItem) {
                    if (entity.isAlive()) {
                        isEmpty = false;
                        break;
                    }
                }
                if (isEmpty) {
                    this.entityOfItem.clear();
                } else {
                    HitResult crosshairTarget = RaycastHitUtils.findCrosshairTarget(user, 20, 20, 1);
                    Vec3d vec3d = crosshairTarget.getPos();
                    if (vec3d != null) {
                        for (Entity entity : this.entityOfItem) {
                            if (entity.isAlive()) {
                                NoneEntity e = new NoneEntity(world);
                                e.setPos(vec3d.x, vec3d.y, vec3d.z);
                                //Entity entityById = world.getEntityById(v);
                                Vector3f calculate = VelocityUtils.calculate(entity, e);
                                Vec3d multiply = new Vec3d(calculate);
//                                entity.setVelocity(entity.getVelocity().add(multiply));
                                if (entity instanceof XianJianEntity xianJianEntity) {
                                    xianJianEntity.targetEntity = e;
                                } else {
                                    entity.setVelocity(multiply);
                                }
                            }
                        }
                    }
                }
            }
            if (this.entityOfItem == null || this.entityOfItem.isEmpty()) {
                // 适配 附魔互乘
                int level = EnchantmentHelper.getLevel(ModEnchantmentRegister.HUCHENG, user.getStackInHand(hand));
                if (level > 0) {
                    for (int i = 0; i < level * level; i++) {
                        spawnXianjianEntity(world, user, hand);
                    }
                } else {
                    spawnXianjianEntity(world, user, hand);
                }
            }

        }
        return ItemUsage.consumeHeldItem(world, user, hand);
    }

    private void spawnXianjianEntity(World world, PlayerEntity user, Hand hand) {
        XianJianEntity entity = new XianJianEntity(user, world, user.getStackInHand(hand), 5, false, false);
        entity.setVelocity(user, user.getPitch(), user.getYaw(), 0F, 0.1F, 1.0F);
        this.addEntitiesOfItem(entity);
        world.spawnEntity(entity);
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

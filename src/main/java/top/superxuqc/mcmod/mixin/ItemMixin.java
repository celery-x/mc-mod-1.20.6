package top.superxuqc.mcmod.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import top.superxuqc.mcmod.common.SpawnLivingEntityUtils;
import top.superxuqc.mcmod.enchantment.BanKaiEnchantment;
import top.superxuqc.mcmod.enchantment.ScareSelfEnchantment;
import top.superxuqc.mcmod.entity.SwordQiEntity;
import top.superxuqc.mcmod.register.ModEntryTypes;
import top.superxuqc.mcmod.register.SoundRegister;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

@Mixin(Item.class)
public class ItemMixin {


    private int nowTick = 0;

    private final int MAX_TICK = 20;

    @Unique
    Random scareSelfRandom = new Random();

    @Unique
    CopyOnWriteArrayList<EntityType> scareSelfEntityType = new CopyOnWriteArrayList<>();

    CopyOnWriteArrayList<EntityType> scareSelfHostileEntityType = new CopyOnWriteArrayList<>();

    CopyOnWriteArrayList<Entity> liveEntity = new CopyOnWriteArrayList<>();

    //public ToolItemMixin(Settings settings) {
        //super(settings);
    //}


    @Inject(at = @At("HEAD"), cancellable = true, method = "Lnet/minecraft/item/Item;use(Lnet/minecraft/world/World;Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/util/Hand;)Lnet/minecraft/util/TypedActionResult;")
    public void useMixin(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable cr){
        Item item = (Item) ((Object)this);
        if ( item instanceof SwordItem) {
            ItemStack stackInHand = user.getStackInHand(hand);
            Set<RegistryEntry<Enchantment>> enchantments = EnchantmentHelper.getEnchantments(stackInHand).getEnchantments();
            boolean isBanKai = false;
            for (RegistryEntry<Enchantment> enchantment : enchantments) {
                isBanKai = enchantment.value() instanceof BanKaiEnchantment;
                if (isBanKai) {
                    break;
                }
            }
            if (isBanKai) {
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
                user.getItemCooldownManager().set(item, 20);
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
            }
            ItemStack itemStack = user.getStackInHand(hand);
            cr.setReturnValue(TypedActionResult.success(itemStack, world.isClient()));
        }
    }


    @Inject(at = @At("TAIL"), method = "Lnet/minecraft/item/Item;postMine(Lnet/minecraft/item/ItemStack;Lnet/minecraft/world/World;Lnet/minecraft/block/BlockState;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/entity/LivingEntity;)Z")
    public void postMineMixin(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity miner, CallbackInfoReturnable ci) {
        if (!(stack.getItem() instanceof ToolItem)) {
            return;
        }
        int isScareSelf = ifScareSelf(stack);
        //System.out.println(isScareSelf);
        if (ifSpawn(isScareSelf)) {
            if (isScareSelf >= 5) {
                liveEntity.addAll(SpawnLivingEntityUtils.spawnScareSelf(SpawnLivingEntityUtils.canSpawnHostileEntityType, world, pos));
            } else  {
                liveEntity.addAll(SpawnLivingEntityUtils.spawnScareSelf(SpawnLivingEntityUtils.canSpawnEntityType, world, pos));
            }
        }
    }

    @Inject(at = @At("TAIL"), method = "Lnet/minecraft/item/Item;postHit(Lnet/minecraft/item/ItemStack;Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/entity/LivingEntity;)Z")
    public void postHitMixin(ItemStack stack, LivingEntity target, LivingEntity attacker, CallbackInfoReturnable ci) {
        if (!(stack.getItem() instanceof SwordItem)) {
            return;
        }
        int isScareSelf = ifScareSelf(stack);
        //System.out.println(isScareSelf);
        if (ifSpawn(isScareSelf)) {
            if (isScareSelf >= 5) {
                liveEntity.addAll(SpawnLivingEntityUtils.spawnScareSelf(SpawnLivingEntityUtils.canSpawnHostileEntityType, target.getWorld(), target.getBlockPos()));
            } else  {
                liveEntity.addAll(SpawnLivingEntityUtils.spawnScareSelf(SpawnLivingEntityUtils.canSpawnEntityType, target.getWorld(), target.getBlockPos()));
            }

        }
    }

    @Inject(at = @At("TAIL"), method = "Lnet/minecraft/item/Item;inventoryTick(Lnet/minecraft/item/ItemStack;Lnet/minecraft/world/World;Lnet/minecraft/entity/Entity;IZ)V")
    public void tickMixin(CallbackInfo ci) {
        if (liveEntity.isEmpty()) {
            return;
        }
        if ( nowTick > MAX_TICK) {
            nowTick = 0;
            //System.out.println("remove all entity");
            for (Entity entity : liveEntity) {
                entity.discard();
            }
            liveEntity.clear();
        }

        nowTick++;
    }

    private void spawnScareSelf(CopyOnWriteArrayList<EntityType> list, World world, BlockPos pos) {
        initScareSelfEntityType();

        int target = scareSelfRandom.nextInt(list.size());
        int index = 0;
        //System.out.println("target :: " + target);
        for (EntityType entityType : list) {
            if (index == target) {
                Entity entity = entityType.create(world);
                if (entity == null) {
                    index ++;
                    continue;
                }
                entity.setPosition(pos.getX(), pos.getY(), pos.getZ());
                world.spawnEntity(entity);
                if(SoundRegister.SCARE_SELF_SOUND_PLAY.get()) {
                    entity.playSound(SoundRegister.SCARE_SELF_SOUND, 1, 1);
                }
                liveEntity.add(entity);
                break;
            }
            index++;
        }
        //System.out.println("end");
    }

    private boolean ifSpawn(int i) {
        return i > 0 && i * 10 > scareSelfRandom.nextInt(100);
    }

    private void initScareSelfEntityType() {
        if (!scareSelfEntityType.isEmpty()) {
            return;
        }
        //System.out.println("init spawnScareSelfEntityType");
        Field[] declaredFields = EntityType.class.getDeclaredFields();

        for (Field f : declaredFields) {
            try {
                if (Modifier.isStatic(f.getModifiers())) {
                    if(f.getType() == EntityType.class) {
                        String typeName = f.getGenericType().getTypeName();
                        //System.out.println("put all :: " + typeName);
                        scareSelfEntityType.add( (EntityType) f.get(null));
                        String substring = typeName.substring(typeName.indexOf("<") + 1, typeName.indexOf(">"));
                        if (Class.forName(substring).getSuperclass() == HostileEntity.class) {
                            //System.out.println("put hostile :: " + typeName);
                            scareSelfHostileEntityType.add((EntityType) f.get(null));
                        }
                    }
                }
            } catch (ClassNotFoundException e) {
                //System.out.println(e.getMessage());
            } catch (IllegalAccessException e) {
                //System.out.println(e.getMessage());
            }
        }
    }


//    private List<Class<?>> findSubclasses(Class<?> parentClass) {
//        List<?> subclasses = new ArrayList<>();
//        for (Class<?> clazz : parentClass.getClass().getDeclaredClasses()) {
//            if (parentClass.isAssignableFrom(clazz) && !parentClass.equals(clazz)) {
//
////                Constructor<?> constructor = clazz.getConstructor(EntityType.class, World.class);
////                constructor.newInstance()
////                subclasses.add(clazz);
//            }
//        }
//        return subclasses;
//    }


    private int ifScareSelf(ItemStack stack) {
        Set<RegistryEntry<Enchantment>> enchantments = EnchantmentHelper.getEnchantments(stack).getEnchantments();
        //System.out.println(enchantments);
        boolean isScareSelf = false;
        for (RegistryEntry<Enchantment> enchantment : enchantments) {
            isScareSelf = enchantment.value() instanceof ScareSelfEnchantment;
            if (isScareSelf) {
                return EnchantmentHelper.getLevel(enchantment.value(), stack);
            }
        }
        return 0;
    }

}

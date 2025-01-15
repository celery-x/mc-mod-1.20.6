package top.superxuqc.mcmod.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.thrown.EnderPearlEntity;
import net.minecraft.item.*;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import top.superxuqc.mcmod.enchantment.ScareSelfEnchantment;
import top.superxuqc.mcmod.register.SoundRegister;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

@Mixin(Item.class)
public class ToolItemMixin {


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

    @Inject(at = @At("TAIL"), method = "Lnet/minecraft/item/Item;postMine(Lnet/minecraft/item/ItemStack;Lnet/minecraft/world/World;Lnet/minecraft/block/BlockState;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/entity/LivingEntity;)Z")
    public void postMineMixin(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity miner, CallbackInfoReturnable ci) {
        if (!(stack.getItem() instanceof ToolItem)) {
            return;
        }
        int isScareSelf = ifScareSelf(stack);
        //System.out.println(isScareSelf);
        if (ifSpawn(isScareSelf)) {
            if (isScareSelf >= 5) {
                spawnScareSelf(scareSelfHostileEntityType, world, pos);
            } else  {
                spawnScareSelf(scareSelfEntityType, world, pos);
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
                spawnScareSelf(scareSelfHostileEntityType, target.getWorld(), target.getBlockPos());
            } else  {
                spawnScareSelf(scareSelfEntityType, target.getWorld(), target.getBlockPos());
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

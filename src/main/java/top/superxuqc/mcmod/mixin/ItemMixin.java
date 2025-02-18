package top.superxuqc.mcmod.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import top.superxuqc.mcmod.common.SpawnLivingEntityUtils;
import top.superxuqc.mcmod.enchantment.BanKaiEnchantment;
import top.superxuqc.mcmod.enchantment.ScareSelfEnchantment;
import top.superxuqc.mcmod.entity.SwordQiEntity;
import top.superxuqc.mcmod.entity.XianJianEntity;
import top.superxuqc.mcmod.item.interfaces.ItemWithEntity;
import top.superxuqc.mcmod.register.ModEnchantmentRegister;
import top.superxuqc.mcmod.register.ModEntryTypes;
import top.superxuqc.mcmod.register.SoundRegister;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

@Mixin(Item.class)
public abstract class ItemMixin implements ItemWithEntity {


    @Shadow
    public abstract TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand);

    private int nowTick = 0;

    private final int MAX_TICK = 20;

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
            boolean isBanKai = EnchantmentHelper.getLevel(ModEnchantmentRegister.BAN_KAI, stackInHand) > 0;
            boolean isXianJian = EnchantmentHelper.getLevel(ModEnchantmentRegister.XIAN_JIAN, stackInHand) > 0;
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
            ItemStack stack = user.getStackInHand(hand);
            int follow = EnchantmentHelper.getLevel(ModEnchantmentRegister.SWORD_DANCE, stack);
            int tian = EnchantmentHelper.getLevel(ModEnchantmentRegister.AIR_CLAW, stack);
//            System.out.println(isXianJian);
            if (isXianJian || follow > 0 || tian > 0) {
                if (world instanceof ServerWorld) {
                    if (this.entityOfItem != null && !this.entityOfItem.isEmpty()) {
                        this.entityOfItem.forEach(v -> {v.discard();});
                        this.entityOfItem.clear();
                    }
                    // 适配 附魔互乘

                    int level = EnchantmentHelper.getLevel(ModEnchantmentRegister.HUCHENG, stack);
                    int amout = EnchantmentHelper.getLevel(ModEnchantmentRegister.AMPLIFY, stack);
                    checkEnchantment(stack);

                    if (level > 0) {
                        for (int i = 0; i < level * level; i++) {
                            spawnXianjianEntity(world, user, hand, amout + 5, follow > 0, tian > 0);
                        }
                    } else {
                        spawnXianjianEntity(world, user, hand, amout + 5, follow > 0, tian > 0);
                    }
                }
            }
            ItemStack itemStack = user.getStackInHand(hand);
            cr.setReturnValue(TypedActionResult.success(itemStack, world.isClient()));
        }
    }

    @Unique
    private void checkEnchantment(ItemStack stack) {
        ItemEnchantmentsComponent.Builder builder = new ItemEnchantmentsComponent.Builder(EnchantmentHelper.getEnchantments(stack));
        int follow = EnchantmentHelper.getLevel(ModEnchantmentRegister.FOLLOW_PROJECTILE, stack);
        int dao = EnchantmentHelper.getLevel(ModEnchantmentRegister.SWORD_DANCE, stack);
        if (follow > 0) {
            builder.set(ModEnchantmentRegister.XIAN_JIAN, 0);
            builder.set(ModEnchantmentRegister.FOLLOW_PROJECTILE, 0);
            builder.set(ModEnchantmentRegister.SWORD_DANCE, 1);
        }
        int tian = EnchantmentHelper.getLevel(ModEnchantmentRegister.TIAN_ZAI, stack);
        if (tian > 0 && (follow > 0 || dao > 0)) {
            builder.set(ModEnchantmentRegister.XIAN_JIAN, 0);
            builder.set(ModEnchantmentRegister.TIAN_ZAI, 0);
            builder.set(ModEnchantmentRegister.AIR_CLAW, 1);
        }
        EnchantmentHelper.set(stack, builder.build());
    }

    private void spawnXianjianEntity(World world, PlayerEntity user, Hand hand, int amount, boolean follow, boolean tianZai) {
        XianJianEntity entity = new XianJianEntity(user, world, user.getStackInHand(hand), amount, follow, tianZai);
        entity.setVelocity(user, user.getPitch(), user.getYaw(), 0F, 0.1F, 1.0F);
        this.addEntitiesOfItem(entity);
        world.spawnEntity(entity);
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

package top.superxuqc.mcmod.entity;

import com.google.common.collect.Lists;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.AbstractSkeletonEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.EntityView;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import top.superxuqc.mcmod.enchantment.BanKaiEnchantment;
import top.superxuqc.mcmod.enchantment.FollowProjectileEnchantment;
import top.superxuqc.mcmod.entity.goal.playself.UseToolGoal;
import top.superxuqc.mcmod.network.payload.PlayerSelfSpawnPayload;
import top.superxuqc.mcmod.register.ModEntryTypes;
import top.superxuqc.mcmod.register.ModItemRegister;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public class PlayerSelfEntity extends TameableEntity {

    private static final TrackedData<ItemStack> MAIN_ITEM = DataTracker.registerData(PlayerSelfEntity.class, TrackedDataHandlerRegistry.ITEM_STACK);
    private static final TrackedData<ItemStack> OFF_ITEM = DataTracker.registerData(PlayerSelfEntity.class, TrackedDataHandlerRegistry.ITEM_STACK);

    private static final TrackedData<Integer> TICK_TIME = DataTracker.registerData(PlayerSelfEntity.class, TrackedDataHandlerRegistry.INTEGER);

    private int MAX_AGE = 0;

    private int age = 0;

    public void setTickTime(Integer tickTime) {
        this.dataTracker.set(TICK_TIME, tickTime);
    }

    public Integer getTickTime() {
        return this.dataTracker.get(TICK_TIME);
    }

    public void setMainItem(ItemStack stack) {
        this.dataTracker.set(MAIN_ITEM, stack == null ? ItemStack.EMPTY : stack);
    }

    public void setAmount(float f) {
        this.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE).setBaseValue(f);
    }

    public void setOffItem(ItemStack stack) {
        this.dataTracker.set(OFF_ITEM, stack == null ? ItemStack.EMPTY : stack);
    }

    public ItemStack getMainItem() {
        return this.dataTracker.get(MAIN_ITEM);
    }

    public ItemStack getOffItem() {
        return this.dataTracker.get(OFF_ITEM);
    }

    public PlayerSelfEntity(EntityType entityType, World world) {
        super(ModEntryTypes.PLAYER_SELF, world);
    }

    public PlayerSelfEntity(EntityType entityType, World world, UUID fatherUuid) {
        super(ModEntryTypes.PLAYER_SELF, world);
        this.setOwnerUuid(fatherUuid);
    }

    public PlayerSelfEntity(EntityType entityType, World world, UUID fatherUuid, ItemStack main, ItemStack off, float amount) {
        super(ModEntryTypes.PLAYER_SELF, world);
        this.setOwnerUuid(fatherUuid);
        this.setMainItem(main);
        this.setOffItem(off);
        this.setAmount(amount);
        this.setOwner(world.getPlayerByUuid(fatherUuid));
    }


    public void initEquipment(Random random, LocalDifficulty localDifficulty) {
        super.initEquipment(random, localDifficulty);
        this.equipStack(EquipmentSlot.MAINHAND, getMainHandStack());
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(1, new SwimGoal(this));
//        this.goalSelector.add(8, new LookAtEntityGoal(this, PlayerEntity.class, 8.0F));
//        this.goalSelector.add(8, new LookAroundGoal(this));
        this.goalSelector.add(5, new FollowOwnerGoal(this, 5.0, 10.0F, 2.0F, false));
        this.goalSelector.add(4, new MeleeAttackGoal(this, 5.0, true));
        this.targetSelector.add(1, new TrackOwnerAttackerGoal(this));
        this.targetSelector.add(2, new AttackWithOwnerGoal(this));
        this.targetSelector.add(3, new RevengeGoal(this).setGroupRevenge());
        this.targetSelector.add(7, new ActiveTargetGoal<>(this, HostileEntity.class, false));
        this.targetSelector.add(8, new UniversalAngerGoal(this, true));
//        this.targetSelector.add(5, new UseToolGoal(this, getWorld(), 5));
    }

    List<UUID> getTrustedUuids() {
        List<UUID> list = Lists.<UUID>newArrayList();
        list.add((UUID)this.dataTracker.get(OWNER_UUID).orElse(null));
        return list;
    }

    @Override
    public ItemStack getMainHandStack() {
        return getMainItem();
    }

    @Override
    protected void mobTick() {
        super.mobTick();
        if (MAX_AGE != 0 && age > MAX_AGE) {
            if (!preDiscard) {
                this.preToDiscard();
            } else {
                this.discard();
            }
        }
        age++;
    }

    private boolean preDiscard = false;

    public void preToDiscard() {
        ServerPlayNetworking.send((ServerPlayerEntity) getOwner(), new PlayerSelfSpawnPayload(getOwnerUuid()));
        MAX_AGE = 4;
        age = 0;
        preDiscard = true;
    }

    @Override
    public ItemStack getOffHandStack() {
        return getOffItem();
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
        builder.add(MAIN_ITEM, ItemStack.EMPTY);
        builder.add(OFF_ITEM, ItemStack.EMPTY);
        builder.add(TICK_TIME, 0);
    }

    @Override
    public boolean tryAttack(Entity target) {
        if (!getWorld().isClient) {
            Set<RegistryEntry<Enchantment>> enchantments = EnchantmentHelper.getEnchantments(getMainHandStack()).getEnchantments();
            boolean isBanKai = false;
            for (RegistryEntry<Enchantment> enchantment : enchantments) {
                isBanKai = enchantment.value() instanceof BanKaiEnchantment;
                if (isBanKai) {
                    break;
                }
            }
            if (isBanKai || getMainItem().isOf(ModItemRegister.ZHAN_YUE)) {
                SwordQiEntity qiEntity = new SwordQiEntity(ModEntryTypes.SWORD_QI_TYPE, this, getWorld());
                qiEntity.setVelocity(this, this.getPitch(), this.getYaw(), 0F, 2.5F, 1.0F);
                qiEntity.setSize(false);
                getWorld().spawnEntity(qiEntity);
            }
        }
        return super.tryAttack(target);
    }

    @Override
    public boolean isBreedingItem(ItemStack stack) {
        return false;
    }

    @Nullable
    @Override
    public PassiveEntity createChild(ServerWorld world, PassiveEntity entity) {
        return null;
    }

    @Override
    public EntityView method_48926() {
        return null;
    }

    @Nullable
    @Override
    public LivingEntity getOwner() {
        return this.getWorld().getPlayerByUuid(getOwnerUuid());
    }

    public static DefaultAttributeContainer.Builder createSelfAttributes() {
        return PlayerEntity.createPlayerAttributes().add(EntityAttributes.GENERIC_FOLLOW_RANGE, 64).add(EntityAttributes.GENERIC_ATTACK_KNOCKBACK).add(EntityAttributes.GENERIC_ATTACK_DAMAGE).add(EntityAttributes.GENERIC_LUCK);
    }
}

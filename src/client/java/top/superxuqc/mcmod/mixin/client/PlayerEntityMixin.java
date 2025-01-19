package top.superxuqc.mcmod.mixin.client;

import com.mojang.authlib.GameProfile;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.thrown.EnderPearlEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.superxuqc.mcmod.enchantment.BanKaiEnchantment;
import top.superxuqc.mcmod.enchantment.FeiLeiEnchantment;
import top.superxuqc.mcmod.keymouse.KeyBindRegister;
import top.superxuqc.mcmod.register.ModItemRegister;
import top.superxuqc.mcmod.register.ModTarKeys;

import java.util.Objects;
import java.util.Set;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity{


    private final int MAX_SHOW_TKCK = 2;

    @Unique
    private int showTick = MAX_SHOW_TKCK;

    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }


//    public void replaceMixin() {
//        // 替换手中附魔后的剑
//        ItemStack mainHandStack = this.getInventory().getMainHandStack();
//        Set<RegistryEntry<Enchantment>> enchantments = EnchantmentHelper.getEnchantments(mainHandStack).getEnchantments();
//        boolean isBanKai = false;
//        for (RegistryEntry<Enchantment> enchantment : enchantments) {
//            isBanKai = enchantment.value() instanceof BanKaiEnchantment;
//            if (isBanKai) {
//                break;
//            }
//        }
//        if (isBanKai) {
////            this.getInventory().dropSelectedItem()
//        }
//    }


//    @Inject(at = @At("TAIL"), method = "Lnet/minecraft/entity/player/PlayerEntity;getProjectileType(Lnet/minecraft/item/ItemStack;)Lnet/minecraft/item/ItemStack;")
//    public void getProjectileTypeMixin() {
//
//
//    }


    @Inject(at = @At("TAIL"), method = "Lnet/minecraft/entity/player/PlayerEntity;tick()V")
    public void tickMixin(CallbackInfo ci) {
        if (KeyBindRegister.show && showTick <= 0) {
            showTick = MAX_SHOW_TKCK;
        }
        if (showTick > 0) {
            Entity entity = this;
            entity.getWorld()
                    .addParticle(
                            ParticleTypes.EXPLOSION,
                            entity.getX(),
                            entity.getY(),
                            entity.getZ(),
                            0,
                            0,
                            0
                    );
            showTick--;
            //System.out.println("showTick : " + showTick);
            //System.out.println(KeyBindRegister.show);
            if (showTick <= 0) {
                KeyBindRegister.show = false;
            }
        }
        PlayerEntity play = (PlayerEntity)(Object) this;
        ItemStack mainHandStack = play.getInventory().getMainHandStack();
        Set<RegistryEntry<Enchantment>> enchantments = EnchantmentHelper.getEnchantments(mainHandStack).getEnchantments();
        boolean isBanKai = false;
        for (RegistryEntry<Enchantment> enchantment : enchantments) {
            isBanKai = enchantment.value() instanceof BanKaiEnchantment;
            if (isBanKai) {
                break;
            }
        }
        float maxHealth = play.getMaxHealth();
        float health = play.getHealth();
        if (isBanKai) {
            if (health / maxHealth <= 0.25) {
                //System.out.println("11111");
            }
        }else if (health / maxHealth > 0.25) {
            //System.out.println("22222");
            int slotWithStack = play.getInventory().getSlotWithStack(ModItemRegister.ZHAN_YUE.getDefaultStack());
            if (slotWithStack != -1){
                //play.getInventory().removeStack(slotWithStack);
            }

//            boolean contains = play.getInventory().contains(ModItemRegister.ZHAN_YUE.getDefaultStack());
//            if (contains) {
//                System.out.println("contains");
//            }
        }
    }

}

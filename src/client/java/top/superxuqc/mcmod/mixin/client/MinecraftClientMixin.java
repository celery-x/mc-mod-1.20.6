package top.superxuqc.mcmod.mixin.client;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.RunArgs;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import top.superxuqc.mcmod.common.VelocityUtils;
import top.superxuqc.mcmod.enchantment.BanKaiEnchantment;
import top.superxuqc.mcmod.enchantment.FollowProjectileEnchantment;
import top.superxuqc.mcmod.entity.NoneEntity;
import top.superxuqc.mcmod.entity.SwordQiEntity;
import top.superxuqc.mcmod.item.QiSwordItem;
import top.superxuqc.mcmod.network.payload.EntityVelocityChangePayload;
import top.superxuqc.mcmod.network.payload.FollowProjectilePayload;
import top.superxuqc.mcmod.register.ModEnchantmentRegister;
import top.superxuqc.mcmod.util.ViewUtils;

import java.util.Set;

import static net.minecraft.item.Items.AIR;
import static top.superxuqc.mcmod.register.SoundRegister.SCARE_SELF_SOUND_PLAY;

@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin {

    private long lastAddTick = 0;

    private final long step = 5;


    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;resetLastAttackedTicks()V")
            , method = "Lnet/minecraft/client/MinecraftClient;doAttack()Z")
    public void doAttactMixin(CallbackInfoReturnable ci) {
        MinecraftClient c = (MinecraftClient)(Object)this;
        //System.out.println("client");
        long now = c.getRenderTime();
        ItemStack mainHandStack = c.player.getMainHandStack();
        Set<RegistryEntry<Enchantment>> enchantments = EnchantmentHelper.getEnchantments(mainHandStack).getEnchantments();
        boolean isBanKai = false;
        boolean isFollowProjectile = false;
        for (RegistryEntry<Enchantment> enchantment : enchantments) {
            isBanKai = isBanKai || enchantment.value() instanceof BanKaiEnchantment;
            isFollowProjectile = isFollowProjectile || enchantment.value() instanceof FollowProjectileEnchantment;
            if (isBanKai && isFollowProjectile) {
                break;
            }
        }
        if (now - lastAddTick > step) {
            Item item = mainHandStack.getItem();
            if (item instanceof QiSwordItem || isBanKai) {
                c.interactionManager.attackEntity(c.player, new NoneEntity(c.world));
            }
        }
//        System.out.println(isFollowProjectile);
        if (isFollowProjectile) {
            Entity entity = ViewUtils.updateCrosshairTarget(MinecraftClient.getInstance(), 0, 20, c.getTickDelta());
            if (entity != null) {
                c.player.sendMessage(Text.translatable("target.get.result", entity.getDisplayName().getString()));
//                System.out.println(entity.getDisplayName().getString());
                ClientPlayNetworking.send(new FollowProjectilePayload(entity.getId()));
            }
        }
        int level = EnchantmentHelper.getLevel(ModEnchantmentRegister.XIAN_JIAN, mainHandStack);
        if (level > 0) {
            Vec3d targetPos = ViewUtils.findTargetPos(MinecraftClient.getInstance(), 5, 20, c.getTickDelta());
            ClientPlayNetworking.send(new EntityVelocityChangePayload(targetPos, -1));
        }

    }
}

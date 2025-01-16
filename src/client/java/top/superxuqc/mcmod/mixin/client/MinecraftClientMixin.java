package top.superxuqc.mcmod.mixin.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.RunArgs;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.hit.EntityHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import top.superxuqc.mcmod.enchantment.BanKaiEnchantment;
import top.superxuqc.mcmod.entity.NoneEntity;
import top.superxuqc.mcmod.item.QiSwordItem;

import java.util.Set;

import static net.minecraft.item.Items.AIR;

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
        if (now - lastAddTick > step) {
            ItemStack mainHandStack = c.player.getMainHandStack();
            Set<RegistryEntry<Enchantment>> enchantments = EnchantmentHelper.getEnchantments(mainHandStack).getEnchantments();
            boolean isBanKai = false;
            for (RegistryEntry<Enchantment> enchantment : enchantments) {
                isBanKai = enchantment.value() instanceof BanKaiEnchantment;
                if (isBanKai) {
                    break;
                }
            }
            Item item = mainHandStack.getItem();
            if (item instanceof QiSwordItem || isBanKai) {
                c.interactionManager.attackEntity(c.player, new NoneEntity(c.world));
            }
        }
    }
}

package top.superxuqc.mcmod.mixin.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.RunArgs;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.hit.EntityHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import top.superxuqc.mcmod.entity.NoneEntity;

import static net.minecraft.item.Items.AIR;

@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin {


    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;resetLastAttackedTicks()V")
            , method = "Lnet/minecraft/client/MinecraftClient;doAttack()Z")
    public void doAttactMixin(CallbackInfoReturnable ci) {
        MinecraftClient c = (MinecraftClient)(Object)this;
        System.out.println("client");
        c.interactionManager.attackEntity(c.player, new NoneEntity(c.world));
    }
}

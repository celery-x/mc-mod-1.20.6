package top.superxuqc.mcmod.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.NetworkThreadUtils;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ConnectedClientData;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.superxuqc.mcmod.entity.NoneEntity;
import top.superxuqc.mcmod.entity.SwordQiEntity;
import top.superxuqc.mcmod.register.ModEntryTypes;

@Mixin(ServerPlayNetworkHandler.class)
public abstract class ServerPlayNetworkHandlerMixin {


    @Inject(at = @At("HEAD"), method = "Lnet/minecraft/server/network/ServerPlayNetworkHandler;onPlayerInteractEntity(Lnet/minecraft/network/packet/c2s/play/PlayerInteractEntityC2SPacket;)V")
    public void onPlayerInteractEntityMixin(PlayerInteractEntityC2SPacket packet, CallbackInfo ci) {
        ServerPlayNetworkHandler h = (ServerPlayNetworkHandler)(Object) this;
        System.out.println("sever");
        ServerWorld serverWorld1 = h.player.getServerWorld();
        Entity entity1 = packet.getEntity(serverWorld1);
        if (entity1 != null && (entity1 instanceof NoneEntity || entity1.getId() == -1) ) {
            SwordQiEntity qiEntity = new SwordQiEntity(ModEntryTypes.SWORD_QI_TYPE, h.player, serverWorld1, 60, false);
            //qiEntity.setItem(itemStack);
//            qiEntity.setVelocity(user, user.getPitch(), user.getYaw(), 0.0F, 1.5F, 1.0F);
            qiEntity.setVelocity(h.player, h.player.getPitch(), h.player.getYaw(), 0F, 0.5F, 1.0F);
            //Vec3d velocity = qiEntity.getVelocity();
            //qiEntity.setVelocity(velocity.x * 10 , velocity.y * 10 , velocity.z * 10);
            serverWorld1.spawnEntity(qiEntity);
        }
    }
}

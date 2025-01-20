package top.superxuqc.mcmod.register;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.Items;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import top.superxuqc.mcmod.entity.PlayerSelfEntity;
import top.superxuqc.mcmod.entity.goal.playself.UseToolGoal;

import java.util.UUID;

public class ModSeverEventRegister {

    public static UUID fatherUuid ;

    public static void init() {
        ServerTickEvents.END_SERVER_TICK.register(new ServerTickEvents.EndTick() {
            @Override
            public void onEndTick(MinecraftServer server) {
                if (fatherUuid != null) {
                    ServerPlayerEntity player = server.getPlayerManager().getPlayer(fatherUuid);

                    World world = player.getWorld();
                    PlayerSelfEntity entity = new PlayerSelfEntity(null, world,
                            fatherUuid, player.getMainHandStack(), player.getOffHandStack(), (float) player.getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE));
                    entity.setPosition(player.getPos().add(2, 2, 2));
                    world.spawnEntity(entity);
                    //player.getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE);
                    fatherUuid = null;
                }

            }
        });

        ServerEntityEvents.ENTITY_UNLOAD.register(new ServerEntityEvents.Unload() {
            @Override
            public void onUnload(Entity entity, ServerWorld serverWorld) {
                if (entity instanceof PlayerSelfEntity) {
                    UseToolGoal.workingMap.remove(entity.getId());
                }
            }
        });
    }

}

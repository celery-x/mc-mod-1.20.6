package top.superxuqc.mcmod.network.handler;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import org.joml.Vector3f;
import top.superxuqc.mcmod.common.VelocityUtils;
import top.superxuqc.mcmod.entity.NoneEntity;
import top.superxuqc.mcmod.item.interfaces.ItemWithEntity;
import top.superxuqc.mcmod.network.payload.EntityVelocityChangePayload;

public class ServerEntityVelocityChangePayloadHandler implements ServerPlayNetworking.PlayPayloadHandler<EntityVelocityChangePayload> {
    @Override
    public void receive(EntityVelocityChangePayload entityVelocityChangePayload, ServerPlayNetworking.Context context) {
        context.server().execute(() -> {
            ServerWorld world = context.server().getWorld(context.player().getSpawnPointDimension());
            if (world != null) {
                if (entityVelocityChangePayload.entityID() != -1) {
                    Entity entityById = world.getEntityById(entityVelocityChangePayload.entityID());
                    if (entityById != null) {
                        entityById.setVelocity(entityVelocityChangePayload.vec3d());
                    }
                } else {
                    Item item = context.player().getMainHandStack().getItem();
                    if (item instanceof ItemWithEntity itemWithEntity) {
                        Entity entityOfItem = itemWithEntity.getEntityOfItem();
                        NoneEntity entity = new NoneEntity(world);
                        Vec3d vec3d = entityVelocityChangePayload.vec3d();
                        entity.setPos(vec3d.x, vec3d.y, vec3d.z);
                        Vector3f calculate = VelocityUtils.calculate(entityOfItem, entity);
                        entityOfItem.setVelocity(new Vec3d(calculate).multiply(3));
                    }
                }
            }
        });
    }
}

package top.superxuqc.mcmod.util;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.*;

public class ViewUtils {

    public static Entity updateCrosshairTarget(MinecraftClient client, double blockLength, double entityLength, float tickDelta) {
        Entity entity = client.getCameraEntity();
        if (entity != null) {

            HitResult hitResult = findCrosshairTarget(entity, blockLength, entityLength, tickDelta);
            return hitResult instanceof EntityHitResult entityHitResult ? entityHitResult.getEntity() : null;

        }
        return null;
    }

    public static Vec3d findTargetPos(MinecraftClient client, double blockLength, double entityLength, float tickDelta) {
        Entity entity = client.getCameraEntity();
        if (entity != null) {

            HitResult hitResult = findCrosshairTarget(entity, blockLength, entityLength, tickDelta);
            return hitResult.getPos();

        }
        return null;
    }

    private static HitResult findCrosshairTarget(Entity camera, double blockInteractionRange, double entityInteractionRange, float tickDelta) {
        double d = Math.max(blockInteractionRange, entityInteractionRange);
        double e = MathHelper.square(d);
        Vec3d vec3d = camera.getCameraPosVec(tickDelta);
        HitResult hitResult = camera.raycast(d, tickDelta, false);
        double f = hitResult.getPos().squaredDistanceTo(vec3d);
        if (hitResult.getType() != HitResult.Type.MISS) {
            e = f;
            d = Math.sqrt(f);
        }

        Vec3d vec3d2 = camera.getRotationVec(tickDelta);
        Vec3d vec3d3 = vec3d.add(vec3d2.x * d, vec3d2.y * d, vec3d2.z * d);
        float g = 1.0F;
        Box box = camera.getBoundingBox().stretch(vec3d2.multiply(d)).expand(1.0, 1.0, 1.0);
        EntityHitResult entityHitResult = ProjectileUtil.raycast(camera, vec3d, vec3d3, box, entity -> !entity.isSpectator() && entity.canHit(), e);
        return entityHitResult != null && entityHitResult.getPos().squaredDistanceTo(vec3d) < f
                ? ensureTargetInRange(entityHitResult, vec3d, entityInteractionRange)
                : ensureTargetInRange(hitResult, vec3d, blockInteractionRange);
    }

    private static HitResult ensureTargetInRange(HitResult hitResult, Vec3d cameraPos, double interactionRange) {
        Vec3d vec3d = hitResult.getPos();
        if (!vec3d.isInRange(cameraPos, interactionRange)) {
            Vec3d vec3d2 = hitResult.getPos();
            Direction direction = Direction.getFacing(vec3d2.x - cameraPos.x, vec3d2.y - cameraPos.y, vec3d2.z - cameraPos.z);
            return BlockHitResult.createMissed(vec3d2, direction, BlockPos.ofFloored(vec3d2));
        } else {
            return hitResult;
        }
    }
}

package top.superxuqc.mcmod.common;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import org.joml.Vector3f;

/**
 * 参考net.minecraft.item.CrossbowItem#calcVelocity(net.minecraft.entity.LivingEntity, net.minecraft.util.math.Vec3d, float)
 */
public class VelocityUtils {


    /**
     * 算出来的值，是从shooter 向target位移的向量
     *
     * @param shooter
     * @param target
     * @return
     */
    public static Vector3f calculate(Entity shooter, Entity target) {
        org.joml.Vector3f vector3f;
        double d = target.getX() - shooter.getX();
        double e = target.getZ() - shooter.getZ();
        double f = Math.sqrt(d * d + e * e);
        double g = target.getBodyY(0.3333333333333333) - shooter.getY() + f * 0.2F;
        vector3f = calcVelocity(shooter, new Vec3d(d, g, e), 0);
        return vector3f;
    }

    private static org.joml.Vector3f calcVelocity(Entity shooter, Vec3d direction, float yaw) {
        org.joml.Vector3f vector3f = direction.toVector3f().normalize();
        org.joml.Vector3f vector3f2 = new org.joml.Vector3f(vector3f).cross(new org.joml.Vector3f(0.0F, 1.0F, 0.0F));
        if ((double)vector3f2.lengthSquared() <= 1.0E-7) {
            Vec3d vec3d = shooter.getOppositeRotationVector(1.0F);
            vector3f2 = new org.joml.Vector3f(vector3f).cross(vec3d.toVector3f());
        }

        org.joml.Vector3f vector3f3 = new org.joml.Vector3f(vector3f).rotateAxis((float) (Math.PI / 2), vector3f2.x, vector3f2.y, vector3f2.z);
        return new org.joml.Vector3f(vector3f).rotateAxis(yaw * (float) (Math.PI / 180.0), vector3f3.x, vector3f3.y, vector3f3.z);
    }

    public static Vec3d toRealPos(Entity entity, float dx, float dy, float dz) {
        Vec2f vec2f = entity.getRotationClient();
        if (!(entity instanceof PlayerEntity)) {
            vec2f = new Vec2f(-vec2f.x, -vec2f.y);
        }
        Vec3d vec3d = entity.getPos();
        float f = MathHelper.cos((vec2f.y + 90.0F) * (float) (Math.PI / 180.0));
        float g = MathHelper.sin((vec2f.y + 90.0F) * (float) (Math.PI / 180.0));
        float h = MathHelper.cos(-vec2f.x * (float) (Math.PI / 180.0));
        float i = MathHelper.sin(-vec2f.x * (float) (Math.PI / 180.0));
        float j = MathHelper.cos((-vec2f.x + 90.0F) * (float) (Math.PI / 180.0));
        float k = MathHelper.sin((-vec2f.x + 90.0F) * (float) (Math.PI / 180.0));
        Vec3d vec3d2 = new Vec3d((double) (f * h), (double) i, (double) (g * h));
        Vec3d vec3d3 = new Vec3d((double) (f * j), (double) k, (double) (g * j));
        Vec3d vec3d4 = vec3d2.crossProduct(vec3d3).multiply(-1.0);
        double d = vec3d2.x * dz + vec3d3.x * dy + vec3d4.x * dx;
        double e = vec3d2.y * dz + vec3d3.y * dy + vec3d4.y * dx;
        double l = vec3d2.z * dz + vec3d3.z * dy + vec3d4.z * dx;
        return new Vec3d(vec3d.x + d, vec3d.y + e, vec3d.z + l);
//        ParticleUtils.genParticle(this.getWorld(), (float) this.getX(), (float) this.getY(), (float) this.getZ());

    }
}

package top.superxuqc.mcmod.common;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.util.math.Vec3d;
import org.joml.Vector3f;

public class VelocityUtils {


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
}

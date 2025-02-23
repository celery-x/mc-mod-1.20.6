package top.superxuqc.mcmod.renderer;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;
import top.superxuqc.mcmod.common.particle.BigBoomArg;
import top.superxuqc.mcmod.common.particle.LightArrowArg;
import top.superxuqc.mcmod.common.particle.SpaceCrackArg;
import top.superxuqc.mcmod.common.particle.XianJianArg;
import top.superxuqc.mcmod.entity.LightArrowEntity;
import top.superxuqc.mcmod.entity.NoneEntity;
import top.superxuqc.mcmod.entity.LightArrowEntity;
import top.superxuqc.mcmod.network.payload.HitCheckPayload;
import top.superxuqc.mcmod.particle.BoomParticleEffect;
import top.superxuqc.mcmod.particle.JianQiParticleEffect;
import top.superxuqc.mcmod.particle.PaintParticleEffect;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

public class LightArrowEntityRenderer<T extends PersistentProjectileEntity> extends EntityRenderer<T> {

    public LightArrowEntityRenderer(EntityRendererFactory.Context ctx) {
        super(ctx);
    }

    @Override
    public void render(T entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        if (entity instanceof LightArrowEntity lightArrowEntity) {
            if (lightArrowEntity.isAlive()) {
                //renderBoom(entity, BigBoomArg.ARG, entity.getWorld());
                if (((LightArrowEntity) entity).getBoom()) {
                    //renderBoom(entity, BigBoomArg.ARG, entity.getWorld());
                } else {
                    for (float[] args : LightArrowArg.ARG) {
                        hitCheck(entity, args);
                        addParticle(entity, args, 1);
                    }
                }
            }
        }
    }

    @Override
    public Identifier getTexture(T entity) {
        return SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE;
    }

    private void renderBoom(T entity, float[][] args, World world) {
        Vec3d vec3d = entity.getVelocity().normalize();
        NoneEntity noneEntity = new NoneEntity(world);
        for (int i = 0; i < 10; i++) {
            System.out.println(i);
            Vec3d add = entity.getPos().add(vec3d.multiply(i));
            System.out.println(add);
            noneEntity.setPos(add.x, add.y, add.z);
            for (float[] arg : args) {
                addBoomParticle(noneEntity, arg, 20 * 10);
            }
        }

    }

    private void addParticle(Entity entity, float[] args, int age) {
        Vec3d realPos = toRealPos(entity, args[3], args[4], args[5]);
        if (!generateParticle) {
            entity.getWorld().addParticle(
//                ParticleTypes.END_ROD,
                    //new JianQiParticleEffect(new Vector3f(args[0], args[1], args[2]), 20 * 30),
                    new PaintParticleEffect(new Vector3f(1, 1, 0.55f), age, 0.1f),
                    realPos.x, realPos.y, realPos.z, 0, 0, 0);
        }
    }

    private void addBoomParticle(Entity entity, float[] args, int age) {
        Vec3d realPos = toRealPos(entity, args[3], args[4], args[5]);
        if (!generateParticle) {
            entity.getWorld().addParticle(
//                ParticleTypes.END_ROD,
                    //new JianQiParticleEffect(new Vector3f(args[0], args[1], args[2]), 20 * 30),
                    new BoomParticleEffect(new Vector3f(1, 1, 0.55f), age, 0.5f),
                    realPos.x, realPos.y, realPos.z, 0, 0, 0);
        }
    }


    @NotNull
    private void hitCheck(T entity, float[] args) {
        Vec3d realPos = toRealPos(entity, args[3], args[4], args[5]);
        if (entity.getWorld().isClient && entity instanceof LightArrowEntity) {
            Vec3i passed;
            for (int i = 0; i < 1; i++) {
                passed = ((LightArrowEntity) entity).passed(realPos.x, realPos.y + i, realPos.z);
                if (passed != null) {
                    ClientPlayNetworking.send(new HitCheckPayload(new BlockPos(passed), (int) ((LightArrowEntity) entity).getDamage()));
                }
                passed = ((LightArrowEntity) entity).passed(realPos.x, realPos.y - i, realPos.z);
                if (passed != null) {
                    ClientPlayNetworking.send(new HitCheckPayload(new BlockPos(passed), (int) ((LightArrowEntity) entity).getDamage()));
                }
                passed = ((LightArrowEntity) entity).passed(realPos.x + i, realPos.y, realPos.z);
                if (passed != null) {
                    ClientPlayNetworking.send(new HitCheckPayload(new BlockPos(passed), (int) ((LightArrowEntity) entity).getDamage()));
                }
                passed = ((LightArrowEntity) entity).passed(realPos.x - i, realPos.y, realPos.z);
                if (passed != null) {
                    ClientPlayNetworking.send(new HitCheckPayload(new BlockPos(passed), (int) ((LightArrowEntity) entity).getDamage()));
                }
                passed = ((LightArrowEntity) entity).passed(realPos.x, realPos.y, realPos.z + i);
                if (passed != null) {
                    ClientPlayNetworking.send(new HitCheckPayload(new BlockPos(passed), (int) ((LightArrowEntity) entity).getDamage()));
                }
                passed = ((LightArrowEntity) entity).passed(realPos.x, realPos.y, realPos.z - i);
                if (passed != null) {
                    ClientPlayNetworking.send(new HitCheckPayload(new BlockPos(passed), (int) ((LightArrowEntity) entity).getDamage()));
                }
            }

        }
    }

    private boolean generateParticle = false;

    private Vec3d toRealPos(Entity entity, float dx, float dy, float dz) {
        Vec2f vec2f = entity.getRotationClient();
        vec2f = new Vec2f(-vec2f.x, -vec2f.y);
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

package top.superxuqc.mcmod.renderer;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.FlyingItemEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.thrown.ThrownEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import org.joml.Vector3f;
import top.superxuqc.mcmod.MyModInitializer;
import top.superxuqc.mcmod.common.particle.*;
import top.superxuqc.mcmod.entity.NoneEntity;
import top.superxuqc.mcmod.entity.XianJianEntity;
import top.superxuqc.mcmod.entity.XianJianEntity;
import top.superxuqc.mcmod.keymouse.KeyBindRegister;
import top.superxuqc.mcmod.network.payload.HitCheckPayload;
import top.superxuqc.mcmod.particle.JianQiParticleEffect;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class XianJianEntityRenderer<T extends PersistentProjectileEntity> extends EntityRenderer<T> {
    private static final float MIN_DISTANCE = 12.25F;
    private final ItemRenderer itemRenderer;
    private final float scale;
    private final boolean lit;


    public XianJianEntityRenderer(EntityRendererFactory.Context context) {
        this(context, 10, true);
    }



    public XianJianEntityRenderer(EntityRendererFactory.Context ctx, float scale, boolean lit) {
        super(ctx);
        this.itemRenderer = ctx.getItemRenderer();
        this.scale = scale;
        this.lit = lit;
    }

    private List<Vec3d> entitySpawnPos = new CopyOnWriteArrayList<>();

    @Override
    protected int getBlockLight(T entity, BlockPos pos) {
        return this.lit ? 15 : super.getBlockLight(entity, pos);
    }

    private int age = 0;

    Vector3f center;

    List<NoneEntity> circlePos = new CopyOnWriteArrayList<>();

    @Override
    public void render(T entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        age++;
        if (entity instanceof XianJianEntity xianJian && xianJian.isTianZai()) {
            center = ((XianJianEntity) entity).getCenter();
            int maxAge = xianJian.getMaxAge();
            if (age < maxAge) {
                for (float[] args : XianJianArg.ARG) {
                    addParticle(entity, args);
                }
                NoneEntity noneEntity = new NoneEntity(entity.getWorld());
                Vec3d pos = entity.getPos();
                noneEntity.setPos(pos.x, pos.y, pos.z);
                double dx = pos.x - center.x;
                double dz = pos.z - center.z;
                double v = Math.atan2(dx, dz) / (Math.PI * 2) * 360;
                noneEntity.setPitch((float) v);
                circlePos.add(noneEntity);
            }
        } else {
            for (float[] args : XianJianArg.ARG) {
                addParticle(entity, args);
            }
        }
        if (!circlePos.isEmpty()) {
            for (NoneEntity circlePo : circlePos) {
                //entity.getWorld().createExplosion(circlePo, circlePo.getX(), circlePo.getY(), circlePo.getZ(), 1.0F, World.ExplosionSourceType.TNT);
                for (float[] args : SpaceCrackArg.ARG) {
                    addLieParticle(circlePo, args);
                }
            }
        }
    }

    @Override
    public Identifier getTexture(T entity) {
        return SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE;
    }


    private void addLieParticle(Entity entity, float[] args) {

        Vec3d realPos = toRealPos(entity, args[3], args[4], args[5]);
        entity.getWorld().addParticle(
//                ParticleTypes.END_ROD,
                new JianQiParticleEffect(new Vector3f(args[0], args[1], args[2])), true,
                realPos.x, realPos.y, realPos.z, 0, 0, 0);
    }

    private void addParticle(T entity, float[] args) {
        Vec3d realPos = toRealPos(entity, args[3], args[4], args[5]);
        if (entity.getWorld().isClient && entity instanceof XianJianEntity && !((XianJianEntity) entity).isTianZai()) {
            Vec3i passed;
            for (int i = 0; i < 3; i++) {
                passed = ((XianJianEntity) entity).passed(realPos.x, realPos.y + i, realPos.z);
                if (passed != null) {
                    ClientPlayNetworking.send(new HitCheckPayload(new BlockPos(passed), ((XianJianEntity) entity).getAmount()));
                }
                passed = ((XianJianEntity) entity).passed(realPos.x, realPos.y - i, realPos.z);
                if (passed != null) {
                    ClientPlayNetworking.send(new HitCheckPayload(new BlockPos(passed), ((XianJianEntity) entity).getAmount()));
                }
                passed = ((XianJianEntity) entity).passed(realPos.x + i, realPos.y, realPos.z);
                if (passed != null) {
                    ClientPlayNetworking.send(new HitCheckPayload(new BlockPos(passed), ((XianJianEntity) entity).getAmount()));
                }
                passed = ((XianJianEntity) entity).passed(realPos.x - i, realPos.y, realPos.z);
                if (passed != null) {
                    ClientPlayNetworking.send(new HitCheckPayload(new BlockPos(passed), ((XianJianEntity) entity).getAmount()));
                }
                passed = ((XianJianEntity) entity).passed(realPos.x, realPos.y, realPos.z + i);
                if (passed != null) {
                    ClientPlayNetworking.send(new HitCheckPayload(new BlockPos(passed), ((XianJianEntity) entity).getAmount()));
                }
                passed = ((XianJianEntity) entity).passed(realPos.x, realPos.y, realPos.z- i);
                if (passed != null) {
                    ClientPlayNetworking.send(new HitCheckPayload(new BlockPos(passed), ((XianJianEntity) entity).getAmount()));
                }
            }

        }
        entity.getWorld().addParticle(
//                ParticleTypes.END_ROD,
                new JianQiParticleEffect(new Vector3f(args[0], args[1], args[2])),
                realPos.x, realPos.y, realPos.z, 0, 0, 0);

    }

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
        Vec3d vec3d2 = new Vec3d((double)(f * h), (double)i, (double)(g * h));
        Vec3d vec3d3 = new Vec3d((double)(f * j), (double)k, (double)(g * j));
        Vec3d vec3d4 = vec3d2.crossProduct(vec3d3).multiply(-1.0);
        double d = vec3d2.x * dz + vec3d3.x * dy + vec3d4.x * dx;
        double e = vec3d2.y * dz + vec3d3.y * dy + vec3d4.y * dx;
        double l = vec3d2.z * dz + vec3d3.z * dy + vec3d4.z * dx;
        return new Vec3d(vec3d.x + d, vec3d.y + e, vec3d.z + l);
//        ParticleUtils.genParticle(this.getWorld(), (float) this.getX(), (float) this.getY(), (float) this.getZ());

    }

}

package top.superxuqc.mcmod.renderer;

import net.minecraft.client.particle.EndRodParticle;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;
import net.minecraft.client.render.entity.ProjectileEntityRenderer;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.FlyingItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.*;
import org.joml.Quaternionf;
import top.superxuqc.mcmod.MyModInitializer;
import top.superxuqc.mcmod.common.*;
import top.superxuqc.mcmod.entity.SwordQiEntity;
import top.superxuqc.mcmod.particle.JianQiParticle;
import top.superxuqc.mcmod.particle.JianQiParticleEffect;

public class SwordQiEntityRenderer<T extends Entity & FlyingItemEntity> extends EntityRenderer<T> {
//    public static final Identifier TEXTURE = Identifier.of(MyModInitializer.MOD_ID, "textures/entity/sword_qi.png");
    public static final Identifier TEXTURE = Identifier.of(MyModInitializer.MOD_ID, "textures/entity/sword_qi2.png");
//    public static final Identifier TEXTURE = new Identifier("textures/entity/projectiles/tipped_arrow.png");

    private static final float MIN_DISTANCE = 12.25F;
    private final ItemRenderer itemRenderer;
    private final float scale;
    private final boolean lit;


    public SwordQiEntityRenderer(EntityRendererFactory.Context context) {
        this(context, 10, true);
    }



    public SwordQiEntityRenderer(EntityRendererFactory.Context ctx, float scale, boolean lit) {
        super(ctx);
        this.itemRenderer = ctx.getItemRenderer();
        this.scale = scale;
        this.lit = lit;
    }


    @Override
    protected int getBlockLight(T entity, BlockPos pos) {
        return this.lit ? 15 : super.getBlockLight(entity, pos);
    }

    @Override
    public void render(T entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {

////        if (entity.age == 1) {
////            if (entity instanceof SwordQiEntity) {
////                Quaternionf rotation = this.dispatcher.getRotation();
////                NbtCompound nbtCompound = new NbtCompound();
////                nbtCompound.putFloat("x", rotation.x);
////                nbtCompound.putFloat("y", rotation.y);
////                nbtCompound.putFloat("z", rotation.z);
////                nbtCompound.putFloat("w", rotation.w);
////
////                ((SwordQiEntity) entity).setFace(nbtCompound);
////            }
////        }
//        if (entity.age >= 2 || !(this.dispatcher.camera.getFocusedEntity().squaredDistanceTo(entity) < 12.25)) {
//            matrices.push();
//            matrices.scale(10, 3, 10);
//
//            //matrices.multiply(this.dispatcher.getRotation());
//            //matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180.0F));
//            //环绕
//            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(MathHelper.lerp(tickDelta, entity.prevYaw, entity.getYaw()) - 90.0F));
//            // 俯仰角
//            int per = 1;
//            if (entity.getY() < entity.prevY) {
//                per = -1;
//            }
//            matrices.multiply(RotationAxis.NEGATIVE_Z.rotationDegrees((float) (0 + -1.2 * RendererUtil.caculateAngle(entity.prevX, entity.prevY, entity.prevZ, entity.getX(), entity.getY(), entity.getZ()))));
////            matrices.multiply(RotationAxis.NEGATIVE_Z.rotationDegrees(60));
//
//            // 左右掀翼
//            matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(90.0F + ((SwordQiEntity)entity).getAngle()));
//
////            if (entity instanceof SwordQiEntity) {
////                NbtCompound nbtCompound = ((SwordQiEntity) entity).getFace();
////
////                Quaternionf quaternionf = new Quaternionf(nbtCompound.getFloat("x"),
////                        nbtCompound.getFloat("y"),
////                        nbtCompound.getFloat("z"),
////                        nbtCompound.getFloat("w"));
////                matrices.multiply(quaternionf);
////            }
//
//
//            this.itemRenderer
//                    .renderItem(
//                            entity.getStack(), ModelTransformationMode.NONE, light, OverlayTexture.DEFAULT_UV, matrices, vertexConsumers, entity.getWorld(), entity.getId()
//                    );
//            matrices.pop();
//            super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light);
//        }
        addParticle(entity);
    }

    private void addParticle(T entity) {
//        this.getWorld().addParticle(new DustParticleEffect(Vec3d.unpackRgb(11154228).toVector3f(), 0.5F), this.getX(), this.getY(), this.getZ() , 0.0, 0.0, 0.0);
//
//        if (argsParticle1 != null) {
//            addParticle(argsParticle1[1]);
//            addParticle(argsParticle1[2]);
//            addParticle(argsParticle1[3]);
//            addParticle(argsParticle1[4]);
//        }
        if (entity instanceof SwordQiEntity) {
            if(((SwordQiEntity) entity).getSize()) {
                for (float[] args : JianqiArgs0.ARG) {
                    addParticle(entity, args);
                }


                for (float[] args : JianqiArgs1.ARG) {
                    addParticle(entity, args);
                }


                for (float[] args : JianqiArgs2.ARG) {
                    addParticle(entity, args);
                }
                for (float[] args : JianqiArgs3.ARG) {
                    addParticle(entity, args);
                }
            }else {
                for (float[] args : JianqiArgMini.ARG) {
                    addParticle(entity, args);
                }
            }

        }
    }

    private void addParticle(T entity, float[] args) {
        Vec3d realPos = toRealPos(entity, args[3], args[4], args[5]);
        entity.getWorld().addParticle(
//                ParticleTypes.END_ROD,
                new JianQiParticleEffect(new Vector3f(args[0], args[1], args[2])),
                realPos.x, realPos.y, realPos.z, 0, 0, 0);
    }

    private Vec3d toRealPos(T entity, float dx, float dy, float dz) {
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

    @Override
    public Identifier getTexture(Entity entity) {
        return SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE;
    }
}

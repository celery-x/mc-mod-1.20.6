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
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import org.joml.Quaternionf;
import top.superxuqc.mcmod.MyModInitializer;
import top.superxuqc.mcmod.common.RendererUtil;
import top.superxuqc.mcmod.entity.SwordQiEntity;
import top.superxuqc.mcmod.particle.JianQiParticle;

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
    }

    @Override
    public Identifier getTexture(Entity entity) {
        return SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE;
    }
}

package top.superxuqc.mcmod.renderer;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;
import top.superxuqc.mcmod.MyModInitializer;
import top.superxuqc.mcmod.entity.ChuanXinZhouEntity;
import top.superxuqc.mcmod.model.ChuanXinZhouModel;
import top.superxuqc.mcmod.register.ModModelLayers;

public class NoneViewEntityRenderer extends EntityRenderer {

    public static final Identifier TEXTURE = Identifier.of(MyModInitializer.MOD_ID, "textures/entity/chuan_xin_zhou.png");

    public NoneViewEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
    }

    @Override
    public Identifier getTexture(Entity entity) {
        return TEXTURE;
    }

    @Override
    public void render(Entity entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        //super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light);
    }
}

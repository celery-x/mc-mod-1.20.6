package top.superxuqc.mcmod.renderer;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import top.superxuqc.mcmod.MyModInitializer;
import top.superxuqc.mcmod.entity.ChuanXinZhouEntity;
import top.superxuqc.mcmod.model.ChuanXinZhouModel;
import top.superxuqc.mcmod.register.ModModelLayers;

public class ChuanXinZhouRenderer extends MobEntityRenderer<ChuanXinZhouEntity, ChuanXinZhouModel> {

    public static final Identifier TEXTURE = Identifier.of(MyModInitializer.MOD_ID, "textures/entity/chuan_xin_zhou.png");

    public ChuanXinZhouRenderer(EntityRendererFactory.Context context) {
        super(context, new ChuanXinZhouModel(context.getPart(ModModelLayers.CHUAN_XIN_ZHOU)), 0.5f);
    }

    @Override
    public Identifier getTexture(ChuanXinZhouEntity entity) {
        return TEXTURE;
    }

    @Override
    public void render(ChuanXinZhouEntity mobEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        matrixStack.scale(0.05f, 0.05f, 0.05f);

        super.render(mobEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }

    @Override
    protected boolean isVisible(ChuanXinZhouEntity entity) {
        return true;
    }
}

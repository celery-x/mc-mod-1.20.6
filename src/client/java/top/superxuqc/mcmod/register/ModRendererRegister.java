package top.superxuqc.mcmod.register;

import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;
import top.superxuqc.mcmod.model.ChuanXinZhouModel;
import top.superxuqc.mcmod.renderer.*;

public class ModRendererRegister {


    public static void init() {
        modelInit();
        EntityRendererRegistry.register(ModEntryTypes.HUCHENG_TNT,  (context) -> new HuChengTnTRenderer(ModBlocksRegister.HUCHENG_TNT_BLOCK, context));
        EntityRendererRegistry.register(ModEntryTypes.SWORD_QI_TYPE, SwordQiEntityRenderer::new);
        EntityRendererRegistry.register(ModEntryTypes.ARROW_TNT, (context) -> new TNTArrowEntityRenderer(context, 0));
        EntityRendererRegistry.register(ModEntryTypes.TNT_ARROW, (context) -> new TNTArrowEntityRenderer(context, 1));
        EntityRendererRegistry.register(ModEntryTypes.PLAYER_SELF, (context) -> new PlayerSelfEntityRenderer(context, false));
        EntityRendererRegistry.register(ModEntryTypes.CHUAN_XIN_ZHOU_TYPR, ChuanXinZhouRenderer::new);
        EntityRendererRegistry.register(ModEntryTypes.NoneViewEntity, NoneViewEntityRenderer::new);
        EntityRendererRegistry.register(ModEntryTypes.ATTRACTION_ENTITY_TYPE, (c) -> new FlyingItemEntityRenderer<>(c, 3, false));
        EntityRendererRegistry.register(ModEntryTypes.XIAN_JIAN_TYPE, XianJianEntityRenderer::new);
        EntityRendererRegistry.register(ModEntryTypes.LIGHT_ARROW, LightArrowEntityRenderer::new);
//        EntityRendererRegistry.register(ModEntryTypes.XIAN_JIAN_TYPE, FlyingItemEntityRenderer::new);
    }

    public static void modelInit() {
        EntityModelLayerRegistry.registerModelLayer(ModModelLayers.CHUAN_XIN_ZHOU, ChuanXinZhouModel::getTexturedModelData);
    }

}

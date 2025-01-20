package top.superxuqc.mcmod.renderer;

import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.ProjectileEntityRenderer;
import net.minecraft.entity.projectile.SpectralArrowEntity;
import net.minecraft.util.Identifier;
import top.superxuqc.mcmod.MyModInitializer;
import top.superxuqc.mcmod.entity.ModArrowEntity;

public class TNTArrowEntityRenderer extends ProjectileEntityRenderer<ModArrowEntity> {
    public static final Identifier TEXTURE = new Identifier(MyModInitializer.MOD_ID,"textures/entity/projectiles/arrow_tnt.png");

    public TNTArrowEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
    }

    @Override
    public Identifier getTexture(ModArrowEntity entity) {
        return TEXTURE;
    }

}

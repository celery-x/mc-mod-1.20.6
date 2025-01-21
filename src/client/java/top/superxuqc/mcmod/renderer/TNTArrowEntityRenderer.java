package top.superxuqc.mcmod.renderer;

import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.ProjectileEntityRenderer;
import net.minecraft.entity.projectile.SpectralArrowEntity;
import net.minecraft.util.Identifier;
import top.superxuqc.mcmod.MyModInitializer;
import top.superxuqc.mcmod.entity.ModArrowEntity;

public class TNTArrowEntityRenderer extends ProjectileEntityRenderer<ModArrowEntity> {
    public static final Identifier TEXTURE = new Identifier(MyModInitializer.MOD_ID,"textures/entity/projectiles/arrow_tnt.png");

    public static final Identifier TEXTURE_1 = new Identifier(MyModInitializer.MOD_ID, "textures/entity/projectiles/tnt_arrow.png");

    private int flag = 0;

    public TNTArrowEntityRenderer(EntityRendererFactory.Context context, int flag) {
        super(context);
        this.flag = flag;
    }

    @Override
    public Identifier getTexture(ModArrowEntity entity) {
        switch (flag) {
            case 0:
                return TEXTURE;
            case 1:
                return TEXTURE_1;
            case 2:
            case 3:
            case 4:
            case 5:
            default:
                return TEXTURE;
        }
    }

}

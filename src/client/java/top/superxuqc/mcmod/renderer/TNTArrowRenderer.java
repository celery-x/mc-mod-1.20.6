package top.superxuqc.mcmod.renderer;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.ProjectileEntityRenderer;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class TNTArrowRenderer extends ProjectileEntityRenderer<ArrowEntity> {
    public static final Identifier TEXTURE = new Identifier("textures/entity/projectiles/arrow_tnt.png");
    public static final Identifier TIPPED_TEXTURE = new Identifier("textures/entity/projectiles/tipped_arrow.png");

    public TNTArrowRenderer(EntityRendererFactory.Context context) {
        super(context);
    }

    public Identifier getTexture(ArrowEntity arrowEntity) {
        return TEXTURE;
    }
}

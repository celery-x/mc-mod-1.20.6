package top.superxuqc.mcmod.register;

import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.Identifier;
import top.superxuqc.mcmod.MyModInitializer;

public class ModModelLayers {
    public static final EntityModelLayer CHUAN_XIN_ZHOU =
            new EntityModelLayer(Identifier.of(MyModInitializer.MOD_ID, "chuan_xin_zhou"), "main");
}

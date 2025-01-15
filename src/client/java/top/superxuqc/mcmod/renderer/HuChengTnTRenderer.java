package top.superxuqc.mcmod.renderer;

import net.minecraft.block.Block;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.TntEntityRenderer;

public class HuChengTnTRenderer extends TntEntityRenderer {

    public HuChengTnTRenderer(Block block, EntityRendererFactory.Context context) {
        super(context);
        ((RenderImpl)this).setBlock(block);
    }

}

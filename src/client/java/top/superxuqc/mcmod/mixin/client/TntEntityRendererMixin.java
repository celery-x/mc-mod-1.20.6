package top.superxuqc.mcmod.mixin.client;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.entity.TntEntityRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import top.superxuqc.mcmod.renderer.RenderImpl;

@Mixin(TntEntityRenderer.class)
public class TntEntityRendererMixin implements RenderImpl {

	@Unique
	private Block block = null;

	@ModifyArg(method = "render(Lnet/minecraft/entity/TntEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/client/render/entity/TntMinecartEntityRenderer;renderFlashingBlock(Lnet/minecraft/client/render/block/BlockRenderManager;Lnet/minecraft/block/BlockState;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;IZ)V"
			),
			index = 1)
	private BlockState replaceTntTexture(BlockState blockState) {
		return block != null ? block.getDefaultState() : blockState;
		//return ModBlocksRegister.HUCHENG_TNT_BLOCK.getDefaultState();
	}

	@Override
	public void setBlock(Block block) {
		this.block = block;
	}
}
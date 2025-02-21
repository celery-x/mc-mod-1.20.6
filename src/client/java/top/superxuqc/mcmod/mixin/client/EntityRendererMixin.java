package top.superxuqc.mcmod.mixin.client;

import me.zziger.obsoverlay.OverlayRenderer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAttachmentType;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.lang.reflect.Constructor;

@Mixin(EntityRenderer.class)
public class EntityRendererMixin {

    @Shadow
    EntityRenderDispatcher dispatcher;

    @Shadow
    TextRenderer textRenderer;

    @Inject(method = "Lnet/minecraft/client/render/entity/EntityRenderer;renderLabelIfPresent(Lnet/minecraft/entity/Entity;Lnet/minecraft/text/Text;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;IF)V",
            at = @At("HEAD"), cancellable = true)
    public void renderLabelIfPresentMixin(Entity entity, Text text, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, float tickDelta, CallbackInfo ci) {
        double d = this.dispatcher.getSquaredDistanceToCamera(entity);
        if (!(d > 4096.0)) {
            Vec3d vec3d = entity.getAttachments().getPointNullable(EntityAttachmentType.NAME_TAG, 0, entity.getYaw(tickDelta));
            if (vec3d != null) {
                boolean bl = !entity.isSneaky();
                int i = "deadmau5".equals(text.getString()) ? -10 : 0;
                matrices.push();
                matrices.translate(vec3d.x, vec3d.y + 0.5, vec3d.z);
                matrices.multiply(this.dispatcher.getRotation());
                matrices.scale(-0.025F, -0.025F, 0.025F);
                Matrix4f matrix4f = matrices.peek().getPositionMatrix();
                float f = MinecraftClient.getInstance().options.getTextBackgroundOpacity(0.25F);
                int j = (int) (f * 255.0F) << 24;
                TextRenderer textRenderer = this.textRenderer;
                float g = (float) (-textRenderer.getWidth(text) / 2);
                try {
                    OverlayRenderer.beginDraw();
                    Constructor<DrawContext> declaredConstructor = DrawContext.class.getDeclaredConstructor(MinecraftClient.class, MatrixStack.class, VertexConsumerProvider.Immediate.class);
                    declaredConstructor.setAccessible(true);
                    DrawContext drawContext = declaredConstructor.newInstance(MinecraftClient.getInstance(), matrices, (VertexConsumerProvider.Immediate) vertexConsumers);
                    drawContext.drawText(textRenderer, text, (int) g, i, Colors.WHITE, false);
                    OverlayRenderer.endDraw();
                } catch (Exception e) {
                    System.out.println(e.getCause());
                }
                textRenderer.draw(
                        text, g, (float) i, 553648127, false, matrix4f, vertexConsumers, bl ? TextRenderer.TextLayerType.SEE_THROUGH : TextRenderer.TextLayerType.NORMAL, j, light
                );
//                if (bl) {
//                    textRenderer.draw(text, g, (float)i, Colors.WHITE, false, matrix4f, vertexConsumers, TextRenderer.TextLayerType.NORMAL, 0, light);
//                }

                matrices.pop();
            }
        }
        ci.cancel();
    }
}

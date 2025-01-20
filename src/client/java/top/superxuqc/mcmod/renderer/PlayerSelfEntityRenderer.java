package top.superxuqc.mcmod.renderer;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.feature.*;
import net.minecraft.client.render.entity.model.ArmorEntityModel;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.DefaultSkinHelper;
import net.minecraft.client.util.SkinTextures;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.Angerable;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.Identifier;
import top.superxuqc.mcmod.entity.PlayerSelfEntity;

@Environment(EnvType.CLIENT)
public class PlayerSelfEntityRenderer extends MobEntityRenderer<PlayerSelfEntity, PlayerEntityModel<PlayerSelfEntity>>  {

    private PlayerListEntry playerListEntry;

    public PlayerSelfEntityRenderer(EntityRendererFactory.Context ctx, boolean slim) {
        super(ctx, new PlayerEntityModel(ctx.getPart(slim ? EntityModelLayers.PLAYER_SLIM : EntityModelLayers.PLAYER), slim), 0.5F);
        this.addFeature(
                new ArmorFeatureRenderer<>(
                        this,
                        new ArmorEntityModel(ctx.getPart(slim ? EntityModelLayers.PLAYER_SLIM_INNER_ARMOR : EntityModelLayers.PLAYER_INNER_ARMOR)),
                        new ArmorEntityModel(ctx.getPart(slim ? EntityModelLayers.PLAYER_SLIM_OUTER_ARMOR : EntityModelLayers.PLAYER_OUTER_ARMOR)),
                        ctx.getModelManager()
                )
        );
        this.addFeature(new PlayerHeldItemFeatureRenderer(this, ctx.getHeldItemRenderer()));
        this.addFeature(new StuckArrowsFeatureRenderer(ctx, this));
        // this.addFeature(new Deadmau5FeatureRenderer(this));
        // this.addFeature(new CapeFeatureRenderer(this));
        this.addFeature(new HeadFeatureRenderer(this, ctx.getModelLoader(), ctx.getHeldItemRenderer()));
        this.addFeature(new ElytraFeatureRenderer(this, ctx.getModelLoader()));
//        this.addFeature(new ShoulderParrotFeatureRenderer<>(this, ctx.getModelLoader()));
        this.addFeature(new TridentRiptideFeatureRenderer(this, ctx.getModelLoader()));
        this.addFeature(new StuckStingersFeatureRenderer(this));
    }

    @Override
    public void render(PlayerSelfEntity mobEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        Integer tickTime = mobEntity.getTickTime();
        tickTime++;
        mobEntity.setTickTime(tickTime);
        super.render(mobEntity, f, g, matrixStack, vertexConsumerProvider, i);
        if (tickTime < 4) {
            mobEntity.getWorld().addParticle(
                    ParticleTypes.EXPLOSION,
                    mobEntity.getX(),
                    mobEntity.getY(),
                    mobEntity.getZ(),
                    0,
                    0,
                    0
            );
        }
    }

    @Override
    public Identifier getTexture(PlayerSelfEntity entity) {
        if (this.playerListEntry == null) {
            this.playerListEntry = MinecraftClient.getInstance().getNetworkHandler().getPlayerListEntry(entity.getOwnerUuid());
        }
        PlayerListEntry playerListEntry = this.playerListEntry;
        SkinTextures skinTextures = playerListEntry == null ? DefaultSkinHelper.getSkinTextures(entity.getUuid()) : playerListEntry.getSkinTextures();
        return skinTextures.texture();
    }
}

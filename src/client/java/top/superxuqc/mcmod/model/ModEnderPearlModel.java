package top.superxuqc.mcmod.model;

import net.fabricmc.fabric.api.renderer.v1.Renderer;
import net.fabricmc.fabric.api.renderer.v1.RendererAccess;
import net.fabricmc.fabric.api.renderer.v1.mesh.MeshBuilder;
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter;
import net.fabricmc.fabric.api.renderer.v1.model.FabricBakedModel;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.model.*;
import net.minecraft.client.render.model.json.ModelElement;
import net.minecraft.client.render.model.json.ModelElementFace;
import net.minecraft.client.render.model.json.ModelOverrideList;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;

public class ModEnderPearlModel implements UnbakedModel, BakedModel, FabricBakedModel {

    SpriteIdentifier sp = new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier("item/lone"));

    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction face, Random random) {
        return null;
    }

    @Override
    public boolean useAmbientOcclusion() {
        return false;
    }

    @Override
    public boolean hasDepth() {
        return false;
    }

    @Override
    public boolean isSideLit() {
        return false;
    }

    @Override
    public boolean isBuiltin() {
        return false;
    }

    @Override
    public Sprite getParticleSprite() {
        return null;
    }

    @Override
    public ModelTransformation getTransformation() {
        return null;
    }

    @Override
    public ModelOverrideList getOverrides() {
        return null;
    }

    @Override
    public Collection<Identifier> getModelDependencies() {
        return null;
    }

    @Override
    public void setParents(Function<Identifier, UnbakedModel> modelLoader) {

    }

    @Nullable
    @Override
    public BakedModel bake(Baker baker, Function<SpriteIdentifier, Sprite> textureGetter, ModelBakeSettings rotationContainer, Identifier modelId) {
        Sprite apply = textureGetter.apply(sp);
        Renderer renderer = RendererAccess.INSTANCE.getRenderer();
        //MeshBuilder builder = renderer.meshBuilder();

//        BasicBakedModel.Builder builder = new BasicBakedModel.Builder(this, ModelOverrideList.EMPTY, false).setParticle(sp);
//
//        for (ModelElement modelElement : this.getElements()) {
//            for (Direction direction : modelElement.faces.keySet()) {
//                ModelElementFace modelElementFace = (ModelElementFace)modelElement.faces.get(direction);
//                Sprite sprite2 = (Sprite)textureGetter.apply(this.resolveSprite(modelElementFace.textureId));
//                if (modelElementFace.cullFace == null) {
//                    builder.addQuad(createQuad(modelElement, modelElementFace, sprite2, direction, settings, id));
//                } else {
//                    builder.addQuad(
//                            Direction.transform(settings.getRotation().getMatrix(), modelElementFace.cullFace),
//                            createQuad(modelElement, modelElementFace, sprite2, direction, settings, id)
//                    );
//                }
//            }
//        }
//
//        return builder.build()


        return null;
    }
}

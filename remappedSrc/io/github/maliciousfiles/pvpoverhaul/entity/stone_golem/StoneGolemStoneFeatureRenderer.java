package io.github.maliciousfiles.pvpoverhaul.entity.stone_golem;

import io.github.maliciousfiles.pvpoverhaul.entity.EntityRendererInit;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModelLoader;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.render.model.json.ModelOverrideList;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

public class StoneGolemStoneFeatureRenderer extends FeatureRenderer<StoneGolemEntity, StoneGolemEntityModel<StoneGolemEntity>> {

    private final StoneGolemEntityModel<StoneGolemEntity> model;
    
    public StoneGolemStoneFeatureRenderer(FeatureRendererContext<StoneGolemEntity, StoneGolemEntityModel<StoneGolemEntity>> context, EntityModelLoader modelLoader) {
        super(context);
        
        this.model = new StoneGolemEntityModel<>(modelLoader.getModelPart(EntityRendererInit.STONE_GOLEM_STONE_LAYER));
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, StoneGolemEntity entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
        getContextModel().copyStateTo(model);
        model.animateModel(entity, limbAngle, limbDistance, tickDelta);
        model.setAngles(entity, limbAngle, limbDistance, entity.age, headYaw, headPitch);

        boolean showBody = !entity.isInvisible();
        boolean translucent = !showBody && !entity.isInvisibleTo(MinecraftClient.getInstance().player);
        boolean showOutline = MinecraftClient.getInstance().hasOutline(entity);

        Identifier id = SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE;
        RenderLayer renderLayer = translucent ? RenderLayer.getItemEntityTranslucentCull(id) : showBody ? getContextModel().getLayer(id) : showOutline ? RenderLayer.getOutline(id) : null;
        VertexConsumer consumer = vertexConsumers.getBuffer(renderLayer);

        this.model.renderStone(matrices, consumer, light, LivingEntityRenderer.getOverlay(entity, 0), 1, 1, 1, translucent ? 0.15f : 1);
    }

    public static void renderStoneBlock(List<Direction> missing, MatrixStack.Entry entry, VertexConsumer vertexConsumer, float red, float green, float blue, int light, int overlay) {
        BlockRenderManager manager = MinecraftClient.getInstance().getBlockRenderManager();

        BlockState state = Blocks.STONE.getDefaultState();
        BakedModel bakedModel = manager.getModel(state);

        manager.getModelRenderer().render(entry, vertexConsumer, state, new BakedModelWithSidesMissing(missing, bakedModel), red, green, blue, light, overlay);
    }

    public static class BakedModelWithSidesMissing implements BakedModel {

        private final List<Direction> missing;
        private final BakedModel inner;

        public BakedModelWithSidesMissing(List<Direction> missing, BakedModel inner) {
            this.missing = missing;
            this.inner = inner;
        }

        @Override
        public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction face, Random random) {
            if (face == null || !missing.contains(face)) return inner.getQuads(state, face, random);
            else return Collections.emptyList();
        }

        @Override
        public boolean useAmbientOcclusion() {
            return inner.useAmbientOcclusion();
        }

        @Override
        public boolean hasDepth() {
            return inner.hasDepth();
        }

        @Override
        public boolean isSideLit() {
            return inner.isSideLit();
        }

        @Override
        public boolean isBuiltin() {
            return inner.isBuiltin();
        }

        @Override
        public Sprite getParticleSprite() {
            return inner.getParticleSprite();
        }

        @Override
        public ModelTransformation getTransformation() {
            return inner.getTransformation();
        }

        @Override
        public ModelOverrideList getOverrides() {
            return inner.getOverrides();
        }
    }
}

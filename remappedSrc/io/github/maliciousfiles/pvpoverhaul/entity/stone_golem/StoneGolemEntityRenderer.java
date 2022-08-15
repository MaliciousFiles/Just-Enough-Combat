package io.github.maliciousfiles.pvpoverhaul.entity.stone_golem;

import io.github.maliciousfiles.pvpoverhaul.PvPOverhaul;
import io.github.maliciousfiles.pvpoverhaul.entity.EntityRendererInit;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.render.model.json.ModelOverrideList;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

public class StoneGolemEntityRenderer extends MobEntityRenderer<StoneGolemEntity, StoneGolemEntityModel<StoneGolemEntity>> {

    public StoneGolemEntityRenderer(EntityRendererFactory.Context context) {
        super(context, new StoneGolemEntityModel<>(context.getPart(EntityRendererInit.STONE_GOLEM_LAYER)), 1.5f); // shadow size of 0.5
        this.addFeature(new StoneGolemStoneFeatureRenderer(this, context.getModelLoader()));
    }

    @Override
    public Identifier getTexture(StoneGolemEntity entity) {
        return PvPOverhaul.identifier("textures/entity/stone_golem/stone_golem.png");
    }
}
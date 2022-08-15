package io.github.maliciousfiles.pvpoverhaul.entity;

import io.github.maliciousfiles.pvpoverhaul.PvPOverhaul;
import io.github.maliciousfiles.pvpoverhaul.entity.critter.CritterEntityModel;
import io.github.maliciousfiles.pvpoverhaul.entity.critter.CritterEntityRenderer;
import io.github.maliciousfiles.pvpoverhaul.entity.shuriken.ShurikenEntityRenderer;
import io.github.maliciousfiles.pvpoverhaul.entity.stone_golem.StoneGolemEntityModel;
import io.github.maliciousfiles.pvpoverhaul.entity.stone_golem.StoneGolemEntityRenderer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.entity.model.EntityModelLayer;

public class EntityRendererInit {

    public static final EntityModelLayer STONE_GOLEM_LAYER = new EntityModelLayer(PvPOverhaul.identifier("stone_golem"), "main");
    public static final EntityModelLayer STONE_GOLEM_STONE_LAYER = new EntityModelLayer(PvPOverhaul.identifier("stone_golem"), "stone");
    public static final EntityModelLayer CRITTER_LAYER = new EntityModelLayer(PvPOverhaul.identifier("critter"), "main");

    public static void clientInit() {
        EntityRendererRegistry.register(EntityInit.STONE_GOLEM, StoneGolemEntityRenderer::new);
        EntityModelLayerRegistry.registerModelLayer(STONE_GOLEM_LAYER, () -> StoneGolemEntityModel.getTexturedModelData(false));
        EntityModelLayerRegistry.registerModelLayer(STONE_GOLEM_STONE_LAYER, () -> StoneGolemEntityModel.getTexturedModelData(true));

        EntityRendererRegistry.register(EntityInit.CRITTER, CritterEntityRenderer::new);
        EntityModelLayerRegistry.registerModelLayer(CRITTER_LAYER, CritterEntityModel::getTexturedModelData);

        EntityRendererRegistry.register(EntityInit.SHURIKEN, ShurikenEntityRenderer::new);
    }
}


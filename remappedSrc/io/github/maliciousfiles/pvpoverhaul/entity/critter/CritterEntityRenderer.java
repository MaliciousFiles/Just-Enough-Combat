package io.github.maliciousfiles.pvpoverhaul.entity.critter;

import io.github.maliciousfiles.pvpoverhaul.PvPOverhaul;
import io.github.maliciousfiles.pvpoverhaul.entity.EntityRendererInit;
import io.github.maliciousfiles.pvpoverhaul.entity.stone_golem.StoneGolemEntity;
import io.github.maliciousfiles.pvpoverhaul.entity.stone_golem.StoneGolemEntityModel;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.util.Identifier;

public class CritterEntityRenderer extends MobEntityRenderer<CritterEntity, CritterEntityModel<CritterEntity>> {

    public CritterEntityRenderer(EntityRendererFactory.Context context) {
        super(context, new CritterEntityModel(context.getPart(EntityRendererInit.CRITTER_LAYER)),  0.25f); // shadow size of 0.25
    }

    @Override
    public Identifier getTexture(CritterEntity entity) {
        return PvPOverhaul.identifier("textures/entity/critter/critter.png");
    }
}

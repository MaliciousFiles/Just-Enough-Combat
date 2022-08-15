package io.github.maliciousfiles.pvpoverhaul.entity.critter;

import ModelPart;
import TexturedModelData;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.util.math.MathHelper;

public class CritterEntityModel<T extends CritterEntity> extends SinglePartEntityModel<T> {

    private final ModelPart root;
    private final ModelPart body;
    private final ModelPart rightFrontLeg;
    private final ModelPart rightMiddleLeg;
    private final ModelPart rightHindLeg;
    private final ModelPart leftFrontLeg;
    private final ModelPart leftMiddleLeg;
    private final ModelPart leftHindLeg;

    public CritterEntityModel(ModelPart root) {
        this.root = root;

        this.body = root.getChild("body");

        this.rightFrontLeg = root.getChild("right_front_leg");
        this.rightMiddleLeg = root.getChild("right_middle_leg");
        this.rightHindLeg = root.getChild("right_hind_leg");
        this.leftFrontLeg = root.getChild("left_front_leg");
        this.leftMiddleLeg = root.getChild("left_middle_leg");
        this.leftHindLeg = root.getChild("left_hind_leg");
    }

    @SuppressWarnings("unused")
    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();

        ModelPartData body = modelPartData.addChild("body", ModelPartBuilder.create().uv(1, 0).cuboid(-3.0F, 0.0F, -2.5F, 6.0F, 4.0F, 5.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 18.0F, 0.0F));

        ModelPartBuilder legBuilder = ModelPartBuilder.create().uv(0, 0).cuboid(-1.0F, 2.0F, -0.5F, 2.0F, 3.0F, 1.0F, new Dilation(0.0F));
        ModelPartData left_front_leg = modelPartData.addChild("left_front_leg", legBuilder, ModelTransform.of(2.5F, 19.0F, 2.5F, 0.1309F, 0.0F, 0.0F));
        ModelPartData left_middle_leg = modelPartData.addChild("left_middle_leg", legBuilder, ModelTransform.of(0.0F, 19.0F, 2.5F, 0.1309F, 0.0F, 0.0F));
        ModelPartData left_hind_leg = modelPartData.addChild("left_hind_leg", legBuilder, ModelTransform.of(-2.5F, 19.0F, 2.5F, 0.1309F, 0.0F, 0.0F));
        ModelPartData right_front_leg = modelPartData.addChild("right_front_leg", legBuilder, ModelTransform.of(2.5F, 19.0F, -2.5F, -0.1309F, 0.0F, 0.0F));
        ModelPartData right_middle_leg = modelPartData.addChild("right_middle_leg", legBuilder, ModelTransform.of(0.0F, 19.0F, -2.5F, -0.1309F, 0.0F, 0.0F));
        ModelPartData right_hind_leg = modelPartData.addChild("right_hind_leg", legBuilder, ModelTransform.of(-2.5F, 19.0F, -2.5F, -0.1309F, 0.0F, 0.0F));
        
        return TexturedModelData.of(modelData, 32, 16);
    }

    @Override
    public void setAngles(CritterEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float headYaw, float headPitch) {
        this.getPart().traverse().forEach(ModelPart::resetTransform);


    }

    @Override
    public ModelPart getPart() {
        return root;
    }
}

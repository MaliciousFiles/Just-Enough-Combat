package io.github.maliciousfiles.pvpoverhaul.entity.stone_golem;

import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.animation.Animation;
import net.minecraft.client.render.entity.animation.AnimationHelper;
import net.minecraft.client.render.entity.animation.Keyframe;
import net.minecraft.client.render.entity.animation.Transformation;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;

import java.util.ArrayList;
import java.util.List;

public class StoneGolemEntityModel<T extends StoneGolemEntity> extends SinglePartEntityModel<T> {

    public static final Animation SLEEP_ANIMATION = Animation.Builder.create(1.5f).addBoneAnimation("head", new Transformation(Transformation.Targets.TRANSLATE, new Keyframe(0f, AnimationHelper.method_41823(0f, 0f, 0f), Transformation.Interpolations.field_37884), new Keyframe(0.25f, AnimationHelper.method_41823(0f, -8.8f, 1.6f), Transformation.Interpolations.field_37885), new Keyframe(0.5416666666666666f, AnimationHelper.method_41823(0f, -3.3200000000000003f, -0.5299999999999998f), Transformation.Interpolations.field_37885), new Keyframe(0.7916666666666666f, AnimationHelper.method_41823(0f, -14.56f, 4.56f), Transformation.Interpolations.field_37885), new Keyframe(0.9583333333333334f, AnimationHelper.method_41823(0f, -12f, 8f), Transformation.Interpolations.field_37885), new Keyframe(1.0833333333333333f, AnimationHelper.method_41823(0f, -14f, 8f), Transformation.Interpolations.field_37885))).addBoneAnimation("head", new Transformation(Transformation.Targets.ROTATE, new Keyframe(0f, AnimationHelper.method_41829(0f, 0f, 0f), Transformation.Interpolations.field_37884), new Keyframe(0.125f, AnimationHelper.method_41829(12.5f, 0f, 0f), Transformation.Interpolations.field_37885), new Keyframe(0.3333333333333333f, AnimationHelper.method_41829(-3.0360708605440414f, -14.1145f, 0.47543099935788624f), Transformation.Interpolations.field_37885), new Keyframe(0.5416666666666666f, AnimationHelper.method_41829(-7.761400536779547f, 14.8687f, 2.0030683550803587f), Transformation.Interpolations.field_37885), new Keyframe(0.75f, AnimationHelper.method_41829(-6.450539933614327f, -6.590747704226214f, 3.725789977837376f), Transformation.Interpolations.field_37885), new Keyframe(0.875f, AnimationHelper.method_41829(6.369368102468907f, 3.4814343873620235f, 2.253805822684044f), Transformation.Interpolations.field_37885), new Keyframe(1.25f, AnimationHelper.method_41829(0f, 0f, 0f), Transformation.Interpolations.field_37885))).addBoneAnimation("torso", new Transformation(Transformation.Targets.TRANSLATE, new Keyframe(0f, AnimationHelper.method_41823(0f, 0f, 0f), Transformation.Interpolations.field_37884), new Keyframe(0.2916666666666667f, AnimationHelper.method_41823(0f, -2.9f, 0f), Transformation.Interpolations.field_37885), new Keyframe(0.4166666666666667f, AnimationHelper.method_41823(0f, -0.2699999999999996f, 0f), Transformation.Interpolations.field_37885), new Keyframe(0.7916666666666666f, AnimationHelper.method_41823(0f, -8f, 0f), Transformation.Interpolations.field_37885), new Keyframe(0.9166666666666666f, AnimationHelper.method_41823(0f, -4.869999999999999f, 0f), Transformation.Interpolations.field_37885), new Keyframe(1.0416666666666667f, AnimationHelper.method_41823(0f, -8f, 0f), Transformation.Interpolations.field_37885), new Keyframe(1.125f, AnimationHelper.method_41823(0f, -6.949999999999999f, 0f), Transformation.Interpolations.field_37885), new Keyframe(1.2083333333333333f, AnimationHelper.method_41823(0f, -8f, 0f), Transformation.Interpolations.field_37885))).addBoneAnimation("torso", new Transformation(Transformation.Targets.ROTATE, new Keyframe(0f, AnimationHelper.method_41829(0f, 0f, 0f), Transformation.Interpolations.field_37884), new Keyframe(0.16666666666666666f, AnimationHelper.method_41829(0f, 0f, 0f), Transformation.Interpolations.field_37884), new Keyframe(0.375f, AnimationHelper.method_41829(5f, 0f, 5f), Transformation.Interpolations.field_37885), new Keyframe(0.5416666666666666f, AnimationHelper.method_41829(-3f, 0f, -3f), Transformation.Interpolations.field_37885), new Keyframe(0.7083333333333334f, AnimationHelper.method_41829(0f, 0f, 0f), Transformation.Interpolations.field_37885), new Keyframe(0.8333333333333334f, AnimationHelper.method_41829(0f, 0f, 0f), Transformation.Interpolations.field_37884), new Keyframe(1.0833333333333333f, AnimationHelper.method_41829(0f, 0f, 0f), Transformation.Interpolations.field_37884))).addBoneAnimation("ridge", new Transformation(Transformation.Targets.TRANSLATE, new Keyframe(0f, AnimationHelper.method_41823(0f, 0f, 0f), Transformation.Interpolations.field_37884), new Keyframe(0.3333333333333333f, AnimationHelper.method_41823(0f, -1f, 0f), Transformation.Interpolations.field_37884), new Keyframe(0.5f, AnimationHelper.method_41823(0f, -2.16f, 0f), Transformation.Interpolations.field_37885), new Keyframe(0.7083333333333334f, AnimationHelper.method_41823(0f, -8.14f, 0f), Transformation.Interpolations.field_37885), new Keyframe(0.9583333333333334f, AnimationHelper.method_41823(0f, -15f, 0f), Transformation.Interpolations.field_37885))).addBoneAnimation("ridge", new Transformation(Transformation.Targets.ROTATE, new Keyframe(0f, AnimationHelper.method_41829(0f, 0f, 0f), Transformation.Interpolations.field_37884), new Keyframe(0.16666666666666666f, AnimationHelper.method_41829(0f, 0f, 0f), Transformation.Interpolations.field_37884), new Keyframe(0.375f, AnimationHelper.method_41829(5f, 0f, 0f), Transformation.Interpolations.field_37885), new Keyframe(0.4583333333333333f, AnimationHelper.method_41829(0f, 0f, 7.5f), Transformation.Interpolations.field_37885), new Keyframe(0.5416666666666666f, AnimationHelper.method_41829(-5f, 0f, 0f), Transformation.Interpolations.field_37885), new Keyframe(0.625f, AnimationHelper.method_41829(0f, 0f, -10f), Transformation.Interpolations.field_37885), new Keyframe(0.7083333333333334f, AnimationHelper.method_41829(3f, 0f, 5f), Transformation.Interpolations.field_37885), new Keyframe(0.7916666666666666f, AnimationHelper.method_41829(0f, 0f, -3f), Transformation.Interpolations.field_37885), new Keyframe(0.875f, AnimationHelper.method_41829(0f, 0f, 0f), Transformation.Interpolations.field_37885))).addBoneAnimation("body2", new Transformation(Transformation.Targets.TRANSLATE, new Keyframe(0f, AnimationHelper.method_41823(0f, 0f, 0f), Transformation.Interpolations.field_37884), new Keyframe(0.375f, AnimationHelper.method_41823(0f, 0f, 0f), Transformation.Interpolations.field_37885), new Keyframe(0.5f, AnimationHelper.method_41823(0f, -1.27f, 0f), Transformation.Interpolations.field_37885), new Keyframe(0.5416666666666666f, AnimationHelper.method_41823(0f, -2.1799999999999997f, 0f), Transformation.Interpolations.field_37885), new Keyframe(1.0833333333333333f, AnimationHelper.method_41823(0f, -5f, 0f), Transformation.Interpolations.field_37884))).addBoneAnimation("body2", new Transformation(Transformation.Targets.ROTATE, new Keyframe(0f, AnimationHelper.method_41829(0f, 0f, 0f), Transformation.Interpolations.field_37884), new Keyframe(0.375f, AnimationHelper.method_41829(0f, 0f, 0f), Transformation.Interpolations.field_37885), new Keyframe(0.4583333333333333f, AnimationHelper.method_41829(0f, 6f, 0f), Transformation.Interpolations.field_37885), new Keyframe(0.5416666666666666f, AnimationHelper.method_41829(0f, -2.8299999999999983f, 0f), Transformation.Interpolations.field_37885), new Keyframe(0.7083333333333334f, AnimationHelper.method_41829(0f, 0f, 0f), Transformation.Interpolations.field_37885), new Keyframe(1.0833333333333333f, AnimationHelper.method_41829(0f, 0f, 0f), Transformation.Interpolations.field_37884))).build();

    private final ModelPart root;
    private final ModelPart torso;
    private final ModelPart body;
    private final ModelPart body2;
    private final ModelPart ridge;
    private final ModelPart rightFrontLeg;
    private final ModelPart leftFrontLeg;
    private final ModelPart rightHindLeg;
    private final ModelPart leftHindLeg;
    private final ModelPart head;

    public StoneGolemEntityModel(ModelPart root) {
        this.root = root;
        this.torso = root.getChild("torso");
        this.body = torso.getChild("body");
        this.body2 = torso.getChild("body2");
        this.ridge= torso.getChild("ridge");
        this.rightFrontLeg = root.getChild("right_front_leg");
        this.leftFrontLeg = root.getChild("left_front_leg");
        this.rightHindLeg = root.getChild("right_hind_leg");
        this.leftHindLeg = root.getChild("left_hind_leg");
        this.head = root.getChild("head");
    }

    @SuppressWarnings("unused")
    public static TexturedModelData getTexturedModelData(boolean stoneLayer) {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();

        Dilation dilation = new Dilation(stoneLayer ? 0 : 0.25f);

        ModelPartData head = modelPartData.addChild("head", ModelPartBuilder.create().uv(0, 102).cuboid(-8.0F, -8.0F, -24.0F, 16.0F, 16.0F, 16.0F, dilation), ModelTransform.pivot(0.0F, -14.0F, -24.0F));

        ModelPartData torso = modelPartData.addChild("torso", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 24.0F, 0.0F));
        ModelPartData body = torso.addChild("body", ModelPartBuilder.create().uv(0, 0).cuboid(-24.0F, -40.0F, -24.0F, 48.0F, 16.0F, 48.0F, dilation), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
        ModelPartData body2 = torso.addChild("body2", ModelPartBuilder.create().uv(0, 64).cuboid(-17.0F, -3.0F, -17.0F, 34.0F, 3.0F, 34.0F, dilation), ModelTransform.pivot(0.0F, -40.0F, 0.0F));
        ModelPartBuilder legBuilder = ModelPartBuilder.create().uv(140, 64).cuboid(-8.0F, 8.0F, -8.0F, 16.0F, 16.0F, 16.0F, dilation);
        ModelPartData right_front_leg = modelPartData.addChild("right_front_leg", legBuilder, ModelTransform.pivot(-16.0F, 0.0F, -16.0F));
        ModelPartData right_hind_leg = modelPartData.addChild("right_hind_leg", legBuilder, ModelTransform.pivot(-16.0F, 0.0F, 16.0F));
        ModelPartData left_front_leg = modelPartData.addChild("left_front_leg", legBuilder, ModelTransform.pivot(16.0F, 0.0F, -16.0F));
        ModelPartData left_hind_leg = modelPartData.addChild("left_hind_leg", legBuilder, ModelTransform.pivot(16.0F, 0.0F, 16.0F));
        ModelPartData ridge = torso.addChild("ridge", ModelPartBuilder.create().uv(88, 64).cuboid(0.0F, -13.0F, -24.0F, 0.0F, 14.0F, 48.0F, dilation), ModelTransform.pivot(0.0F, -40.0F, 0.0F));

        return TexturedModelData.of(modelData, 256, 256);
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
        super.render(matrices, vertices, 15728640, overlay, red, green, blue, alpha);
    }

    @Override
    public void setAngles(StoneGolemEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float headYaw, float headPitch) {
        this.getPart().traverse().forEach(ModelPart::resetTransform);

        this.head.yaw = headYaw * 0.017453292F; // PI/180
        this.head.pitch = headPitch * 0.017453292F; // PI/180

        this.rightHindLeg.pitch = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
        this.leftHindLeg.pitch = MathHelper.cos(limbSwing * 0.6662F + 3.1415927F) * 1.4F * limbSwingAmount;
        this.rightFrontLeg.pitch = MathHelper.cos(limbSwing * 0.6662F + 3.1415927F) * 1.4F * limbSwingAmount;
        this.leftFrontLeg.pitch = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;

        this.updateAnimation(entity.sleepingAnimationState, SLEEP_ANIMATION, ageInTicks, entity.animationReverse ? -0.5f : 0.5f);
    }

    public void renderStone(MatrixStack matrices, VertexConsumer blockVertices, int light, int overlay, float red, float green, float blue, float alpha) {
        matrices.push();
        getPart().rotate(matrices);

        renderBox(rightFrontLeg, matrices, blockVertices, light, overlay, red, green, blue);
        renderBox(leftFrontLeg, matrices, blockVertices, light, overlay, red, green, blue);
        renderBox(rightHindLeg, matrices, blockVertices, light, overlay, red, green, blue);
        renderBox(leftHindLeg, matrices, blockVertices, light, overlay, red, green, blue);
        renderBox(head, matrices, blockVertices, light, overlay, red, green, blue);

        matrices.push();
        torso.rotate(matrices);
        renderBox(body, matrices, blockVertices, light, overlay, red, green, blue);
        matrices.pop();

        matrices.pop();
    }

    private void renderBox(ModelPart part, MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue) {
        part.forEachCuboid(matrices, (matrix, path, index, cuboid) -> {
            double minX = cuboid.minX/16;
            double maxX = cuboid.maxX/16;
            double minZ = cuboid.minZ/16;
            double maxZ = cuboid.maxZ/16;
            double minY = cuboid.minY/16;
            double maxY = cuboid.maxY/16;

            for (double x = minX; x < maxX; x++) {
                for (double z = minZ; z < maxZ; z++) {
                    for (double y = minY; y < maxY; y++) {
                        matrices.push();
                        matrices.translate(x, y, z);

                        List<Direction> missing = new ArrayList<>();

                        if (x != minX) missing.add(Direction.WEST);
                        if (x != maxX-1) missing.add(Direction.EAST);
                        if (z != minZ) missing.add(Direction.NORTH);
                        if (z != maxZ-1) missing.add(Direction.SOUTH);
                        if (y != minY) missing.add(Direction.DOWN);
                        if (y != maxY-1) missing.add(Direction.UP);

                        StoneGolemStoneFeatureRenderer.renderStoneBlock(missing, matrices.peek(), vertices, red, green, blue, light, overlay);

                        matrices.pop();
                    }
                }
            }
        });
    }

    @Override
    public ModelPart getPart() {
        return root;
    }
}

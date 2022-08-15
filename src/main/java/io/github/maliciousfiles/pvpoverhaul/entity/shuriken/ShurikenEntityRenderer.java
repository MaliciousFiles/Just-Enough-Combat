package io.github.maliciousfiles.pvpoverhaul.entity.shuriken;

import io.github.maliciousfiles.pvpoverhaul.PvPOverhaul;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3f;

@Environment(EnvType.CLIENT)
public class ShurikenEntityRenderer extends EntityRenderer<ShurikenEntity> {
    private final ItemRenderer itemRenderer;

    public ShurikenEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
        this.itemRenderer = context.getItemRenderer();
    }

    public void render(ShurikenEntity shuriken, float yaw, float tickDelta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        shuriken.updateSpin(tickDelta);

        matrixStack.push();

        matrixStack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(MathHelper.lerp(tickDelta, shuriken.prevYaw, shuriken.getYaw()) - 90.0F));
//        matrixStack.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(MathHelper.lerp(tickDelta, shuriken.prevPitch, shuriken.getPitch())));
        matrixStack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(180.0F));

        matrixStack.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(shuriken.getSpin()));

        matrixStack.scale(1.75f, 1.75f, 1.75f);
        matrixStack.translate(0,-0.12,0);

        this.itemRenderer.renderItem(shuriken.asItemStack(), ModelTransformation.Mode.GROUND, i, OverlayTexture.DEFAULT_UV, matrixStack, vertexConsumerProvider, shuriken.getId());
        matrixStack.pop();

        super.render(shuriken, yaw, tickDelta, matrixStack, vertexConsumerProvider, i);
    }

    public Identifier getTexture(ShurikenEntity shuriken) {
        return SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE;
    }

}

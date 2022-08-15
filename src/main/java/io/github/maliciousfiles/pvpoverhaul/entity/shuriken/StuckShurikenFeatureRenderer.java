package io.github.maliciousfiles.pvpoverhaul.entity.shuriken;

import io.github.maliciousfiles.pvpoverhaul.item.ItemInit;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.feature.StuckObjectsFeatureRenderer;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.MathHelper;

@Environment(EnvType.CLIENT)
public class StuckShurikenFeatureRenderer<T extends LivingEntity, M extends PlayerEntityModel<T>> extends StuckObjectsFeatureRenderer<T, M> {
    private final EntityRenderDispatcher dispatcher;

    public StuckShurikenFeatureRenderer(EntityRendererFactory.Context context, LivingEntityRenderer<T, M> entityRenderer) {
        super(entityRenderer);
        this.dispatcher = context.getRenderDispatcher();
    }

    protected int getObjectCount(T entity) {
        return entity.getDataTracker().get(ItemInit.STUCK_SHURIKEN_DATA);
    }

    protected void renderObject(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, Entity entity, float directionX, float directionY, float directionZ, float tickDelta) {
        float f = MathHelper.sqrt(directionX * directionX + directionZ * directionZ);
        ShurikenEntity shuriken = new ShurikenEntity(entity.world, entity.getX(), entity.getY(), entity.getZ());

        shuriken.setYaw((float)(Math.atan2(directionX, directionZ) * 57.2957763671875));
        shuriken.setPitch((float)(Math.atan2(directionY, f) * 57.2957763671875));
        shuriken.prevYaw = shuriken.getYaw();
        shuriken.prevPitch = shuriken.getPitch();

        this.dispatcher.render(shuriken, 0.0, 0.0, 0.0, 0.0F, tickDelta, matrices, vertexConsumers, light);
    }
}
package io.github.maliciousfiles.jec.entity.projectile.weapon;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.StuckInBodyLayer;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;

public class StuckThrowableWeaponLayer<T extends LivingEntity, M extends PlayerModel<T>> extends StuckInBodyLayer<T, M> {

    private final EntityDataAccessor<Integer> data;
    private final EntityRenderDispatcher dispatcher;
    private final EntityType<? extends ThrowableWeaponEntity> type;

    public StuckThrowableWeaponLayer(EntityRendererProvider.Context context, LivingEntityRenderer<T, M> renderer, EntityType<? extends ThrowableWeaponEntity> type) {
        super(renderer);
        this.data = ThrowableWeaponEntities.getThrowableWeaponData(type);
        this.dispatcher = context.getEntityRenderDispatcher();
        this.type = type;
    }

    @Override
    protected int numStuck(T entity) {
        return entity.getEntityData().get(data);
    }

    protected void renderStuckItem(PoseStack poseStack, MultiBufferSource bufferSource, int light, Entity entity, float directionX, float directionY, float directionZ, float tickDelta) {
        float f = Mth.sqrt(directionX * directionX + directionZ * directionZ);
        ThrowableWeaponEntity projectile = type.create(entity.level());
        // disable spinning
        if (projectile instanceof SpinningThrowableWeaponEntity spin) spin.updateSpin(-1);

        projectile.setPos(entity.getX(), entity.getY(), entity.getZ());
        projectile.setYRot((float)(Math.atan2(directionX, directionZ) * Mth.RAD_TO_DEG));
        projectile.setXRot((float)(Math.atan2(directionY, f) * Mth.RAD_TO_DEG));
        projectile.yRotO = projectile.getYRot();
        projectile.xRotO = projectile.getXRot();
        this.dispatcher.render(projectile, 0.0, 0.0, 0.0, 0.0F, tickDelta, poseStack, bufferSource, light);
    }
}

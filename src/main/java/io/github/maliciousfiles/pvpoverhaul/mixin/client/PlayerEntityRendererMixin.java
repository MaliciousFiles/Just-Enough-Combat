package io.github.maliciousfiles.pvpoverhaul.mixin.client;

import io.github.maliciousfiles.pvpoverhaul.entity.shuriken.StuckShurikenFeatureRenderer;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntityRenderer.class)
public abstract class PlayerEntityRendererMixin extends LivingEntityRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> {

    private PlayerEntityRendererMixin() {
        //noinspection ConstantConditions
        super(null, null, 0);
    }

    // STUCK SHURIKEN RENDERER
    @Inject(method = "<init>", at = @At("TAIL"))
    private void init(EntityRendererFactory.Context ctx, boolean slim, CallbackInfo ci) {
        this.addFeature(new StuckShurikenFeatureRenderer<>(ctx, this));
    }
}

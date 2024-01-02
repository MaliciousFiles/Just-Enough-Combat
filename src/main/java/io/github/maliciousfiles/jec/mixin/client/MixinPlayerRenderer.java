package io.github.maliciousfiles.jec.mixin.client;

import io.github.maliciousfiles.jec.entity.projectile.weapon.StuckThrowableWeaponLayer;
import io.github.maliciousfiles.jec.entity.projectile.weapon.ThrowableWeaponEntities;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerRenderer.class)
public class MixinPlayerRenderer {
    @Inject(method="<init>", at=@At("TAIL"))
    private void addThrowableLayers(EntityRendererProvider.Context ctx, boolean slim, CallbackInfo ci) {
        PlayerRenderer renderer = (PlayerRenderer) (Object) this;

        ThrowableWeaponEntities.getThrowableWeapons().forEach(type ->
                renderer.addLayer(new StuckThrowableWeaponLayer<>(ctx, renderer, type)));
    }
}

package io.github.maliciousfiles.pvpoverhaul.mixin.vengeance_totem;

import io.github.maliciousfiles.pvpoverhaul.item.ItemInit;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {

    @Inject(method = "initDataTracker", at = @At("HEAD"))
    private void initDataTracker(CallbackInfo ci) {
        ((PlayerEntity) (Object) this).getDataTracker().startTracking(ItemInit.VENGEANCE_DATA, 0);
    }
}

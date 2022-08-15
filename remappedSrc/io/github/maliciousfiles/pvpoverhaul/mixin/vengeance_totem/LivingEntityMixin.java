package io.github.maliciousfiles.pvpoverhaul.mixin.vengeance_totem;

import io.github.maliciousfiles.pvpoverhaul.PvPOverhaul;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {

    @Inject(method = "tryUseTotem", at = @At("HEAD"), cancellable = true)
    private void canUseTotem(DamageSource source, CallbackInfoReturnable<Boolean> cir) {
        if (source == PvPOverhaul.VENGEANCE_TOTEM_TIME) {
            cir.setReturnValue(false);
        }
    }
}

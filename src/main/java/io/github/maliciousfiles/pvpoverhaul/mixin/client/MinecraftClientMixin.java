package io.github.maliciousfiles.pvpoverhaul.mixin.client;

import io.github.maliciousfiles.pvpoverhaul.item.weapon.SingleHandedWeapon;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {

    // DUAL WIELDING OFFHAND ATTACK
    private boolean offhandSwing = false;

    @Redirect(method = "doAttack", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;swingHand(Lnet/minecraft/util/Hand;)V"))
    private void swingOffHand(ClientPlayerEntity instance, Hand hand) {
        instance.swingHand(offhandSwing ? Hand.OFF_HAND : hand);
    }

    @Inject(method = "doAttack", at = @At("HEAD"))
    private void swingOffHand(CallbackInfoReturnable<Boolean> cir) {
        ClientPlayerEntity player = ((MinecraftClient) (Object) this).player;

        offhandSwing = SingleHandedWeapon.canOffhandAttack(player, player.getAttackCooldownProgress(0));
    }
}

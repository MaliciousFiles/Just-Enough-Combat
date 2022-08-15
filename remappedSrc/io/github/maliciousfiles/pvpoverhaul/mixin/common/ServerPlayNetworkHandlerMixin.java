package io.github.maliciousfiles.pvpoverhaul.mixin.common;

import io.github.maliciousfiles.pvpoverhaul.AttributeInit;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ServerPlayNetworkHandler.class)
public class ServerPlayNetworkHandlerMixin {

    @Redirect(method = "onPlayerInteractEntity", at = @At(value = "FIELD", target = "Lnet/minecraft/server/network/ServerPlayNetworkHandler;MAX_BREAK_SQUARED_DISTANCE:D", opcode = 178)) // Opcodes.GETSTATIC
    private double getAttackReach() {
        double attackReach = ((ServerPlayNetworkHandler) (Object) this).getPlayer().getAttributeValue(AttributeInit.ATTACK_REACH);

        return attackReach * attackReach;
    }
}

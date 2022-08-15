package io.github.maliciousfiles.pvpoverhaul.mixin.client;

import io.github.maliciousfiles.pvpoverhaul.client.SoundInit;
import io.github.maliciousfiles.pvpoverhaul.entity.CustomEntityStatuses;
import io.github.maliciousfiles.pvpoverhaul.item.ItemInit;
import io.github.maliciousfiles.pvpoverhaul.particle.ParticleInit;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayNetworkHandlerMixin {

    @Shadow @Final private MinecraftClient client;

    // TOTEM OF VENGEANCE
    @Redirect(method = "onEntityStatus", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;handleStatus(B)V"))
    private void handleStatus(Entity instance, byte status) {
        ClientPlayNetworkHandler handler = (ClientPlayNetworkHandler) (Object) this;
        if (status == CustomEntityStatuses.USE_TOTEM_OF_VENGEANCE) {
            this.client.particleManager.addEmitter(instance, ParticleInit.TOTEM_OF_VENGEANCE, 30);
            handler.getWorld().playSound(instance.getX(), instance.getY(), instance.getZ(), SoundInit.ITEM_VENGEANCE_TOTEM_USE, instance.getSoundCategory(), 1.0F, 1.0F, false);
            if (instance == this.client.player) {
                this.client.gameRenderer.showFloatingItem(getActiveTotem(this.client.player));
            }
        } else {
            instance.handleStatus(status);
        }
    }

    private static ItemStack getActiveTotem(PlayerEntity player) {
        for (Hand hand : Hand.values()) {
            ItemStack itemStack = player.getStackInHand(hand);
            if (itemStack.isOf(ItemInit.TOTEM_OF_VENGEANCE)) {
                return itemStack;
            }
        }

        return new ItemStack(ItemInit.TOTEM_OF_VENGEANCE);
    }
}

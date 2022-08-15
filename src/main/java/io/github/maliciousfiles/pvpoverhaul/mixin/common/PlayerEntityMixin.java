package io.github.maliciousfiles.pvpoverhaul.mixin.common;

import io.github.maliciousfiles.pvpoverhaul.AttributeInit;
import io.github.maliciousfiles.pvpoverhaul.item.ItemInit;
import io.github.maliciousfiles.pvpoverhaul.item.weapon.LanceItem;
import io.github.maliciousfiles.pvpoverhaul.item.weapon.SingleHandedWeapon;
import net.minecraft.entity.Entity;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {

    // STOP SWEEPING EDGE ON LANCE
    @Redirect(method="attack", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;getItem()Lnet/minecraft/item/Item;", ordinal = 0))
    private Item isSword(ItemStack instance) {
        Item item = instance.getItem();

        return item instanceof LanceItem ? null : item;
    }

    // CUSTOM ATTACK REACH
    @ModifyConstant(method = "attack", constant = @Constant(doubleValue = 9.0))
    private double getAttackReach(double _attackReach) {
        double attackReach = ((PlayerEntity) (Object) this).getAttributeValue(AttributeInit.ATTACK_REACH);

        return attackReach * attackReach;
    }

    // STUCK SHURIKEN COUNTER
    @Inject(method = "initDataTracker", at = @At("HEAD"))
    private void initDataTracker(CallbackInfo ci) {
        ((PlayerEntity) (Object) this).getDataTracker().startTracking(ItemInit.STUCK_SHURIKEN_DATA, 0);
    }

    // DUEL WIELDING
    @Inject(method = "createPlayerAttributes", at = @At("RETURN"))
    private static void addAttributes(final CallbackInfoReturnable<DefaultAttributeContainer.Builder> info) {
        info.getReturnValue().add(AttributeInit.DUEL_WIELDING_TIME);
    }

    @Inject(method = "resetLastAttackedTicks", at = @At("HEAD"))
    private void isOffhandedAttack(CallbackInfo ci) {
        PlayerEntity player = (PlayerEntity) (Object) this;

        SingleHandedWeapon.wasSingleHanded.put(player.getId(), !SingleHandedWeapon.canOffhandAttack(player, player.getAttackCooldownProgress(0)) && SingleHandedWeapon.readyForOffhandAttack(player, player.getAttackCooldownProgress(0)));
    }

//    @Redirect(method = "attack", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;getMainHandStack()Lnet/minecraft/item/ItemStack;"))
//    private ItemStack getStack(PlayerEntity instance) {
//
//        return null;
//    }

    @Inject(method = "getAttackCooldownProgress", at = @At("RETURN"), cancellable = true)
    private void extendAttackCooldownProgress(float baseTime, CallbackInfoReturnable<Float> cir) {
        PlayerEntity player = (PlayerEntity) (Object) this;
        if (SingleHandedWeapon.canOffhandAttack(player, 1)) {
            cir.setReturnValue(1F);
        }
    }
}
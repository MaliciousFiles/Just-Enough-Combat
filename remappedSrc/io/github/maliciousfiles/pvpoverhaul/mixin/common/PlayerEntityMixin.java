package io.github.maliciousfiles.pvpoverhaul.mixin.common;

import io.github.maliciousfiles.pvpoverhaul.AttributeInit;
import io.github.maliciousfiles.pvpoverhaul.item.LanceItem;
import it.unimi.dsi.fastutil.objects.ObjectObjectImmutablePair;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Redirect;

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
}

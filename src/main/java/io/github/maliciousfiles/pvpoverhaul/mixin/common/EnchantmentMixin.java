package io.github.maliciousfiles.pvpoverhaul.mixin.common;

import io.github.maliciousfiles.pvpoverhaul.item.weapon.ShurikenItem;
import net.minecraft.enchantment.*;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Predicate;

@Mixin(Enchantment.class)
public class EnchantmentMixin {

    // SHURIKEN ENCHANTMENTS
    @Inject(method = "isAcceptableItem", at = @At("RETURN"), cancellable = true)
    private void acceptable(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        //noinspection ConstantConditions
        if ((Object) this instanceof PiercingEnchantment || (Object) this instanceof LoyaltyEnchantment || (Object) this instanceof MultishotEnchantment || (Object) this instanceof DamageEnchantment) {
            cir.setReturnValue(cir.getReturnValue() || stack.getItem() instanceof ShurikenItem);
        }
    }

    @Inject(method = "canAccept", at = @At("RETURN"), cancellable = true)
    private void compatible(Enchantment other, CallbackInfoReturnable<Boolean> cir) {
        Predicate<Enchantment> predicate = (ench) -> ench instanceof PiercingEnchantment || ench instanceof LoyaltyEnchantment || ench instanceof MultishotEnchantment;

        if (predicate.test((Enchantment) (Object) this)) {
            cir.setReturnValue(cir.getReturnValue() && !predicate.test(other));
        }
    }
}

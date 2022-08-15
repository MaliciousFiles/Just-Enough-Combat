package io.github.maliciousfiles.pvpoverhaul.mixin.common;

import net.minecraft.item.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = {Item.class, HoneyBottleItem.class, MilkBucketItem.class, PotionItem.class} )
public class DrinkableItemMixin {

    // LOWER USE TIME FOR LIQUIDS
    @Inject(method="getMaxUseTime", at = @At("RETURN"), cancellable = true)
    private void getMaxUseTime(ItemStack stack, CallbackInfoReturnable<Integer> cir) {
        Item item = stack.getItem();

        if (item instanceof HoneyBottleItem || item instanceof MilkBucketItem || item instanceof PotionItem || item instanceof StewItem || item instanceof SuspiciousStewItem) {
            cir.setReturnValue(20);
        }
    }
}

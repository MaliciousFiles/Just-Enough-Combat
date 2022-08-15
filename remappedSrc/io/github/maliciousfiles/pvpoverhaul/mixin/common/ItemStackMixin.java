package io.github.maliciousfiles.pvpoverhaul.mixin.common;

import io.github.maliciousfiles.pvpoverhaul.item.TooltipHandler;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;

@Mixin(ItemStack.class)
public class ItemStackMixin {

    private ItemStack.TooltipSection prevSection;
    private int prevFlags;

    @Inject(at = @At(value = "INVOKE", shift = At.Shift.BEFORE, target = "Lnet/minecraft/item/ItemStack;isSectionVisible(ILnet/minecraft/item/ItemStack$TooltipSection;)Z"), method="getTooltip", locals = LocalCapture.CAPTURE_FAILHARD)
    private void appendCustomTooltip(PlayerEntity player, TooltipContext context, CallbackInfoReturnable<List<Text>> cir, List<Text> tooltip, MutableText mutableText, int i) {
        ItemStack stack = (ItemStack) (Object) this;

        TooltipHandler.handlers.get(stack.getItem()).forEach(entry -> {
            if (prevSection == entry.getKey()) entry.getValue().appendTooltip(stack, player, tooltip, context, prevSection, i);
        });
    }

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/item/TooltipContext;isAdvanced()Z", ordinal = 2), method = "getTooltip", locals = LocalCapture.CAPTURE_FAILHARD)
    private void appendCustomTooltipReturn(PlayerEntity player, TooltipContext context, CallbackInfoReturnable<List<Text>> cir, List<Text> tooltip) {
        appendCustomTooltip(player, context, cir, tooltip, null, prevFlags);
    }

    @Redirect(method = "getTooltip", at=@At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;isSectionVisible(ILnet/minecraft/item/ItemStack$TooltipSection;)Z"))
    private boolean isTargetVisible(int flags, ItemStack.TooltipSection tooltipSection) {
        prevSection = tooltipSection;
        prevFlags = flags;

        return (flags & tooltipSection.getFlag()) == 0;
    }
}

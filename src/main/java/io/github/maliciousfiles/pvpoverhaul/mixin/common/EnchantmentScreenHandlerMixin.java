package io.github.maliciousfiles.pvpoverhaul.mixin.common;

import io.github.maliciousfiles.pvpoverhaul.PvPOverhaul;
import io.github.maliciousfiles.pvpoverhaul.item.weapon.ShurikenItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.EnchantmentScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(EnchantmentScreenHandler.class)
public abstract class EnchantmentScreenHandlerMixin extends ScreenHandler {

    private EnchantmentScreenHandlerMixin() {super(null, 0);}

    // ALLOW SHURIKENS TO STACK
    @Redirect(method = "<init>(ILnet/minecraft/entity/player/PlayerInventory;Lnet/minecraft/screen/ScreenHandlerContext;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/screen/EnchantmentScreenHandler;addSlot(Lnet/minecraft/screen/slot/Slot;)Lnet/minecraft/screen/slot/Slot;", ordinal = 0))
    private Slot addSlot(EnchantmentScreenHandler instance, Slot slot) {
        PvPOverhaul.LOGGER.info("addSlot");
        return this.addSlot(new Slot(slot.inventory, slot.getIndex(), slot.x, slot.y) {
            @Override
            public boolean canInsert(ItemStack stack) {
                PvPOverhaul.LOGGER.info("canInsert ("+stack+")");
                return true;
            }

            @Override
            public int getMaxItemCount(ItemStack stack) {
                PvPOverhaul.LOGGER.info("maxItemCount ("+stack+")");
                return stack.getItem() instanceof ShurikenItem ? stack.getMaxCount() : 1;
            }
        });
    }

    int value = 1;
    @ModifyConstant(method = "transferSlot", constant = @Constant(intValue = 1))
    private int transferMultiple(int constant) {
        return value;
    }

    @Inject(method = "transferSlot", at = @At(shift = At.Shift.BEFORE, value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;setCount(I)V"), locals = LocalCapture.CAPTURE_FAILHARD)
    private void setValue(PlayerEntity player, int index, CallbackInfoReturnable<ItemStack> cir, ItemStack itemStack, Slot slot, ItemStack itemStack2, ItemStack itemStack3) {
        if (itemStack2.getItem() instanceof ShurikenItem) {
            value = itemStack2.getCount();
        }
    }

    @Inject(method = "transferSlot", at = @At(shift = At.Shift.AFTER, value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;decrement(I)V"))
    private void resetValue(PlayerEntity player, int index, CallbackInfoReturnable<ItemStack> cir) {
        value = 1;
    }
}

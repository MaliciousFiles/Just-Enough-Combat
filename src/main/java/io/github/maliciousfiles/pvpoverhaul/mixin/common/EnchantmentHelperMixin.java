package io.github.maliciousfiles.pvpoverhaul.mixin.common;

import io.github.maliciousfiles.pvpoverhaul.item.weapon.ShurikenItem;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentLevelEntry;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(EnchantmentHelper.class)
public class EnchantmentHelperMixin {

    // SHURIKEN ENCHANTS
    private static Enchantment enchant;
    private static ItemStack stack;

    @Redirect(method = "getPossibleEntries", at = @At(value = "INVOKE", target = "Lnet/minecraft/enchantment/EnchantmentTarget;isAcceptableItem(Lnet/minecraft/item/Item;)Z"))
    private static boolean isAcceptable(EnchantmentTarget instance, Item item) {
        if (item instanceof ShurikenItem) {
            return enchant.isAcceptableItem(stack);
        }

        return instance.isAcceptableItem(item);
    }

    @Redirect(method = "getPossibleEntries", at = @At(value = "FIELD", target = "Lnet/minecraft/enchantment/Enchantment;type:Lnet/minecraft/enchantment/EnchantmentTarget;", opcode = 180)) // Opcodes.GETFIELD
    private static EnchantmentTarget getEnchantment(Enchantment instance) {
        enchant = instance;

        return instance.type;
    }

    @Inject(method = "getPossibleEntries", at = @At("HEAD"))
    private static void getItemStack(int power, ItemStack itemStack, boolean treasureAllowed, CallbackInfoReturnable<List<EnchantmentLevelEntry>> cir) {
        stack = itemStack;
    }
}

package io.github.maliciousfiles.pvpoverhaul.mixin.common;

import io.github.maliciousfiles.pvpoverhaul.item.ItemInit;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ArmorMaterials;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ArmorItem.class)
public class ArmorItemMixin {

    // USE CUSTOM VANILLA ARMOR SETS
    @ModifyVariable(at = @At("HEAD"), method = "<init>", argsOnly = true)
    private static ArmorMaterial onArmorItemInstantiate(ArmorMaterial material) {
        if (material == ArmorMaterials.LEATHER) {
            return ItemInit.LEATHER;
        } else if (material == ArmorMaterials.CHAIN) {
            return ItemInit.CHAIN;
        }

        return material;
    }
}

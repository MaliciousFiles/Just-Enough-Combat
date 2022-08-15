package io.github.maliciousfiles.pvpoverhaul.item.armor;

import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;

import java.util.UUID;

public class VanillaArmorChangeHandler extends AttributeArmorChangeHandler {
    public VanillaArmorChangeHandler() {
        super(EntityAttributes.GENERIC_ARMOR, "7cdc404d-df9d-4a54-8693-79b12f509a4a", 0.30, EntityAttributeModifier.Operation.MULTIPLY_TOTAL);
    }
}

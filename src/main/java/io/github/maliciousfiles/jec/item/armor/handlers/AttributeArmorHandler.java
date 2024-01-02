package io.github.maliciousfiles.jec.item.armor.handlers;

import io.github.maliciousfiles.jec.item.armor.ArmorSet;
import io.github.maliciousfiles.jec.util.UUIDGenerator;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ArmorItem;

public class AttributeArmorHandler extends ArmorSet.ArmorHandler {
    private final Attribute attribute;
    private final AttributeModifier[] itemModifiers;
    private final AttributeModifier setModifier;

    public AttributeArmorHandler(Attribute attribute, float setBonus) {
        this.attribute = attribute;
        this.itemModifiers = null;
        this.setModifier = new AttributeModifier(UUIDGenerator.getUUID(0), "Set bonus modifier", setBonus, AttributeModifier.Operation.MULTIPLY_BASE);
    }
    public AttributeArmorHandler(Attribute attribute, float itemBonus, float setBonus) {
        this.attribute = attribute;

        this.itemModifiers = new AttributeModifier[ArmorItem.Type.values().length];
        for (int i = 0; i < itemModifiers.length; i++) {
            itemModifiers[i] = new AttributeModifier(UUIDGenerator.getUUID(i), "Armor modifier "+i, itemBonus, AttributeModifier.Operation.MULTIPLY_BASE);
        }

        this.setModifier = new AttributeModifier(UUIDGenerator.getUUID(itemModifiers.length), "Set bonus modifier", setBonus, AttributeModifier.Operation.MULTIPLY_BASE);
    }

    @Override
    protected void equip(LivingEntity entity, ArmorItem.Type slot) {
        if (itemModifiers == null) return;
        entity.getAttribute(attribute).addTransientModifier(itemModifiers[slot.ordinal()]);
    }

    @Override
    protected void unequip(LivingEntity entity, ArmorItem.Type slot) {
        if (itemModifiers == null) return;
        entity.getAttribute(attribute).removeModifier(itemModifiers[slot.ordinal()].getId());
    }

    @Override
    protected void activateSetBonus(LivingEntity entity) {
        entity.getAttribute(attribute).addTransientModifier(setModifier);
    }

    @Override
    protected void deactivateSetBonus(LivingEntity entity) {
        entity.getAttribute(attribute).removeModifier(setModifier.getId());
    }
}

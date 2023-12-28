package io.github.maliciousfiles.jec.items.armor.handlers;

import io.github.maliciousfiles.jec.items.armor.ArmorSet;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ArmorItem;

import java.util.UUID;

public class AttributeArmorHandler extends ArmorSet.ArmorHandler {
    private final Attribute attribute;
    private final AttributeModifier[] itemModifiers;
    private final AttributeModifier setModifier;

    public AttributeArmorHandler(Attribute attribute, float setBonus, String uuid) {
        this.attribute = attribute;
        this.itemModifiers = null;
        this.setModifier = new AttributeModifier(UUID.fromString(uuid), "Set bonus modifier", setBonus, AttributeModifier.Operation.MULTIPLY_BASE);
    }
    public AttributeArmorHandler(Attribute attribute, float itemBonus, float setBonus, String[] uuids) {
        this.attribute = attribute;

        this.itemModifiers = new AttributeModifier[ArmorItem.Type.values().length];
        for (int i = 0; i < itemModifiers.length; i++) {
            itemModifiers[i] = new AttributeModifier(UUID.fromString(uuids[i]), "Armor modifier "+i, itemBonus, AttributeModifier.Operation.MULTIPLY_BASE);
        }

        this.setModifier = new AttributeModifier(UUID.fromString(uuids[ArmorItem.Type.values().length]), "Set bonus modifier", setBonus, AttributeModifier.Operation.MULTIPLY_BASE);
    }

    @Override
    protected void equip(LivingEntity entity, ArmorItem.Type slot) {
        if (itemModifiers == null) return;
        entity.getAttribute(attribute).addPermanentModifier(itemModifiers[slot.ordinal()]);
    }

    @Override
    protected void unequip(LivingEntity entity, ArmorItem.Type slot) {
        if (itemModifiers == null) return;
        entity.getAttribute(attribute).removePermanentModifier(itemModifiers[slot.ordinal()].getId());
    }

    @Override
    protected void activateSetBonus(LivingEntity entity) {
        entity.getAttribute(attribute).addPermanentModifier(setModifier);
    }

    @Override
    protected void deactivateSetBonus(LivingEntity entity) {
        entity.getAttribute(attribute).removePermanentModifier(setModifier.getId());
    }
}

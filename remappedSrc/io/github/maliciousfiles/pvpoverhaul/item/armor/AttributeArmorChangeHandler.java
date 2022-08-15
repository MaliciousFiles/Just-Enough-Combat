package io.github.maliciousfiles.pvpoverhaul.item.armor;

import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.player.PlayerEntity;

import java.util.UUID;

public class AttributeArmorChangeHandler implements ArmorChangeHandler {

    private final EntityAttribute attribute;
    private final EntityAttributeModifier modifier;

    public AttributeArmorChangeHandler(EntityAttribute attribute, String uuid, double value, EntityAttributeModifier.Operation operation) {
        this.attribute = attribute;
        this.modifier = new EntityAttributeModifier(UUID.fromString(uuid), "Armor modifier", value, operation);
    }

    @Override
    public void onSetBonus(PlayerEntity player, boolean enabled) {
        EntityAttributeInstance instance = player.getAttributeInstance(attribute);

        if (instance == null) return;

        instance.removeModifier(modifier);

        if (enabled) {
            instance.addPersistentModifier(modifier);
        }
    }
}

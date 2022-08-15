package io.github.maliciousfiles.pvpoverhaul.item.armor;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;

import java.util.List;

public interface ArmorChangeHandler {

    default void onArmorChange(PlayerEntity player, List<EquipmentSlot> equipped) {}

    default void onSetBonus(PlayerEntity player, boolean enabled) {}
}

package io.github.maliciousfiles.pvpoverhaul.item.weapon;

import io.github.maliciousfiles.pvpoverhaul.AttributeInit;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public interface SingleHandedWeapon {

    Map<Integer, Boolean> wasSingleHanded = new HashMap<>();

    static boolean readyForOffhandAttack(PlayerEntity player, float cooldownProgress) {
        return player.getMainHandStack().getItem() instanceof SingleHandedWeapon && player.getOffHandStack().getItem() instanceof SingleHandedWeapon && cooldownProgress == 1;
    }

    static boolean canOffhandAttack(PlayerEntity player, float cooldownProgress) {
        try {
            Field lastAttacked = LivingEntity.class.getDeclaredField("lastAttackedTicks");
            lastAttacked.setAccessible(true);

            return readyForOffhandAttack(player, cooldownProgress) && wasSingleHanded.getOrDefault(player.getId(), false) && (Integer) lastAttacked.get(player) <= player.getAttributeValue(AttributeInit.DUEL_WIELDING_TIME);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}

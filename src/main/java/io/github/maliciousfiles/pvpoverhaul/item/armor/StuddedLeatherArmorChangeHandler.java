package io.github.maliciousfiles.pvpoverhaul.item.armor;

import io.github.maliciousfiles.pvpoverhaul.event.EntityDamageCallback;
import io.github.maliciousfiles.pvpoverhaul.item.ItemInit;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.util.math.random.Random;

public class StuddedLeatherArmorChangeHandler implements ArmorChangeHandler, EntityDamageCallback {
    @Override
    public float entityDamage(DamageSource source, Entity damaged, float damage) {
        if (damaged instanceof LivingEntity attacked && source.getAttacker() instanceof LivingEntity attacker) {
            Random random = attacked.getRandom();
            for (int i = 0; i < ItemInit.STUDDED_LEATHER.getEquipped(attacked); i++) {
                if (random.nextFloat() < 0.15) {
                    attacker.getMainHandStack().damage(1, attacker, (entity) -> entity.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND));
                }
            }
        }

        return damage;
    }
}

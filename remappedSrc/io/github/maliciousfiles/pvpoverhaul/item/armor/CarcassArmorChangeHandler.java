package io.github.maliciousfiles.pvpoverhaul.item.armor;

import io.github.maliciousfiles.pvpoverhaul.event.EntityDamageCallback;
import io.github.maliciousfiles.pvpoverhaul.item.ItemInit;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.EntityDamageSource;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;

public class CarcassArmorChangeHandler implements ArmorChangeHandler, EntityDamageCallback {

    public CarcassArmorChangeHandler() {
        EntityDamageCallback.EVENT.register(this);
    }

    @Override
    public float entityDamage(DamageSource source, Entity damaged, float damage) {
        if (source instanceof EntityDamageSource entity && entity.getSource() instanceof PlayerEntity player) {
            int equipped = ItemInit.CARCASS.getEquipped(player);

            for (int i = 0; i < equipped; i++) {
                if (Math.random() <= 0.75) {
                    damage += 0.5;
                }
            }

            if (damaged instanceof LivingEntity living && equipped == 4) {
                living.addStatusEffect(new StatusEffectInstance(StatusEffect.byRawId(17)), player); // 17 = hunger
            }
        }

        return damage;
    }
}

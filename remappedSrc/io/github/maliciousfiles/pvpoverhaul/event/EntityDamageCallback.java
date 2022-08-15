package io.github.maliciousfiles.pvpoverhaul.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.EntityDamageSource;

public interface EntityDamageCallback {

    Event<EntityDamageCallback> EVENT = EventFactory.createArrayBacked(EntityDamageCallback.class,
        (listeners) -> (source, damaged, damage) -> {
            for (EntityDamageCallback listener : listeners) {
                damage = listener.entityDamage(source, damaged, damage);
            }

            return damage;
        }
    );

    float entityDamage(DamageSource source, Entity damaged, float damage);
}

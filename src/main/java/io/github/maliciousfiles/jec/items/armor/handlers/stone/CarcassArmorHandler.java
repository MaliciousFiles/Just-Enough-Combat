package io.github.maliciousfiles.jec.items.armor.handlers.stone;

import io.github.maliciousfiles.jec.items.armor.ArmorSet;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;

public class CarcassArmorHandler extends ArmorSet.ArmorHandler {
    @SubscribeEvent
    private void onDealDamage(LivingDamageEvent evt) {
        int equipped;
        if (!(evt.getSource().getDirectEntity() instanceof LivingEntity living) || (equipped = set.getEquipped(living)) == 0) return;

        float damage = evt.getAmount();
        for (int i = 0; i < equipped; i++) {
            if (Math.random() <= 0.75) {
                evt.getEntity().level().addParticle(new SimpleParticleType(true), evt.getEntity().getX(), evt.getEntity().getY()+1, evt.getEntity().getZ(), 0.1, 0.1, 0.1);
                damage += 0.5f;
            }
        }
        evt.setAmount(damage);

        if (equipped == ArmorItem.Type.values().length) {
            evt.getEntity().forceAddEffect(new MobEffectInstance(MobEffects.HUNGER, 5), evt.getSource().getDirectEntity());
        }

    }
}

package io.github.maliciousfiles.jec.items.armor.handlers.stone;

import io.github.maliciousfiles.jec.items.armor.ArmorSet;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.living.LivingEvent;

public class BoneArmorHandler extends ArmorSet.ArmorHandler {
    @SubscribeEvent
    public void onVisibilityCheck(LivingEvent.LivingVisibilityEvent evt) {
        if (evt.getLookingEntity() instanceof LivingEntity living &&
                evt.getEntity().getType() == EntityType.SKELETON &&
                set.getEquipped(living) == 4) {
            evt.modifyVisibility(0.25f);
        }
    }
}

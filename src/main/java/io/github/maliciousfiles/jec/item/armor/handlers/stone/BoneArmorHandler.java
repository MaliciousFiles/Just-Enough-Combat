package io.github.maliciousfiles.jec.item.armor.handlers.stone;

import io.github.maliciousfiles.jec.item.armor.ArmorSet;
import io.github.maliciousfiles.jec.item.armor.IArmorable;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.living.LivingEvent;

public class BoneArmorHandler extends ArmorSet.ArmorHandler {
    @SubscribeEvent
    private void onVisibilityCheck(LivingEvent.LivingVisibilityEvent evt) {
        if (evt.getLookingEntity() instanceof LivingEntity living &&
                evt.getEntity().getType() == EntityType.SKELETON &&
                set.getEquipped(living) == 4) {
            evt.modifyVisibility(0.25f);
        }
    }
}

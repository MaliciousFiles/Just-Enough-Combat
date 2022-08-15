package io.github.maliciousfiles.pvpoverhaul.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;

public interface ArmorChangeCallback {

    Event<ArmorChangeCallback> EVENT = EventFactory.createArrayBacked(ArmorChangeCallback.class,
        (listeners) -> (player) -> {
            for (ArmorChangeCallback listener : listeners) {
                ActionResult result = listener.armorChange(player);

                if (result != ActionResult.PASS) {
                    return result;
                }
            }

            return ActionResult.PASS;
        }
    );

    ActionResult armorChange(PlayerEntity player);
}

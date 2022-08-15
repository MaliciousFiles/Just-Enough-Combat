package io.github.maliciousfiles.pvpoverhaul.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;

public interface VehicleChangeCallback {

    Event<VehicleChangeCallback> EVENT = EventFactory.createArrayBacked(VehicleChangeCallback.class,
        (listeners) -> (entity) -> {
            for (VehicleChangeCallback listener : listeners) {
                ActionResult result = listener.vehicleChange(entity);

                if (result != ActionResult.PASS) {
                    return result;
                }
            }

            return ActionResult.PASS;
        }
    );

    ActionResult vehicleChange(Entity entity);
}

package io.github.maliciousfiles.pvpoverhaul.item.armor;

import io.github.maliciousfiles.pvpoverhaul.event.VehicleChangeCallback;
import net.minecraft.entity.Entity;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;

public class ChainArmorChangeHandler extends AttributeArmorChangeHandler implements VehicleChangeCallback {

    public static final double MOUNTED_BONUS = 3.5;

    public ChainArmorChangeHandler() {
        super(EntityAttributes.GENERIC_ATTACK_DAMAGE, "f15788e4-3daf-4158-9760-be20e02afb81", MOUNTED_BONUS, EntityAttributeModifier.Operation.ADDITION);
    }

    @Override
    public void onSetBonus(PlayerEntity player, boolean enabled) {
        if (!enabled) super.onSetBonus(player, false);
    }

    @Override
    public ActionResult vehicleChange(Entity entity) {
        if (entity instanceof PlayerEntity player) super.onSetBonus(player, player.hasVehicle());

        return ActionResult.PASS;
    }
}

package io.github.maliciousfiles.jec.util;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.event.entity.living.LivingEvent;
import org.jetbrains.annotations.NotNull;

// basically a carbon copy of LivingEquipmentChangeEvent from NeoForge, but triggered in a useful location
// TODO: /clear and creative trash
public class LivingEquipEvent extends LivingEvent {
    private final EquipmentSlot slot;
    private final ItemStack from;
    private final ItemStack to;

    public LivingEquipEvent(LivingEntity entity, EquipmentSlot slot, @NotNull ItemStack from, @NotNull ItemStack to) {
        super(entity);
        this.slot = slot;
        this.from = from;
        this.to = to;
    }

    public EquipmentSlot getSlot() {
        return this.slot;
    }

    public @NotNull ItemStack getFrom() {
        return this.from;
    }

    public @NotNull ItemStack getTo() {
        return this.to;
    }
}

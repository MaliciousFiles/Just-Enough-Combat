package io.github.maliciousfiles.jec.event;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ItemTooltipPartEvent extends ItemTooltipEvent {
    private final ItemStack.TooltipPart part;

    public ItemTooltipPartEvent(@NotNull ItemStack itemStack, @Nullable Player player, List<Component> list, TooltipFlag flags, ItemStack.TooltipPart part) {
        super(itemStack, player, list, flags);
        this.part = part;
    }

    public ItemStack.TooltipPart getPart() {
        return part;
    }
}

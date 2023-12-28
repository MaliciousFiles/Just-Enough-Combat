package io.github.maliciousfiles.jec.util;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface CustomTooltip {

    default void appendCustomTooltip(ItemStack stack, @Nullable Player player, List<Component> tooltip, TooltipFlag flags, ItemStack.TooltipPart part) {}
}

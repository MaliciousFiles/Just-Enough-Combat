package io.github.maliciousfiles.pvpoverhaul.item;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

import java.util.List;
import java.util.Map;

public interface TooltipHandler {

    Multimap<Item, Map.Entry<ItemStack.TooltipSection, TooltipHandler>> handlers = ArrayListMultimap.create();

    void appendTooltip(ItemStack item, PlayerEntity player, List<Text> tooltip, TooltipContext context, ItemStack.TooltipSection section, int flags);
}

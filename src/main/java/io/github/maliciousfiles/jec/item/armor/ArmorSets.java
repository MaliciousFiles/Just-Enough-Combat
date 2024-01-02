package io.github.maliciousfiles.jec.item.armor;

import io.github.maliciousfiles.jec.JEC;
import io.github.maliciousfiles.jec.item.armor.handlers.AttributeArmorHandler;
import io.github.maliciousfiles.jec.item.armor.handlers.stone.BoneArmorHandler;
import io.github.maliciousfiles.jec.item.armor.handlers.stone.CarcassArmorHandler;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.*;

public class ArmorSets {
    protected static final Map<String, ArmorSet> ARMOR_SETS = new HashMap<>();
    private static final DeferredHolder<CreativeModeTab, CreativeModeTab> ARMOR_TAB = JEC.CREATIVE_MODE_TABS.register("armor", () -> CreativeModeTab.builder()
            .title(Component.literal("Just Enough Armor"))
            .icon(Items.IRON_CHESTPLATE::getDefaultInstance)
            .displayItems((parameters, output) -> {
                for (ArmorSet set : ARMOR_SETS.values()) {
                    for (ArmorItem.Type type : ArmorItem.Type.values()) {
                        output.accept(set.getArmorPiece(type).get().getDefaultInstance());
                    }
                }
            }).build());

    public static final ArmorSet CARCASS = new ArmorSet.Builder("carcass", new CarcassArmorHandler())
            .weight(ArmorSet.ArmorWeight.MEDIUM)
            .durability(3)
            .defense(ArmorItem.Type.HELMET, 1)
            .defense(ArmorItem.Type.CHESTPLATE, 2)
            .defense(ArmorItem.Type.LEGGINGS, 1)
            .defense(ArmorItem.Type.BOOTS, 1)
            .enchantability(5)
            .repairIngredient(Ingredient.of(Items.LEATHER, Items.ROTTEN_FLESH))
            .build();

    public static final ArmorSet CLOTH = new ArmorSet.Builder("cloth", new AttributeArmorHandler(Attributes.MOVEMENT_SPEED, 0.05f, 0.25f))
            .weight(ArmorSet.ArmorWeight.LIGHT)
            .durability(4)
            .defense(ArmorItem.Type.HELMET, 1)
            .defense(ArmorItem.Type.CHESTPLATE, 2)
            .defense(ArmorItem.Type.LEGGINGS, 2)
            .defense(ArmorItem.Type.BOOTS, 1)
            .enchantability(15)
            .repairIngredient(Ingredient.of(ItemTags.WOOL))
            .hasItemBonus()
            .build();

    public static final ArmorSet BONE = new ArmorSet.Builder("bone", new BoneArmorHandler())
            .weight(ArmorSet.ArmorWeight.HEAVY)
            .durability(7)
            .defense(ArmorItem.Type.HELMET, 2)
            .defense(ArmorItem.Type.CHESTPLATE, 4)
            .defense(ArmorItem.Type.LEGGINGS, 3)
            .defense(ArmorItem.Type.BOOTS, 2)
            .enchantability(12)
            .knockbackResistance(0.05f)
            .repairIngredient(Ingredient.of(Items.BONE))
            .build();

    public static final ArmorSet LEATHER = new ArmorSet.Builder("leather", new AttributeArmorHandler(Attributes.ARMOR, 0.3f))
            .weight(ArmorSet.ArmorWeight.LIGHT)
            .durability(5)
            .defense(ArmorItem.Type.HELMET, 1)
            .defense(ArmorItem.Type.CHESTPLATE, 3)
            .defense(ArmorItem.Type.LEGGINGS, 2)
            .defense(ArmorItem.Type.BOOTS, 1)
            .enchantability(15)
            .repairIngredient(Ingredient.of(Items.LEATHER))
            .vanilla()
            .build();

    public static ArmorSet getArmorSet(ItemStack item) {
        return item.getItem() instanceof ArmorItem armor ? ARMOR_SETS.get(armor.getMaterial().getName()) : null;
    }

    public static void init() {}
}
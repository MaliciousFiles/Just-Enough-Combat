package io.github.maliciousfiles.jec.items.armor;

import io.github.maliciousfiles.jec.JEC;
import io.github.maliciousfiles.jec.items.armor.handlers.AttributeArmorHandler;
import io.github.maliciousfiles.jec.items.armor.handlers.stone.BoneArmorHandler;
import io.github.maliciousfiles.jec.items.armor.handlers.stone.CarcassArmorHandler;
import net.minecraft.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

public class ArmorSets {
    private static final List<ArmorSet> ARMOR_SETS = new ArrayList<>();
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> ARMOR_TAB = JEC.CREATIVE_MODE_TABS.register("armor", () -> CreativeModeTab.builder()
            .title(Component.literal("Just Enough Armor"))
            .icon(Items.IRON_CHESTPLATE::getDefaultInstance)
            .displayItems((parameters, output) -> {
                for (ArmorSet set : ARMOR_SETS) {
                    for (ArmorItem.Type type : ArmorItem.Type.values()) {
                        output.accept(set.getArmorPiece(type).get().getDefaultInstance());
                    }
                }
            }).build());

    public static final ArmorSet CARCASS = register(new ArmorSet("carcass", ArmorSet.ArmorWeight.MEDIUM,
            new CarcassArmorHandler(), 3, Util.make(new EnumMap<>(ArmorItem.Type.class), m -> {
                m.put(ArmorItem.Type.HELMET, 1);
                m.put(ArmorItem.Type.CHESTPLATE, 2);
                m.put(ArmorItem.Type.LEGGINGS, 1);
                m.put(ArmorItem.Type.BOOTS, 1);
            }), 5, 0, 0, Ingredient.of(Items.LEATHER, Items.ROTTEN_FLESH)));
    public static final ArmorSet CLOTH = register(new ArmorSet("cloth", ArmorSet.ArmorWeight.LIGHT,
            new AttributeArmorHandler(Attributes.MOVEMENT_SPEED, 0.05f, 0.25f,
                    new String[] {"9da1a479-41d4-4623-9114-40a1cc710f8c", "9e53e27d-ca9f-45b5-88b7-c8cfbc072cef",
                            "c4d7fe5e-b93f-4ec3-8164-aec700f38716", "86840a86-449a-4041-a3de-d24c91803be1",
                            "3d915da8-5492-4c5d-91b5-0758c3ecfbb9"}),
            4, Util.make(new EnumMap<>(ArmorItem.Type.class), m -> {
                m.put(ArmorItem.Type.HELMET, 1);
                m.put(ArmorItem.Type.CHESTPLATE, 2);
                m.put(ArmorItem.Type.LEGGINGS, 2);
                m.put(ArmorItem.Type.BOOTS, 1);
            }), 15, 0, 0, Ingredient.of(ItemTags.WOOL), true, false));
    public static final ArmorSet BONE = register(new ArmorSet("bone", ArmorSet.ArmorWeight.HEAVY,
            new BoneArmorHandler(), 7, Util.make(new EnumMap<>(ArmorItem.Type.class), m -> {
                m.put(ArmorItem.Type.HELMET, 2);
                m.put(ArmorItem.Type.CHESTPLATE, 4);
                m.put(ArmorItem.Type.LEGGINGS, 3);
                m.put(ArmorItem.Type.BOOTS, 2);
            }), 12, 0, 0, Ingredient.of(Items.BONE)));
    public static final ArmorSet LEATHER = register(new ArmorSet("leather", ArmorSet.ArmorWeight.LIGHT,
            new AttributeArmorHandler(Attributes.ARMOR, 0.3f, "444a2b7e-d697-4462-86a9-06d76649d3cd"),
            5, Util.make(new EnumMap<>(ArmorItem.Type.class), m -> {
                m.put(ArmorItem.Type.HELMET, 1);
                m.put(ArmorItem.Type.CHESTPLATE, 3);
                m.put(ArmorItem.Type.LEGGINGS, 2);
                m.put(ArmorItem.Type.BOOTS, 1);
            }), 15, 0, 0, Ingredient.of(Items.LEATHER), false, true));



    private static ArmorSet register(ArmorSet set) {
        ARMOR_SETS.add(set);
        NeoForge.EVENT_BUS.register(set);
        return set;
    }

    public static boolean hasSetBonus(Player player) {
        for (ArmorSet set : ARMOR_SETS) {
            if (set.getEquipped(player) == ArmorItem.Type.values().length) {
                return true;
            }
        }
        return false;
    }

    // static reference to get the class to load :)
    public static void init() {}
}
package io.github.maliciousfiles.pvpoverhaul.item;

import Item;
import io.github.maliciousfiles.pvpoverhaul.PvPOverhaul;
import io.github.maliciousfiles.pvpoverhaul.item.armor.*;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.recipe.Ingredient;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.tag.ItemTags;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.registry.Registry;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ItemInit {

    private static final List<ArmorSet> ARMOR_SETS = new ArrayList<>();

    // STAGE 1 (stone)
    public static ArmorSet CLOTH = register(new ArmorSet(ArmorSet.ArmorType.LIGHT, 4, 15, Ingredient.fromTag(ItemTags.WOOL), 0, 0, "cloth"))
            .head(1, PvPOverhaul.settings())
            .chest(2, PvPOverhaul.settings())
            .legs(2, PvPOverhaul.settings())
            .feet(1, PvPOverhaul.settings())
            .armorChangeHandler(new AttributeArmorChangeHandler(EntityAttributes.GENERIC_ATTACK_DAMAGE, "0b70f4ff-5b34-40b7-b546-a64231ad171f", 0.10, EntityAttributeModifier.Operation.MULTIPLY_BASE), true, false, false);
    public static ArmorSet CARCASS = register(new ArmorSet(ArmorSet.ArmorType.MEDIUM, 3, 5, Ingredient.ofItems(Items.LEATHER, Items.ROTTEN_FLESH), 0, 0, "carcass"))
            .head(1, PvPOverhaul.settings())
            .chest(2, PvPOverhaul.settings())
            .legs(1, PvPOverhaul.settings())
            .feet(1, PvPOverhaul.settings())
            .armorChangeHandler(new CarcassArmorChangeHandler(), true, true, false);
    public static ArmorSet BONE = register(new ArmorSet(ArmorSet.ArmorType.HEAVY, 7, 12, Ingredient.ofItems(Items.BONE), 0, 0, "bone"))
            .head(2, PvPOverhaul.settings())
            .chest(4, PvPOverhaul.settings())
            .legs(3, PvPOverhaul.settings())
            .feet(2, PvPOverhaul.settings())
            .armorChangeHandler(new ArmorChangeHandler() {}, true, false, false);
    public static ArmorSet LEATHER = register(new ArmorSet(ArmorSet.ArmorType.LIGHT, 5, new int[] {1, 2, 3, 1}, 15, Ingredient.ofItems(Items.LEATHER), 0, 0, "leather"))
            .armorChangeHandler(new VanillaArmorChangeHandler(), true, false, false);

    // STAGE 2 (iron)
    public static ArmorSet IRON = register(new ArmorSet(ArmorSet.ArmorType.MEDIUM, 15, new int[] {2, 5, 6, 2}, 15, Ingredient.ofItems(Items.IRON_INGOT), 0, 0, "iron"))
            .armorChangeHandler(new VanillaArmorChangeHandler(), true, false, false);
    public static ArmorSet CHAIN = register(new ArmorSet(ArmorSet.ArmorType.LIGHT, 15, new int[] {1, 4, 5, 2}, 9, Ingredient.ofItems(Items.IRON_INGOT), 0, 0, "chain") {

        @Override
        public void register(boolean register) {
            super.register(register);

            getItems().forEach((s, i) -> TooltipHandler.handlers.put(i, Map.entry(ItemStack.TooltipSection.MODIFIERS, this)));
        }

        @Override
        public void appendTooltip(ItemStack stack, @Nullable PlayerEntity player, List<Text> tooltip, TooltipContext context, ItemStack.TooltipSection section, int flags) {
            if (section == ItemStack.TooltipSection.ADDITIONAL) {
                super.appendTooltip(stack, player, tooltip, context, section, flags);
            } else if (player != null && getEquipped(player) == 4 && section == ItemStack.TooltipSection.MODIFIERS) {
                tooltip.add(ScreenTexts.EMPTY);
                tooltip.add(Text.translatable("item.modifiers.mount").formatted(Formatting.GRAY));
                tooltip.add(Text.literal(" ").append(Text.translatable("attribute.modifier.plus." + EntityAttributeModifier.Operation.ADDITION.getId(), ItemStack.MODIFIER_FORMAT.format(ChainArmorChangeHandler.MOUNTED_BONUS), " ", Text.translatable(EntityAttributes.GENERIC_ATTACK_DAMAGE.getTranslationKey())).formatted(Formatting.BLUE)));
            }
        }
    }).armorChangeHandler(new ChainArmorChangeHandler(), true, false, false);
    public static ArmorSet STUDDED_LEATHER = register(new ArmorSet(ArmorSet.ArmorType.MEDIUM, 13, 13, Ingredient.ofItems(Items.LEATHER), 0, 0.025f, "studded_leather"))
            .armorChangeHandler(new StuddedLeatherArmorChangeHandler(), false, true,false);


    // WEAPONS
    public static LanceItem LANCE = new LanceItem(ToolMaterials.IRON, 1, -2.8f, PvPOverhaul.settings().maxDamage(238));


    // MISC
    public static Item SHURIKEN = new ShurikenItem(PvPOverhaul.settings().maxCount(16).group(PvPOverhaul.ITEM_GROUP));
    public static Item TOTEM_OF_VENGEANCE = new Item(PvPOverhaul.settings().maxCount(1));


    // MISC
    public static final TrackedData<Integer> VENGEANCE_DATA = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.INTEGER);

    private static ArmorSet register(ArmorSet set) {
        ARMOR_SETS.add(set);
        
        return set;
    }
    
    public static void init() {
        CLOTH.register(true);
        CARCASS.register(true);
        BONE.register(true);

        LEATHER.item(EquipmentSlot.HEAD, (ArmorItem) Items.LEATHER_HELMET)
                .item(EquipmentSlot.CHEST, (ArmorItem) Items.LEATHER_CHESTPLATE)
                .item(EquipmentSlot.LEGS, (ArmorItem) Items.LEATHER_LEGGINGS)
                .item(EquipmentSlot.FEET, (ArmorItem) Items.LEATHER_BOOTS)
                .register(false);
        CHAIN.item(EquipmentSlot.HEAD, (ArmorItem) Items.CHAINMAIL_HELMET)
                .item(EquipmentSlot.CHEST, (ArmorItem) Items.CHAINMAIL_CHESTPLATE)
                .item(EquipmentSlot.LEGS, (ArmorItem) Items.CHAINMAIL_LEGGINGS)
                .item(EquipmentSlot.FEET, (ArmorItem) Items.CHAINMAIL_BOOTS)
                .register(false);
        IRON.item(EquipmentSlot.HEAD, (ArmorItem) Items.IRON_HELMET)
                .item(EquipmentSlot.CHEST, (ArmorItem) Items.IRON_CHESTPLATE)
                .item(EquipmentSlot.LEGS, (ArmorItem) Items.IRON_LEGGINGS)
                .item(EquipmentSlot.FEET, (ArmorItem) Items.IRON_BOOTS)
                .register(false);


        Registry.register(Registry.ITEM, PvPOverhaul.identifier("lance"), LANCE);
        Registry.register(Registry.ITEM, PvPOverhaul.identifier("vengeance_totem"), TOTEM_OF_VENGEANCE);
        Registry.register(Registry.ITEM, PvPOverhaul.identifier("shuriken"), SHURIKEN);
    }

    public static boolean hasSetBonus(PlayerEntity player) {
        return ARMOR_SETS.stream().anyMatch(set -> set.getEquipped(player) == 4);
    }
}
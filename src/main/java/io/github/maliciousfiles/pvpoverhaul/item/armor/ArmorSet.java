package io.github.maliciousfiles.pvpoverhaul.item.armor;

import com.google.common.collect.ImmutableMap;
import io.github.maliciousfiles.pvpoverhaul.PvPOverhaul;
import io.github.maliciousfiles.pvpoverhaul.event.ArmorChangeCallback;
import io.github.maliciousfiles.pvpoverhaul.item.TooltipHandler;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.registry.Registry;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.*;

public class ArmorSet implements ArmorMaterial, ArmorChangeCallback, TooltipHandler {

    private static final String[] SPEED_UUIDS = new String[] {"b8d6a848-4a8c-42de-99e6-83deecc7fd6a", "c1cd0ee3-31fa-457e-89fa-38e8114d5654",
            "4b27c414-56f4-4a6b-babc-639cd5d96a1b", "19ae7caa-8ae2-4e4e-9397-804136c3fead"};
    private static final int[] BASE_DURABILITY = new int[]{13, 15, 16, 11};

    private final Map<EquipmentSlot, ArmorItem> items = new HashMap<>();
//    private final int[] durability = new int[] {0, 0, 0, 0};
    private final int[] protectionAmounts;
    private final String[] ids = new String[] {"", "", "", ""};
    
    private final ArmorType type;
    private final int durabilityMultiplier;
    private final int enchantability;
    private final Ingredient repairIngredient;
    private final float toughness;
    private final float knockbackResistance;
    private final String name;

    private boolean hasSetBonus = false;
    private boolean hasItemBonus = false;
    private boolean individualItemBonus = false;
    private ArmorChangeHandler changeHandler;

    public ArmorSet(ArmorType type, int durabilityMultiplier, int enchantability, Ingredient repairIngredient, float toughness, float knockbackResistance, String name) {
        this(type, durabilityMultiplier, new int[] {0, 0, 0, 0}, enchantability, repairIngredient, toughness, knockbackResistance, name);
    }

    // to fill the `protectionAmounts` array if overriding a vanilla armor set
    public ArmorSet(ArmorType type, int durabilityMultiplier, int[] protectionAmounts, int enchantability, Ingredient repairIngredient, float toughness, float knockbackResistance, String name) {
        this.type = type;
        this.durabilityMultiplier = durabilityMultiplier;
        this.protectionAmounts = protectionAmounts;
        this.enchantability = enchantability;
        this.repairIngredient = repairIngredient;
        this.toughness = toughness;
        this.knockbackResistance = knockbackResistance;
        this.name = name;
    }

    public enum ArmorType {
        LIGHT(0),
        MEDIUM(0.03),
        HEAVY(0.06);

        public final double slowness;

        ArmorType(double slowness) {
            this.slowness = slowness;
        }
    }

    public ArmorSet armorChangeHandler(ArmorChangeHandler changeHandler, boolean hasSetBonus, boolean hasItemBonus, boolean individualItemBonus) {
        this.hasSetBonus = hasSetBonus;
        this.hasItemBonus = hasItemBonus;
        this.individualItemBonus = individualItemBonus;
        this.changeHandler = changeHandler;

        return this;
    }

    // to fill the `items` map if overriding a vanilla armor set
    public ArmorSet item(EquipmentSlot slot, ArmorItem item) {
        items.put(slot, item);

        return this;
    }

    public ArmorSet item(EquipmentSlot slot, /*int durability,*/ int protectionAmount, Item.Settings settings) {
//        this.durability[slot.getEntitySlotId()] = durability;
        this.protectionAmounts[slot.getEntitySlotId()] = protectionAmount;
        this.ids[slot.getEntitySlotId()] = name.toLowerCase()+"_"+getSlotName(slot).toLowerCase();


        ArmorItem item = new ArmorItem(this, slot, settings);

        items.put(slot, item);

        return this;
    }

    public ArmorSet head(/*int /*durability,*/ int protectionAmount, Item.Settings settings) {
        return item(EquipmentSlot.HEAD, /*durability,*/ protectionAmount, settings);
    }

    public ArmorSet chest(/*int /*durability,*/ int protectionAmount, Item.Settings settings) {
        return item(EquipmentSlot.CHEST, /*durability,*/ protectionAmount, settings);
    }

    public ArmorSet legs(/*int /*durability,*/ int protectionAmount, Item.Settings settings) {
        return item(EquipmentSlot.LEGS, /*durability,*/ protectionAmount, settings);
    }

    public ArmorSet feet(/*int /*durability,*/ int protectionAmount, Item.Settings settings) {
        return item(EquipmentSlot.FEET, /*durability,*/ protectionAmount, settings);
    }

    public void register(boolean register) {
        ArmorChangeCallback.EVENT.register(this);

        for (Map.Entry<EquipmentSlot, ArmorItem> entry : items.entrySet()) {
            TooltipHandler.handlers.put(entry.getValue(), Map.entry(ItemStack.TooltipSection.ADDITIONAL, this));

            if (register) Registry.register(Registry.ITEM, PvPOverhaul.identifier(ids[entry.getKey().getEntitySlotId()]), entry.getValue());
        }
    }

    public int getEquipped(@NotNull LivingEntity player) {
        int armorPieces = 0;

        for (Map.Entry<EquipmentSlot, ArmorItem> entry : items.entrySet()) {
            if (player.getEquippedStack(entry.getKey()).itemMatches(Registry.ITEM.getEntry(Registry.ITEM.getKey(entry.getValue()).orElseThrow()).orElseThrow())) {
                armorPieces++;
            }
        }

        return armorPieces;
    }

    public Map<EquipmentSlot, ArmorItem> getItems() {
        return ImmutableMap.copyOf(items);
    }

    public void appendTooltip(ItemStack stack, @Nullable PlayerEntity player, List<Text> tooltip, TooltipContext context, ItemStack.TooltipSection section, int flags) {
        if (hasItemBonus) {
            EquipmentSlot slot = Arrays.stream(EquipmentSlot.values()).filter(s -> items.get(s) == stack.getItem()).findAny().orElseThrow();
            String key = "item_bonus." + name + (individualItemBonus ? "." + slot.getName().toLowerCase() : "");

            tooltip.add(Text.empty());
            tooltip.add(Text.translatable(key + ".title").setStyle(Style.EMPTY.withUnderline(true).withColor(getRGB(240, 240, 240))));
            tooltip.add(Text.translatable(key + ".description").setStyle(Style.EMPTY.withColor(getRGB(206, 206, 206)).withItalic(true)));
        }

        if (hasSetBonus) {
            String key = "set_bonus." + name;
            int numArmor = player == null ? 0 : getEquipped(player);

            tooltip.add(Text.empty());
            tooltip.add(Text.translatable(key + ".title").append(Text.literal(" (" + numArmor + "/4)")).setStyle(Style.EMPTY.withUnderline(true).withColor(numArmor == 4 ? getRGB(240, 240, 240) : getRGB(100, 100, 100))));
            tooltip.add(Text.translatable(key + ".description").setStyle(Style.EMPTY.withColor(numArmor == 4 ? getRGB(206, 206, 206) : getRGB(66, 66, 66)).withItalic(true)));
        }
    }

    private static int getRGB(int r, int g, int b) {
        int rgb = r;
        rgb = (rgb << 8) + g;
        rgb = (rgb << 8) + b;

        return rgb;
    }

    @Override
    public final ActionResult armorChange(PlayerEntity player) {
        if (player.world.isClient) return ActionResult.PASS;

        List<EquipmentSlot> equipped = new ArrayList<>();
        boolean setBonus = true;

        for (Map.Entry<EquipmentSlot, ArmorItem> entry : items.entrySet()) {
            EntityAttributeInstance movementSpeed = player.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED);
            EntityAttributeModifier modifier = new EntityAttributeModifier(UUID.fromString(SPEED_UUIDS[entry.getKey().getEntitySlotId()]), "Armor modifier", -ArmorSet.this.type.slowness, EntityAttributeModifier.Operation.MULTIPLY_BASE);

            ItemStack equippedStack = player.getEquippedStack(entry.getKey());

            if (equippedStack == null || equippedStack.isEmpty()) {
                movementSpeed.removeModifier(modifier);
                setBonus = false;
                continue;
            }

            if (equippedStack.isOf(entry.getValue())) {
                movementSpeed.removeModifier(modifier);
                movementSpeed.addPersistentModifier(modifier);

                PvPOverhaul.LOGGER.info("adding modifier "+ entry.getValue());

                equipped.add(entry.getKey());
            } else {
                setBonus = false;
            }
        }

        this.changeHandler.onArmorChange(player, equipped);
        this.changeHandler.onSetBonus(player, setBonus);

        return ActionResult.PASS;
    }

    @Override
    public int getDurability(EquipmentSlot slot) {
        return BASE_DURABILITY[slot.getEntitySlotId()] * durabilityMultiplier;
//        return durability[slot.getEntitySlotId()];
    }

    @Override
    public int getProtectionAmount(EquipmentSlot slot) {
        return protectionAmounts[slot.getEntitySlotId()];
    }

    @Override
    public int getEnchantability() {
        return enchantability;
    }

    @Override
    public SoundEvent getEquipSound() {
        return null;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return repairIngredient;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public float getToughness() {
        return toughness;
    }

    @Override
    public float getKnockbackResistance() {
        return knockbackResistance;
    }

    private static String getSlotName(EquipmentSlot slot) {
        return switch (slot) {
            case MAINHAND -> "MAIN_HAND";
            case OFFHAND -> "OFF_HAND";
            case FEET -> "BOOTS";
            case LEGS -> "LEGGINGS";
            case CHEST -> "CHESTPLATE";
            case HEAD -> "HELMET";
        };
    }
}

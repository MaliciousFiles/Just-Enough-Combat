package io.github.maliciousfiles.jec.item.armor;

import io.github.maliciousfiles.jec.JEC;
import io.github.maliciousfiles.jec.event.ItemTooltipPartEvent;
import io.github.maliciousfiles.jec.util.UUIDGenerator;
import io.github.maliciousfiles.jec.util.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.living.LivingEquipmentChangeEvent;
import net.neoforged.neoforge.registries.DeferredItem;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;
import java.util.*;

// TODO: actual builder
@Mod.EventBusSubscriber(modid=JEC.MODID)
public class ArmorSet {
    private final EnumMap<ArmorItem.Type, DeferredItem<ArmorItem>> armorPieces = new EnumMap<>(ArmorItem.Type.class);
    private String name = "";
    private ArmorHandler handler = null;
    private final CustomArmorMaterial material = new CustomArmorMaterial();
    private ArmorWeight weight = ArmorWeight.LIGHT;
    private boolean hasItemBonus = false, vanilla = false;

    private ArmorSet() {}

    public ArmorSet register() {
        if (Arrays.stream(handler.getClass().getDeclaredMethods()).anyMatch(m->m.isAnnotationPresent(SubscribeEvent.class))) {
            NeoForge.EVENT_BUS.register(handler);
        }

        for (ArmorItem.Type type : ArmorItem.Type.values()) {
            DeferredItem<ArmorItem> item;
            if (vanilla) {
                item = DeferredItem.createItem(new ResourceLocation(name+"_"+type.getName()));
            } else {
                item = JEC.ITEMS.registerItem(name+"_"+type.getName(),
                        props -> new ArmorItem(material, type, props));
            }
            armorPieces.put(type, item);
        }

        ArmorSets.ARMOR_SETS.put(name, this);

        return this;
    }

    public DeferredItem<ArmorItem> getArmorPiece(ArmorItem.Type type) {
        return armorPieces.get(type);
    }

    public int getEquipped(LivingEntity entity) {
        int equipped = 0;
        for (ArmorItem.Type type : ArmorItem.Type.values()) {
            if (entity.getItemBySlot(type.getSlot()).getItem() == armorPieces.get(type).get()) equipped++;
        }
        return equipped;
    }

    private void armorChange(LivingEntity entity, ArmorItem.Type type, boolean equipped) {
        AttributeInstance speed = entity.getAttribute(Attributes.MOVEMENT_SPEED);
        UUID modifierUUID = UUIDGenerator.getUUID(type.ordinal());

        if (equipped) {
            handler.equip(entity, type);
            speed.addTransientModifier(new AttributeModifier(modifierUUID, "armor_weight", -weight.slowness, AttributeModifier.Operation.MULTIPLY_BASE));
        } else {
            handler.unequip(entity, type);
            speed.removeModifier(modifierUUID);
        }
    }

    @SubscribeEvent
    private static void onEquipmentChange(LivingEquipmentChangeEvent evt) {
        if (!evt.getSlot().isArmor()) return;

        ArmorSet from = ArmorSets.getArmorSet(evt.getFrom());
        ArmorSet to = ArmorSets.getArmorSet(evt.getTo());

        // Is there now a set bonus?
        boolean hasSet = to != null;

        // Check each slot for armor type
        for (ArmorItem.Type type : ArmorItem.Type.values()) {

            if (evt.getSlot() == type.getSlot()) {
                // For the slot that was changed, trigger handlers
                if (from != null) {
                    from.armorChange(evt.getEntity(), type, false);
                }
                if (to != null) {
                    to.armorChange(evt.getEntity(), type, true);
                }
            } else {
                // Check if other slots are same set as new piece
                Item item = evt.getEntity().getLastArmorItem(type.getSlot()).getItem();

                hasSet = hasSet && to.getArmorPiece(type).get() == item;
            }
        }

        IArmorable entity = (IArmorable) evt.getEntity();
        if (entity.jec$getSetBonus() != null) {
            entity.jec$setSetBonus(null);
            from.handler.deactivateSetBonus(evt.getEntity());
        }
        if (hasSet) {
            entity.jec$setSetBonus(to);
            to.handler.activateSetBonus(evt.getEntity());
        }

        evt.getEntity().setLastArmorItem(evt.getSlot(), evt.getTo());
    }

    @SubscribeEvent
    private static void onTooltip(ItemTooltipPartEvent evt) {
        ArmorSet set;
        if (evt.getPart() != ItemStack.TooltipPart.ADDITIONAL || (set = ArmorSets.getArmorSet(evt.getItemStack())) == null) return;

        if (set.hasItemBonus) {
            String key = "item_bonus." + set.name;

            evt.getToolTip().add(Component.empty());
            evt.getToolTip().add(Component.translatable(key + ".title").setStyle(Style.EMPTY.withUnderlined(true).withColor(Util.getRGB(220, 220, 220))));
            evt.getToolTip().add(Component.translatable(key + ".description").setStyle(Style.EMPTY.withColor(Util.getRGB(186, 186, 186)).withItalic(true)));
        }

        String key = "set_bonus." + set.name;
        int numArmor = evt.getEntity() == null ? 0 : set.getEquipped(evt.getEntity());

        evt.getToolTip().add(Component.empty());
        evt.getToolTip().add(Component.translatable(key + ".title").append(Component.literal(" (" + numArmor + "/4)")).setStyle(Style.EMPTY.withUnderlined(true).withColor(numArmor == 4 ? Util.getRGB(240, 240, 240) : Util.getRGB(100, 100, 100))));
        evt.getToolTip().add(Component.translatable(key + ".description").setStyle(Style.EMPTY.withColor(numArmor == ArmorItem.Type.values().length ? Util.getRGB(206, 206, 206) : Util.getRGB(66, 66, 66)).withItalic(true)));
    }

    public enum ArmorWeight {
        LIGHT(0),
        MEDIUM(0.03f),
        HEAVY(0.06f);

        public final float slowness;
        ArmorWeight(float slowness) {
            this.slowness = slowness;
        }

    }
    public static abstract class ArmorHandler {

        protected ArmorSet set;

        protected void equip(LivingEntity entity, ArmorItem.Type slot) {}
        protected void unequip(LivingEntity entity, ArmorItem.Type slot) {}
        protected void activateSetBonus(LivingEntity entity) {}
        protected void deactivateSetBonus(LivingEntity entity) {}
    }
    private class CustomArmorMaterial implements ArmorMaterial {
        private final EnumMap<ArmorItem.Type, Integer> defense = new EnumMap<>(ArmorItem.Type.class);
        private Ingredient repairIngredient = Ingredient.EMPTY;
        private int durabilityMultiplier = 1, enchantability = 0;
        private float toughness = 0, knockbackResistance = 0;

        @Override
        public int getDurabilityForType(ArmorItem.@NotNull Type type) {
            return ArmorMaterials.HEALTH_FUNCTION_FOR_TYPE.get(type)*durabilityMultiplier;
        }

        @Override
        public int getDefenseForType(ArmorItem.@NotNull Type type) {
            return defense.get(type);
        }

        @Override
        public int getEnchantmentValue() {
            return enchantability;
        }

        @Override
        public @NotNull SoundEvent getEquipSound() {
            return SoundEvents.ARMOR_EQUIP_GENERIC;
        }

        @Override
        public @NotNull Ingredient getRepairIngredient() {
            return repairIngredient;
        }

        @Override
        public @NotNull String getName() {
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
    }

    public static class Builder {
        private ArmorSet building = new ArmorSet();

        public Builder(String name, ArmorHandler handler) {
            building.name = name;
            building.handler = handler;
            building.handler.set = building;
        }

        public Builder weight(ArmorWeight weight) {
            building.weight = weight;
            return this;
        }

        public Builder defense(ArmorItem.Type type, int defense) {
            building.material.defense.put(type, defense);
            return this;
        }

        public Builder durability(int durabilityMultiplier) {
            building.material.durabilityMultiplier = durabilityMultiplier;
            return this;
        }

        public Builder enchantability(int enchantability) {
            building.material.enchantability = enchantability;
            return this;
        }

        public Builder toughness(float toughness) {
            building.material.toughness = toughness;
            return this;
        }

        public Builder knockbackResistance(float knockbackResistance) {
            building.material.knockbackResistance = knockbackResistance;
            return this;
        }

        public Builder hasItemBonus() {
            building.hasItemBonus = true;
            return this;
        }

        public Builder vanilla() {
            building.vanilla = true;
            return this;
        }

        public Builder repairIngredient(Ingredient repairIngredient) {
            building.material.repairIngredient = repairIngredient;
            return this;
        }

        public ArmorSet build() {
            ArmorSet built = building;
            building = new ArmorSet();

            return built.register();
        }
    }
}

package io.github.maliciousfiles.jec.items.armor;

import io.github.maliciousfiles.jec.JEC;
import io.github.maliciousfiles.jec.util.CustomTooltip;
import io.github.maliciousfiles.jec.util.LivingEquipEvent;
import io.github.maliciousfiles.jec.util.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.registries.DeferredItem;
import org.jetbrains.annotations.Nullable;

import java.util.*;

// TODO: make builder
public class ArmorSet {
    private static final String[] WEIGHT_UUIDS = new String[] {"b8d6a848-4a8c-42de-99e6-83deecc7fd6a", "c1cd0ee3-31fa-457e-89fa-38e8114d5654",
            "4b27c414-56f4-4a6b-babc-639cd5d96a1b", "19ae7caa-8ae2-4e4e-9397-804136c3fead"};

    private final EnumMap<ArmorItem.Type, DeferredItem<ArmorItem>> armorPieces = new EnumMap<>(ArmorItem.Type.class);
    private final ArmorWeight weight;
    private final ArmorHandler handler;

    private final boolean hasItemBonus;

    public ArmorSet(String name, ArmorWeight weight, ArmorHandler handler, int durabilityMultiplier, EnumMap<ArmorItem.Type, Integer> defense, int enchantability, float toughness, float knockbackResistance, Ingredient repairIngredient) {
        this(name, weight, handler, durabilityMultiplier, defense, enchantability, toughness, knockbackResistance, repairIngredient, false, false);
    }

    public ArmorSet(String name, ArmorWeight weight, ArmorHandler handler, int durabilityMultiplier, EnumMap<ArmorItem.Type, Integer> defense, int enchantability, float toughness, float knockbackResistance, Ingredient repairIngredient, boolean hasItemBonus, boolean isVanilla) {
        this.hasItemBonus = hasItemBonus;
        this.weight = weight;

        this.handler = handler;
        handler.set = this;
        if (Arrays.stream(handler.getClass().getMethods()).anyMatch(m->m.isAnnotationPresent(SubscribeEvent.class))) {
            NeoForge.EVENT_BUS.register(handler);
        }

        for (ArmorItem.Type type : ArmorItem.Type.values()) {
            armorPieces.put(type, (isVanilla ? JEC.VANILLA_ITEMS : JEC.ITEMS).registerItem(name+"_"+type.getName(),
                    props -> new ArmorPiece(
                            ArmorMaterials.HEALTH_FUNCTION_FOR_TYPE.get(type)*durabilityMultiplier,
                            defense.get(type), enchantability, repairIngredient,
                            name, toughness, knockbackResistance, type, props)));
        }
    }
    public class ArmorPiece extends ArmorItem implements CustomTooltip {
        public ArmorPiece(int durability, int defense, int enchantability, Ingredient repairIngredient, String name, float toughness, float knockbackResistance, Type type, Properties properties) {
            super(new ArmorMaterial() {
                public int getDurabilityForType(Type type) {return durability;}
                public int getDefenseForType(Type type) {return defense;}
                public int getEnchantmentValue() {return enchantability;}
                public SoundEvent getEquipSound() {return SoundEvents.ARMOR_EQUIP_GENERIC;}
                public Ingredient getRepairIngredient() {return repairIngredient;}
                public String getName() {return name;}
                public float getToughness() {return toughness;}
                public float getKnockbackResistance() {return knockbackResistance;}
            }, type, properties);
        }



        @Override
        public void appendCustomTooltip(ItemStack stack, @Nullable Player player, List<Component> tooltip, TooltipFlag flags, ItemStack.TooltipPart part) {
            if (!part.equals(ItemStack.TooltipPart.ADDITIONAL)) return;
            if (hasItemBonus) {
                String key = "item_bonus." + material.getName();

                tooltip.add(Component.empty());
                tooltip.add(Component.translatable(key + ".title").setStyle(Style.EMPTY.withUnderlined(true).withColor(Util.getRGB(220, 220, 220))));
                tooltip.add(Component.translatable(key + ".description").setStyle(Style.EMPTY.withColor(Util.getRGB(186, 186, 186)).withItalic(true)));
            }

            String key = "set_bonus." + material.getName();
            int numArmor = player == null ? 0 : getEquipped(player);

            tooltip.add(Component.empty());
            tooltip.add(Component.translatable(key + ".title").append(Component.literal(" (" + numArmor + "/4)")).setStyle(Style.EMPTY.withUnderlined(true).withColor(numArmor == 4 ? Util.getRGB(240, 240, 240) : Util.getRGB(100, 100, 100))));
            tooltip.add(Component.translatable(key + ".description").setStyle(Style.EMPTY.withColor(numArmor == Type.values().length ? Util.getRGB(206, 206, 206) : Util.getRGB(66, 66, 66)).withItalic(true)));
        }
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
        UUID modifierUUID = UUID.fromString(WEIGHT_UUIDS[type.ordinal()]);

        if (equipped) {
            handler.equip(entity, type);
            speed.addPermanentModifier(new AttributeModifier(modifierUUID, "armor_weight", -weight.slowness, AttributeModifier.Operation.MULTIPLY_BASE));
        } else {
            handler.unequip(entity, type);
            speed.removePermanentModifier(modifierUUID);
        }
    }

    @SubscribeEvent
    private void onEquipmentChange(LivingEquipEvent evt) {
        if (!(evt.getTo().getItem() instanceof ArmorPiece || evt.getFrom().getItem() instanceof ArmorPiece)) return;

        boolean hasSet = true;
        boolean hadSet = true;

        for (ArmorItem.Type type : ArmorItem.Type.values()) {
            if (evt.getSlot() == type.getSlot()) {
                if (evt.getTo().getItem() == armorPieces.get(type).get()) {
                    armorChange(evt.getEntity(), type, true);
                    hadSet = false;
                } else if (evt.getFrom().getItem() == armorPieces.get(type).get()) {
                    armorChange(evt.getEntity(), type, false);
                    hasSet = false;
                }
            } else {
                boolean equipped = evt.getEntity().getItemBySlot(type.getSlot()).getItem() == armorPieces.get(type).get();
                hasSet &= equipped;
                hadSet &= equipped;
            }
        }

        if (hasSet && !hadSet) handler.activateSetBonus(evt.getEntity());
        else if (!hasSet && hadSet) handler.deactivateSetBonus(evt.getEntity());
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
}

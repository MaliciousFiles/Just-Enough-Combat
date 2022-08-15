package io.github.maliciousfiles.pvpoverhaul.item;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import io.github.maliciousfiles.pvpoverhaul.AttributeInit;
import io.github.maliciousfiles.pvpoverhaul.PvPOverhaul;
import io.github.maliciousfiles.pvpoverhaul.event.EntityDamageCallback;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.Vec3d;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class LanceItem extends SwordItem implements EntityDamageCallback, TooltipHandler {

    private static final UUID ATTACK_REACH_MODIFIER = UUID.fromString("5e06c25d-bd18-4b71-b411-2e2f6cafbf40");

    public LanceItem(ToolMaterial toolMaterial, int attackDamage, float attackSpeed, Settings settings) {
        super(toolMaterial, attackDamage, attackSpeed, settings);

        EntityDamageCallback.EVENT.register(this);
        TooltipHandler.handlers.put(this, Map.entry(ItemStack.TooltipSection.MODIFIERS, this));
    }

    @Override
    public Multimap<EntityAttribute, EntityAttributeModifier> getAttributeModifiers(ItemStack stack, EquipmentSlot slot) {
        ImmutableMultimap.Builder<EntityAttribute, EntityAttributeModifier> builder = ImmutableMultimap.builder();
        builder.putAll(super.getAttributeModifiers(stack, slot));

        if (slot == EquipmentSlot.MAINHAND) builder.put(AttributeInit.ATTACK_REACH, new EntityAttributeModifier(ATTACK_REACH_MODIFIER, "Weapon modifier", 1, EntityAttributeModifier.Operation.ADDITION));

        return builder.build();
    }

    @Override
    public void appendTooltip(ItemStack stack, PlayerEntity player, List<Text> tooltip, TooltipContext context, ItemStack.TooltipSection section, int flags) {
        tooltip.add(ScreenTexts.EMPTY);
        tooltip.add(Text.translatable("item.modifiers.mount").formatted(Formatting.GRAY));
        tooltip.add(Text.translatable("attribute.modifier.plus." + EntityAttributeModifier.Operation.ADDITION.getId(), " ", Text.translatable(EntityAttributes.GENERIC_ATTACK_DAMAGE.getTranslationKey())).append(Text.literal(" ")).append(Text.translatable("item.modifiers.mount.text")).formatted(Formatting.BLUE));
    }

    @Override
    public float entityDamage(DamageSource source, Entity damaged, float damage) {
        // for some reason the vehicle doesn't have velocity server-side, so it has to be client-side
        if (source.getSource() instanceof PlayerEntity player && player.world.isClient && player.getRootVehicle() != player && player.getMainHandStack().getItem() instanceof LanceItem) {
            Vec3d vel = player.getRootVehicle().getVelocity();
            double speed = Math.sqrt(vel.x * vel.x + vel.z * vel.z);

            PvPOverhaul.LOGGER.info(speed+": "+(-1.93620778944 * Math.pow(12.878949995, -2.7*speed + 0.36) + 5));

            damage += -1.93620778944 * Math.pow(12.878949995, -2.7*speed + 0.36) + 5;
        }

        return damage;
    }
}

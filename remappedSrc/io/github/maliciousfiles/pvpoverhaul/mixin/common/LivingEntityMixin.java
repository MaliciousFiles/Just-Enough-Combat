package io.github.maliciousfiles.pvpoverhaul.mixin.common;

import com.mojang.logging.LogUtils;
import io.github.maliciousfiles.pvpoverhaul.AttributeInit;
import io.github.maliciousfiles.pvpoverhaul.entity.CustomEntityStatuses;
import io.github.maliciousfiles.pvpoverhaul.event.ArmorChangeCallback;
import io.github.maliciousfiles.pvpoverhaul.item.ItemInit;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.entity.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.SkeletonEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;
import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {

    // INSTANTIATE ATTACK REACH ATTRIBUTE
    @Inject(method = "createLivingAttributes", at = @At("RETURN"))
    private static void addAttributes(final CallbackInfoReturnable<DefaultAttributeContainer.Builder> info) {
        info.getReturnValue().add(AttributeInit.ATTACK_REACH);
    }

    // ARMOR CHANGE EVENT
    @Inject(at = @At("HEAD"), method = "onEquipStack")
    private void onSetArmor(EquipmentSlot slot, ItemStack oldStack, ItemStack newStack, CallbackInfo ci) {
        //noinspection ConstantConditions
        if (slot.getType() == EquipmentSlot.Type.ARMOR && (Object) this instanceof PlayerEntity player) {
            ArmorChangeCallback.EVENT.invoker().armorChange(player);
        }
    }

    // BONE ARMOR
    @Inject(at = @At("RETURN"), method = "getAttackDistanceScalingFactor", cancellable = true)
    private void modifyAttackDistanceScalingFactor(Entity entity, CallbackInfoReturnable<Double> cir) {
        //noinspection ConstantConditions
        if ((Object) this instanceof PlayerEntity player && entity instanceof SkeletonEntity && ItemInit.BONE.getEquipped(player) == 4) {
            cir.setReturnValue(cir.getReturnValueD() * 0.5);
        }
    }
}
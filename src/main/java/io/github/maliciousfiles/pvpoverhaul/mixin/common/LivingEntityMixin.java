package io.github.maliciousfiles.pvpoverhaul.mixin.common;

import io.github.maliciousfiles.pvpoverhaul.AttributeInit;
import io.github.maliciousfiles.pvpoverhaul.PvPOverhaul;
import io.github.maliciousfiles.pvpoverhaul.entity.shuriken.ShurikenEntity;
import io.github.maliciousfiles.pvpoverhaul.event.ArmorChangeCallback;
import io.github.maliciousfiles.pvpoverhaul.item.ItemInit;
import net.minecraft.entity.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.SkeletonEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
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

    // SHURIKEN REDUCED KNOCKBACK
    private boolean shuriken = false;
    @Inject(method = "damage", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;takeKnockback(DDD)V", shift= At.Shift.BEFORE))
    private void takeKnockback(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (source.getSource() instanceof ShurikenEntity && source.getName().equals("shuriken")) {
            shuriken = true;
        }
    }

    @Redirect(method = "damage", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;takeKnockback(DDD)V"))
    private void strength(LivingEntity instance, double strength, double x, double z) {
        if (shuriken) strength /= 2;

        shuriken = false;

        instance.takeKnockback(strength, x, z);
    }
}
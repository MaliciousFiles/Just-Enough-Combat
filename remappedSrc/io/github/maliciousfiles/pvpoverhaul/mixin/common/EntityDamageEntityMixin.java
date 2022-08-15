package io.github.maliciousfiles.pvpoverhaul.mixin.common;

import io.github.maliciousfiles.pvpoverhaul.entity.stone_golem.StoneGolemEntity;
import io.github.maliciousfiles.pvpoverhaul.event.EntityDamageCallback;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.pathing.EntityNavigation;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.EntityDamageSource;
import net.minecraft.entity.damage.ProjectileDamageSource;
import net.minecraft.entity.decoration.AbstractDecorationEntity;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.item.Item;
import net.minecraft.item.MilkBucketItem;
import net.minecraft.item.PotionItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = {Entity.class, LivingEntity.class, AbstractMinecartEntity.class, BoatEntity.class, AbstractDecorationEntity.class, ArmorStandEntity.class, ItemEntity.class})
public class EntityDamageEntityMixin {
    private float amount;

    // ENTITY DAMAGE EVENT
    @Inject(at = @At("HEAD"), method = "damage")
    private void onDamage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        this.amount = EntityDamageCallback.EVENT.invoker().entityDamage(source, (Entity) (Object) this, amount);

        //noinspection ConstantConditions
        if ((source instanceof EntityDamageSource || (source instanceof ProjectileDamageSource pds && pds.getAttacker() != null)) && (Object) this instanceof PlayerEntity entity && amount > 0) {
            Item item = entity.getActiveItem().getItem();
            if (item instanceof PotionItem || item.isFood() || item instanceof MilkBucketItem) {
                entity.clearActiveItem();
            }
        }
    }

    @ModifyVariable(at = @At(value = "HEAD", shift = At.Shift.AFTER), method = "damage", argsOnly = true)
    private float setDamage(float amount) {
        return this.amount;
    }
}

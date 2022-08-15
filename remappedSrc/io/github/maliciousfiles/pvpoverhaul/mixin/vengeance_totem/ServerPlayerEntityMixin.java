package io.github.maliciousfiles.pvpoverhaul.mixin.vengeance_totem;

import io.github.maliciousfiles.pvpoverhaul.PvPOverhaul;
import io.github.maliciousfiles.pvpoverhaul.entity.CustomEntityStatuses;
import io.github.maliciousfiles.pvpoverhaul.item.ItemInit;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageTracker;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

@Mixin(ServerPlayerEntity.class)
public class ServerPlayerEntityMixin {

    private ServerPlayerEntity getEntity() {
        return (ServerPlayerEntity) (Object) this;
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("HEAD"))
    private void writeCustomDataToNbt(NbtCompound nbt, CallbackInfo ci) {
        nbt.putInt("Vengeance", getEntity().getDataTracker().get(ItemInit.VENGEANCE_DATA));
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("HEAD"))
    private void readCustomDataFromNbt(NbtCompound nbt, CallbackInfo ci) {
        getEntity().getDataTracker().set(ItemInit.VENGEANCE_DATA, nbt.getInt("Vengeance"));
    }

    @Inject(method = "onDeath", at = @At("HEAD"), cancellable = true)
    private void onDeath(DamageSource source, CallbackInfo ci) {
        if (tryVengeance(getEntity(), source)) {
            getEntity().getDataTracker().set(ItemInit.VENGEANCE_DATA, 400);

            ci.cancel();
        }
    }

    @Redirect(method = "onDeath", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/damage/DamageTracker;getDeathMessage()Lnet/minecraft/text/Text;"))
    private Text customDeathMessage(DamageTracker instance) {
        if (instance.getMostRecentDamage().getDamageSource() == PvPOverhaul.VENGEANCE_TOTEM_TIME) {
            return Text.translatable("death.time.vengeance", getEntity().getDisplayName());
        }

        if (instance.getMostRecentDamage().getAttacker() instanceof PlayerEntity player && player.getDataTracker().get(ItemInit.VENGEANCE_DATA) > 0) {
            ItemStack item = player.getMainHandStack();
            String text = "death.attack.vengeance";

            return item.hasCustomName() ? Text.translatable(text+".item", player.getDisplayName(), getEntity().getDisplayName(), item.toHoverableText()) : Text.translatable(text, player.getDisplayName(), getEntity().getDisplayName());
        }

        return instance.getDeathMessage();
    }

    @Inject(method = "isInvulnerableTo", at = @At("RETURN"), cancellable = true)
    private void isInvulnerable(DamageSource damageSource, CallbackInfoReturnable<Boolean> cir) {
        if (!damageSource.isOutOfWorld() && damageSource != PvPOverhaul.VENGEANCE_TOTEM_TIME && getEntity().getDataTracker().get(ItemInit.VENGEANCE_DATA) > 0) {
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "tick", at = @At(value = "HEAD"))
    private void tick(CallbackInfo ci) {
        if (getEntity().getDataTracker().get(ItemInit.VENGEANCE_DATA) > 0) {
            int next = getEntity().getDataTracker().get(ItemInit.VENGEANCE_DATA) - 1;
            getEntity().getDataTracker().set(ItemInit.VENGEANCE_DATA, next);

            if (next == 0) {
                getEntity().damage(PvPOverhaul.VENGEANCE_TOTEM_TIME, Float.MAX_VALUE);
            }
        }
    }

    private boolean tryVengeance(LivingEntity living, DamageSource source) {
        if (source.isOutOfWorld() || source == PvPOverhaul.VENGEANCE_TOTEM_TIME) {
            return false;
        } else {
            ItemStack itemStack = null;

            for (Hand hand : Hand.values()) {
                ItemStack itemStack2 = living.getStackInHand(hand);
                if (itemStack2.isOf(ItemInit.TOTEM_OF_VENGEANCE)) {
                    itemStack = itemStack2.copy();
                    itemStack2.decrement(1);
                    break;
                }
            }

            if (itemStack != null) {
                if (living instanceof ServerPlayerEntity player) {
                    player.incrementStat(Stats.USED.getOrCreateStat(ItemInit.TOTEM_OF_VENGEANCE));
                }

                living.setHealth(20);
                living.clearStatusEffects();
                living.world.sendEntityStatus(living, CustomEntityStatuses.USE_TOTEM_OF_VENGEANCE);
            }

            return itemStack != null;
        }
    }
}

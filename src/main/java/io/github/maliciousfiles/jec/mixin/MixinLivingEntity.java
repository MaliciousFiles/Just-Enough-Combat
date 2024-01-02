package io.github.maliciousfiles.jec.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import io.github.maliciousfiles.jec.attack.ISwingable;
import io.github.maliciousfiles.jec.attack.ISwinger;
import io.github.maliciousfiles.jec.entity.projectile.weapon.ThrowableWeaponEntities;
import io.github.maliciousfiles.jec.entity.projectile.weapon.ThrowableWeaponEntity;
import io.github.maliciousfiles.jec.item.armor.ArmorSet;
import io.github.maliciousfiles.jec.item.armor.IArmorable;
import io.github.maliciousfiles.jec.packets.ClientboundAnimateSwingPayload;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;
import java.util.Map;

@Mixin(LivingEntity.class)
public class MixinLivingEntity implements ISwinger, IArmorable {
    @Shadow public int swingTime;

    // define all the stuck throwable weapon synched data
    @Inject(method="defineSynchedData", at=@At("TAIL"))
    public void defineThrowableWeaponCount(CallbackInfo ci) {
        ThrowableWeaponEntities.getThrowableWeapons().stream().map(ThrowableWeaponEntities::getThrowableWeaponData).forEach(data -> ((LivingEntity) (Object) this).getEntityData().define(data, 0));
    }

    // need to register the data accessors when the class is loaded to avoid overlapping IDs
    @Inject(method="<clinit>", at=@At("TAIL"))
    private static void registerThrowableWeapons(CallbackInfo ci) {
        ThrowableWeaponEntities.init();
    }

    @Unique
    private final Map<EntityType<? extends ThrowableWeaponEntity>, Integer> jec$removeTime = new HashMap<>();

    // decrement stuck throwable weapon count
    @Inject(method="tick", at=@At(value="INVOKE", target="Lnet/minecraft/world/entity/LivingEntity;getArrowCount()I"))
    private void decrementThrowableWeaponCount(CallbackInfo ci) {
        LivingEntity living = (LivingEntity) (Object) this;
        ThrowableWeaponEntities.getThrowableWeapons().forEach(type -> {
            EntityDataAccessor<Integer> data = ThrowableWeaponEntities.getThrowableWeaponData(type);

            int count = living.getEntityData().get(data);
            if (count > 0) {
                int time = jec$removeTime.getOrDefault(type, 0);
                if (time <= 0) {
                    time = 20 * (30 - count);
                }

                time--;
                if (time <= 0) {
                    living.getEntityData().set(data, count - 1);
                }

                jec$removeTime.put(type, time);
            }
        });
    }

    // custom swing animation stuff
    @Unique
    private ISwingable.SwingType jec$swingType;
    @Unique
    private int jec$swingDuration = 1;

    // TODO: while swinging, prevent changing held item
    @WrapOperation(method="updateSwingTime", at=@At(value="INVOKE", target="Lnet/minecraft/world/entity/LivingEntity;getCurrentSwingDuration()I"))
    private int swingDuration(LivingEntity instance, Operation<Integer> original) {
        if (!instance.swinging) {
            if (jec$swingType != null && instance.level() instanceof ServerLevel level) {
                jec$swingType.makeParticle(instance, level);
            }
            jec$swingType = null;
        }

        return jec$swingType != null ? jec$swingDuration : original.call(instance);
    }

    // TODO: prevent swinging while swing is happening clientside, since serverside duration is shorter
    @Override
    public void jec$swing(ISwingable.SwingType type, float duration) {
        if (jec$swingType != null) return;

        LivingEntity entity = (LivingEntity) (Object) this;

        entity.swingingArm = InteractionHand.MAIN_HAND;
        entity.swingTime = -1;
        entity.swinging = true;
        jec$swingType = type;

        if (!entity.level().isClientSide) {
            ((ServerLevel) entity.level()).getChunkSource().broadcastAndSend(entity, new ClientboundAnimateSwingPayload(entity.getId(), type, duration));
            duration *= ISwingable.SwingType.CHARGE_TIME;
        }

        jec$swingDuration = Math.round(duration*20);
    }

    @Override
    public ISwingable.SwingType jec$getCurrentSwingType() {
        return jec$swingType;
    }
}

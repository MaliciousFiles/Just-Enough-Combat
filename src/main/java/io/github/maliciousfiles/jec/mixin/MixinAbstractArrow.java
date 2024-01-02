package io.github.maliciousfiles.jec.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import io.github.maliciousfiles.jec.entity.projectile.weapon.ThrowableWeaponEntities;
import io.github.maliciousfiles.jec.entity.projectile.weapon.ThrowableWeaponEntity;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(AbstractArrow.class)
public class MixinAbstractArrow {
    @WrapOperation(method="onHitEntity", at=@At(value="INVOKE", target="Lnet/minecraft/world/entity/LivingEntity;setArrowCount(I)V"))
    private void setThrowableWeaponCount(LivingEntity instance, int count, Operation<Void> original) {
        if ((Object) this instanceof ThrowableWeaponEntity weapon) {
            EntityDataAccessor<Integer> data = ThrowableWeaponEntities.getThrowableWeaponData((EntityType<? extends ThrowableWeaponEntity>) weapon.getType());
            instance.getEntityData().set(data, instance.getEntityData().get(data)+1);
        } else {
            original.call(instance, count);
        }
    }
}

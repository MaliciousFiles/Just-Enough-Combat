package io.github.maliciousfiles.jec.entity.projectile.weapon;

import io.github.maliciousfiles.jec.item.weapon.ThrowableWeapon;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class SpinningThrowableWeaponEntity extends ThrowableWeaponEntity {
    public SpinningThrowableWeaponEntity(EntityType<? extends ThrowableWeaponEntity> type, Level level, ItemStack item) {
        super(type, level, item);
    }

    public SpinningThrowableWeaponEntity(EntityType<? extends ThrowableWeaponEntity> type, LivingEntity owner, Level level, ItemStack item) {
        super(type, owner, level, item);
    }

    private float spin = 0;

    public float updateSpin(float tickDelta) {
        return spin < 0 ? 0 : inGround ? spin : (spin += 16 * tickDelta);
    }
}

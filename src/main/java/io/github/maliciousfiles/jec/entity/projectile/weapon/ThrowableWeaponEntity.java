package io.github.maliciousfiles.jec.entity.projectile.weapon;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

// TODO: after deflecting, only renders for them
// TODO: loyalty doesn't work
public class ThrowableWeaponEntity extends AbstractArrow implements ItemSupplier {
    // basically all the loyalty stuff is taken straight from ThrownTrident
    private static final EntityDataAccessor<Byte> LOYALTY_LEVEL = SynchedEntityData.defineId(ThrowableWeaponEntity.class, EntityDataSerializers.BYTE);
    private boolean shouldReturn;

    public ThrowableWeaponEntity(EntityType<? extends ThrowableWeaponEntity> type, Level level, ItemStack item) {
        super(type, level, item);
    }

    public ThrowableWeaponEntity(EntityType<? extends ThrowableWeaponEntity> type, LivingEntity owner, Level level, ItemStack item) {
        super(type, owner, level, item);
    }

    // TODO: do better
    private int returnTick = 0;
    @Override
    public void tick() {
        shouldReturn |= inGroundTime > 4;

        Entity owner = this.getOwner();
        int loyalty = getLoyaltyLevel();
        if (loyalty > 0 && (this.shouldReturn || this.isNoPhysics()) && owner != null) {
            if (!this.isValidOwner()) {
                if (!this.level().isClientSide && this.pickup == Pickup.ALLOWED) {
                    this.spawnAtLocation(this.getPickupItem(), 0.1F);
                }

                this.discard();
            } else {
                this.setNoPhysics(true);
                Vec3 vec3 = owner.getEyePosition().subtract(this.position());
                this.setPosRaw(this.getX(), this.getY() + vec3.y * 0.015 * loyalty, this.getZ());
                if (this.level().isClientSide) {
                    this.yOld = this.getY();
                }

                double d0 = 0.05 * loyalty;
                this.setDeltaMovement(this.getDeltaMovement().scale(0.95).add(vec3.normalize().scale(d0)));
                if (returnTick == 0) {
                    this.playSound(SoundEvents.TRIDENT_RETURN, 10.0F, 1.0F);
                }
                returnTick++;
            }
        }

        super.tick();
    }

    // always called in #hitEntity
    @Override
    public DamageSources damageSources() {
        this.shouldReturn = true;
        return super.damageSources();
    }

    private boolean isValidOwner() {
        Entity owner = this.getOwner();

        return owner != null && owner.isAlive() && (!(owner instanceof ServerPlayer) || !owner.isSpectator());
    }

    @Override
    protected void tickDespawn() {
        if (this.pickup != Pickup.ALLOWED || getLoyaltyLevel() <= 0) {
            super.tickDespawn();
        }
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(LOYALTY_LEVEL, (byte)0);
    }

    public void setLoyaltyLevel(int level) {
        this.entityData.set(LOYALTY_LEVEL, (byte)level);
    }

    public byte getLoyaltyLevel() {
        return this.entityData.get(LOYALTY_LEVEL);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.setLoyaltyLevel(tag.getByte("LoyaltyLevel"));
    }

    @Override
    public ItemStack getItem() {
        return getPickupItem();
    }
}

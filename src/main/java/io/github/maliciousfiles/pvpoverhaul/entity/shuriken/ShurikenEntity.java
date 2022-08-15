package io.github.maliciousfiles.pvpoverhaul.entity.shuriken;

import com.google.common.collect.Lists;
import io.github.maliciousfiles.pvpoverhaul.entity.EntityInit;
import io.github.maliciousfiles.pvpoverhaul.item.ItemInit;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.ProjectileDamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.GameStateChangeS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;

public class ShurikenEntity extends PersistentProjectileEntity {

    private static final TrackedData<Byte> LOYALTY = DataTracker.registerData(ShurikenEntity.class, TrackedDataHandlerRegistry.BYTE);
    private static final TrackedData<ItemStack> ITEM = DataTracker.registerData(ShurikenEntity.class, TrackedDataHandlerRegistry.ITEM_STACK);

    private float spin = 0.0f;
    private boolean shouldReturn;
    public int returnTimer;
    private IntOpenHashSet piercedEntities;
    private List<Entity> piercingKilledEntities;

    public ShurikenEntity(EntityType<? extends ShurikenEntity> type, World world) {
        super(type, world);
    }

    public ShurikenEntity(World world, double x, double y, double z) {
        super(EntityInit.SHURIKEN, x, y, z, world);
    }

    public ShurikenEntity(World world, LivingEntity owner, ItemStack item) {
        super(EntityInit.SHURIKEN, owner, world);

        item = item.copy();
        item.setCount(1);

        this.setPierceLevel((byte) EnchantmentHelper.getLevel(Enchantments.PIERCING, item));
        this.getDataTracker().set(LOYALTY, (byte) EnchantmentHelper.getLoyalty(item));
        this.getDataTracker().set(ITEM, item);
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();

        getDataTracker().startTracking(LOYALTY, (byte) 0);
        getDataTracker().startTracking(ITEM, new ItemStack(ItemInit.SHURIKEN));
    }

    private boolean isOwnerAlive() {
        Entity entity = this.getOwner();
        if (entity != null && entity.isAlive()) {
            return !(entity instanceof ServerPlayerEntity) || !entity.isSpectator();
        } else {
            return false;
        }
    }

    public void tick() {
        if (this.inGroundTime > 4 && getDataTracker().get(LOYALTY) > 0) {
            this.shouldReturn = true;
        }

        Entity entity = this.getOwner();
        int loyalty = this.dataTracker.get(LOYALTY);
        if (loyalty > 0 && (this.shouldReturn || this.isNoClip()) && entity != null) {
            if (!this.isOwnerAlive()) {
                if (!this.world.isClient && this.pickupType == PickupPermission.ALLOWED) {
                    this.dropStack(this.asItemStack(), 0.1F);
                }

                this.discard();
            } else {
                this.setNoClip(true);
                Vec3d vec3d = entity.getEyePos().subtract(this.getPos());
                this.setPos(this.getX(), this.getY() + vec3d.y * 0.015 * (double)loyalty, this.getZ());
                if (this.world.isClient) {
                    this.lastRenderY = this.getY();
                }

                double d = (0.05/3) * (double)loyalty; // quarter as fast as a trident
                this.setVelocity((this.returnTimer == 0 ? Vec3d.ZERO : this.getVelocity()).multiply(0.95).add(vec3d.normalize().multiply(d)));
                if (this.returnTimer == 0) {
                    this.playSound(SoundEvents.ITEM_TRIDENT_RETURN, 10.0F, 1.0F);
                }

                ++this.returnTimer;
            }
        }

        super.tick();
    }

    @Override
    protected EntityHitResult getEntityCollision(Vec3d currentPosition, Vec3d nextPosition) {
        return this.shouldReturn ? null : super.getEntityCollision(currentPosition, nextPosition);
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        Entity entity = entityHitResult.getEntity();

        float damage = (float) this.getDamage();
        if (entity instanceof LivingEntity livingEntity) {
            damage += EnchantmentHelper.getAttackDamage(asItemStack(), livingEntity.getGroup());
        }

        if (this.getPierceLevel() > 0) {
            if (this.piercedEntities == null) {
                this.piercedEntities = new IntOpenHashSet(5);
            }

            if (this.piercingKilledEntities == null) {
                this.piercingKilledEntities = Lists.newArrayListWithCapacity(5);
            }

            if (this.piercedEntities.size() >= this.getPierceLevel() + 1) {
                discardOrReturn();
                return;
            }

            this.piercedEntities.add(entity.getId());
        }

        if (this.isCritical()) {
            long l = this.random.nextInt((int) (damage / 2 + 2));
            damage = (int)Math.min(l + damage, 2147483647L);
        }

        Entity owner = this.getOwner();
        DamageSource damageSource;
        if (owner == null) {
            damageSource = shurikenDamageSource(this, this);
        } else {
            damageSource = shurikenDamageSource(this, owner);
            if (owner instanceof LivingEntity) {
                ((LivingEntity)owner).onAttacking(entity);
            }
        }

        if (entity.damage(damageSource, (float)damage)) {
            if (entity.getType() == EntityType.ENDERMAN) {
                return;
            }

            if (entity instanceof LivingEntity livingEntity) {
                if (!this.world.isClient && this.getPierceLevel() <= 0 && getDataTracker().get(LOYALTY) <= 0 && livingEntity instanceof PlayerEntity player) {
                    player.getDataTracker().set(ItemInit.STUCK_SHURIKEN_DATA, player.getDataTracker().get(ItemInit.STUCK_SHURIKEN_DATA) + 1);
                }

                if (!this.world.isClient && owner instanceof LivingEntity) {
                    EnchantmentHelper.onUserDamaged(livingEntity, owner);
                    EnchantmentHelper.onTargetDamaged((LivingEntity)owner, livingEntity);
                }

                this.onHit(livingEntity);
                if (livingEntity != owner && livingEntity instanceof PlayerEntity && owner instanceof ServerPlayerEntity && !this.isSilent()) {
                    ((ServerPlayerEntity)owner).networkHandler.sendPacket(new GameStateChangeS2CPacket(GameStateChangeS2CPacket.PROJECTILE_HIT_PLAYER, 0.0F));
                }

                if (!entity.isAlive() && this.piercingKilledEntities != null) {
                    this.piercingKilledEntities.add(livingEntity);
                }

                if (!this.world.isClient && owner instanceof ServerPlayerEntity serverPlayerEntity) {
                    if (this.piercingKilledEntities != null && this.isShotFromCrossbow()) {
                        Criteria.KILLED_BY_CROSSBOW.trigger(serverPlayerEntity, this.piercingKilledEntities);
                    } else if (!entity.isAlive() && this.isShotFromCrossbow()) {
                        Criteria.KILLED_BY_CROSSBOW.trigger(serverPlayerEntity, List.of(entity));
                    }
                }
            }

            this.playSound(this.getSound(), 1.0F, 1.2F / (this.random.nextFloat() * 0.2F + 0.9F));
            if (this.getPierceLevel() <= 0) {
                discardOrReturn();
            }
        } else {
            this.setVelocity(this.getVelocity().multiply(-0.1));
            this.setYaw(this.getYaw() + 180.0F);
            this.prevYaw += 180.0F;
            if (!this.world.isClient && this.getVelocity().lengthSquared() < 1.0E-7) {
                discardOrReturn();

                if (this.pickupType == PersistentProjectileEntity.PickupPermission.ALLOWED && !this.shouldReturn) {
                    this.dropStack(this.asItemStack(), 0.1F);
                }
            }
        }
    }

    @Override
    protected void onBlockHit(BlockHitResult blockHitResult) {
        super.onBlockHit(blockHitResult);

        if (piercedEntities != null) piercedEntities.clear();
        if (piercingKilledEntities != null) piercingKilledEntities.clear();
    }

    @Override
    protected boolean canHit(Entity entity) {
        return super.canHit(entity) && (this.piercedEntities == null || !this.piercedEntities.contains(entity.getId()));
    }

    private void discardOrReturn() {
        if (getDataTracker().get(LOYALTY) <= 0) {
            this.discard();
        } else {
            this.shouldReturn = true;
        }
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);

        if (nbt.contains("Shuriken", 10)) {
            getDataTracker().set(ITEM, ItemStack.fromNbt(nbt.getCompound("Shuriken")));
        }
        this.shouldReturn = nbt.getBoolean("ShouldReturn");
        this.dataTracker.set(LOYALTY, (byte)EnchantmentHelper.getLoyalty(asItemStack()));
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);

        nbt.put("Shuriken", asItemStack().writeNbt(new NbtCompound()));
        nbt.putBoolean("ShouldReturn", this.shouldReturn);
    }

    @Override
    public void age() {
        if (this.dataTracker.get(LOYALTY) <= 0) {
            super.age();
        }
    }

    @Override
    protected ItemStack asItemStack() {
        return getDataTracker().get(ITEM).copy();
    }

    @Override
    protected boolean tryPickup(PlayerEntity player) {
        return super.tryPickup(player) || this.isNoClip() && this.isOwner(player) && player.getInventory().insertStack(this.asItemStack());
    }

    public void onPlayerCollision(PlayerEntity player) {
        if (this.isOwner(player) || this.getOwner() == null) {
            super.onPlayerCollision(player);
        }

    }

    public void updateSpin(float tickDelta) {
        if (!this.inGround) {
            this.spin += this.getVelocity().length()*24 * tickDelta;
        }
    }
    public float getSpin() {return spin;}


    public static DamageSource shurikenDamageSource(PersistentProjectileEntity source, Entity attacker) {
        return new ProjectileDamageSource("shuriken", source, attacker).setProjectile();
    }
}

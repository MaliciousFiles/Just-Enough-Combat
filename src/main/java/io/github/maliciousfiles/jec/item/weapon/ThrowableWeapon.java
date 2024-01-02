package io.github.maliciousfiles.jec.item.weapon;

import io.github.maliciousfiles.jec.entity.projectile.weapon.ThrowableWeaponEntity;
import net.minecraft.data.models.blockstates.PropertyDispatch;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;

import java.util.function.Supplier;

// TODO: charging
// TODO: display ranged damage in tooltip
public class ThrowableWeapon extends Item {
    private final PropertyDispatch.TriFunction<LivingEntity, Level, ItemStack, ThrowableWeaponEntity> entitySupplier;
    private final int cooldown;
    private final double baseDamage;
    private final SoundEvent soundEvent;

    public ThrowableWeapon(Supplier<EntityType<? extends ThrowableWeaponEntity>> type, PropertyDispatch.QuadFunction<EntityType<? extends ThrowableWeaponEntity>, LivingEntity, Level, ItemStack, ThrowableWeaponEntity> entitySupplier, Properties properties, ThrowableProperties throwableProps) {
        super(properties);

        this.entitySupplier = (owner, level, stack) -> entitySupplier.apply(type.get(), owner, level, stack);
        this.cooldown = throwableProps.cooldown;
        this.baseDamage = throwableProps.baseDamage;
        this.soundEvent = throwableProps.soundEvent;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        // TODO: sound
        level.playSound(null, player.getX(), player.getY(), player.getZ(), soundEvent, SoundSource.NEUTRAL, 0.5F, 0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F));

        player.getCooldowns().addCooldown(this, cooldown);

        if (!level.isClientSide) {
            int projectiles = Math.min((player.isCreative() ? stack.getMaxStackSize() : stack.getCount())-1, stack.getEnchantmentLevel(Enchantments.MULTISHOT) * 2);

            for (int i = -Mth.ceil(projectiles/2f); i <= Mth.floor(projectiles/2f); i++) {
                ThrowableWeaponEntity entity = entitySupplier.apply(player, level, stack.copyWithCount(1));

                entity.setOwner(player);
                entity.setBaseDamage(baseDamage);
                entity.setPierceLevel((byte) stack.getEnchantmentLevel(Enchantments.PIERCING));
                entity.shootFromRotation(player, player.getXRot(), player.getYRot(), 0, 2.5f, 1.0f);

                stack.setCount(stack.getCount()-1);
                level.addFreshEntity(entity);
            }
        }

        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
    }

    public static class ThrowableProperties {
        protected int cooldown = 0;
        protected double baseDamage = 0;
        protected SoundEvent soundEvent = SoundEvents.EGG_THROW;

        public ThrowableProperties cooldown(int cooldown) {
            this.cooldown = cooldown;
            return this;
        }

        public ThrowableProperties baseDamage(double baseDamage) {
            this.baseDamage = baseDamage;
            return this;
        }

        public ThrowableProperties soundEvent(SoundEvent soundEvent) {
            this.soundEvent = soundEvent;
            return this;
        }
    }
}

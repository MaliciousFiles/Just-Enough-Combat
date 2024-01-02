package io.github.maliciousfiles.jec.entity.projectile.weapon;

import io.github.maliciousfiles.jec.JEC;
import io.github.maliciousfiles.jec.entity.projectile.weapon.SpinningThrowableWeaponEntity;
import io.github.maliciousfiles.jec.entity.projectile.weapon.ThrowableWeaponEntity;
import io.github.maliciousfiles.jec.item.weapon.ThrowableWeapon;
import io.github.maliciousfiles.jec.item.weapon.Weapons;
import io.github.maliciousfiles.jec.util.DeferredDispenseItemBehaviorRegistry;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.core.Position;
import net.minecraft.core.dispenser.AbstractProjectileDispenseBehavior;
import net.minecraft.data.models.blockstates.PropertyDispatch;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mod.EventBusSubscriber(modid = JEC.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ThrowableWeaponEntities {
    private static final Map<DeferredHolder<EntityType<?>, ? extends EntityType<? extends ThrowableWeaponEntity>>, EntityDataAccessor<Integer>> THROWABLE_WEAPONS = new HashMap<>();

    public static final DeferredHolder<EntityType<?>, EntityType<SpinningThrowableWeaponEntity>> SHURIKEN = registerThrowableWeapon("shuriken", Weapons.SHURIKEN, SpinningThrowableWeaponEntity::new);

    private static <T extends ThrowableWeaponEntity> DeferredHolder<EntityType<?>, EntityType<T>> registerThrowableWeapon(String name, DeferredItem<? extends ThrowableWeapon> item, PropertyDispatch.TriFunction<EntityType<T>, Level, ItemStack, T> constructor) {
        DeferredHolder<EntityType<?>, EntityType<T>> holder = register(name, EntityType.Builder.<T>of((type, level) -> constructor.apply(type, level, item.get().getDefaultInstance()), MobCategory.MISC)
                .sized(0.5f, 0.5f).clientTrackingRange(4).updateInterval(20));

        DeferredDispenseItemBehaviorRegistry.register(item, new AbstractProjectileDispenseBehavior() {
            @Override
            protected Projectile getProjectile(Level level, Position pos, ItemStack itemStack) {
                ThrowableWeaponEntity projectile = constructor.apply(holder.get(), level, itemStack.copyWithCount(1));
                projectile.setPos(pos.x(), pos.y(), pos.z());
                projectile.pickup = AbstractArrow.Pickup.ALLOWED;

                return projectile;
            }
        });

        THROWABLE_WEAPONS.put(holder, SynchedEntityData.defineId(LivingEntity.class, EntityDataSerializers.INT));
        return holder;
    }

    private static <T extends Entity> DeferredHolder<EntityType<?>, EntityType<T>> register(String name, EntityType.Builder<T> builder) {
        return JEC.ENTITY_TYPES.register(name, () -> builder.build(name));
    }

    public static List<? extends EntityType<? extends ThrowableWeaponEntity>> getThrowableWeapons() {
        return THROWABLE_WEAPONS.keySet().stream().map(DeferredHolder::get).toList();
    }

    public static EntityDataAccessor<Integer> getThrowableWeaponData(EntityType<? extends ThrowableWeaponEntity> type) {
        for (Map.Entry<DeferredHolder<EntityType<?>, ? extends EntityType<? extends ThrowableWeaponEntity>>, EntityDataAccessor<Integer>> entry : THROWABLE_WEAPONS.entrySet()) {
            if (entry.getKey().get() == type) return entry.getValue();
        }

        return null;
    }

    @SubscribeEvent
    public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers evt) {
        THROWABLE_WEAPONS.keySet().forEach(holder->evt.registerEntityRenderer(holder.get(), context -> new ThrownItemRenderer<>(context, 1.5f, false)));
    }

    public static void init() {}
}

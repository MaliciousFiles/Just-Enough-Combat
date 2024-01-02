package io.github.maliciousfiles.jec.item.weapon;

import io.github.maliciousfiles.jec.JEC;
import io.github.maliciousfiles.jec.entity.projectile.weapon.ThrowableWeaponEntities;
import io.github.maliciousfiles.jec.entity.projectile.weapon.SpinningThrowableWeaponEntity;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;

import java.util.ArrayList;
import java.util.List;

public class Weapons {
    private static final List<DeferredItem<? extends Item>> WEAPONS = new ArrayList<>();

    private static final DeferredHolder<CreativeModeTab, CreativeModeTab> WEAPONS_TAB = JEC.CREATIVE_MODE_TABS.register("weapons", () -> CreativeModeTab.builder()
            .title(Component.literal("Just Enough Weapons"))
            .icon(Items.IRON_SWORD::getDefaultInstance)
            .displayItems((parameters, output) -> {
                output.acceptAll(WEAPONS.stream().map(DeferredItem::get).map(Item::getDefaultInstance).toList());
            }).build());

    public static final DeferredItem<ThrowableWeapon> SHURIKEN = register(JEC.ITEMS.registerItem("shuriken",
            properties->new ThrowableWeapon(ThrowableWeaponEntities.SHURIKEN::get, SpinningThrowableWeaponEntity::new,
                    properties.stacksTo(16),
                    new ThrowableWeapon.ThrowableProperties()
                            .baseDamage(1)
                            .cooldown(15))));

    private static <I extends Item> DeferredItem<I> register(DeferredItem<I> item) {
        WEAPONS.add(item);
        return item;
    }

    public static void init() {}
}

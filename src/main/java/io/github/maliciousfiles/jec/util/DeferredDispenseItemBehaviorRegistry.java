package io.github.maliciousfiles.jec.util;

import io.github.maliciousfiles.jec.JEC;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.DispenserBlock;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.RegisterEvent;

import java.util.HashMap;
import java.util.Map;

@Mod.EventBusSubscriber(modid= JEC.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DeferredDispenseItemBehaviorRegistry {

    private static final Map<DeferredItem<?>, DispenseItemBehavior> REGISTRY = new HashMap<>();

    public static void register(DeferredItem<?> item, DispenseItemBehavior behavior) {
        REGISTRY.put(item, behavior);
    }

    @SubscribeEvent
    public static void onItemsRegister(FMLCommonSetupEvent evt) {
        REGISTRY.forEach(DispenserBlock::registerBehavior);
    }
}

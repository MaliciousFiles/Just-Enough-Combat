package io.github.maliciousfiles.jec;

import io.github.maliciousfiles.jec.item.weapon.Weapons;
import io.github.maliciousfiles.jec.item.armor.ArmorSets;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// PARTICLE METHODS: x, y, z, count, xDist, yDist, zDist, maxSpeed
@Mod(JEC.MODID)
public class JEC
{
    public static final String MODID = "jec";
    public static final Logger LOGGER = LogManager.getLogger();

    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(MODID);
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MODID);
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(Registries.ENTITY_TYPE, MODID);
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    public JEC(IEventBus modEventBus)
    {
        BLOCKS.register(modEventBus);
        ITEMS.register(modEventBus);
        ENTITY_TYPES.register(modEventBus);
        CREATIVE_MODE_TABS.register(modEventBus);

        // clinit
        ArmorSets.init();
        Weapons.init();
    }

    public static ResourceLocation location(String name) {
        return new ResourceLocation(MODID, name);
    }
}

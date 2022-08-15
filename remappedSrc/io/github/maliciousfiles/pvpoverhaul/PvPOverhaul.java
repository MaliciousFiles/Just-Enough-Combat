package io.github.maliciousfiles.pvpoverhaul;

import io.github.maliciousfiles.pvpoverhaul.entity.EntityInit;
import io.github.maliciousfiles.pvpoverhaul.item.ItemInit;
import io.github.maliciousfiles.pvpoverhaul.particle.ParticleInit;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class PvPOverhaul implements ModInitializer {

    public static final String MOD_ID = "pvp-overhaul";
    public static final Logger LOGGER = LoggerFactory.getLogger("PvP Overhaul");

    public static final ItemGroup ITEM_GROUP = FabricItemGroupBuilder.create(new Identifier(MOD_ID, "general"))
            .icon(() -> new ItemStack(Items.DIAMOND_SWORD))
            .build();

    public static final DamageSource VENGEANCE_TOTEM_TIME;

    static {
        try {
            Constructor<DamageSource> init = DamageSource.class.getDeclaredConstructor(String.class);
            init.setAccessible(true);

            VENGEANCE_TOTEM_TIME = init.newInstance("vengeance");
        } catch (InvocationTargetException | NoSuchMethodException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onInitialize() {
        ItemInit.init();
        EntityInit.init();
        AttributeInit.init();
        ParticleInit.init();
    }

    public static Identifier identifier(String id) {
        return new Identifier(MOD_ID, id);
    }

    public static FabricItemSettings settings() {
        return new FabricItemSettings().group(ITEM_GROUP);
    }
}

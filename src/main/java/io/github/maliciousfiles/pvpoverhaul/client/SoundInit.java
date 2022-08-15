package io.github.maliciousfiles.pvpoverhaul.client;

import io.github.maliciousfiles.pvpoverhaul.PvPOverhaul;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.registry.Registry;

public class SoundInit {

    public static final SoundEvent ITEM_VENGEANCE_TOTEM_USE = new SoundEvent(PvPOverhaul.identifier("item.vengeance_totem.use"));

    public static void clientInit() {
        Registry.register(Registry.SOUND_EVENT, ITEM_VENGEANCE_TOTEM_USE.getId(), ITEM_VENGEANCE_TOTEM_USE);
    }
}

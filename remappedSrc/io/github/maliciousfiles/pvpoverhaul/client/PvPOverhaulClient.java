package io.github.maliciousfiles.pvpoverhaul.client;

import io.github.maliciousfiles.pvpoverhaul.entity.EntityRendererInit;
import io.github.maliciousfiles.pvpoverhaul.particle.ParticleClientInit;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class PvPOverhaulClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        EntityRendererInit.clientInit();
        ParticleClientInit.clientInit();
        SoundInit.clientInit();
    }
}

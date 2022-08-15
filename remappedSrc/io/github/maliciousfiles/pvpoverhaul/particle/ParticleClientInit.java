package io.github.maliciousfiles.pvpoverhaul.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;

@Environment(EnvType.CLIENT)
public class ParticleClientInit {

    public static void clientInit() {
        ParticleFactoryRegistry.getInstance().register(ParticleInit.TOTEM_OF_VENGEANCE, VengeanceTotemParticle.Factory::new);
    }
}

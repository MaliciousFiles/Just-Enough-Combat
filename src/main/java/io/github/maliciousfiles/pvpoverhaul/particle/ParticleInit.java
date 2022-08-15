package io.github.maliciousfiles.pvpoverhaul.particle;

import io.github.maliciousfiles.pvpoverhaul.PvPOverhaul;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.registry.Registry;

public class ParticleInit {

    public static final DefaultParticleType TOTEM_OF_VENGEANCE = FabricParticleTypes.simple();

    public static void init() {
        Registry.register(Registry.PARTICLE_TYPE, PvPOverhaul.identifier("totem_of_vengeance"), TOTEM_OF_VENGEANCE);
    }
}

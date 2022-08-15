package io.github.maliciousfiles.pvpoverhaul;

import net.minecraft.entity.attribute.ClampedEntityAttribute;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.util.registry.Registry;

public class AttributeInit {

    public static final EntityAttribute ATTACK_REACH = new ClampedEntityAttribute("attribute.name.generic.attack_reach", 3,0, 1024).setTracked(true);
    public static final EntityAttribute DUEL_WIELDING_TIME = new ClampedEntityAttribute("attribute.name.generic.duel_wielding_time", 20,0, 1024).setTracked(true);

    public static void init() {
        Registry.register(Registry.ATTRIBUTE, PvPOverhaul.identifier("generic.attack_reach"), ATTACK_REACH);
        Registry.register(Registry.ATTRIBUTE, PvPOverhaul.identifier("generic.duel_wielding_time"), DUEL_WIELDING_TIME);
    }
}

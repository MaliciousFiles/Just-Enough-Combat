package io.github.maliciousfiles.pvpoverhaul;

import net.minecraft.entity.attribute.ClampedEntityAttribute;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.util.registry.Registry;

public class AttributeInit {

    public static final EntityAttribute ATTACK_REACH = new ClampedEntityAttribute("attribute.name.generic.attack_reach", 3,0, 6).setTracked(true);

    public static void init() {
        Registry.register(Registry.ATTRIBUTE, PvPOverhaul.identifier("generic.attack_reach"), ATTACK_REACH);
    }
}

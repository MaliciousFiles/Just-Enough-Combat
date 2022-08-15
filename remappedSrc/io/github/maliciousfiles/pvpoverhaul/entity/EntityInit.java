package io.github.maliciousfiles.pvpoverhaul.entity;

import io.github.maliciousfiles.pvpoverhaul.PvPOverhaul;
import io.github.maliciousfiles.pvpoverhaul.entity.critter.CritterEntity;
import io.github.maliciousfiles.pvpoverhaul.entity.critter.CritterEntityModel;
import io.github.maliciousfiles.pvpoverhaul.entity.shuriken.ShurikenEntity;
import io.github.maliciousfiles.pvpoverhaul.entity.stone_golem.StoneGolemEntity;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.util.registry.Registry;

public class EntityInit {

    public static final EntityType<StoneGolemEntity> STONE_GOLEM = Registry.register(Registry.ENTITY_TYPE, PvPOverhaul.identifier("stone_golem"), FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, StoneGolemEntity::new).dimensions(EntityDimensions.fixed(2.9f, 2.5f)).build());
    public static final EntityType<CritterEntity> CRITTER = Registry.register(Registry.ENTITY_TYPE, PvPOverhaul.identifier("critter"), FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, CritterEntity::new).dimensions(EntityDimensions.fixed(0.4f, 0.3f)).build());
    public static final EntityType<ShurikenEntity> SHURIKEN = Registry.register(Registry.ENTITY_TYPE, PvPOverhaul.identifier("shuriken"), FabricEntityTypeBuilder.create(SpawnGroup.MISC, (EntityType.EntityFactory<ShurikenEntity>) ShurikenEntity::new).dimensions(EntityDimensions.fixed(0.75f, 0.75f)).build());

    public static void init() {
        FabricDefaultAttributeRegistry.register(STONE_GOLEM, StoneGolemEntity.createStoneGolemAttributes());
        FabricDefaultAttributeRegistry.register(CRITTER, CritterEntity.createCritterAttributes());
    }
}

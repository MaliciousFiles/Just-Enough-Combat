package io.github.maliciousfiles.pvpoverhaul.entity.critter;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.world.World;

public class CritterEntity extends HostileEntity {

    public CritterEntity(EntityType<? extends CritterEntity> entityType, World world) {
        super(entityType, world);
    }

    public static DefaultAttributeContainer.Builder createCritterAttributes() {
        return HostileEntity.createHostileAttributes().add(EntityAttributes.GENERIC_FOLLOW_RANGE).add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.6).add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 2).add(EntityAttributes.GENERIC_ARMOR, 0).add(EntityAttributes.GENERIC_MAX_HEALTH, 4);
    }

}

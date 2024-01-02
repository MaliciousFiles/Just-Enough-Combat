package io.github.maliciousfiles.jec.item.armor;


// Interface to allow LivingEntitys to store an active set bonus
public interface IArmorable {

    void jec$setSetBonus(ArmorSet set);

    ArmorSet jec$getSetBonus();

}

package io.github.maliciousfiles.pvpoverhaul.entity.shuriken;

import io.github.maliciousfiles.pvpoverhaul.entity.EntityInit;
import io.github.maliciousfiles.pvpoverhaul.item.ItemInit;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.FlyingItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ShurikenEntity extends PersistentProjectileEntity {

    private float spin = 0.0f;
    public ShurikenEntity(EntityType<? extends ShurikenEntity> type, World world) {
        super(type, world);
    }

    public ShurikenEntity(World world, double x, double y, double z) {
        super(EntityInit.SHURIKEN, x, y, z, world);
    }

    public ShurikenEntity(World world, LivingEntity owner) {
        super(EntityInit.SHURIKEN, owner, world);
    }

    @Override
    protected ItemStack asItemStack() {
        return new ItemStack(ItemInit.SHURIKEN);
    }

    public void updateSpin(float tickDelta) {
        if (!this.inGround) {
            this.spin += this.getVelocity().length()*24 * tickDelta;
        }
    }
    public float getSpin() {return spin;}

}

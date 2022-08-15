package io.github.maliciousfiles.pvpoverhaul.item.weapon;

import io.github.maliciousfiles.pvpoverhaul.entity.shuriken.ShurikenEntity;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Quaternion;
import net.minecraft.util.math.Vec3f;
import net.minecraft.world.World;

public class ShurikenItem extends Item {

    public ShurikenItem(Settings settings) {
        super(settings);
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return true;
    }

    @Override
    public int getEnchantability() {
        return 12;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);

        world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.ENTITY_ENDER_PEARL_THROW, SoundCategory.NEUTRAL, 0.5F, 0.4F / (world.getRandom().nextFloat() * 0.4F + 0.8F));

        user.getItemCooldownManager().set(this, 10);

        if (!world.isClient) {

            int extraShurikens = Math.min((user.getAbilities().creativeMode ? stack.getMaxCount() : stack.getCount())-1, EnchantmentHelper.getLevel(Enchantments.MULTISHOT, stack) * 2);

            for (int i = -MathHelper.ceil(extraShurikens/2f); i <= MathHelper.floor(extraShurikens/2f); i++) {
                ShurikenEntity shuriken = new ShurikenEntity(world, user, stack);

                shuriken.setDamage(3);

                Vec3f vec3f = new Vec3f(user.getRotationVec(1.0F));
                vec3f.rotate(new Quaternion(new Vec3f(user.getOppositeRotationVector(1.0F)), i * 5, true));

                shuriken.setVelocity(vec3f.getX(), vec3f.getY(), vec3f.getZ(), 2.5F, 1.0F);

                user.incrementStat(Stats.USED.getOrCreateStat(this));

                if (user.getAbilities().creativeMode) shuriken.pickupType = PersistentProjectileEntity.PickupPermission.CREATIVE_ONLY;
                else stack.decrement(1);

                world.spawnEntity(shuriken);
            }
        }

        return TypedActionResult.success(stack, world.isClient());
    }
}

package io.github.maliciousfiles.jec.mixins;

import io.github.maliciousfiles.jec.util.LivingEquipEvent;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.NeoForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class MixinLivingEntity {
    @Inject(method="onEquipItem", at=@At(value="INVOKE",target="Lnet/minecraft/world/entity/LivingEntity;gameEvent(Lnet/minecraft/world/level/gameevent/GameEvent;)V"))
    private void fireEquipEvent(EquipmentSlot slot, ItemStack from, ItemStack to, CallbackInfo ci) {
        NeoForge.EVENT_BUS.post(new LivingEquipEvent((LivingEntity) (Object) this, slot, from, to));
    }
}

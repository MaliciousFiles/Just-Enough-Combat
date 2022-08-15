package io.github.maliciousfiles.pvpoverhaul.mixin.common;

import io.github.maliciousfiles.pvpoverhaul.event.VehicleChangeCallback;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public class EntityMixin {

    // VEHICLE CHANGE EVENT
    @Inject(method = "dismountVehicle", at=@At(value = "FIELD", target="Lnet/minecraft/entity/Entity;vehicle:Lnet/minecraft/entity/Entity;", opcode=181)) // Opcodes.PUTFIELD
    private void dismount(CallbackInfo ci) {
        VehicleChangeCallback.EVENT.invoker().vehicleChange((Entity) (Object) this);
    }

    @Inject(method = "startRiding(Lnet/minecraft/entity/Entity;Z)Z", at=@At(value = "FIELD", target="Lnet/minecraft/entity/Entity;vehicle:Lnet/minecraft/entity/Entity;", opcode=181)) // Opcodes.PUTFIELD
    private void mount(Entity entity, boolean force, CallbackInfoReturnable<Boolean> cir) {
        VehicleChangeCallback.EVENT.invoker().vehicleChange((Entity) (Object) this);
    }
}

package io.github.maliciousfiles.jec.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import io.github.maliciousfiles.jec.entity.projectile.weapon.SpinningThrowableWeaponEntity;
import io.github.maliciousfiles.jec.entity.projectile.weapon.ThrowableWeaponEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.ItemSupplier;
import org.joml.Quaternionf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ThrownItemRenderer.class)
public class MixinThrownItemRenderer<T extends Entity & ItemSupplier> {
    // make spinning throwable weapons spin
    @Inject(method="render", at=@At(value="INVOKE", target="Lnet/minecraft/client/renderer/entity/ItemRenderer;renderStatic(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemDisplayContext;IILcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;Lnet/minecraft/world/level/Level;I)V"))
    private void spin(T entity, float yaw, float tickDelta, PoseStack poseStack, MultiBufferSource bufferSource, int i, CallbackInfo ci) {
        if (entity instanceof SpinningThrowableWeaponEntity spin) {
            poseStack.mulPose(Axis.ZP.rotationDegrees(spin.updateSpin(tickDelta)));
            poseStack.translate(0,-0.12,0);
        }
    }

    // make them rotate according to yaw rather than camera rotation
    @WrapOperation(method="render", at=@At(value="INVOKE", target="Lnet/minecraft/client/renderer/entity/EntityRenderDispatcher;cameraOrientation()Lorg/joml/Quaternionf;"))
    private Quaternionf fixRotation(EntityRenderDispatcher instance, Operation<Quaternionf> original, @Local(argsOnly=true, ordinal=0) T entity, @Local(argsOnly = true, ordinal=1) float tickDelta) {
        if (entity instanceof ThrowableWeaponEntity weapon) {
            return Axis.YP.rotationDegrees(weapon.getViewYRot(tickDelta) - 90);
        } else {
            return original.call(instance);
        }
    }

    // won't render unless tickCount > 2, but we don't care for throwable weapons
    @WrapOperation(method="render", at=@At(value="FIELD", target="Lnet/minecraft/world/entity/Entity;tickCount:I"))
    private int onRender(Entity instance, Operation<Integer> original) {
        if (instance instanceof ThrowableWeaponEntity) {
            return 3;
        } else {
            return original.call(instance);
        }
    }
}

package io.github.maliciousfiles.jec.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import io.github.maliciousfiles.jec.attack.ISwingable;
import io.github.maliciousfiles.jec.attack.ISwinger;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HumanoidModel.class)
public class MixinHumanoidModel <T extends LivingEntity> {
    @WrapOperation(method="setupAnim(Lnet/minecraft/world/entity/LivingEntity;FFFFF)V", at=@At(value="INVOKE", target="Lnet/minecraft/client/model/HumanoidModel;setupAttackAnimation(Lnet/minecraft/world/entity/LivingEntity;F)V"))
    public void customAttackAnims(HumanoidModel<T> instance, T entity, float bob, Operation<Void> original) {
        ISwinger swinger = (ISwinger) entity;

        float progress = instance.attackTime*instance.attackTime*instance.attackTime*instance.attackTime*(3-2*instance.attackTime*instance.attackTime);//instance.attackTime*(0.75f + instance.attackTime*instance.attackTime*(5.5f-5.25f*instance.attackTime));
        ModelPart arm = instance.getArm(entity.getMainArm());

        if (swinger.jec$getCurrentSwingType() == null || !swinger.jec$getCurrentSwingType().swingModel(arm, progress)) {
            original.call(instance, entity, bob);
        }
    }
}

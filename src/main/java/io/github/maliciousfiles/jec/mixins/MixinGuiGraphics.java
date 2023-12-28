package io.github.maliciousfiles.jec.mixins;

import com.google.common.collect.ImmutableMap;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.vertex.VertexFormatElement;
import io.github.maliciousfiles.jec.JEC;
import io.github.maliciousfiles.jec.Shader;
import io.github.maliciousfiles.jec.items.armor.ArmorSets;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Supplier;

@Mixin(GuiGraphics.class)
public class MixinGuiGraphics {
    @Unique
    private static final VertexFormat POSITION_TEX_TEX = new VertexFormat(ImmutableMap.<String, VertexFormatElement>builder().put("Position", DefaultVertexFormat.ELEMENT_POSITION).put("UV0", DefaultVertexFormat.ELEMENT_UV).put("UV3", DefaultVertexFormat.ELEMENT_UV).build());

    // index is used to let the shader know its location on the armor bar
    @Unique
    int justEnoughCombat$index = -1; // -1 if inactive

    // used to reset index so that I don't have to use another mixin
    @Unique
    ResourceLocation justEnoughCombat$prevTexture = null;
    @Inject(method="blitSprite(Lnet/minecraft/resources/ResourceLocation;IIII)V", at=@At("HEAD"))
    private void checkTexture(ResourceLocation texture, int left, int top, int width, int height, CallbackInfo ci) {
        if (Minecraft.getInstance().player == null || ArmorSets.hasSetBonus(Minecraft.getInstance().player)) return;

        boolean isArmor = justEnoughCombat$isArmor(texture);
        if (justEnoughCombat$isArmor(justEnoughCombat$prevTexture) != isArmor) {
            justEnoughCombat$index = isArmor ? 0 : -1;
        }
        justEnoughCombat$prevTexture = texture;
    }

    // if the texture is an armor bar, pass in the index to the shader
    @WrapOperation(method="innerBlit(Lnet/minecraft/resources/ResourceLocation;IIIIIFFFF)V", at=@At(value="INVOKE", target="Lcom/mojang/blaze3d/vertex/VertexConsumer;uv(FF)Lcom/mojang/blaze3d/vertex/VertexConsumer;"))
    private VertexConsumer modifyVector(VertexConsumer instance, float u, float v, Operation<VertexConsumer> original) {
        VertexConsumer orig = original.call(instance, u, v);
        if (justEnoughCombat$index == -1) return orig;

        return orig.uv(justEnoughCombat$index /4 + justEnoughCombat$index %4/2, (justEnoughCombat$index+++3)%4/2);
    }

    // if the texture is an armor bar, use the proper vertex format
    @ModifyExpressionValue(method="innerBlit(Lnet/minecraft/resources/ResourceLocation;IIIIIFFFF)V", at=@At(value="FIELD", target="Lcom/mojang/blaze3d/vertex/DefaultVertexFormat;POSITION_TEX:Lcom/mojang/blaze3d/vertex/VertexFormat;"))
    private VertexFormat modifyVertexFormat(VertexFormat original) {
        return justEnoughCombat$index == -1 ? original : POSITION_TEX_TEX;
    }

    // if the texture is an armor bar, use the proper shader
    @WrapOperation(method="innerBlit(Lnet/minecraft/resources/ResourceLocation;IIIIIFFFF)V", at=@At(value="INVOKE", target="Lcom/mojang/blaze3d/systems/RenderSystem;setShader(Ljava/util/function/Supplier;)V"))
    private void enableShader(Supplier<ShaderInstance> supplier, Operation<Void> original) {
        original.call(justEnoughCombat$index == -1 ? supplier : (Supplier<ShaderInstance>) Shader::getHudSetBonus);
    }

    // if the texture is an armor bar, use the proper shader texture
    @Inject(method="innerBlit(Lnet/minecraft/resources/ResourceLocation;IIIIIFFFF)V", at=@At(value="INVOKE", target="Lcom/mojang/blaze3d/systems/RenderSystem;setShaderTexture(ILnet/minecraft/resources/ResourceLocation;)V"))
    private void enableShaderTexture(CallbackInfo ci) {
        if (justEnoughCombat$index != -1) {
            RenderSystem.setShaderTexture(3, JEC.location("textures/gui/set_bonus_gradient.png"));
        }
    }

    @Unique
    private static boolean justEnoughCombat$isArmor(ResourceLocation loc) {
        return loc != null && loc.getPath().startsWith("hud/armor");
    }
}

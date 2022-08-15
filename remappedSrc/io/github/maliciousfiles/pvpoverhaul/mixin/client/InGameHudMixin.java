package io.github.maliciousfiles.pvpoverhaul.mixin.client;

import VertexFormat;
import VertexFormatElement;
import com.google.common.collect.ImmutableMap;
import com.mojang.blaze3d.systems.RenderSystem;
import io.github.maliciousfiles.pvpoverhaul.PvPOverhaul;
import io.github.maliciousfiles.pvpoverhaul.client.Shaders;
import io.github.maliciousfiles.pvpoverhaul.item.ItemInit;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class InGameHudMixin {

    private static final VertexFormatElement GRADIENT_ELEMENT = new VertexFormatElement(0, VertexFormatElement.ComponentType.FLOAT, VertexFormatElement.Type.UV, 2);
    private static final VertexFormat POSITION_TEXTURE_GRADIENT = new VertexFormat(ImmutableMap.<String, VertexFormatElement>builder().put("Position", VertexFormats.POSITION_ELEMENT).put("UV0", VertexFormats.TEXTURE_ELEMENT).put("UV3", GRADIENT_ELEMENT).build());

    private static int index;

    // CUSTOM ARMOR BAR
    @Redirect(at = @At(value="INVOKE", target="Lnet/minecraft/client/gui/hud/InGameHud;drawTexture(Lnet/minecraft/client/util/math/MatrixStack;IIIIII)V"), slice = @Slice(from = @At(value="CONSTANT", args="stringValue=armor"), to = @At(value="CONSTANT", args="stringValue=health")), method="renderStatusBars")
    private void drawMatrix(InGameHud instance, MatrixStack matrices, int x, int y, int u, int v, int width, int height) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (ItemInit.hasSetBonus(client.player)) {
            RenderSystem.setShader(Shaders::getHudSetBonus);
            RenderSystem.setShaderTexture(3, PvPOverhaul.identifier("textures/gui/set_bonus_gradient.png"));
            BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();


            Matrix4f matrix = matrices.peek().getPositionMatrix();
            int x1 = x + width;
            int y1 = y + height;
            float u0 = u / 256f;
            float u1 = u0 + width/256f;
            float v0 = v / 256f;
            float v1 = v0 + height/256f;
            int z = instance.getZOffset();

            bufferBuilder.begin(VertexFormat.DrawMode.QUADS, POSITION_TEXTURE_GRADIENT);
            bufferBuilder.vertex(matrix, x, y, z).texture(u0, v0).texture(index, 0).next();
            bufferBuilder.vertex(matrix, x, y1, z).texture(u0, v1).texture(index, 1).next();
            bufferBuilder.vertex(matrix, x1, y1, z).texture(u1, v1).texture(1+index, 1).next();
            bufferBuilder.vertex(matrix, x1, y, z).texture(u1, v0).texture(1+index, 0).next();
            BufferRenderer.drawWithShader(bufferBuilder.end());

            index++;
        } else {
            instance.drawTexture(matrices, x, y, u, v, width, height);
        }
    }

    @Inject(at=@At("HEAD"), method="renderStatusBars")
    private void renderStatusBars(MatrixStack matrices, CallbackInfo ci) {
        index = 0;
    }

    // VENGEANCE HEARTS
    @Redirect(method="renderStatusBars", at=@At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;getHealth()F"))
    private float getHealth(PlayerEntity instance) {
        if (instance.getDataTracker().get(ItemInit.VENGEANCE_DATA) > 0) {
            return 0;
        }

        return instance.getHealth();
    }
}
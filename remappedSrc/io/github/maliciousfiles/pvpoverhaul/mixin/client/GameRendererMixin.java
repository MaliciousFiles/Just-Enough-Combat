package io.github.maliciousfiles.pvpoverhaul.mixin.client;

import com.mojang.datafixers.util.Pair;
import io.github.maliciousfiles.pvpoverhaul.AttributeInit;
import io.github.maliciousfiles.pvpoverhaul.PvPOverhaul;
import io.github.maliciousfiles.pvpoverhaul.client.Shaders;
import io.github.maliciousfiles.pvpoverhaul.item.ItemInit;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Program;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.Shader;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;

@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {

    @Shadow protected abstract void loadShader(Identifier id);

    // TOTEM OF VENGEANCE SHADER
    private static final Identifier TOTEM_SHADER = PvPOverhaul.identifier("shaders/post/vengeance.json");

    @Inject(method = "tick", at=@At("TAIL"))
    private void tick(CallbackInfo ci) {
        GameRenderer renderer = (GameRenderer) (Object) this;

        if (renderer.getClient().getCameraEntity() instanceof PlayerEntity player) {
            boolean shaderActive = renderer.getShader() != null && renderer.getShader().getName().equals(TOTEM_SHADER.toString());

            if (
                player.getDataTracker().get(ItemInit.VENGEANCE_DATA) > 0 ||
                (
                    player.getDamageTracker().getMostRecentDamage() != null &&
                    player.getDamageTracker().getMostRecentDamage().getDamageSource() == PvPOverhaul.VENGEANCE_TOTEM_TIME
                )
            ) {
                if (!shaderActive) this.loadShader(TOTEM_SHADER);
            } else if (!player.isDead() && shaderActive) {
                renderer.disableShader();
            }
        }
    }

    // REGISTER SHADERS
    @Inject(method = "loadShaders", at = @At(value="INVOKE", target="Ljava/util/List;add(Ljava/lang/Object;)Z", ordinal=0, shift= At.Shift.BEFORE), locals = LocalCapture.CAPTURE_FAILHARD)
    private void loadShaders(ResourceManager manager, CallbackInfo ci, List<Program> _programsToClose, List<Pair<Shader, Consumer<Shader>>> shadersToLoad) throws IOException {
        Shaders.loadShaders(manager, shadersToLoad);
    }


    // CUSTOM ATTACK REACH
    @ModifyConstant(method = "updateTargetedEntity", constant = @Constant(doubleValue = 3.0))
    private double getAttackReach(double _attackReach) {

        return MinecraftClient.getInstance().player.getAttributeValue(AttributeInit.ATTACK_REACH);
    }

    @ModifyConstant(method = "updateTargetedEntity", constant = @Constant(doubleValue = 9.0))
    private double getSquaredAttackReach(double _attackReach) {
        double attackReach = MinecraftClient.getInstance().player.getAttributeValue(AttributeInit.ATTACK_REACH);

        return attackReach * attackReach;
    }
}

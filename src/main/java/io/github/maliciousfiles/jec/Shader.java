package io.github.maliciousfiles.jec;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import net.minecraft.client.renderer.ShaderInstance;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.RegisterShadersEvent;

import java.io.IOException;

@Mod.EventBusSubscriber(modid = JEC.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Shader {
    private static ShaderInstance hudSetBonus;

    @SubscribeEvent
    public static void onShaderLoad(RegisterShadersEvent evt) throws IOException {
        evt.registerShader(new ShaderInstance(evt.getResourceProvider(), JEC.location("hud_set_bonus"), DefaultVertexFormat.POSITION_TEX),
                s -> hudSetBonus = s);
    }

    public static ShaderInstance getHudSetBonus() {
        return hudSetBonus;
    }
}

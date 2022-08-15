package io.github.maliciousfiles.pvpoverhaul.client;

import com.mojang.datafixers.util.Pair;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.Shader;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.resource.ResourceManager;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;

@Environment(EnvType.CLIENT)
public class Shaders {

    private static Shader hudSetBonus;

    public static void loadShaders(ResourceManager manager, List<Pair<Shader, Consumer<Shader>>> shaders) throws IOException {
        shaders.add(Pair.of(new Shader(manager, "hud_set_bonus", VertexFormats.POSITION_TEXTURE), (shader) -> hudSetBonus = shader));
    }

    public static Shader getHudSetBonus() {
        return hudSetBonus;
    }
}

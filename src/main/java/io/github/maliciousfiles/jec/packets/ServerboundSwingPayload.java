package io.github.maliciousfiles.jec.packets;

import io.github.maliciousfiles.jec.JEC;
import io.github.maliciousfiles.jec.attack.ISwingable;
import io.github.maliciousfiles.jec.attack.ISwinger;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.game.ServerGamePacketListener;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;

public record ServerboundSwingPayload() implements CustomPacketPayload {
    public static ResourceLocation ID = JEC.location("swing");

    public ServerboundSwingPayload(FriendlyByteBuf buf) {
        this();
    }

    @Override
    public void write(FriendlyByteBuf buf) {}

    @Override
    public ResourceLocation id() {
        return ID;
    }

    public void handle(PlayPayloadContext ctx) {
        Player player = ctx.player().get();
        ISwingable swingable = (ISwingable) player.getMainHandItem().getItem();

        // TODO: after duration/2, actually do the attack

        ((ISwinger) player).jec$swing(swingable.jec$getSwingType(), swingable.jec$getSwingDuration());
    }
}

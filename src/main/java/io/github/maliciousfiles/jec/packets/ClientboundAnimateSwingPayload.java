package io.github.maliciousfiles.jec.packets;

import io.github.maliciousfiles.jec.JEC;
import io.github.maliciousfiles.jec.attack.ISwingable;
import io.github.maliciousfiles.jec.attack.ISwinger;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;

public record ClientboundAnimateSwingPayload(int entityId, ISwingable.SwingType type, float duration) implements CustomPacketPayload {
    public static final ResourceLocation ID = JEC.location("animate_swing");

    public ClientboundAnimateSwingPayload(FriendlyByteBuf buf) {
        this(buf.readInt(), buf.readEnum(ISwingable.SwingType.class), buf.readFloat());
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeInt(entityId());
        buf.writeEnum(type());
        buf.writeFloat(duration());
    }

    @Override
    public ResourceLocation id() {
        return ID;
    }

    public void handle(PlayPayloadContext ctx) {
        ISwinger player = (ISwinger) ctx.level().get().getEntity(entityId);

        player.jec$swing(type(), duration());
    }
}

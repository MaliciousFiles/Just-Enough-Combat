package io.github.maliciousfiles.jec.packets;

import io.github.maliciousfiles.jec.JEC;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlerEvent;

@Mod.EventBusSubscriber(modid = JEC.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Packets {
    @SubscribeEvent
    public static void onRegisterPackets(RegisterPayloadHandlerEvent evt) {
        // TODO: probably wanna go ahead and make a system for this
        evt.registrar(JEC.MODID)
                .play(ClientboundAnimateSwingPayload.ID, ClientboundAnimateSwingPayload::new, handler -> handler
                        .client(ClientboundAnimateSwingPayload::handle))
                .play(ServerboundSwingPayload.ID, ServerboundSwingPayload::new, handler -> handler
                        .server(ServerboundSwingPayload::handle));
    }
}

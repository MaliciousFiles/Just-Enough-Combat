package io.github.maliciousfiles.jec.attack;

import io.github.maliciousfiles.jec.JEC;
import io.github.maliciousfiles.jec.packets.ServerboundSwingPayload;
import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.InputEvent;

@Mod.EventBusSubscriber(modid = JEC.MODID)
public class AttackHandler {

    @SubscribeEvent
    public static void onClientPlayerClick(InputEvent.InteractionKeyMappingTriggered evt) {
        Minecraft client = Minecraft.getInstance();
        HitResult hitResult = client.hitResult;

        if (!evt.isAttack() || hitResult instanceof BlockHitResult bhr && !client.level.getBlockState(bhr.getBlockPos()).isAir()) return;

        evt.setCanceled(true);
        evt.setSwingHand(false);

        // TODO: unless currently swinging
        client.getConnection().send(new ServerboundSwingPayload());
    }
}

package io.github.maliciousfiles.jec.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import io.github.maliciousfiles.jec.event.ItemTooltipPartEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.neoforged.neoforge.common.NeoForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import javax.annotation.Nullable;
import java.util.List;

@Mixin(ItemStack.class)
public abstract class MixinItemStack {
    @WrapOperation(method="getTooltipLines", at=@At(value="FIELD", target="Lnet/minecraft/world/item/ItemStack$TooltipPart;*:Lnet/minecraft/world/item/ItemStack$TooltipPart;"))
    private ItemStack.TooltipPart shouldShowInTooltip(Operation<ItemStack.TooltipPart> original, @Nullable Player player, TooltipFlag flag, @Local List<Component> list) {
        ItemStack.TooltipPart part = original.call();

        NeoForge.EVENT_BUS.post(new ItemTooltipPartEvent((ItemStack) (Object) this, player, list, flag, part));

        return part;
    }
}
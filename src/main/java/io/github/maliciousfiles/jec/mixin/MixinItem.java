package io.github.maliciousfiles.jec.mixin;

import io.github.maliciousfiles.jec.attack.ISwingable;
import net.minecraft.world.item.*;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Item.class)
public class MixinItem implements ISwingable {
    @Override
    public SwingType jec$getSwingType() {
        Object item = this;
        return item instanceof SwordItem || item instanceof ShovelItem ? SwingType.SLASH :
                item instanceof AxeItem || item instanceof PickaxeItem || item instanceof HoeItem ? SwingType.OVERHEAD :
                        item instanceof TridentItem ? SwingType.STAB : SwingType.PUNCH;
    }

    @Override
    public float jec$getSwingDuration() {
        return 0.55f;
    }
}

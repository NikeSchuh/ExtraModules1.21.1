package de.nike.extramodules2.mixin;

import com.brandon3055.draconicevolution.api.config.BooleanProperty;
import com.brandon3055.draconicevolution.api.modules.Module;
import com.brandon3055.draconicevolution.api.modules.entities.AutoFeedEntity;
import com.brandon3055.draconicevolution.api.modules.entities.ShieldControlEntity;
import de.nike.extramodules2.ExtraModules2;
import net.minecraft.world.level.levelgen.DensityFunctions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({AutoFeedEntity.class})
public class ShieldControlEntityMixin {

    // Match the constructor signature EXACTLY
    @Inject(method = "<init>", at = @At("TAIL"))
    private void onConstruct(CallbackInfo ci) {
        ExtraModules2.LOGGER.info("Constructed ShieldControlEntity");

    }

}

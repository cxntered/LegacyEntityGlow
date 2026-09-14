package dev.cxntered.legacyentityglow.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import dev.cxntered.legacyentityglow.config.ModConfig;
import dev.cxntered.legacyentityglow.util.RenderUtils;
import net.minecraft.client.render.item.ItemRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ItemRenderer.class)
public class ItemRendererMixin_FixTintedItemGlow {
    @ModifyExpressionValue(method = "renderBakedItemQuads", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/model/BakedQuad;hasColor()Z"))
    private boolean fixTintedItemGlowColor(boolean original) {
        if (ModConfig.enabled.get() && ModConfig.outlineLayers.get() && RenderUtils.isSolidRendering()) {
            return false;
        }

        return original;
    }
}

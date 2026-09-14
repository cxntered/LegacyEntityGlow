package dev.cxntered.legacyentityglow.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.cxntered.legacyentityglow.config.ModConfig;
import dev.cxntered.legacyentityglow.util.RenderUtils;
import net.minecraft.client.render.entity.feature.ArmorFeatureRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ArmorFeatureRenderer.class)
abstract class ArmorFeatureRendererMixin_FixDyedArmorGlow {
    @WrapOperation(method = "renderFeature", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/platform/GlStateManager;color(FFFF)V", ordinal = 0))
    private void fixDyedArmorGlowColor(float f, float g, float h, float i, Operation<Void> original) {
        if (ModConfig.enabled.get() && ModConfig.outlineLayers.get() && RenderUtils.isSolidRendering()) {
            original.call(1.0F, 1.0F, 1.0F, i);
        } else {
            original.call(f, g, h, i);
        }
    }
}

package dev.cxntered.legacyentityglow.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import dev.cxntered.legacyentityglow.config.ModConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(WorldRenderer.class)
abstract class WorldRendererMixin_ForceGlow {
    @Shadow @Final private MinecraftClient client;

    @ModifyExpressionValue(method = "isEntityOutline", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/ClientPlayerEntity;isSpectator()Z"))
    private boolean bypassSpectatorCheck(boolean original) {
        return original || ModConfig.enabled.get();
    }

    @ModifyExpressionValue(method = "isEntityOutline", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/option/KeyBinding;isPressed()Z"))
    private boolean bypassKeyPressCheck(boolean original) {
        return original || ModConfig.enabled.get();
    }

    @ModifyExpressionValue(method = "renderEntities", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;shouldRender(DDD)Z", ordinal = 1))
    private boolean forceRenderGlow(boolean original, Entity entity, @Local(ordinal = 2) Entity entity3) {
        boolean showSpectatorOutline = this.client.player.isSpectator() && this.client.options.spectatorOutlines.isPressed();
        boolean isEntityGlowing = ModConfig.enabled.get() && (((EntityInvoker) entity3).legacyentityglow$getFlag(6) || (ModConfig.forceSelfGlow.get() && entity3 == this.client.player));
        return original && (isEntityGlowing || showSpectatorOutline);
    }
}

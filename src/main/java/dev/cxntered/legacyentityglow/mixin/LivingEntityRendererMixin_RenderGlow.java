package dev.cxntered.legacyentityglow.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.platform.GLX;
import com.mojang.blaze3d.platform.GlStateManager;
import dev.cxntered.legacyentityglow.config.ModConfig;
import dev.cxntered.legacyentityglow.util.RenderUtils;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.scoreboard.Team;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntityRenderer.class)
abstract class LivingEntityRendererMixin_RenderGlow<T extends LivingEntity> extends EntityRenderer<T> {
    @Shadow protected abstract void renderFeatures(T livingEntity, float f, float g, float h, float i, float j, float k, float l);

    @SuppressWarnings("unused")
    protected LivingEntityRendererMixin_RenderGlow(EntityRenderDispatcher entityRenderDispatcher) {
        super(entityRenderDispatcher);
    }

    @WrapMethod(method = "method_10257")
    private boolean setupSolidState(T livingEntity, Operation<Boolean> original) {
        if (!ModConfig.enabled.get() || !ModConfig.outlineLayers.get()) {
            return original.call(livingEntity);
        }

        GlStateManager.disableLighting();
        GlStateManager.activeTexture(GLX.lightmapTextureUnit);
        GlStateManager.disableTexture();
        GlStateManager.activeTexture(GLX.textureUnit);
        return true;
    }

    @Inject(method = "render(Lnet/minecraft/entity/LivingEntity;DDDFF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/LivingEntityRenderer;renderModel(Lnet/minecraft/entity/LivingEntity;FFFFFF)V", ordinal = 0))
    private void setupSolidRendering(T livingEntity, double d, double e, double f, float g, float h, CallbackInfo ci) {
        if (!ModConfig.enabled.get() || !ModConfig.outlineLayers.get()) return;

        int color = 0xFFFFFF;
        Team team = (Team) livingEntity.getScoreboardTeam();
        if (team != null) {
            String string = TextRenderer.getFormattingOnly(team.getPrefix());
            if (string.length() >= 2) {
                color = this.getFontRenderer().getColor(string.charAt(1));
            }
        }

        RenderUtils.setSolidRendering(true);
        GlStateManager.enableColorMaterial();
        RenderUtils.setupSolidRenderingTextureCombine(color);
    }

    @WrapOperation(method = "render(Lnet/minecraft/entity/LivingEntity;DDDFF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/LivingEntityRenderer;renderModel(Lnet/minecraft/entity/LivingEntity;FFFFFF)V", ordinal = 0))
    private void outlineLayers(LivingEntityRenderer<T> instance, T livingEntity, float f, float g, float h, float i, float j, float k, Operation<Void> original, @Local(argsOnly = true, ordinal = 1) float tickDelta) {
        original.call(instance, livingEntity, f, g, h, i, j, k);

        if (!ModConfig.enabled.get() || !ModConfig.outlineLayers.get()) return;

        if (!(livingEntity instanceof PlayerEntity player) || !player.isSpectator()) {
            this.renderFeatures(livingEntity, f, g, tickDelta, h, i, j, k);
        }

        RenderUtils.tearDownSolidRenderingTextureCombine();
        GlStateManager.disableColorMaterial();
        RenderUtils.setSolidRendering(false);
    }

    @WrapMethod(method = "method_10259")
    private void tearDownSolidState(Operation<Void> original) {
        if (!ModConfig.enabled.get() || !ModConfig.outlineLayers.get()) {
            original.call();
            return;
        }

        GlStateManager.enableLighting();
        GlStateManager.activeTexture(GLX.lightmapTextureUnit);
        GlStateManager.enableTexture();
        GlStateManager.activeTexture(GLX.textureUnit);
    }
}

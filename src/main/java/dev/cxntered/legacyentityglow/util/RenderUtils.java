package dev.cxntered.legacyentityglow.util;

import com.mojang.blaze3d.platform.GLX;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import java.nio.FloatBuffer;

public class RenderUtils {
    private static final FloatBuffer COLOR_BUFFER = BufferUtils.createFloatBuffer(4);

    public static void setupSolidRenderingTextureCombine(int color) {
        COLOR_BUFFER.put(0, (color >> 16 & 0xFF) / 255.0F);
        COLOR_BUFFER.put(1, (color >> 8 & 0xFF) / 255.0F);
        COLOR_BUFFER.put(2, (color & 0xFF) / 255.0F);
        COLOR_BUFFER.put(3, (color >> 24 & 0xFF) / 255.0F);
        GL11.glTexEnv(GL11.GL_TEXTURE_ENV, GL11.GL_TEXTURE_ENV_COLOR, COLOR_BUFFER);
        GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, GL11.GL_TEXTURE_ENV_MODE, GLX.combine);
        GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, GLX.combineRgb, GLX.constant);
        GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, GLX.source0Rgb, GLX.constant);
        GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, GLX.operand0Rgb, GL11.GL_SRC_COLOR);
        GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, GLX.combineAlpha, GLX.constant);
        GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, GLX.source0Alpha, GLX.textureUnit);
        GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, GLX.operand0Alpha, GL11.GL_SRC_ALPHA);
    }

    public static void tearDownSolidRenderingTextureCombine() {
        GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, GL11.GL_TEXTURE_ENV_MODE, GL11.GL_MODULATE);
        GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, GLX.combineRgb, GL11.GL_MODULATE);
        GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, GLX.combineAlpha, GL11.GL_MODULATE);
        GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, GLX.source0Rgb, GLX.textureUnit);
        GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, GLX.source0Alpha, GLX.textureUnit);
        GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, GLX.operand0Rgb, GL11.GL_SRC_COLOR);
        GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, GLX.operand0Alpha, GL11.GL_SRC_ALPHA);
    }
}

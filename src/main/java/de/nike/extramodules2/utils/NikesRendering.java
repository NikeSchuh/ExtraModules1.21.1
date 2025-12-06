package de.nike.extramodules2.utils;

import codechicken.lib.gui.modular.lib.GuiRender;
import codechicken.lib.render.buffer.TransformingVertexConsumer;
import com.brandon3055.brandonscore.client.render.RenderUtils;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.network.chat.Component;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;

public class NikesRendering {

    @OnlyIn(Dist.CLIENT)
    public static void drawChargeProgress(GuiRender render, int x, int y, int width, int height, double progress, int bgColor) {
        double diameter = (double)Math.min(width, height) * 0.425;
        render.rect((double)x, (double)y, (double)width, (double)height, bgColor);
        VertexConsumer builder = new TransformingVertexConsumer(render.buffers().getBuffer(RenderUtils.FAN_TYPE), render.pose());
        builder.addVertex((float)x + (float)width / 2.0F, (float)y + (float)height / 2.0F, 0.0F).setColor(0, 255, 255, 128);

        for(double d = (double)0.0F; d <= (double)1.0F; d += 0.03333333333333333) {
            double angle = d * progress + (double)0.5F - progress;
            double vertX = (double)x + (double)width / (double)2.0F + Math.sin(angle * (Math.PI * 2D)) * diameter;
            double vertY = (double)y + (double)height / (double)2.0F + Math.cos(angle * (Math.PI * 2D)) * diameter;
            builder.addVertex((float)vertX, (float)vertY, 0.0F).setColor(255, 255, 255, 128);
        }

        RenderUtils.endBatch(render.buffers());
    }

}

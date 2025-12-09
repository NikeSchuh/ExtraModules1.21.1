package de.nike.extramodules2.client.entities;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexBuffer;
import com.mojang.blaze3d.vertex.VertexConsumer;
import de.nike.extramodules2.entities.projectiles.DraconicLightningChain;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LightningBoltRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Vector3d;

import java.awt.*;
import java.util.Random;

public class DraconicLightningChainRenderer extends EntityRenderer<DraconicLightningChain> {

    public DraconicLightningChainRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(DraconicLightningChain lightningChain, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {

        super.render(lightningChain, entityYaw, partialTick, poseStack, bufferSource, packedLight);
        long randSeed = lightningChain.getLightningSeed();

        Entity startEntity = lightningChain.getStartEntity();
        Entity endEntity = lightningChain.getEndEntity();

        if(startEntity == null) return;
        if(endEntity == null) return;

        poseStack.pushPose();
        poseStack.translate(
                -lightningChain.position().x,
                -lightningChain.position().y,
                -lightningChain.position().z
        );

        int segCount = Math.max(6, Math.min(30, ((int) startEntity.blockPosition().distSqr(endEntity.blockPosition()))));
        float scaleMod = 2f;
        float deflectMod = 1f;
        boolean autoScale = false;
        float segTaper = 0.125F;
        int colour = lightningChain.getLightningColor();
        poseStack.pushPose();
        rendeArcP2P(poseStack, bufferSource, startEntity instanceof Player ? startEntity.getEyePosition().add(0, 0.5, 0) : startEntity.position().add(0, endEntity.getEyeHeight() / 2F, 0) ,endEntity.position().add(0, endEntity.getEyeHeight() / 2F, 0) ,segCount ,randSeed ,scaleMod ,deflectMod ,autoScale ,segTaper ,colour);
        poseStack.popPose();
        poseStack.popPose();
    }

    @Override
    public ResourceLocation getTextureLocation(DraconicLightningChain draconicLightningChain) {
        return ResourceLocation.fromNamespaceAndPath("", "");
    }

    public static void rendeArcP2P(PoseStack mStack, MultiBufferSource getter, Vec3 startPos, Vec3 endPos, int segCount, long randSeed, float scaleMod, float deflectMod, boolean autoScale, float segTaper, int colour) {

        double height = endPos.y - startPos.y;

        double minHeight = 1.25;
        if (Math.abs(height) < 0.25) {
            height = Math.copySign(minHeight, height == 0 ? 1 : height);
        }

        float relScale = autoScale ? (float) height / 128F : 0.05F; //A scale value calculated by comparing the bolt height to that of vanilla lightning
        float segHeight = (float) height / segCount;
        float[] segXOffset = new float[segCount + 1];
        float[] segZOffset = new float[segCount + 1];
        float xOffSum = 0;
        float zOffSum = 0;

        Random random = new Random(randSeed);
        for (int segment = 0; segment < segCount + 1; segment++) {
            segXOffset[segment] = xOffSum + (float) startPos.x;
            segZOffset[segment] = zOffSum + (float) startPos.z;
            //Figure out what the total offset will be so we can subtract it at the start in order to end up in the correct spot at the end.
            if (segment < segCount) {
                xOffSum += (5 - (random.nextFloat() * 10)) * relScale * deflectMod;
                zOffSum += (5 - (random.nextFloat() * 10)) * relScale * deflectMod;
            }
        }

        xOffSum -= (float) (endPos.x - startPos.x);
        zOffSum -= (float) (endPos.z - startPos.z);

        VertexConsumer builder = getter.getBuffer(RenderType.lightning());
        Matrix4f matrix4f = mStack.last().pose();

        for (int layer = 0; layer < 4; ++layer) {
            float red = ((colour >> 16) & 0xFF) / 255F;
            float green = ((colour >> 8) & 0xFF) / 255F;
            float blue = (colour & 0xFF) / 255F;
            float alpha = 0.3F;
            if (layer == 0) {
                red = green = blue = alpha = 1;
            }

            for (int seg = 0; seg < segCount; seg++) {
                float pos = seg / (float)(segCount);
                float x = segXOffset[seg] - (xOffSum * pos);
                float z = segZOffset[seg] - (zOffSum * pos);

                float nextPos = (seg + 1) / (float)(segCount);
                float nextX = segXOffset[seg+1] - (xOffSum * nextPos);
                float nextZ = segZOffset[seg+1] - (zOffSum * nextPos);

                //The size of each shell
                float layerOffsetA = (0.1F + (layer * 0.2F * (1F + segTaper))) * relScale * scaleMod;
                float layerOffsetB = (0.1F + (layer * 0.2F * (1F - segTaper))) * relScale * scaleMod;

                addSegmentQuad(matrix4f, builder, x, (float) startPos.y, z, seg, nextX, nextZ, red, green, blue, alpha, layerOffsetA, layerOffsetB, false, false, true, false, segHeight);    //North Side
                addSegmentQuad(matrix4f, builder, x, (float) startPos.y, z, seg, nextX, nextZ, red, green, blue, alpha, layerOffsetA, layerOffsetB, true, false, true, true, segHeight);      //East Side
                addSegmentQuad(matrix4f, builder, x, (float) startPos.y, z, seg, nextX, nextZ, red, green, blue, alpha, layerOffsetA, layerOffsetB, true, true, false, true, segHeight);      //South Side
                addSegmentQuad(matrix4f, builder, x, (float) startPos.y, z, seg, nextX, nextZ, red, green, blue, alpha, layerOffsetA, layerOffsetB, false, true, false, false, segHeight);    //West Side
            }
        }
    }

    private static void addSegmentQuad(Matrix4f matrix4f, VertexConsumer builder, float x1, float yOffset, float z1, int segIndex, float x2, float z2, float red, float green, float blue, float alpha, float offsetA, float offsetB, boolean invA, boolean invB, boolean invC, boolean invD, float segHeight) {
        builder.addVertex(matrix4f, x1 + (invA ? offsetB : -offsetB), yOffset + segIndex * segHeight, z1 + (invB ? offsetB : -offsetB)).setColor(red, green, blue, alpha);
        builder.addVertex(matrix4f, x2 + (invA ? offsetA : -offsetA), yOffset + (segIndex + 1F) * segHeight, z2 + (invB ? offsetA : -offsetA)).setColor(red, green, blue, alpha);
        builder.addVertex(matrix4f, x2 + (invC ? offsetA : -offsetA), yOffset + (segIndex + 1F) * segHeight, z2 + (invD ? offsetA : -offsetA)).setColor(red, green, blue, alpha);
        builder.addVertex(matrix4f, x1 + (invC ? offsetB : -offsetB), yOffset + segIndex * segHeight, z1 + (invD ? offsetB : -offsetB)).setColor(red, green, blue, alpha);
    }
}

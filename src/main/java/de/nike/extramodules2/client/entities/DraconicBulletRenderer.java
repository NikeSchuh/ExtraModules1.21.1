package de.nike.extramodules2.client.entities;

import codechicken.lib.vec.Vector3;
import com.brandon3055.brandonscore.api.TimeKeeper;
import com.brandon3055.draconicevolution.client.render.EffectLib;
import com.brandon3055.draconicevolution.client.render.tile.RenderTileChaosCrystal;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import de.nike.extramodules2.ExtraModules2;
import de.nike.extramodules2.client.entities.model.DraconicBulletModel;
import de.nike.extramodules2.entities.projectiles.DraconicBulletEntity;
import de.nike.extramodules2.utils.NikesMath;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import org.joml.Vector3f;

import java.awt.*;

public class DraconicBulletRenderer extends EntityRenderer<DraconicBulletEntity> {


    public DraconicBulletRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    private float timePassed = 0;

    @Override
    public void render(DraconicBulletEntity bulletEntity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        timePassed+=partialTick;
        poseStack.pushPose();
        poseStack.mulPose(Axis.YP.rotationDegrees((float) (NikesMath.lerp(bulletEntity.yRotO, bulletEntity.getYRot(), 0.05f) - 90F)));
        poseStack.mulPose(Axis.ZP.rotationDegrees((float) (NikesMath.lerp(bulletEntity.xRotO, bulletEntity.getXRot(), 0.05f) - 90F)));

        Vector3 startPos = new Vector3(0, -0.1, 0); //Bottom
        Vector3 endPos = new Vector3(0, 0.1, 0); //Top
        poseStack.scale(2, 2 ,2);
        int segCount = 15;
        long randSeed = (TimeKeeper.getClientTick() / 2);
        float scaleMod = 2f;
        float deflectMod = 1f;
        boolean autoScale = true;
        float segTaper = 0.125F;
        int color = bulletEntity.getColor();
        EffectLib.renderLightningP2P(poseStack, bufferSource, startPos ,endPos ,segCount ,randSeed ,scaleMod ,deflectMod ,autoScale ,segTaper ,color);

     /*/   VertexConsumer consumer = bufferSource.getBuffer(
               RenderType.entityCutout(getTextureLocation(bulletEntity))
        );

        model.renderToBuffer(poseStack, consumer, packedLight, OverlayTexture.NO_OVERLAY, Color.WHITE.getRGB());/*/


        poseStack.popPose();
        // super.render(bulletEntity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(DraconicBulletEntity draconicBulletEntity) {
        return null;
    }
}

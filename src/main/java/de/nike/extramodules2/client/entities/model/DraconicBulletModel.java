package de.nike.extramodules2.client.entities.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import de.nike.extramodules2.ExtraModules2;
import de.nike.extramodules2.entities.projectiles.DraconicBulletEntity;
import de.nike.extramodules2.utils.ColorUtil;
import de.nike.extramodules2.utils.NikesMath;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;

public class DraconicBulletModel extends EntityModel<DraconicBulletEntity> {
    // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor

    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(ExtraModules2.MODID, "textures/entity/draconic_bullet.png"), "main");

    private final ModelPart bb_main;

    public DraconicBulletModel(ModelPart root) {
        this.bb_main = root.getChild("bb_main");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition bb_main = partdefinition.addOrReplaceChild("bb_main", CubeListBuilder.create().texOffs(0, 0).addBox(-7.0F, -14.0F, -10.0F, 14.0F, 14.0F, 20.0F, new CubeDeformation(0.0F))
                .texOffs(0, 34).addBox(-5.0F, -12.125F, -14.125F, 10.0F, 10.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(30, 34).addBox(-3.0F, -10.125F, -16.125F, 6.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 128, 128);
    }

    @Override
    public void setupAnim(DraconicBulletEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int color) {
        bb_main.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
    }

}
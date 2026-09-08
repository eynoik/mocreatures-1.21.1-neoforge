/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.client.renderer.entity.legacy;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import drzhark.mocreatures.MoCreatures;
import drzhark.mocreatures.client.renderer.entity.MoCNameplateRenderer;
import drzhark.mocreatures.client.model.legacy.MoCLegacyModelBigCat1;
import drzhark.mocreatures.client.model.legacy.MoCLegacyModelBigCat2;
import drzhark.mocreatures.entity.hunter.MoCEntityBigCat;
import drzhark.mocreatures.entity.hunter.MoCEntityLion;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class MoCLegacyRenderBigCat extends MoCNameplateRenderer<MoCEntityBigCat, MoCLegacyModelBigCat2<MoCEntityBigCat>> {

    public MoCLegacyModelBigCat2 bigcat1;

    public MoCLegacyRenderBigCat(EntityRendererProvider.Context renderManagerIn, MoCLegacyModelBigCat2 modelbigcat2, MoCLegacyModelBigCat1 modelbigcat1, float f) {
        super(renderManagerIn, modelbigcat2, f);
        this.addLayer(new LayerMoCBigCat(this, renderManagerIn));
        this.bigcat1 = modelbigcat2;
    }

    @Override
    public ResourceLocation getTextureLocation(MoCEntityBigCat entitybigcat) {
        return entitybigcat.getTexture();
    }

    @Override
    protected void scale(MoCEntityBigCat entitybigcat, PoseStack poseStack, float f) {
        this.bigcat1.sitting = entitybigcat.getIsSitting();
        this.bigcat1.tamed = entitybigcat.getIsTamed();
        stretch(entitybigcat, poseStack);
    }

    protected void stretch(MoCEntityBigCat entitybigcat, PoseStack poseStack) {
        float f = entitybigcat.getMoCAge() * 0.01F;
        if (entitybigcat.getIsAdult()) {
            f = 1.0F;
        }
        poseStack.scale(f, f, f);
    }

    // Render mane
    private class LayerMoCBigCat extends RenderLayer<MoCEntityBigCat, MoCLegacyModelBigCat2<MoCEntityBigCat>> {

        private final MoCLegacyRenderBigCat mocRenderer;
        private final MoCLegacyModelBigCat1<MoCEntityBigCat> mocModel;

        public LayerMoCBigCat(MoCLegacyRenderBigCat render, EntityRendererProvider.Context context) {
            super(render);
            this.mocRenderer = render;
            this.mocModel = new MoCLegacyModelBigCat1<>(context.bakeLayer(MoCLegacyModelBigCat1.LAYER_LOCATION));
        }

        @Override
        public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, MoCEntityBigCat entitybigcat, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
            ResourceLocation resourcelocation;
            if (entitybigcat instanceof MoCEntityLion && entitybigcat.hasMane()) {
                if (entitybigcat.getTypeMoC() == 7) {
                    resourcelocation = MoCreatures.proxy.getModelTexture("big_cat_white_lion_legacy_layer.png");
                } else {
                    resourcelocation = MoCreatures.proxy.getModelTexture("big_cat_lion_legacy_layer_male.png");
                }
            } else {
                resourcelocation = MoCreatures.proxy.getModelTexture("big_cat_lion_legacy_layer_female.png");
            }
            this.mocModel.setupAnim(entitybigcat, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
            VertexConsumer vertexconsumer = buffer.getBuffer(RenderType.entityCutoutNoCull(resourcelocation));
            this.mocModel.renderToBuffer(poseStack, vertexconsumer, packedLight, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
        }
    }
}

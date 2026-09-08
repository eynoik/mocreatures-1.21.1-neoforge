/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import drzhark.mocreatures.client.model.MoCModelShark;
import drzhark.mocreatures.entity.aquatic.MoCEntityShark;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class MoCRenderShark extends MoCNameplateRenderer<MoCEntityShark, MoCModelShark<MoCEntityShark>> {

    public MoCRenderShark(EntityRendererProvider.Context renderManagerIn, MoCModelShark modelbase, float f) {
        super(renderManagerIn, modelbase, f);
    }

    @SuppressWarnings("removal")
    @Override
    public void render(MoCEntityShark entityshark, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLightIn) {
        super.render(entityshark, entityYaw, partialTicks, poseStack, buffer, packedLightIn);

    }

    @Override
    protected void scale(MoCEntityShark entityshark, PoseStack poseStack, float partialTickTime) {
        stretch(entityshark, poseStack);
    }

    protected void stretch(MoCEntityShark entityshark, PoseStack poseStack) {
        poseStack.scale((entityshark.getMoCAge() * 0.01F) / 2, (entityshark.getMoCAge() * 0.01F) / 2, (entityshark.getMoCAge() * 0.01F) / 2);
    }

    @Override
    public ResourceLocation getTextureLocation(MoCEntityShark entityshark) {
        return entityshark.getTexture();
    }
}

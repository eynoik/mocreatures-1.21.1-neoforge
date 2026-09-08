/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import drzhark.mocreatures.client.model.MoCModelDolphin;
import drzhark.mocreatures.entity.aquatic.MoCEntityDolphin;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class MoCRenderDolphin extends MoCNameplateRenderer<MoCEntityDolphin, MoCModelDolphin<MoCEntityDolphin>> {

    public MoCRenderDolphin(EntityRendererProvider.Context renderManagerIn, MoCModelDolphin modelbase, float f) {
        super(renderManagerIn, modelbase, f);
    }

    @SuppressWarnings("removal")
    @Override
    public void render(MoCEntityDolphin entitydolphin, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLightIn) {
        super.render(entitydolphin, entityYaw, partialTicks, poseStack, buffer, packedLightIn);

    }

    @Override
    protected void scale(MoCEntityDolphin entitydolphin, PoseStack poseStack, float partialTickTime) {
        stretch(entitydolphin, poseStack);
    }

    protected void stretch(MoCEntityDolphin entitydolphin, PoseStack poseStack) {
        poseStack.scale(entitydolphin.getMoCAge() * 0.01F, entitydolphin.getMoCAge() * 0.01F, entitydolphin.getMoCAge() * 0.01F);
    }

    @Override
    public ResourceLocation getTextureLocation(MoCEntityDolphin entitydolphin) {
        return entitydolphin.getTexture();
    }
}

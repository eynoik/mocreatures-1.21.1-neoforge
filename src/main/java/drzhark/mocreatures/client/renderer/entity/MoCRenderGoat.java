/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import drzhark.mocreatures.client.model.MoCModelGoat;
import drzhark.mocreatures.entity.neutral.MoCEntityGoat;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class MoCRenderGoat extends MoCNameplateRenderer<MoCEntityGoat, MoCModelGoat<MoCEntityGoat>> {

    private final MoCModelGoat tempGoat;
    float depth = 0F;

    public MoCRenderGoat(EntityRendererProvider.Context renderManagerIn, MoCModelGoat modelbase, float f) {
        super(renderManagerIn, modelbase, f);
        this.tempGoat = modelbase;
    }

    @Override
    public ResourceLocation getTextureLocation(MoCEntityGoat entitygoat) {
        return entitygoat.getTexture();
    }

    @Override
    protected void scale(MoCEntityGoat entitygoat, PoseStack poseStack, float partialTick) {
        poseStack.translate(0.0F, this.depth, 0.0F);
        stretch(entitygoat, poseStack);
    }

    @Override
    public void render(MoCEntityGoat entitygoat, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLightIn) {
        this.tempGoat.typeInt = entitygoat.getTypeMoC();
        this.tempGoat.age = entitygoat.getMoCAge() * 0.01F;
        this.tempGoat.bleat = entitygoat.getBleating();
        this.tempGoat.attacking = entitygoat.getAttacking();
        this.tempGoat.legMov = entitygoat.legMovement();
        this.tempGoat.earMov = entitygoat.earMovement();
        this.tempGoat.tailMov = entitygoat.tailMovement();
        this.tempGoat.eatMov = entitygoat.mouthMovement();
        super.render(entitygoat, entityYaw, partialTicks, poseStack, buffer, packedLightIn);

    }

    protected void stretch(MoCEntityGoat entitygoat, PoseStack poseStack) {
        float scale = entitygoat.getMoCAge() * 0.01F;
        poseStack.scale(scale, scale, scale);
    }
}

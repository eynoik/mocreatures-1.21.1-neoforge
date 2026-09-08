/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.client.renderer.entity;


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import drzhark.mocreatures.client.model.MoCModelHorse;
import drzhark.mocreatures.entity.passive.MoCEntityHorse;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class MoCRenderHorse extends MoCRenderMoC<MoCEntityHorse, MoCModelHorse<MoCEntityHorse>> {

    public MoCRenderHorse(EntityRendererProvider.Context context, MoCModelHorse<MoCEntityHorse> modelbase) {
        super(context, modelbase, 0.5F);
    }

    @Override
    public void render(MoCEntityHorse entityHorse, float entityYaw, float partialTicks, PoseStack poseStack,
            MultiBufferSource buffer, int packedLight) {
        if (entityHorse.getIsGhost() || entityHorse.getVanishC() != 0) {
            poseStack.pushPose();

            poseStack.scale(1.0F, -1.0F, 1.0F);
            poseStack.translate(0.0D, (double) (-1.501F), 0.0D);

            // Perform your scaling and transformations as usual
            this.scale(entityHorse, poseStack, partialTicks);

            // Determine transparency conditionally
            float transparency;

            if (entityHorse.getVanishC() != 0) {
                transparency = 1.0F - (((float) (entityHorse.getVanishC())) / 100);
            } else {
                transparency = entityHorse.tFloat();
            }

            // Explicitly select translucent or opaque render type based on transparency
            RenderType renderType = transparency < 1.0F
                    ? RenderType.entityTranslucent(getTextureLocation(entityHorse))
                    : RenderType.entityCutout(getTextureLocation(entityHorse));

            // Get VertexConsumer explicitly from original buffer
            VertexConsumer vertexConsumer = buffer.getBuffer(renderType);

            // Setup rotations, animations, etc., as usual
            float bodyRot = Mth.rotLerp(partialTicks, entityHorse.yBodyRotO, entityHorse.yBodyRot);
            float headRot = Mth.rotLerp(partialTicks, entityHorse.yHeadRotO, entityHorse.yHeadRot);
            float headRotDelta = headRot - bodyRot;
            float pitch = Mth.lerp(partialTicks, entityHorse.xRotO, entityHorse.getXRot());

            setupRotations(entityHorse, poseStack, entityHorse.tickCount + partialTicks, bodyRot, partialTicks, 1.0F);
            model.prepareMobModel(entityHorse, 0, 0, partialTicks);
            model.setupAnim(entityHorse, 0, 0, entityHorse.tickCount + partialTicks, headRotDelta, pitch);

            // Render the model explicitly using transparency
            int tint = ((Math.round(transparency * 255.0F) & 0xFF) << 24) | 0x00FFFFFF;
            model.renderToBuffer(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY, tint);

            poseStack.popPose();

            renderNameAndHealth(entityHorse, poseStack, buffer, packedLight, partialTicks);
        } else {
            this.renderMoC(entityHorse, entityYaw, partialTicks, poseStack, buffer, packedLight);
        }
    }

    @Override
    public ResourceLocation getTextureLocation(MoCEntityHorse entityhorse) {
        return entityhorse.getTexture();
    }

    protected void adjustHeight(MoCEntityHorse entityhorse, float FHeight, PoseStack poseStack) {
        poseStack.translate(0.0F, FHeight, 0.0F);
    }

    @Override
    protected void scale(MoCEntityHorse entityhorse, PoseStack poseStack, float partialTicks) {
        if (!entityhorse.getIsAdult() || entityhorse.getTypeMoC() > 64) {
            stretch(entityhorse, poseStack);
        }
        if (entityhorse.getIsGhost()) {
            adjustHeight(entityhorse, -0.3F + (entityhorse.tFloat() / 5F), poseStack);
        }
        super.scale(entityhorse, poseStack, partialTicks);
    }

    protected void stretch(MoCEntityHorse entityhorse, PoseStack poseStack) {
        float sizeFactor = entityhorse.getMoCAge() * 0.01F;
        if (entityhorse.getIsAdult()) {
            sizeFactor = 1.0F;
        }
        if (entityhorse.getTypeMoC() > 64) // donkey
        {
            sizeFactor *= 0.9F;
        }
        poseStack.scale(sizeFactor, sizeFactor, sizeFactor);
    }
    
    @Override
    protected boolean hasVanishingEffect(MoCEntityHorse entityIn) {
        return entityIn.getVanishC() != 0;
    }
    
    @Override
    protected float getVanishingTransparency(MoCEntityHorse entityIn) {
        return 1.0F - (((float) (entityIn.getVanishC())) / 100);
    }
    
    @Override
    protected float getGhostTransparency(MoCEntityHorse entityIn) {
        return entityIn.tFloat();
    }
}

/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import drzhark.mocreatures.MoCreatures;
import drzhark.mocreatures.entity.IMoCEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityAttachment;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.client.ClientHooks;
import net.neoforged.neoforge.client.event.RenderNameTagEvent;
import net.neoforged.neoforge.common.NeoForge;
import org.joml.Matrix4f;

/** One nameplate path for both vanilla name tags and MoC pets. */
public abstract class MoCNameplateRenderer<T extends Mob, M extends EntityModel<T>> extends MobRenderer<T, M> {
    private static final double PET_DISPLAY_DISTANCE_SQUARED = 16.0 * 16.0;

    protected MoCNameplateRenderer(EntityRendererProvider.Context context, M model, float shadowSize) {
        super(context, model, shadowSize);
    }

    @Override
    public void render(T entity, float yaw, float partialTick, PoseStack pose, MultiBufferSource buffers, int light) {
        // EntityRenderer posts RenderNameTagEvent and invokes renderNameTag once.
        super.render(entity, yaw, partialTick, pose, buffers, light);
        renderPetHealth(entity, pose, buffers, light, partialTick);
    }

    @Override
    protected boolean shouldShowName(T entity) {
        if (super.shouldShowName(entity)) {
            return true;
        }
        Minecraft minecraft = Minecraft.getInstance();
        return entity instanceof IMoCEntity pet && pet.getIsTamed()
                && MoCreatures.proxy.getDisplayPetName() && !pet.getPetName().isEmpty()
                && Minecraft.renderNames() && minecraft.player != null
                && !entity.isInvisibleTo(minecraft.player) && !entity.isVehicle() && !entity.isPassenger()
                && this.entityRenderDispatcher.distanceToSqr(entity) < PET_DISPLAY_DISTANCE_SQUARED;
    }

    @Override
    protected void renderNameTag(T entity, Component text, PoseStack pose, MultiBufferSource buffers,
                                 int light, float partialTick) {
        if (!ClientHooks.isNameplateInRenderDistance(entity, this.entityRenderDispatcher.distanceToSqr(entity))) {
            return;
        }
        Vec3 attachment = entity.getAttachments().getNullable(EntityAttachment.NAME_TAG, 0, entity.getViewYRot(partialTick));
        if (attachment == null) {
            return;
        }
        pose.pushPose();
        try {
            pose.translate(attachment.x, attachment.y + 0.5, attachment.z);
            pose.mulPose(this.entityRenderDispatcher.cameraOrientation());
            pose.scale(0.025F, -0.025F, 0.025F);
            Font font = this.getFont();
            float x = -font.width(text) / 2.0F;
            float y = "deadmau5".equals(text.getString()) ? -10.0F : 0.0F;
            int background = (int) (Minecraft.getInstance().options.getBackgroundOpacity(0.25F) * 255.0F) << 24;
            // A single depth-tested text pass, with the font's own background.
            // Never use the GUI render type or flush the caller's shared buffers here.
            font.drawInBatch(text, x, y, 0xFFFFFFFF, false, pose.last().pose(), buffers,
                    Font.DisplayMode.NORMAL, background, light);
        } finally {
            pose.popPose();
        }
    }

    /** Manual model renderers (ghosts/vanishing horses) skip EntityRenderer.render. */
    protected void renderNameAndHealth(T entity, PoseStack pose, MultiBufferSource buffers, int light, float partialTick) {
        var event = new RenderNameTagEvent(entity, entity.getDisplayName(), this, pose, buffers, light, partialTick);
        NeoForge.EVENT_BUS.post(event);
        if (event.canRender().isTrue() || event.canRender().isDefault() && shouldShowName(entity)) {
            renderNameTag(entity, event.getContent(), pose, buffers, light, partialTick);
        }
        renderPetHealth(entity, pose, buffers, light, partialTick);
    }

    private void renderPetHealth(T entity, PoseStack pose, MultiBufferSource buffers, int light, float partialTick) {
        Minecraft minecraft = Minecraft.getInstance();
        if (!(entity instanceof IMoCEntity pet) || !pet.getIsTamed() || !MoCreatures.proxy.getDisplayPetHealth()
                || !Minecraft.renderNames() || minecraft.player == null || entity.isInvisibleTo(minecraft.player)
                || entity.isVehicle() || entity.isPassenger()
                || this.entityRenderDispatcher.distanceToSqr(entity) >= PET_DISPLAY_DISTANCE_SQUARED) {
            return;
        }
        Vec3 attachment = entity.getAttachments().getNullable(EntityAttachment.NAME_TAG, 0, entity.getViewYRot(partialTick));
        if (attachment == null) {
            return;
        }
        float maxHealth = entity.getMaxHealth();
        float ratio = maxHealth > 0.0F ? Mth.clamp(entity.getHealth() / maxHealth, 0.0F, 1.0F) : 0.0F;
        pose.pushPose();
        try {
            pose.translate(attachment.x, attachment.y + 0.5, attachment.z);
            pose.mulPose(this.entityRenderDispatcher.cameraOrientation());
            pose.scale(0.025F, -0.025F, 0.025F);
            // POSITION_COLOR_LIGHTMAP: no missing white texture, UVs, overlay or normals.
            VertexConsumer vertices = buffers.getBuffer(RenderType.textBackground());
            Matrix4f matrix = pose.last().pose();
            float split = -20.0F + 40.0F * ratio;
            quad(vertices, matrix, -20.0F, split, 0xFF00B300, light);
            quad(vertices, matrix, split, 20.0F, 0xFFB30000, light);
        } finally {
            pose.popPose();
        }
    }

    private static void quad(VertexConsumer vertices, Matrix4f matrix, float left, float right, int color, int light) {
        if (right <= left) return;
        vertices.addVertex(matrix, left, -8.0F, 0.0F).setColor(color).setLight(light);
        vertices.addVertex(matrix, left, -4.0F, 0.0F).setColor(color).setLight(light);
        vertices.addVertex(matrix, right, -4.0F, 0.0F).setColor(color).setLight(light);
        vertices.addVertex(matrix, right, -8.0F, 0.0F).setColor(color).setLight(light);
    }
}

/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.network.message;

import drzhark.mocreatures.client.gui.MoCGUIEntityNamer;
import drzhark.mocreatures.entity.IMoCEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import drzhark.mocreatures.MoCConstants;
import net.minecraft.world.entity.Entity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.fml.DistExecutor;
import drzhark.mocreatures.network.MoCNetworkContext;

import java.util.function.Supplier;

public class MoCMessageNameGUI implements CustomPacketPayload {

    public static final Type<MoCMessageNameGUI> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(MoCConstants.MOD_ID, "name_gui"));
    public static final StreamCodec<FriendlyByteBuf, MoCMessageNameGUI> STREAM_CODEC = StreamCodec.ofMember(MoCMessageNameGUI::encode, MoCMessageNameGUI::new);
    public int entityId;

    public MoCMessageNameGUI() {}

    public MoCMessageNameGUI(int entityId) {
        this.entityId = entityId;
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeInt(this.entityId);
    }

    public MoCMessageNameGUI(FriendlyByteBuf buffer) {
        this.entityId = buffer.readInt();
    }

    public static void onMessage(MoCMessageNameGUI message, Supplier<MoCNetworkContext> ctx) {
        // We're already on the client when we receive this packet
        if (DistExecutor.unsafeRunForDist(
                () -> () -> {
                    ctx.get().enqueueWork(() -> handleClient(message));
                    return true;
                },
                () -> () -> false)) {
            // Only executed on client side
        }
        ctx.get().setPacketHandled(true);
    }

    @OnlyIn(Dist.CLIENT)
    private static void handleClient(MoCMessageNameGUI message) {
        Minecraft mc = Minecraft.getInstance();
        if (mc == null || mc.player == null || mc.level == null) return;

        Entity ent = mc.level.getEntity(message.entityId);
        if (ent instanceof IMoCEntity) {
            IMoCEntity mocEntity = (IMoCEntity) ent;
            mc.setScreen(new MoCGUIEntityNamer(mocEntity, mocEntity.getPetName()));
        }
    }


    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    @Override
    public String toString() {
        return "MoCMessageNameGUI - entityId: " + this.entityId;
    }
}

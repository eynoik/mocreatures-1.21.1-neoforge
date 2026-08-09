/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.network.message;

import drzhark.mocreatures.entity.IMoCEntity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import drzhark.mocreatures.MoCConstants;
import drzhark.mocreatures.network.MoCNetworkContext;

import java.util.function.Supplier;

public class MoCMessageEntityDive implements CustomPacketPayload {

    public static final Type<MoCMessageEntityDive> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(MoCConstants.MOD_ID, "entity_dive"));
    public static final StreamCodec<FriendlyByteBuf, MoCMessageEntityDive> STREAM_CODEC = StreamCodec.ofMember(MoCMessageEntityDive::encode, MoCMessageEntityDive::new);

    public MoCMessageEntityDive() {
    }

    public void encode(FriendlyByteBuf buffer) {
    }

    public MoCMessageEntityDive(FriendlyByteBuf buffer) {
    }

    public static void onMessage(MoCMessageEntityDive message, Supplier<MoCNetworkContext> ctx) {
        ctx.get().enqueueWork(() -> {
            if (ctx.get().getSender() != null && ctx.get().getSender().getVehicle() instanceof IMoCEntity) {
                ((IMoCEntity) ctx.get().getSender().getVehicle()).makeEntityDive();
            }
        });
        ctx.get().setPacketHandled(true);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

}

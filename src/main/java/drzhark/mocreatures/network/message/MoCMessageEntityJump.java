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
import net.minecraft.world.entity.Entity;
import drzhark.mocreatures.network.MoCNetworkContext;

import java.util.function.Supplier;

public class MoCMessageEntityJump implements CustomPacketPayload {

    public static final Type<MoCMessageEntityJump> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(MoCConstants.MOD_ID, "entity_jump"));
    public static final StreamCodec<FriendlyByteBuf, MoCMessageEntityJump> STREAM_CODEC = StreamCodec.ofMember(MoCMessageEntityJump::encode, MoCMessageEntityJump::new);

    public MoCMessageEntityJump() {
    }

    public void encode(FriendlyByteBuf buffer) {
        // No data to encode
    }

    public MoCMessageEntityJump(FriendlyByteBuf buffer) {
        // No data to decode
    }

    public static void onMessage(MoCMessageEntityJump message, Supplier<MoCNetworkContext> ctx) {
        ctx.get().enqueueWork(() -> {
            // This is a server-side message handler
            if (ctx.get().getSender() != null) {
                Entity vehicle = ctx.get().getSender().getVehicle();
                if (vehicle instanceof IMoCEntity) {
                    ((IMoCEntity) vehicle).makeEntityJump();
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }


    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    @Override
    public String toString() {
        return "MoCMessageEntityJump";
    }
}

/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.network.message;

import drzhark.mocreatures.entity.passive.MoCEntityHorse;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import drzhark.mocreatures.MoCConstants;
import net.minecraft.world.entity.Entity;
import drzhark.mocreatures.network.MoCNetworkContext;

import java.util.function.Supplier;

public class MoCMessageVanish implements CustomPacketPayload {

    public static final Type<MoCMessageVanish> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(MoCConstants.MOD_ID, "vanish"));
    public static final StreamCodec<FriendlyByteBuf, MoCMessageVanish> STREAM_CODEC = StreamCodec.ofMember(MoCMessageVanish::encode, MoCMessageVanish::new);

    public int entityId;

    public MoCMessageVanish() {
    }

    public MoCMessageVanish(int entityId) {
        this.entityId = entityId;
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeInt(this.entityId);
    }

    public MoCMessageVanish(FriendlyByteBuf buffer) {
        this.entityId = buffer.readInt();
    }

    public static void onMessage(MoCMessageVanish message, Supplier<MoCNetworkContext> ctx) {
        ctx.get().enqueueWork(() -> {
            Entity ent = Minecraft.getInstance().player.level().getEntity(message.entityId);
            if (ent instanceof MoCEntityHorse) {
                ((MoCEntityHorse) ent).setVanishC((byte) 1);
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
        return String.format("MoCMessageVanish - entityId:%s", this.entityId);
    }
}

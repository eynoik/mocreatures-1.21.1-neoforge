/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.network.message;

import drzhark.mocreatures.entity.tameable.IMoCTameable;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import drzhark.mocreatures.MoCConstants;
import net.minecraft.world.entity.Entity;
import drzhark.mocreatures.network.MoCNetworkContext;

import java.util.function.Supplier;

public class MoCMessageHeart implements CustomPacketPayload {

    public static final Type<MoCMessageHeart> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(MoCConstants.MOD_ID, "heart"));
    public static final StreamCodec<FriendlyByteBuf, MoCMessageHeart> STREAM_CODEC = StreamCodec.ofMember(MoCMessageHeart::encode, MoCMessageHeart::new);

    public int entityId;

    public MoCMessageHeart() {
    }

    public MoCMessageHeart(int entityId) {
        this.entityId = entityId;
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeInt(this.entityId);
    }

    public MoCMessageHeart(FriendlyByteBuf buffer) {
        this.entityId = buffer.readInt();
    }

    public static void onMessage(MoCMessageHeart message, Supplier<MoCNetworkContext> ctx) {
        ctx.get().enqueueWork(() -> {
            Entity ent = Minecraft.getInstance().player.level().getEntity(message.entityId);
            if (ent instanceof IMoCTameable) {
                ((IMoCTameable) ent).spawnHeart();
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
        return String.format("MoCMessageHeart - entityId:%s", this.entityId);
    }
}

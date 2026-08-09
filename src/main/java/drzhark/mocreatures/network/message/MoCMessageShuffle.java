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

public class MoCMessageShuffle implements CustomPacketPayload {

    public static final Type<MoCMessageShuffle> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(MoCConstants.MOD_ID, "shuffle"));
    public static final StreamCodec<FriendlyByteBuf, MoCMessageShuffle> STREAM_CODEC = StreamCodec.ofMember(MoCMessageShuffle::encode, MoCMessageShuffle::new);

    public int entityId;
    public boolean flag;

    public MoCMessageShuffle() {
    }

    public MoCMessageShuffle(int entityId, boolean flag) {
        this.entityId = entityId;
        this.flag = flag;
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeInt(this.entityId);
        buffer.writeBoolean(this.flag);
    }

    public MoCMessageShuffle(FriendlyByteBuf buffer) {
        this.entityId = buffer.readInt();
        this.flag = buffer.readBoolean();
    }

    public static void onMessage(MoCMessageShuffle message, Supplier<MoCNetworkContext> ctx) {
        ctx.get().enqueueWork(() -> {
            Entity ent = Minecraft.getInstance().player.level().getEntity(message.entityId);
            if (ent instanceof MoCEntityHorse) {
                if (message.flag) {
                    //((MoCEntityHorse) ent).shuffle();
                } else {
                    ((MoCEntityHorse) ent).shuffleCounter = 0;
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
        return String.format("MoCMessageShuffle - entityId:%s, flag:%s", this.entityId, this.flag);
    }
}

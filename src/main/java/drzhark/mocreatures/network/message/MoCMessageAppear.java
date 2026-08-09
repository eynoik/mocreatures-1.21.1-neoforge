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

public class MoCMessageAppear implements CustomPacketPayload {

    public static final Type<MoCMessageAppear> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(MoCConstants.MOD_ID, "appear"));
    public static final StreamCodec<FriendlyByteBuf, MoCMessageAppear> STREAM_CODEC = StreamCodec.ofMember(MoCMessageAppear::encode, MoCMessageAppear::new);

    public int entityId;

    public MoCMessageAppear(int entityId) {
        this.entityId = entityId;
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeInt(this.entityId);
    }

    public MoCMessageAppear(FriendlyByteBuf buffer) {
        this.entityId = buffer.readInt();
    }

    public static void onMessage(MoCMessageAppear message, Supplier<MoCNetworkContext> ctx) {
        ctx.get().enqueueWork(() -> {
            Minecraft minecraft = Minecraft.getInstance();
            if (minecraft.level != null) {
                Entity entity = minecraft.level.getEntity(message.entityId);
                if (entity instanceof MoCEntityHorse) {
                    ((MoCEntityHorse) entity).MaterializeFX();
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
        return String.format("MoCMessageAppear - entityId:%s", this.entityId);
    }
}

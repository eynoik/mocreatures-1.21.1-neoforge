/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.network.message;

import drzhark.mocreatures.entity.IMoCEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import drzhark.mocreatures.MoCConstants;
import net.minecraft.world.entity.Entity;
import drzhark.mocreatures.network.MoCNetworkContext;

import java.util.function.Supplier;

public class MoCMessageAnimation implements CustomPacketPayload {

    public static final Type<MoCMessageAnimation> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(MoCConstants.MOD_ID, "animation"));
    public static final StreamCodec<FriendlyByteBuf, MoCMessageAnimation> STREAM_CODEC = StreamCodec.ofMember(MoCMessageAnimation::encode, MoCMessageAnimation::new);

    public int entityId;
    public int animationType;

    public MoCMessageAnimation(int entityId, int animationType) {
        this.entityId = entityId;
        this.animationType = animationType;
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeInt(this.entityId);
        buffer.writeInt(this.animationType);
    }

    public MoCMessageAnimation(FriendlyByteBuf buffer) {
        this.entityId = buffer.readInt();
        this.animationType = buffer.readInt();
    }

    public static void onMessage(MoCMessageAnimation message, Supplier<MoCNetworkContext> ctx) {
        ctx.get().enqueueWork(() -> {
            Minecraft minecraft = Minecraft.getInstance();
            if (minecraft.level != null) {
                Entity entity = minecraft.level.getEntity(message.entityId);
                if (entity instanceof IMoCEntity) {
                    ((IMoCEntity) entity).performAnimation(message.animationType);
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
        return String.format("MoCMessageAnimation - entityId:%s, animationType:%s", this.entityId, this.animationType);
    }
}

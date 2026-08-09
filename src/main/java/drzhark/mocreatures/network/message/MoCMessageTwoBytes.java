/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.network.message;

import drzhark.mocreatures.entity.hostile.MoCEntityGolem;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import drzhark.mocreatures.MoCConstants;
import net.minecraft.world.entity.Entity;
import drzhark.mocreatures.network.MoCNetworkContext;

import java.util.function.Supplier;

public class MoCMessageTwoBytes implements CustomPacketPayload {

    public static final Type<MoCMessageTwoBytes> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(MoCConstants.MOD_ID, "two_bytes"));
    public static final StreamCodec<FriendlyByteBuf, MoCMessageTwoBytes> STREAM_CODEC = StreamCodec.ofMember(MoCMessageTwoBytes::encode, MoCMessageTwoBytes::new);

    public int entityId;
    public byte slot;
    public byte value;

    public MoCMessageTwoBytes() {
    }

    public MoCMessageTwoBytes(int entityId, byte slot, byte value) {
        this.entityId = entityId;
        this.slot = slot;
        this.value = value;
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeInt(this.entityId);
        buffer.writeByte(this.slot);
        buffer.writeByte(this.value);
    }

    public MoCMessageTwoBytes(FriendlyByteBuf buffer) {
        this.entityId = buffer.readInt();
        this.slot = buffer.readByte();
        this.value = buffer.readByte();
    }

    public static void onMessage(MoCMessageTwoBytes message, Supplier<MoCNetworkContext> ctx) {
        ctx.get().enqueueWork(() -> {
            Minecraft minecraft = Minecraft.getInstance();
            if (minecraft.level != null) {
                Entity ent = minecraft.level.getEntity(message.entityId);
                if (ent instanceof MoCEntityGolem) {
                    ((MoCEntityGolem) ent).saveGolemCube(message.slot, message.value);
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
        return String.format("MoCMessageTwoBytes - entityId:%s, slot:%s, value:%s", this.entityId, this.slot, this.value);
    }
}

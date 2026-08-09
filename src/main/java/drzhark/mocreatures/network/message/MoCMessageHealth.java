/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.network.message;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import drzhark.mocreatures.MoCConstants;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import drzhark.mocreatures.network.MoCNetworkContext;

import java.util.function.Supplier;

public class MoCMessageHealth implements CustomPacketPayload {

    public static final Type<MoCMessageHealth> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(MoCConstants.MOD_ID, "health"));
    public static final StreamCodec<FriendlyByteBuf, MoCMessageHealth> STREAM_CODEC = StreamCodec.ofMember(MoCMessageHealth::encode, MoCMessageHealth::new);

    public int entityId;
    public float health;

    public MoCMessageHealth(int entityId, float health) {
        this.entityId = entityId;
        this.health = health;
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeInt(this.entityId);
        buffer.writeFloat(this.health);
    }

    public MoCMessageHealth(FriendlyByteBuf buffer) {
        this.entityId = buffer.readInt();
        this.health = buffer.readFloat();
    }

    public static void onMessage(MoCMessageHealth message, Supplier<MoCNetworkContext> ctx) {
        ctx.get().enqueueWork(() -> {
            Minecraft minecraft = Minecraft.getInstance();
            if (minecraft.level != null) {
                Entity entity = minecraft.level.getEntity(message.entityId);
                if (entity instanceof Mob mob) {
                    mob.setHealth(message.health);
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
        return String.format("MoCMessageHealth - entityId:%s, health:%s", this.entityId, this.health);
    }
}

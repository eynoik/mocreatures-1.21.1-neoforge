/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.network;

import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.concurrent.CompletableFuture;

/** Minimal adapter used while preserving the original 1.20.1 packet handler bodies. */
public final class MoCNetworkContext {
    private final IPayloadContext context;

    public MoCNetworkContext(IPayloadContext context) {
        this.context = context;
    }

    public CompletableFuture<Void> enqueueWork(Runnable task) {
        return context.enqueueWork(task);
    }

    public ServerPlayer getSender() {
        return context.player() instanceof ServerPlayer serverPlayer ? serverPlayer : null;
    }

    /** NeoForge payload handlers are considered handled once the callback returns. */
    public void setPacketHandled(boolean handled) {
        // Compatibility no-op.
    }
}

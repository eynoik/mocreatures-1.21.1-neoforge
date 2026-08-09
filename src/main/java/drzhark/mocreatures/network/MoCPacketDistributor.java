/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.network;

import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

import java.util.function.Supplier;

/**
 * Small source-compatibility layer for the old Forge PacketDistributor target syntax.
 * Actual packets are sent through NeoForge's payload PacketDistributor.
 */
public final class MoCPacketDistributor {
    private MoCPacketDistributor() {}

    public static final TargetType PLAYER = new TargetType(TargetKind.PLAYER);
    public static final TargetType NEAR = new TargetType(TargetKind.NEAR);
    public static final TargetType TRACKING_ENTITY = new TargetType(TargetKind.TRACKING_ENTITY);

    public enum TargetKind { PLAYER, NEAR, TRACKING_ENTITY }

    public static final class TargetType {
        private final TargetKind kind;
        private TargetType(TargetKind kind) { this.kind = kind; }
        public Target with(Supplier<?> supplier) { return new Target(kind, supplier.get()); }
    }

    public record Target(TargetKind kind, Object value) {}

    public record TargetPoint(double x, double y, double z, double radius, ResourceKey<Level> dimension) {}

    public static void send(Target target, net.minecraft.network.protocol.common.custom.CustomPacketPayload payload) {
        switch (target.kind()) {
            case PLAYER -> net.neoforged.neoforge.network.PacketDistributor.sendToPlayer((ServerPlayer) target.value(), payload);
            case TRACKING_ENTITY -> net.neoforged.neoforge.network.PacketDistributor.sendToPlayersTrackingEntity((Entity) target.value(), payload);
            case NEAR -> {
                TargetPoint point = (TargetPoint) target.value();
                MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
                if (server == null) return;
                ServerLevel level = server.getLevel(point.dimension());
                if (level != null) {
                    net.neoforged.neoforge.network.PacketDistributor.sendToPlayersNear(
                            level, null, point.x(), point.y(), point.z(), point.radius(), payload);
                }
            }
        }
    }
}

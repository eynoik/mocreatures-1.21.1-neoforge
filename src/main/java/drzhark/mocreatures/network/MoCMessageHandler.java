/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.network;

import drzhark.mocreatures.network.message.*;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

import java.util.function.BiConsumer;

/** NeoForge 1.21.1 payload registration and a small send compatibility facade. */
public final class MoCMessageHandler {
    private static final String PROTOCOL_VERSION = "1";
    public static final ChannelFacade INSTANCE = new ChannelFacade();

    private MoCMessageHandler() {}

    public static void register(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar(PROTOCOL_VERSION);

        // Server -> client
        registerClient(registrar, MoCMessageAnimation.TYPE, MoCMessageAnimation.STREAM_CODEC, MoCMessageAnimation::onMessage);
        registerClient(registrar, MoCMessageAppear.TYPE, MoCMessageAppear.STREAM_CODEC, MoCMessageAppear::onMessage);
        registerClient(registrar, MoCMessageAttachedEntity.TYPE, MoCMessageAttachedEntity.STREAM_CODEC, MoCMessageAttachedEntity::onMessage);
        registerClient(registrar, MoCMessageExplode.TYPE, MoCMessageExplode.STREAM_CODEC, MoCMessageExplode::onMessage);
        registerClient(registrar, MoCMessageHealth.TYPE, MoCMessageHealth.STREAM_CODEC, MoCMessageHealth::onMessage);
        registerClient(registrar, MoCMessageHeart.TYPE, MoCMessageHeart.STREAM_CODEC, MoCMessageHeart::onMessage);
        registerClient(registrar, MoCMessageNameGUI.TYPE, MoCMessageNameGUI.STREAM_CODEC, MoCMessageNameGUI::onMessage);
        registerClient(registrar, MoCMessageShuffle.TYPE, MoCMessageShuffle.STREAM_CODEC, MoCMessageShuffle::onMessage);
        registerClient(registrar, MoCMessageTwoBytes.TYPE, MoCMessageTwoBytes.STREAM_CODEC, MoCMessageTwoBytes::onMessage);
        registerClient(registrar, MoCMessageVanish.TYPE, MoCMessageVanish.STREAM_CODEC, MoCMessageVanish::onMessage);

        // Client -> server
        registerServer(registrar, MoCMessageEntityDive.TYPE, MoCMessageEntityDive.STREAM_CODEC, MoCMessageEntityDive::onMessage);
        registerServer(registrar, MoCMessageEntityJump.TYPE, MoCMessageEntityJump.STREAM_CODEC, MoCMessageEntityJump::onMessage);
        registerServer(registrar, MoCMessageInstaSpawn.TYPE, MoCMessageInstaSpawn.STREAM_CODEC, MoCMessageInstaSpawn::onMessage);
        registerServer(registrar, MoCMessageUpdatePetName.TYPE, MoCMessageUpdatePetName.STREAM_CODEC, MoCMessageUpdatePetName::onMessage);
    }

    private static <T extends CustomPacketPayload> void registerClient(
            PayloadRegistrar registrar,
            CustomPacketPayload.Type<T> type,
            net.minecraft.network.codec.StreamCodec<? super net.minecraft.network.RegistryFriendlyByteBuf, T> codec,
            BiConsumer<T, java.util.function.Supplier<MoCNetworkContext>> oldHandler) {
        registrar.playToClient(type, codec, (payload, context) -> oldHandler.accept(payload, () -> new MoCNetworkContext(context)));
    }

    private static <T extends CustomPacketPayload> void registerServer(
            PayloadRegistrar registrar,
            CustomPacketPayload.Type<T> type,
            net.minecraft.network.codec.StreamCodec<? super net.minecraft.network.RegistryFriendlyByteBuf, T> codec,
            BiConsumer<T, java.util.function.Supplier<MoCNetworkContext>> oldHandler) {
        registrar.playToServer(type, codec, (payload, context) -> oldHandler.accept(payload, () -> new MoCNetworkContext(context)));
    }

    public static final class ChannelFacade {
        public void send(MoCPacketDistributor.Target target, CustomPacketPayload payload) {
            MoCPacketDistributor.send(target, payload);
        }

        public void sendToServer(CustomPacketPayload payload) {
            PacketDistributor.sendToServer(payload);
        }
    }
}

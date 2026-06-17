package com.playerwarp.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.List;

public record SyncPlayersPayload(List<String> playerNames) implements CustomPacketPayload {

    public static final Type<SyncPlayersPayload> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath("playerwarp", "sync_players"));

    public static final StreamCodec<ByteBuf, SyncPlayersPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8.apply(ByteBufCodecs.list()),
            SyncPlayersPayload::playerNames,
            SyncPlayersPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handleOnClient(SyncPlayersPayload payload, IPayloadContext ctx) {
        ctx.enqueueWork(() -> com.playerwarp.client.ClientWarpState.setPlayerNames(payload.playerNames()));
    }
}

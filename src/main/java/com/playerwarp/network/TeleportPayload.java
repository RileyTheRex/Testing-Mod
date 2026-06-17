package com.playerwarp.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record TeleportPayload(String targetName) implements CustomPacketPayload {

    public static final Type<TeleportPayload> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath("playerwarp", "teleport"));

    public static final StreamCodec<ByteBuf, TeleportPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8, TeleportPayload::targetName,
            TeleportPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(TeleportPayload payload, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            ServerPlayer sender = (ServerPlayer) ctx.player();
            MinecraftServer server = sender.getServer();
            if (server == null) return;

            ServerPlayer target = server.getPlayerList().getPlayerByName(payload.targetName());
            if (target == null || target.getUUID().equals(sender.getUUID())) return;

            // Teleport — handles cross-dimension via level check
            if (sender.serverLevel() == target.serverLevel()) {
                sender.teleportTo(target.getX(), target.getY(), target.getZ());
            } else {
                sender.teleportTo(target.serverLevel(),
                        target.getX(), target.getY(), target.getZ(),
                        java.util.Set.of(), sender.getYRot(), sender.getXRot());
            }

            // 5 minutes (6000 ticks) of invulnerability: Resistance V = 100% damage reduction
            int duration = 6000;
            sender.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, duration, 4, false, true, true));
            sender.addEffect(new MobEffectInstance(MobEffects.REGENERATION, duration, 1, false, true, true));

            sender.displayClientMessage(
                    net.minecraft.network.chat.Component.translatable("playerwarp.chat.teleported", payload.targetName()),
                    true
            );
        });
    }
}

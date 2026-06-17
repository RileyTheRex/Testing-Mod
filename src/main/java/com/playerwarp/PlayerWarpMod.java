package com.playerwarp;

import com.playerwarp.init.ModItems;
import com.playerwarp.network.SyncPlayersPayload;
import com.playerwarp.network.TeleportPayload;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@Mod("playerwarp")
public class PlayerWarpMod {

    public static final String MODID = "playerwarp";

    public PlayerWarpMod(IEventBus modEventBus, ModContainer modContainer) {
        ModItems.ITEMS.register(modEventBus);
        modEventBus.addListener(PlayerWarpMod::registerPayloads);
    }

    private static void registerPayloads(RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar("1.0.0");
        registrar.playToServer(TeleportPayload.TYPE, TeleportPayload.STREAM_CODEC, TeleportPayload::handle);
        registrar.playToClient(SyncPlayersPayload.TYPE, SyncPlayersPayload.STREAM_CODEC, SyncPlayersPayload::handleOnClient);
    }
}

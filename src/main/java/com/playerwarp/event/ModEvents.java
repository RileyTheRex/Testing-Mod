package com.playerwarp.event;

import com.playerwarp.init.ModItems;
import com.playerwarp.network.SyncPlayersPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDropsEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.List;

@EventBusSubscriber(modid = "playerwarp", bus = EventBusSubscriber.Bus.GAME)
public final class ModEvents {

    private ModEvents() {}

    @SubscribeEvent
    public static void onLivingDrops(LivingDropsEvent event) {
        if (event.getEntity() instanceof Player) return;
        if (event.getEntity().level().isClientSide) return;

        if (event.getEntity().getRandom().nextFloat() < 0.01f) {
            ItemEntity drop = new ItemEntity(
                    event.getEntity().level(),
                    event.getEntity().getX(),
                    event.getEntity().getY() + event.getEntity().getBbHeight() / 2.0,
                    event.getEntity().getZ(),
                    new ItemStack(ModItems.WARP_DRIVE.get())
            );
            event.getDrops().add(drop);
        }
    }

    @SubscribeEvent
    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        MinecraftServer server = event.getEntity().getServer();
        if (server == null) return;
        broadcastPlayerList(server, null);
    }

    @SubscribeEvent
    public static void onPlayerLogout(PlayerEvent.PlayerLoggedOutEvent event) {
        MinecraftServer server = event.getEntity().getServer();
        if (server == null) return;
        // Exclude the leaving player since they're still in the list at this moment
        String leavingName = event.getEntity().getGameProfile().getName();
        broadcastPlayerList(server, leavingName);
    }

    private static void broadcastPlayerList(MinecraftServer server, String excludeName) {
        List<String> names = server.getPlayerList().getPlayers().stream()
                .map(p -> p.getGameProfile().getName())
                .filter(name -> !name.equals(excludeName))
                .toList();
        PacketDistributor.sendToAllPlayers(new SyncPlayersPayload(names));
    }
}

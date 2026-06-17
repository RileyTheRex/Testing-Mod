package com.playerwarp.client;

import net.neoforged.neoforge.network.PacketDistributor;
import com.playerwarp.network.TeleportPayload;

import java.util.ArrayList;
import java.util.List;

public final class ClientWarpState {

    private static List<String> playerNames = new ArrayList<>();
    private static int selectedIndex = 0;

    private ClientWarpState() {}

    public static void setPlayerNames(List<String> names) {
        playerNames = new ArrayList<>(names);
        if (selectedIndex >= playerNames.size()) {
            selectedIndex = 0;
        }
    }

    public static void cycle(int delta) {
        if (playerNames.isEmpty()) return;
        selectedIndex = Math.floorMod(selectedIndex + delta, playerNames.size());
    }

    public static String getSelected() {
        if (playerNames.isEmpty()) return null;
        return playerNames.get(selectedIndex);
    }

    public static int getSelectedIndex() {
        return selectedIndex;
    }

    public static List<String> getPlayerNames() {
        return playerNames;
    }

    public static void sendTeleport() {
        String target = getSelected();
        if (target != null) {
            PacketDistributor.sendToServer(new TeleportPayload(target));
        }
    }
}

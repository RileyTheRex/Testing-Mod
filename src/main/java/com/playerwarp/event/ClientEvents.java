package com.playerwarp.event;

import com.mojang.blaze3d.vertex.PoseStack;
import com.playerwarp.client.ClientWarpState;
import com.playerwarp.init.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.client.event.RenderGuiEvent;

@EventBusSubscriber(modid = "playerwarp", bus = EventBusSubscriber.Bus.GAME, value = Dist.CLIENT)
public final class ClientEvents {

    private ClientEvents() {}

    @SubscribeEvent
    public static void onMouseScroll(InputEvent.MouseScrollingEvent event) {
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;
        if (player == null || mc.screen != null) return;
        if (!(player.getMainHandItem().getItem() instanceof com.playerwarp.item.WarpDriveItem)) return;
        if (!net.minecraft.client.gui.screens.Screen.hasControlDown()) return;

        event.setCanceled(true);
        int delta = event.getScrollDeltaY() > 0 ? -1 : 1;
        ClientWarpState.cycle(delta);
    }

    @SubscribeEvent
    public static void onRenderHud(RenderGuiEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;
        if (player == null) return;
        if (!(player.getMainHandItem().getItem() instanceof com.playerwarp.item.WarpDriveItem)) return;
        if (!net.minecraft.client.gui.screens.Screen.hasControlDown()) return;

        GuiGraphics gui = event.getGuiGraphics();
        int screenWidth = mc.getWindow().getGuiScaledWidth();
        int screenHeight = mc.getWindow().getGuiScaledHeight();

        String selected = ClientWarpState.getSelected();
        Component text = selected != null
                ? Component.translatable("playerwarp.hud.select", selected)
                : Component.translatable("playerwarp.hud.no_players");

        int textWidth = mc.font.width(text);
        int x = (screenWidth - textWidth) / 2;
        int y = screenHeight / 2 + 20;

        gui.drawString(mc.font, text, x, y, 0xFFFFFF);
    }
}

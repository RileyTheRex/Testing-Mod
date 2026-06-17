package com.playerwarp.item;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.loading.FMLEnvironment;

public class WarpDriveItem extends Item {

    public WarpDriveItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (level.isClientSide) {
            handleClientUse(player);
        }
        return InteractionResultHolder.success(player.getItemInHand(hand));
    }

    private static void handleClientUse(Player player) {
        if (FMLEnvironment.dist == Dist.CLIENT) {
            com.playerwarp.client.ClientWarpState.sendTeleport();
        }
    }
}

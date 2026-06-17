package com.playerwarp.init;

import com.playerwarp.item.WarpDriveItem;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModItems {

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems("playerwarp");

    public static final DeferredItem<WarpDriveItem> WARP_DRIVE = ITEMS.registerItem(
            "warp_drive",
            WarpDriveItem::new,
            new Item.Properties().durability(20)
    );

    private ModItems() {}
}

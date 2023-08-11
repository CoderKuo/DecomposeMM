package com.dakuo.decomposemm.service.mythic;

import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.bukkit.adapters.BukkitItemStack;
import io.lumine.mythic.core.items.MythicItem;
import org.bukkit.inventory.ItemStack;

import java.util.Optional;

public class MythicMobs5 implements IMythicMobs{
    @Override
    public ItemStack getItem(String id, int amount) {
        Optional<MythicItem> item = MythicBukkit.inst().getItemManager().getItem(id);
        return item.map(mythicItem -> ((BukkitItemStack) mythicItem.generateItemStack(amount)).build()).orElse(null);
    }

    @Override
    public ItemStack getItem(String id) {
        return getItem(id,1);
    }
}

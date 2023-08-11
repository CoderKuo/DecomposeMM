package com.dakuo.decomposemm.service.mythic;

import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitItemStack;
import io.lumine.xikage.mythicmobs.items.MythicItem;
import org.bukkit.inventory.ItemStack;

import java.util.Optional;

public class MythicMobs4 implements IMythicMobs{

    @Override
    public ItemStack getItem(String id,int amount) {
        Optional<MythicItem> item = MythicMobs.inst().getItemManager().getItem(id);
        return item.map(mythicItem -> ((BukkitItemStack) mythicItem.generateItemStack(amount)).build()).orElse(null);
    }

    @Override
    public ItemStack getItem(String id) {
        return getItem(id,1);
    }

}

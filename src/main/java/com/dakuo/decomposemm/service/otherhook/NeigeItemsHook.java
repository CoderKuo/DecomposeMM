package com.dakuo.decomposemm.service.otherhook;

import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;
import pers.neige.neigeitems.manager.ItemManager;

import java.util.Map;
import java.util.Objects;

public class NeigeItemsHook {

    public ItemStack getItem(OfflinePlayer player, String id, int amount, Map<String,String> data){
        ItemStack itemStack = Objects.requireNonNull(ItemManager.INSTANCE.getItem(id)).getItemStack(player, data);
        itemStack.setAmount(amount);
        return itemStack;
    }

}

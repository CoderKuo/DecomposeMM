package com.dakuo.decomposemm.inventory;

import com.dakuo.decomposemm.DecomposeMM;
import com.dakuo.decomposemm.service.DataService;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.*;

public class DInventoryMonitor implements Listener {
    private DInventory inventory;
    private Player player;


    @EventHandler
    public void openMonitor(InventoryOpenEvent event){
        Inventory inventory = event.getInventory();
        if (!checkInventory(inventory)){
            return;
        }
        this.player = (Player) event.getPlayer();
        DInventoryHolder holder = (DInventoryHolder) inventory.getHolder();
        this.inventory = holder.inventoryInstance;

    }

    public boolean isButton(int slot){
        return Objects.equals(inventory.slotsMap.get(slot), "b");
    }


    @EventHandler
    public void monitor(InventoryClickEvent event){
        Inventory clickedInventory = event.getClickedInventory();
        if (!checkInventory(clickedInventory)){
            return;
        }

        if (!isButton(event.getRawSlot())) {
            return;
        }
        event.setCancelled(true);
        if (!inventory.isClicked) {
            inventory.isClicked = true;
            inventory.updateClick();

            return;
        }
        decompose(holder);
    }



    public void decompose(DInventoryHolder holder){
        List<ItemStack> items = getItems();
        Iterator<ItemStack> iterator = items.iterator();
        List<ItemStack> resultItems = new ArrayList<>();
        int success = 0;
        int fail = 0;
        while (iterator.hasNext()) {
            ItemStack next = iterator.next();
            if (next == holder.inventoryInstance.getButton()) {
                continue;
            }
            List<ItemStack> itemStacks = DataService.getInstance().toItems(next,player);
            if (itemStacks != null) {
                resultItems.addAll(itemStacks);
                success += next.getAmount();
                continue;
            }
            resultItems.add(next);
            fail += next.getAmount();
        }
        clear();
        giveItem(resultItems);
        DecomposeMM plugin = DecomposeMM.getPlugin(DecomposeMM.class);
        this.player.sendMessage(plugin.getConfig().getString("message.success").replace("{1}", String.valueOf(success)).replace("{2}", String.valueOf(fail)).replace("&","§"));
        holder.inventoryInstance.isClicked = false;
        inventory.setItem(buttonSlot, holder.inventoryInstance.getButton());
    }



    @EventHandler
    public void onClose(InventoryCloseEvent e) {
        if (e.getInventory().getHolder() instanceof DInventoryHolder) {
            if (DecomposeMM.getPlugin(DecomposeMM.class).getAutoDecompose()){
                decompose((DInventoryHolder) e.getInventory().getHolder());
            }else {
                List<ItemStack> items = getItems();
                giveItem(items);
            }
        }
    }

    public boolean checkInventory(Inventory inventory){
        if (inventory == null){
            return false;
        }
        return inventory.getHolder() instanceof DInventoryHolder;
    }



    private List<ItemStack> getItems() {
        List<ItemStack> itemStacks = new ArrayList<>();
        for (int i = 0; i < inventory.getSize(); i++) {
            ItemStack item = inventory.getItem(i);
            if (item != null && !item.getType().equals(Material.AIR) && i != buttonSlot) {
                itemStacks.add(item);
            }
        }
        return itemStacks;
    }

    public void clear() {
        for (int i = 0; i < inventory.getSize(); i++) {
            if (i == buttonSlot) {
                continue;
            }
            inventory.clear(i);
        }
    }


    private void giveItem(List<ItemStack> itemStacks) {
        PlayerInventory playerInventory = player.getInventory();
        for (ItemStack itemStack : itemStacks) {
            if (isFull(player)) {
                player.getWorld().dropItem(player.getLocation(), itemStack);
                continue;
            }
            playerInventory.addItem(itemStack);
        }
        player.updateInventory();
    }

    public boolean isFull(Player player) {
        return Arrays.<ItemStack>stream(player.getInventory().getStorageContents()).noneMatch(itemStack -> (itemStack == null || itemStack.getType().equals(Material.AIR)));
    }


}

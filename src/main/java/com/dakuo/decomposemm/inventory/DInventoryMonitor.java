package com.dakuo.decomposemm.inventory;

import com.dakuo.decomposemm.DecomposeMM;
import com.dakuo.decomposemm.service.DataService;
import org.bukkit.Material;
import org.bukkit.entity.Item;
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
import java.util.stream.Stream;

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

        if (isButton(event.getRawSlot())){
            event.setCancelled(true);
            if (!inventory.isClicked) {
                inventory.isClicked = true;
                inventory.updateClick();

                return;
            }
            decompose((DInventoryHolder) clickedInventory.getHolder());
        }

        if (canClicked(event.getRawSlot())){
            event.setCancelled(true);
            return;
        }





    }

    public boolean canClicked(Integer index){
        String o = inventory.slotsMap.getOrDefault(index, "o");
        return (!o.equals(" ") && !o.equals("o"));
    }



    public void decompose(DInventoryHolder holder){
        Map<Integer, ItemStack> items = getItems();
        Iterator<ItemStack> iterator = items.values().iterator();
        List<ItemStack> resultItems = new ArrayList<>();
        int success = 0;
        int fail = 0;
        while (iterator.hasNext()) {
            ItemStack next = iterator.next();
            List<ItemStack> itemStacks = DataService.getInstance().toItems(next,player);
            if (itemStacks != null) {
                resultItems.addAll(itemStacks);
                success += next.getAmount();
                continue;
            }
            resultItems.add(next);
            fail += next.getAmount();
        }
        clear(items);
        giveItem(resultItems);
        DecomposeMM plugin = DecomposeMM.getPlugin(DecomposeMM.class);
        this.player.sendMessage(plugin.getConfig().getString("message.success").replace("{1}", String.valueOf(success)).replace("{2}", String.valueOf(fail)).replace("&","§"));
        holder.inventoryInstance.isClicked = false;
        inventory.updateClick();
    }



    @EventHandler
    public void onClose(InventoryCloseEvent e) {
        if (e.getInventory().getHolder() instanceof DInventoryHolder) {
            if (DecomposeMM.getPlugin(DecomposeMM.class).getAutoDecompose()){
                decompose((DInventoryHolder) e.getInventory().getHolder());
            }else {
                Map<Integer,ItemStack> items = getItems();
                List<ItemStack> itemsList = new ArrayList<>(items.values());
                giveItem(itemsList);
            }
        }
    }

    public boolean checkInventory(Inventory inventory){
        if (inventory == null){
            return false;
        }
        return inventory.getHolder() instanceof DInventoryHolder;
    }



    private Map<Integer,ItemStack> getItems() {
        Map<Integer,ItemStack> itemStacks = new HashMap<>();
//        for (int i = 0; i < inventory.getInventory().getSize(); i++) {
//            ItemStack item = inventory.getInventory().getItem(i);
//            if (item != null && !item.getType().equals(Material.AIR) && i != buttonSlot) {
//                itemStacks.add(item);
//            }
//        }
        inventory.slotsMap.entrySet().stream().filter(integerStringEntry -> {
            return integerStringEntry.getValue().equals("o") || Objects.equals(integerStringEntry.getValue(), " ");
        }).forEach(integerStringEntry -> {
            ItemStack item = inventory.getInventory().getItem(integerStringEntry.getKey());
            if (item != null && !item.getType().equals(Material.AIR)) {
                itemStacks.put(integerStringEntry.getKey(),item);
            }
        });
        return itemStacks;
    }

    public void clear(Map<Integer,ItemStack> items) {
        items.keySet().forEach(inventory.getInventory()::clear);
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

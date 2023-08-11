package com.dakuo.decomposemm.inventory;

import com.dakuo.decomposemm.DecomposeMM;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;

import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class DInventory implements Listener {
    private final Inventory inventory;
    private final DecomposeMM plugin;
    public Player player;

    public Map<Integer,String> slotsMap;

    private Map<String,ItemStack> itemStackMap = new HashMap<>();

    public boolean isClicked = false;

    public DInventory(Player player) {
        plugin = DecomposeMM.getPlugin(DecomposeMM.class);
        ConfigurationSection gui = plugin.getGui();
        String title = gui.getString("title").replace("&", "§");
        int size = gui.getInt("size");
        inventory = Bukkit.createInventory(new DInventoryHolder(this), size, title);
        updateClick();

        this.player = player;
    }

    public void updateClick(){
        if (isClicked){
            Map<String,ItemStack> items = new HashMap<>();
            itemStackMap.forEach((s, itemStack) -> {
                items.put(s,getItemStackReadyFromSlot(s,itemStack));
            });
            slotsMap.forEach((integer, s) -> {
                if (!s.equals("o") && !s.equals(" ")) {
                    ItemStack orDefault = items.getOrDefault(s, new ItemStack(Material.AIR));
                    inventory.setItem(integer, orDefault);
                }
            });
        }else{
            List<String> slots = plugin.getGui().getStringList("slots");
            slotsMap = getSlotsMap(slots);

            slotsMap.forEach((integer, s) -> {
                if (!s.equals("o") && !s.equals(" ")){
                    ItemStack itemStackFormSlot = getItemStackFromSlot(s);
                    itemStackMap.put(s,itemStackFormSlot);
                    inventory.setItem(integer,itemStackFormSlot);
                }
            });
        }

    }

    private ItemStack getItemStackReadyFromSlot(String slot,ItemStack itemStack){
        ConfigurationSection gui = plugin.getGui();
        ConfigurationSection readySection = gui.getConfigurationSection("items." + slot + ".ready");
        if (readySection == null) return itemStack;
        if (readySection.contains("material")){
            Material material = Material.getMaterial(readySection.getString("material"));
            if (material == null){
                material = Material.AIR;
            }
            itemStack.setType(material);
        }

        return handleItemMeta(itemStack,readySection);
    }


    private ItemStack getItemStackFromSlot(String slot){
        ConfigurationSection gui = plugin.getGui();
        ConfigurationSection items = gui.getConfigurationSection("items."+slot);
        return getItemStackFromSection(items);
    }

    private ItemStack getItemStackFromSection(ConfigurationSection section){
        Material material = Material.getMaterial(section.getString("material"));
        if (material == null){
            material = Material.AIR;
        }
        return handleItemMeta(new ItemStack(material), section);
    }

    private ItemStack handleItemMeta(ItemStack itemStack,ConfigurationSection section){
        ItemMeta itemMeta = itemStack.hasItemMeta() ? itemStack.getItemMeta() : Bukkit.getItemFactory().getItemMeta(itemStack.getType());
        section.getKeys(false).forEach(key->{
            switch (key) {
                case "name":
                    itemMeta.setDisplayName(section.getString("name").replace("&", "§"));
                case "lore":
                    List<String> lore = section.getStringList("lore");
                    lore.replaceAll(l -> l.replace("&", "§"));
                    itemMeta.setLore(lore);
                case "data":
                    int data = section.getInt("data", -1);
                    if (data != -1) {
                        if (itemMeta instanceof Damageable){
                            ((Damageable) itemMeta).setDamage(data);
                        }
                    }
                case "customModelData":
                    if (section.contains("customModelData")){
                        int customModelData = section.getInt("customModelData");
                        itemMeta.setCustomModelData(customModelData);
                    }
            }
        });
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    private Map<Integer,String> getSlotsMap(List<String> slots){
        Map<Integer, String> map = new HashMap<>();
        int currentIndex = 0;

        for (String s : slots) {
            for (int i1 = 0; i1 < s.length(); i1++) {
                char currentChar = s.charAt(i1);

                if (currentChar == '`') {
                    int endIndex = s.indexOf('`', i1 + 1);
                    String buttonText = s.substring(i1+1, endIndex);
                    map.put(currentIndex, buttonText);
                    currentIndex++;
                    i1 = endIndex;
                } else {
                    map.put(currentIndex, String.valueOf(currentChar));
                    currentIndex++;
                }
            }
        }
        return map;
    }

    public void open() {
        player.openInventory(inventory);
    }

    public ItemStack getButton() {
        ItemStack item = null;
        ConfigurationSection gui = plugin.getGui();
        ConfigurationSection button = gui.getConfigurationSection("button");
        String name = button.getString("name").replace("&", "§");
        String material = button.getString("material");
        List<String> lore = button.getStringList("Lore");
        lore.replaceAll((a) -> a.replace("&", "§"));
        ConfigurationSection status = gui.getConfigurationSection("status");
        if (!isClicked) {
            String start = status.getString("start").replace("&", "§");
            lore.replaceAll((a) -> a.replace("{status}", start));
        } else {
            String ready = status.getString("ready").replace("&", "§");
            lore.replaceAll((a) -> a.replace("{status}", ready));
        }
        if (NumberUtils.isNumber(material)) {
            item = new ItemStack(Material.getMaterial(material));
        } else {
            item = new ItemStack(Material.valueOf(material));
        }
        ItemMeta itemMeta = item.hasItemMeta() ? item.getItemMeta() : Bukkit.getItemFactory().getItemMeta(item.getType());
        itemMeta.setDisplayName(name);
        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);

        return item;
    }

    public Inventory getInventory(){return inventory;}


}

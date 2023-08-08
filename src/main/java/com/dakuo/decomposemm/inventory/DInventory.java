package com.dakuo.decomposemm.inventory;

import com.dakuo.decomposemm.DecomposeMM;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class DInventory implements Listener {
    private final Inventory inventory;
    private final DecomposeMM plugin;
    public Player player;

    public boolean isClicked = false;

    public final int buttonSlot;

    public DInventory(Player player) {
        plugin = DecomposeMM.getPlugin(DecomposeMM.class);
        ConfigurationSection gui = plugin.getGui();
        String title = gui.getString("title").replace("&", "§");
        int size = gui.getInt("size");
        inventory = Bukkit.createInventory(new DInventoryHolder(this), size, title);
        buttonSlot = gui.getInt("button.slot");
        inventory.setItem(buttonSlot, getButton());
        this.player = player;
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


}

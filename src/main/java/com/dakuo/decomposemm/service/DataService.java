package com.dakuo.decomposemm.service;


import com.dakuo.decomposemm.DecomposeMM;
import io.lumine.mythic.api.adapters.AbstractItemStack;
import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.bukkit.adapters.BukkitItemStack;
import io.lumine.mythic.core.items.MythicItem;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.*;

public class DataService {
    private static DataService instance = new DataService();
    
    private List<DataBean> dataBeanList = new ArrayList<>();
    
    private DataService(){
        
    }
    
    public void loadData(File file){
        dataBeanList.clear();
        YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(file);
        Set<String> keys = yamlConfiguration.getKeys(false);
        for (String key : keys) {
            ConfigurationSection key1 = yamlConfiguration.getConfigurationSection(key);
            String name = key1.getString("name",null);
            String material = key1.getString("material",null);
            String lore = key1.getString("lore",null);
            List<String> result = key1.getStringList("result");

            DataBean dataBean = new DataBean(key, name,material , lore, result);
            dataBeanList.add(dataBean);
        }
        Bukkit.getConsoleSender().sendMessage("§f[§a分解§f] §a分解配置加载成功,共加载 §f"+dataBeanList.size()+" §a条配置.");
    }

    public static DataService getInstance() {
        return instance;
    }

    public List<ItemStack> toItems(ItemStack itemStack,Player player){
        for (DataBean dataBean : dataBeanList) {
            String material = dataBean.getMaterial();
            if (material != null) {
                boolean number = NumberUtils.isNumber(material);
                if (number) {
                    Material type = itemStack.getType();
                    if (type.getId() != Integer.parseInt(material)) {
                        continue;
                    }
                }
            }
            if (dataBean.getName() != null) {
                String name = dataBean.getName().replace("&", "§");
                String displayName = itemStack.getItemMeta().getDisplayName();
                String s = ChatColor.stripColor(name);
                String s1 = ChatColor.stripColor(displayName);
                if (!s1.equalsIgnoreCase(s)) {
                    continue;
                }
            }
            if (dataBean.getLore() != null) {
                String lore = dataBean.getLore();
                boolean b = itemStack.hasItemMeta();
                if (!b) {
                    continue;
                }
                boolean b1 = itemStack.getItemMeta().hasLore();
                if (!b1) {
                    continue;
                }
                List<String> lore1 = itemStack.getItemMeta().getLore();
                lore1.replaceAll(ChatColor::stripColor);
                boolean b2 = lore1.stream().anyMatch(a -> (a.equalsIgnoreCase(lore) || a.contains(lore)));
                if (!b2) {
                    continue;
                }
            }
            List<String> items = dataBean.getResult();
            int amount = itemStack.getAmount();
            List<ItemStack> itemsByConfig = new ArrayList<>();
            for (int i = 0; i < amount; i++) {
                itemsByConfig.addAll(getItemsByConfig(items,player));
            }
            return itemsByConfig;
        }
        return null;
    }

    private List<ItemStack> getItemsByConfig(List<String> items,Player player){
        List<ItemStack> itemStacks = new ArrayList<>();
        for (int i = 0; i < items.size(); i++) {
            String s = items.get(i);
            String[] s1 = s.split(" ");
            if (s1[0].contains("cmd")){
                String substring = s.substring(s.indexOf("{")+1, s.lastIndexOf("}"));
                String replace = substring.replace("%player%", player.getName());
                String chance = s.substring(s.lastIndexOf(".") - 1, s.length());
                boolean b = randomChance(Double.parseDouble(chance));
                if (b) {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), replace);
                }
                continue;
            }
            if (s1[0].contains("money")){
                int amount;
                if (s1[1].contains("-")){
                    String[] amountOption = s1[1].split("-");
                    int start = Integer.parseInt(amountOption[0]);
                    int end = Integer.parseInt(amountOption[1]);
                    amount = randomInt(end, start);
                }else{
                    amount = Integer.parseInt(s1[1]);
                }
                boolean b = randomChance(Double.parseDouble(s1[2]));
                if (b) {
                    DecomposeMM.economy.depositPlayer(player,amount);
                    DecomposeMM plugin = DecomposeMM.getPlugin(DecomposeMM.class);
                    boolean use = plugin.getConfig().getBoolean("message.money.use");
                    if (use) {
                        player.sendMessage(plugin.getConfig().getString("message.money.msg").replace("{1}", String.valueOf(amount).replace("&", "§")));
                    }
                }
                continue;
            }
            if (s1[0].contains("msg")){
                String msg_ = s.replace("msg ", "").replace("&","§");
                player.sendMessage(msg_);
                continue;
            }
            boolean b = randomChance(Double.parseDouble(s1[2]));
            if (!b){
                continue;
            }
            int amount;
            if (s1[1].contains("-")){
                String[] amountOption = s1[1].split("-");
                int start = Integer.parseInt(amountOption[0]);
                int end = Integer.parseInt(amountOption[1]);
                amount = randomInt(end, start);
            }else{
                amount = Integer.parseInt(s1[1]);
            }
            Optional<MythicItem> item = MythicBukkit.inst().getItemManager().getItem(s1[0]);
            boolean present = item.isPresent();
            if (present) {
                MythicItem mythicItem = item.get();
                AbstractItemStack abstractItemStack = mythicItem.generateItemStack(amount);
                BukkitItemStack bukkitItemStack = (BukkitItemStack) abstractItemStack;
                ItemStack build = bukkitItemStack.build();
                itemStacks.add(build);
            }

        }
        return  itemStacks;
    }

    private static boolean randomChance(double chance){
        return new Random().nextDouble() < chance;
    }

    private static int randomInt(int end,int start){
        return new Random().nextInt(end - start + 1) + start;
    }

    public List<DataBean> getDataBeanList() {
        return dataBeanList;
    }
}

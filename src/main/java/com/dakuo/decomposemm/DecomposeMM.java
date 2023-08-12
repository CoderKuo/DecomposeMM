package com.dakuo.decomposemm;

import com.dakuo.decomposemm.inventory.DInventory;
import com.dakuo.decomposemm.inventory.DInventoryMonitor;
import com.dakuo.decomposemm.service.DataService;
import com.dakuo.decomposemm.service.decompose.parser.ParserLoader;
import com.dakuo.decomposemm.service.mythic.IMythicMobs;
import com.dakuo.decomposemm.service.mythic.MythicMobs4;
import com.dakuo.decomposemm.service.mythic.MythicMobs5;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class DecomposeMM extends JavaPlugin {
    private ConfigurationSection gui;
    private boolean autoDecompose = false;
    public static Economy economy = null;

    public IMythicMobs iMythicMobs = null;

    @Override
    public void onEnable() {
        long startTime = System.currentTimeMillis();

        Metrics metrics = new Metrics(this);

        loadConfig();
        getServer().getPluginManager().registerEvents(new DInventoryMonitor(),this);
        getCommand("fj").setExecutor(new Command());

        RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
        if (economyProvider != null) {
            economy = economyProvider.getProvider();
        } else {
            Bukkit.getConsoleSender().sendMessage("§f[§e分解§f] §c§l初始化Vault失败,请检查是否缺少经济插件.");
        }
        boolean b = loadMythicMobs();
        if (!b){
            Bukkit.getConsoleSender().sendMessage("§f[§e分解§f] §c§l初始化MythicMobs失败,如果需要使用mm请检查是否正确安装.");
        }
        ParserLoader.load();

        long endTime = System.currentTimeMillis();
        Bukkit.getConsoleSender().sendMessage("§f[§e分解§f] §a插件已启动,共耗时 §f"+(endTime-startTime)+" §ams.");
    }

    @Override
    public void onDisable() {
    }

    public void loadConfig(){
        saveDefaultConfig();
        File file = new File(getDataFolder() + "/data.yml");
        if (!file.exists()){
            saveResource("data.yml",false);
        }
        DataService.getInstance().loadData(file);
        reloadConfig();
        FileConfiguration config = getConfig();
        gui = config.getConfigurationSection("gui");
        autoDecompose = config.getBoolean("autoDecompose");
    }

    public ConfigurationSection getGui(){
        return gui;
    }

    public boolean getAutoDecompose(){
        return autoDecompose;
    }

    public boolean loadMythicMobs(){
        try{
            Class.forName("io.lumine.xikage.mythicmobs.MythicMobs");
            iMythicMobs = new MythicMobs4();
            Bukkit.getConsoleSender().sendMessage("§f[§e分解§f] §aMythicMobs4 加载成功！");
            return true;
        }catch (Exception e){
        }

        try{
            Class.forName("io.lumine.mythic.bukkit.MythicBukkit");
            iMythicMobs = new MythicMobs5();
            Bukkit.getConsoleSender().sendMessage("§f[§e分解§f] §aMythicMobs5 加载成功！");
            return true;
        }catch (Exception e){
        }
        return false;
    }
}

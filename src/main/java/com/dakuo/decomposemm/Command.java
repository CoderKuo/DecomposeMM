package com.dakuo.decomposemm;

import com.dakuo.decomposemm.inventory.DInventory;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Command implements TabExecutor{


    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        if (!label.equalsIgnoreCase("fj")){
            return true;
        }

        if (args.length == 1){
            switch (args[0]){
                case "open":
                    if (!(sender instanceof Player)){
                        sender.sendMessage("请输入/fj open 玩家名 使用此命令");
                        return true;
                    }
                    new DInventory((Player) sender).open();
                    break;
                case "reload":
                    if (sender.isOp()) {
                        DecomposeMM.getPlugin(DecomposeMM.class).loadConfig();
                        sender.sendMessage("重载成功");
                    }else{
                        sender.sendMessage("权限不足");
                    }
                    return true;
            }
        }else if(args.length == 2 && args[0].equalsIgnoreCase("open")){
            Player player = Bukkit.getPlayer(args[1]);
            if (!player.isOnline()){
                sender.sendMessage("玩家未在线");
                return true;
            }
            new DInventory(player).open();
            return true;
        }

        if (sender.isOp()) {
            sender.sendMessage("§a§l=========分解=========");
            sender.sendMessage("§a§l/fj open 打开分解菜单");
            sender.sendMessage("§a§l/fj reload 重载配置文件");
            sender.sendMessage("§a§l======================");
        }


        return true;
    }


    @Override
    public List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command command, String alias, String[] args) {
        List<String> strs = new ArrayList<>();
        if (args.length < 1){
            strs.add("open");
            if (sender.isOp()) {
                strs.add("reload");
            }
        }
        return strs;
    }
}

package com.dakuo.decomposemm.service.decompose.parser;

import com.dakuo.decomposemm.service.decompose.DecomposeAnalyze;
import org.bukkit.OfflinePlayer;

import java.util.ArrayList;
import java.util.List;

public class CommandParser extends IParser{

    private static CommandParser instance;
    private CommandParser(){

    }

    public static CommandParser getInstance() {
        if (instance == null){
            instance = new CommandParser();
        }
        return instance;
    }

    @Override
    public DecomposeAnalyze parse(OfflinePlayer player,String param) {
        return null;
    }

    @Override
    public List<String> getName() {
        return new ArrayList<String>(){{add("cmd");add("command");}};
    }
}

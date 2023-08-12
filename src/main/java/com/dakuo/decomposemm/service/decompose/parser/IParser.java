package com.dakuo.decomposemm.service.decompose.parser;

import com.dakuo.decomposemm.service.decompose.DecomposeAnalyze;
import org.bukkit.OfflinePlayer;

import java.util.List;

public abstract class IParser {


    public abstract DecomposeAnalyze parse(OfflinePlayer player,String param);


    public abstract List<String> getName();





}

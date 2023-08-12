package com.dakuo.decomposemm.service.decompose;

import com.dakuo.decomposemm.service.decompose.parser.IParser;
import com.dakuo.decomposemm.service.decompose.parser.ParserLoader;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.List;


/**
 * 分解解析器
 * 负责一个配方整体的解析
 * @author dakuo
 */
public class DecomposeAnalyze {

    private OfflinePlayer player;
    private List<Param> param;

    private IParser parser;

    public DecomposeAnalyze(OfflinePlayer player,ConfigurationSection section){
        this.player = player;


    }

    public DecomposeAnalyze(OfflinePlayer player, List<Param> param) {
        this.player = player;
        this.param = param;
    }

    public IParser getParser() {
        ParserLoader.parsers.stream().filter(aClass -> {
            try {
                Method getInstance = aClass.getDeclaredMethod("getInstance");
                IParser invoke = (IParser) getInstance.invoke(null);
                invoke.getName().contains()




            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        });

    }


    public static class Param{
        private String parser;
        private String chance;
        private LinkedHashMap<String,String> params;

        public Param(String parser, String chance, LinkedHashMap<String,String> params) {
            this.parser = parser;
            this.chance = chance;
            this.params = params;
        }

//        public static Param matchParam(ConfigurationSection section){
//            section.getList()
//        }

        public String getParser() {
            return parser;
        }

        public void setParser(String parser) {
            this.parser = parser;
        }

        public String getChance() {
            return chance;
        }

        public void setChance(String chance) {
            this.chance = chance;
        }

        public LinkedHashMap<String,String> getParams() {
            return params;
        }

        public void setParams(LinkedHashMap<String,String> params) {
            this.params = params;
        }

        @Override
        public String toString() {
            return "Param{" +
                    "parser='" + parser + '\'' +
                    ", chance='" + chance + '\'' +
                    ", params=" + params +
                    '}';
        }
    }



}



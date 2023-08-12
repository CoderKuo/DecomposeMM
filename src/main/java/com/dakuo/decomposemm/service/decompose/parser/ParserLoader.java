package com.dakuo.decomposemm.service.decompose.parser;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ParserLoader {

    public static List<Class<? extends IParser>> parsers = new ArrayList<>();

    public static void load(){
        parsers = (List<Class<? extends IParser>>) getSubclassesOfAbstractClass("com.dakuo.decomposemm.service.decompose.parser", IParser.class);

    }


    private static List<Class<? extends IParser>> getSubclassesOfAbstractClass(String packageName, Class<? extends IParser> abstractClass) {
        List<Class<? extends IParser>> subclassList = new ArrayList<>();
        try {
            String path = packageName.replace('.', '/');
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            URL url = classLoader.getResource(path);

            if (url != null) {
                File packageDir = new File(url.toURI());
                if (packageDir.exists() && packageDir.isDirectory()) {
                    File[] files = packageDir.listFiles();
                    if (files != null) {
                        for (File file : files) {
                            String fileName = file.getName();
                            if (fileName.endsWith(".class")) {
                                String className = packageName + '.' + fileName.substring(0, fileName.length() - 6);
                                Class<? extends IParser> clazz = (Class<? extends IParser>) Class.forName(className);
                                if (abstractClass.isAssignableFrom(clazz) && !abstractClass.equals(clazz)) {
                                    subclassList.add(clazz);
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return subclassList;
    }

}

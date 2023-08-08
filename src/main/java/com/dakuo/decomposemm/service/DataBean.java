package com.dakuo.decomposemm.service;

import java.util.List;
import java.util.Map;

public class DataBean {
    private String id;
    private String name;
    private String material;
    private String lore;

    private List<String> result;

    public DataBean() {
    }

    public DataBean(String id, String name, String material, String lore, List<String> result) {
        this.id = id;
        this.name = name;
        this.material = material;
        this.lore = lore;
        this.result = result;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getLore() {
        return lore;
    }

    public void setLore(String lore) {
        this.lore = lore;
    }

    public List<String> getResult() {
        return result;
    }

    public void setResult(List<String> result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "DataBean{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", material='" + material + '\'' +
                ", lore='" + lore + '\'' +
                ", result=" + result +
                '}';
    }

}

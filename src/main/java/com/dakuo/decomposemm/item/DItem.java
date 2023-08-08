package com.dakuo.decomposemm.item;

import java.util.List;

public class DItem {
    private String id;
    private String name;
    private String material;
    private String lore;


    public DItem(String id, String name, String material, String lore) {
        this.id = id;
        this.name = name;
        this.material = material;
        this.lore = lore;
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

    @Override
    public String toString() {
        return "DItem{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", material='" + material + '\'' +
                ", lore='" + lore + '\'' +
                '}';
    }
}



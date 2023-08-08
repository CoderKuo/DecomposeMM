package com.dakuo.decomposemm.inventory;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class DInventoryHolder implements InventoryHolder {
    public DInventory inventoryInstance;

    public DInventoryHolder(DInventory inventoryInstance) {
        this.inventoryInstance = inventoryInstance;
    }

    @Override
    public Inventory getInventory() {
        return null;
    }
}

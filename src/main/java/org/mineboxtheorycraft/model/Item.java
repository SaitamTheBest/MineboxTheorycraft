package org.mineboxtheorycraft.model;


import java.io.Serializable;
import java.util.ArrayList;

public class Item implements Serializable {
    private String name;
    private Long price;
    private ArrayList<Item> craftItem;

    public Item(String name, Long price) {
        this.name = name;
        this.price = price;
        craftItem = new ArrayList<Item>();
    }

    public void addItemForCraft(Item item) {
        craftItem.add(item);
    }

    public void removeItemForCraft(Item itemToRemove) {
        for(Item item : craftItem) {
            if (item.equals(itemToRemove)){
                craftItem.remove(item);
                return;
            }
        }
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

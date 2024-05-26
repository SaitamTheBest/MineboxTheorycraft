package org.mineboxtheorycraft.model;


import jdk.jfr.Percentage;

import java.io.Serializable;
import java.net.URL;
import java.util.HashMap;

public class Item implements Serializable {
    private String name;
    private Long price;
    private URL urlImage;

    private HashMap<Item,Long> craftItem;

    public Item(String name, Long price, URL urlImage) {
        this.name = name;
        this.price = price;
        this.urlImage = urlImage;
        craftItem = new HashMap<Item,Long>();
    }

    public Item(String name, Long price) {
        this.name = name;
        this.price = price;
        this.urlImage = null;
        craftItem = new HashMap<Item,Long>();
    }


    public URL getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(URL urlImage) {
        this.urlImage = urlImage;
    }


    public void addItemForCraft(Item item, Long quantity) {
        craftItem.put(item, quantity);
    }

    public void removeItemForCraft(Item itemToRemove) {
        craftItem.remove(itemToRemove);
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

    public Long itemPriceCraft() {
        long itemPriceCraft = 0L;
        if (craftItem.isEmpty()) {
            return itemPriceCraft;
        }
        else {
            for(HashMap.Entry<Item,Long> entry : craftItem.entrySet()) {
                itemPriceCraft += entry.getKey().getPrice() * entry.getValue();
            }
        }
        return itemPriceCraft;
    }

    public double rentabilyCraftPercentage() {
        double percentage = 0;
        if (craftItem.isEmpty()) {
            return percentage;
        } else {
            long itemPriceCraft = itemPriceCraft();
            long difference = price - itemPriceCraft;
            percentage = ((double) difference / price) * 100;
        }
        return percentage;
    }


    public HashMap<Item, Long> getCraftItem() {
        return craftItem;
    }
}

package org.mineboxtheorycraft.model;

import org.javacord.api.interaction.SelectMenuInteraction;
import org.mineboxtheorycraft.message.ListItemsMessage;

import java.util.ArrayList;
import java.util.Comparator;

import static org.mineboxtheorycraft.filedata.FileIOItemData.itemArrayList;

public class SortMethodItemsList {
    public static ArrayList<Item> alphabeticMethod() {
        ArrayList<Item> alphabeticList = itemArrayList;
        alphabeticList.sort(Comparator.comparing(Item::getName));

        return alphabeticList;
    }

    public static ArrayList<Item> priceMethod() {
        ArrayList<Item> priceList = itemArrayList;
        priceList.sort(Comparator.comparing(Item::getPrice).reversed());

        return priceList;
    }

    public static ArrayList<Item> rentabilityMethod() {
        ArrayList<Item> rentabilityList = itemArrayList;
        rentabilityList.sort(Comparator.comparing(Item::rentabilyCraftPercentage));

        return rentabilityList;
    }
}

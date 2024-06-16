package org.mineboxtheorycraft.model;

import org.mineboxtheorycraft.filedata.FileIOItemData;

public class RequestAverageRentabiltyItems {
    public static String IsRentable(Item item) {
        String isRentable = "";

        double interval = 0.25;
        double averageCraftRentability = average();

        if (item.rentabilyCraftPercentage() > averageCraftRentability * (1 + interval)) {
            isRentable = "Au dessus de la moyenne";
        } else if (item.rentabilyCraftPercentage() < averageCraftRentability * (1 - interval)) {
            isRentable = "En dessous de la moyenne";
        } else {
            isRentable = "Dans la moyenne";
        }

        return isRentable;
    }

    public static double average() {
        double sum = 0;

        for (Item item : FileIOItemData.itemArrayList) {
            sum += item.rentabilyCraftPercentage();
        }

        double average = sum / FileIOItemData.itemArrayList.size();

        return average;
    }
}

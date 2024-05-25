package org.mineboxtheorycraft.filedata;

import org.mineboxtheorycraft.model.Item;

import java.io.*;
import java.util.ArrayList;

public class FileIO {
    public static ArrayList<Item> itemArrayList  = new ArrayList<Item>();

    public static void writeObject(Item item) throws IOException {
        itemArrayList.add(item);
        ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream("data_items.txt"));
        outputStream.writeObject(itemArrayList);
        outputStream.close();
    }

    public static void readObjects() throws IOException, ClassNotFoundException {
        File file = new File("data_items.txt");
        if (!file.exists() || file.length() == 0) {
            itemArrayList = new ArrayList<Item>();
            return;
        }
        ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(file));
        ArrayList<Item> items = (ArrayList<Item>) inputStream.readObject();
        itemArrayList.addAll(items);
        inputStream.close();
    }

    public static int searchItem(String name){
        if (!itemArrayList.isEmpty()) {
            for (int i = 0; i < itemArrayList.toArray().length; i++) {
                if (itemArrayList.get(i).getName().equals(name)) {
                    return i;
                }
            }
        }
        return -1;
    }
}

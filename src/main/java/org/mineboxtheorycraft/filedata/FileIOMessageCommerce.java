package org.mineboxtheorycraft.filedata;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileIOMessageCommerce {
    public static String messageCommerce;

    public static void writeMessageInFile(String message) throws IOException {
        Path file = Paths.get("message_commerce.txt");
        Files.write(file,message.getBytes());
        messageCommerce = message;
    }

    public static void readMessageInFile() throws IOException {
        Path file = Paths.get("message_commerce.txt");
        messageCommerce = Files.readString(file);
    }
}

package org.mineboxtheorycraft;

import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.intent.Intent;
import org.javacord.api.interaction.*;
import org.mineboxtheorycraft.filedata.FileIO;
import org.mineboxtheorycraft.listener.AddItemListener;
import org.mineboxtheorycraft.listener.SearchItemListener;
import org.mineboxtheorycraft.model.Item;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Main {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        DiscordApi api = new DiscordApiBuilder()
                .setToken(args[0])
                .setAllIntents()
                .login().join();

        //Slash Commands
        System.out.println("[Start] Enregistrement des SlashCommands");
        SlashCommand.with("add-item","Ajoute un item",Arrays.asList(
                        SlashCommandOption.create
                                (SlashCommandOptionType.STRING,"name","Nom de l'item",true),
                        SlashCommandOption.create
                                (SlashCommandOptionType.LONG,"price","Prix de l'item",true)))
                .createGlobal(api).join();
        SlashCommand.with("search","Recherche un item",Arrays.asList(
                SlashCommandOption
                        .create(SlashCommandOptionType.STRING,"name","Nom de l'item",true)
                )
        ).createGlobal(api).join();
        System.out.println("[End] Enregistrement des SlashCommands");
        //SlashCommandsListener
        api.addSlashCommandCreateListener(new AddItemListener());
        api.addSlashCommandCreateListener(new SearchItemListener());

        //Load items
        System.out.println("[Start] Lecture des données");
        FileIO.readObjects();
        System.out.println("[End] Lecture des données");
    }
}
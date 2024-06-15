package org.mineboxtheorycraft;

import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.interaction.*;
import org.mineboxtheorycraft.filedata.FileIOItemData;
import org.mineboxtheorycraft.filedata.FileIOMessageCommerce;
import org.mineboxtheorycraft.listener.*;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

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
                    (SlashCommandOptionType.LONG,"price","Prix de l'item",true),
            SlashCommandOption.create
                    (SlashCommandOptionType.ATTACHMENT,"image","Prix de l'item",false))
        ).createGlobal(api).join();
        SlashCommand.with("search","Recherche un item",Arrays.asList(
            SlashCommandOption
                    .create(SlashCommandOptionType.STRING,"name","Nom de l'item",true)
            )
        ).createGlobal(api).join();
        SlashCommand.with("modify","Modifier un item",Arrays.asList(
            SlashCommandOption
                    .create(SlashCommandOptionType.STRING,"name","Nom de l'item",true)
            )
        ).createGlobal(api).join();
        SlashCommand.with("message-commerce","Afficher ou modifier le message commerce").createGlobal(api).join();
        SlashCommand.with("craft-item","Définit les craft d'un item",Arrays.asList(
                        SlashCommandOption
                                .create(SlashCommandOptionType.STRING,"name","Nom de l'item",true)
                )
        ).createGlobal(api).join();
        SlashCommand.with("list-items","Afficher la liste des items suivant un tri").createGlobal(api).join();
        System.out.println("[End] Enregistrement des SlashCommands");

        //SlashCommandsListener
        api.addSlashCommandCreateListener(new AddItemListener());
        api.addSlashCommandCreateListener(new SearchItemListener());

        ModifyItemListener modifyItemListener = new ModifyItemListener();
        api.addSlashCommandCreateListener(modifyItemListener);
        api.addModalSubmitListener(modifyItemListener);
        api.addSlashCommandCreateListener(new MessageCommerceListener());

        ModifyMessageCommerceListener modifyMessageCommerceListener = new ModifyMessageCommerceListener();
        api.addButtonClickListener(modifyMessageCommerceListener);
        api.addModalSubmitListener(modifyMessageCommerceListener);

        CraftItemListener craftItemListener = new CraftItemListener();
        api.addButtonClickListener(craftItemListener);
        api.addSlashCommandCreateListener(craftItemListener);
        api.addSelectMenuChooseListener(craftItemListener);
        api.addModalSubmitListener(craftItemListener);

        ListItemsListener listItemsListener = new ListItemsListener();
        api.addSlashCommandCreateListener(listItemsListener);
        api.addSelectMenuChooseListener(listItemsListener);


        //Load items
        System.out.println("[Start] Lecture des données");
        FileIOItemData.readObjects();
        FileIOMessageCommerce.readMessageInFile();
        System.out.println("[End] Lecture des données");
    }
}
package org.mineboxtheorycraft;

import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.activity.ActivityType;
import org.javacord.api.interaction.*;
import org.mineboxtheorycraft.filedata.FileIOItemData;
import org.mineboxtheorycraft.filedata.FileIOMessageCommerce;
import org.mineboxtheorycraft.listener.*;
import org.mineboxtheorycraft.message.CraftItemMessage;

import java.io.IOException;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        DiscordApi api = new DiscordApiBuilder()
                .setToken(args[0])
                .setAllIntents()
                .login().join();

        /// ***** Slash Commands *****
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
        SlashCommand.with("add-craft-item","Ajoute un craft d'un item",Arrays.asList(
                        SlashCommandOption
                                .create(SlashCommandOptionType.STRING,"name-item-to-modify","Nom de l'item dont vous voulez ajouter un craft",true),
                SlashCommandOption
                        .create(SlashCommandOptionType.STRING,"name-item-add","Nom de l'item",true)
                )
        ).createGlobal(api).join();
        SlashCommand.with("delete-craft-item","Ajoute un craft d'un item",Arrays.asList(
                        SlashCommandOption
                                .create(SlashCommandOptionType.STRING,"name","Nom de l'item dont vous voulez supprimer un craft",true)
                )
        ).createGlobal(api).join();
        SlashCommand.with("list-items","Afficher la liste des items suivant un tri").createGlobal(api).join();
        SlashCommand.with("craft","Calcul le prix du craft et le nombre d'item",Arrays.asList(
                        SlashCommandOption
                                .create(SlashCommandOptionType.STRING,"name","Nom de l'item que vous voulez craft",true),
                        SlashCommandOption
                                .create(SlashCommandOptionType.LONG,"quantity","Quantité à craft",true)
                )
        ).createGlobal(api).join();
        System.out.println("[End] Enregistrement des SlashCommands");

        /// ***** SlashCommandsListener *****

        // Command /add-item
        api.addSlashCommandCreateListener(new AddItemListener());

        // Command /search
        SearchItemListener searchItemListener = new SearchItemListener();
        api.addSlashCommandCreateListener(searchItemListener);

        // Command /modify
        ModifyItemListener modifyItemListener = new ModifyItemListener();
        api.addSlashCommandCreateListener(modifyItemListener);
        api.addModalSubmitListener(modifyItemListener);
        api.addSlashCommandCreateListener(new MessageCommerceListener());

        // Command /message-commerce
        ModifyMessageCommerceListener modifyMessageCommerceListener = new ModifyMessageCommerceListener();
        api.addButtonClickListener(modifyMessageCommerceListener);
        api.addModalSubmitListener(modifyMessageCommerceListener);

        // Command /add-craft-item
        AddCraftItemListener addCraftItemListener = new AddCraftItemListener();
        api.addSlashCommandCreateListener(addCraftItemListener);
        api.addModalSubmitListener(addCraftItemListener);

        // Command /delete-craft-item
        DeleteCraftItemListener deleteCraftItemListener = new DeleteCraftItemListener();
        api.addSlashCommandCreateListener(deleteCraftItemListener);
        api.addSelectMenuChooseListener(deleteCraftItemListener);

        // Command /list-items
        ListItemsListener listItemsListener = new ListItemsListener();
        api.addSlashCommandCreateListener(listItemsListener);
        api.addSelectMenuChooseListener(listItemsListener);

        // Command /craft
        CraftItemListener craftItemListener = new CraftItemListener();
        api.addSlashCommandCreateListener(craftItemListener);

        /// ***** Load items *****
        System.out.println("[Start] Lecture des données");
        FileIOItemData.readObjects();
        FileIOMessageCommerce.readMessageInFile();
        System.out.println("[End] Lecture des données");

        /// ***** Activity *****
        Random rand = new Random();
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(() -> {
            String nameRandomItem = FileIOItemData.itemArrayList.get(rand.nextInt(FileIOItemData.itemArrayList.size())).getName();
            api.updateActivity(ActivityType.WATCHING, nameRandomItem);
            }, 0, 5, TimeUnit.MINUTES);
    }
}
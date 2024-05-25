package org.mineboxtheorycraft;

import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.intent.Intent;
import org.javacord.api.interaction.*;
import org.mineboxtheorycraft.listener.AddItemListener;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        DiscordApi api = new DiscordApiBuilder()
                .setToken(args[0])
                .addIntents(Intent.MESSAGE_CONTENT)
                .login().join();

        //Slash Commands
        System.out.println("[Start] Enregistrement des SlashCommands");
        SlashCommand.with("add-item","Ajoute un item",Arrays.asList(
                        SlashCommandOption.create
                                (SlashCommandOptionType.STRING,"name","Nom de l'item",true),
                        SlashCommandOption.create
                                (SlashCommandOptionType.LONG,"price","Prix de l'item",true)))
                .createGlobal(api).join();
        System.out.println("[End] Enregistrement des SlashCommands");
        //SlashCommandsListener
        api.addSlashCommandCreateListener(new AddItemListener());
    }
}
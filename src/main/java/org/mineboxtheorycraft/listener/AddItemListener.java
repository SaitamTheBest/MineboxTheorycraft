package org.mineboxtheorycraft.listener;

import org.javacord.api.event.interaction.SlashCommandCreateEvent;
import org.javacord.api.interaction.SlashCommandInteraction;
import org.javacord.api.listener.interaction.SlashCommandCreateListener;

public class AddItemListener implements SlashCommandCreateListener {
    @Override
    public void onSlashCommandCreate(SlashCommandCreateEvent slashCommandCreateEvent) {
        SlashCommandInteraction interaction = slashCommandCreateEvent.getSlashCommandInteraction();
        String command = interaction.getCommandName();
        if (command.equals("add-item"))
        {
            String name = interaction.getArgumentStringValueByName("name").get();
            interaction.createImmediateResponder()
                    .setContent("Objet : " + name)
                    .respond();
        }
    }
}

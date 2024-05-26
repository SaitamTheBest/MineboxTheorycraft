package org.mineboxtheorycraft.listener;

import org.javacord.api.event.interaction.SlashCommandCreateEvent;
import org.javacord.api.interaction.SlashCommandInteraction;
import org.javacord.api.listener.interaction.SlashCommandCreateListener;
import org.mineboxtheorycraft.filedata.FileIOItemData;
import org.mineboxtheorycraft.message.AddItemMessage;
import org.mineboxtheorycraft.model.Item;

import java.io.IOException;

public class AddItemListener implements SlashCommandCreateListener {
    @Override
    public void onSlashCommandCreate(SlashCommandCreateEvent slashCommandCreateEvent) {
        SlashCommandInteraction interaction = slashCommandCreateEvent.getSlashCommandInteraction();
        String command = interaction.getCommandName();
        if (command.equals("add-item"))
        {
            String name = interaction.getArgumentStringValueByName("name").get();
            Long price = interaction.getArgumentLongValueByName("price").get();

            Item item = new Item(name,price);
            try {
                FileIOItemData.writeObject(item);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            interaction.createImmediateResponder()
                    .setContent("")
                    .addEmbed(AddItemMessage.AddItemEmbed(name,price))
                    .respond();
        }
    }
}

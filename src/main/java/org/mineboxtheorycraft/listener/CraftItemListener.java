package org.mineboxtheorycraft.listener;

import org.javacord.api.event.interaction.SlashCommandCreateEvent;
import org.javacord.api.interaction.SlashCommandInteraction;
import org.javacord.api.listener.interaction.SlashCommandCreateListener;
import org.mineboxtheorycraft.filedata.FileIOItemData;
import org.mineboxtheorycraft.message.CraftItemMessage;
import org.mineboxtheorycraft.model.Item;

public class CraftItemListener implements SlashCommandCreateListener {
    @Override
    public void onSlashCommandCreate(SlashCommandCreateEvent slashCommandCreateEvent) {
        SlashCommandInteraction interaction = slashCommandCreateEvent.getSlashCommandInteraction();
        String command = interaction.getCommandName();
        if (command.equals("craft")){
            String name = interaction.getArgumentStringValueByName("name").get();
            Long quantity = interaction.getArgumentLongValueByName("quantity").get();

            int index = FileIOItemData.searchItem(name);
            if (index == -1) {
                interaction.createImmediateResponder().setContent("Introuvable").respond();
            }
            else {
                Item item = FileIOItemData.itemArrayList.get(index);
                interaction.createImmediateResponder()
                        .addEmbed(CraftItemMessage.craftItemPresentation(item,quantity))
                        .respond();
            }
        }
    }
}

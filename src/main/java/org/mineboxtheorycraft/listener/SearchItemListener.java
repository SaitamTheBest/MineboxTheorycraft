package org.mineboxtheorycraft.listener;

import org.javacord.api.entity.message.MessageFlag;
import org.javacord.api.entity.message.component.ActionRow;
import org.javacord.api.entity.message.component.SelectMenu;
import org.javacord.api.entity.message.component.SelectMenuOption;
import org.javacord.api.event.interaction.SelectMenuChooseEvent;
import org.javacord.api.event.interaction.SlashCommandCreateEvent;
import org.javacord.api.interaction.SelectMenuInteraction;
import org.javacord.api.interaction.SlashCommandInteraction;
import org.javacord.api.listener.interaction.SelectMenuChooseListener;
import org.javacord.api.listener.interaction.SlashCommandCreateListener;
import org.mineboxtheorycraft.filedata.FileIOItemData;
import org.mineboxtheorycraft.message.PresentationItemMessage;
import org.mineboxtheorycraft.model.Item;
import org.mineboxtheorycraft.model.SortMethodItemsList;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SearchItemListener implements SlashCommandCreateListener {
    @Override
    public void onSlashCommandCreate(SlashCommandCreateEvent slashCommandCreateEvent) {
        SlashCommandInteraction interaction = slashCommandCreateEvent.getSlashCommandInteraction();
        String command = interaction.getCommandName();
        if (command.equals("search")){
            String name = interaction.getArgumentStringValueByName("name").orElse("");
            int index = FileIOItemData.searchItem(name);
            if (index == -1) {
                interaction.createImmediateResponder().setContent("Introuvable").respond();
            } else {
                Item item = FileIOItemData.itemArrayList.get(index);
                interaction.createImmediateResponder()
                        .addEmbed(PresentationItemMessage.presentationItem(item))
                        .respond();
            }
        }
    }
}

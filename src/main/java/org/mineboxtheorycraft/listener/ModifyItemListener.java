package org.mineboxtheorycraft.listener;

import org.javacord.api.entity.message.component.ActionRow;
import org.javacord.api.entity.message.component.TextInput;
import org.javacord.api.entity.message.component.TextInputStyle;
import org.javacord.api.event.interaction.SlashCommandCreateEvent;
import org.javacord.api.interaction.SlashCommandInteraction;
import org.javacord.api.listener.interaction.SlashCommandCreateListener;
import org.mineboxtheorycraft.filedata.FileIO;
import org.mineboxtheorycraft.model.Item;

public class ModifyItemListener implements SlashCommandCreateListener {
    @Override
    public void onSlashCommandCreate(SlashCommandCreateEvent slashCommandCreateEvent) {
        SlashCommandInteraction interaction = slashCommandCreateEvent.getSlashCommandInteraction();
        String command = interaction.getCommandName();
        if (command.equals("modify")) {
            String name = interaction.getArgumentStringValueByName("name").get();

            int index = FileIO.searchItem(name);
            if (index == -1) {
                interaction.createImmediateResponder().setContent("Introuvable").respond();
            } else {
                Item item = FileIO.itemArrayList.get(index);

                interaction.respondWithModal("modifyModal","Modifier l'item "+ item.getName()
                        , ActionRow.of(TextInput.create(
                                TextInputStyle.SHORT,
                                        "modifyName",
                                        "Nom de l'item",
                                        "Actuel = "+item.getName(),
                                        ""),
                                TextInput.create(
                                        TextInputStyle.SHORT,
                                        "modifyPrice",
                                        "Prix de l'item",
                                        "Actuel = "+item.getName(),
                                        "")
                        )
                );
            }
        }
    }
}

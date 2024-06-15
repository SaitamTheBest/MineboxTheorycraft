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

public class SearchItemListener implements SlashCommandCreateListener, SelectMenuChooseListener {
    @Override
    public void onSlashCommandCreate(SlashCommandCreateEvent slashCommandCreateEvent) {
        SlashCommandInteraction interaction = slashCommandCreateEvent.getSlashCommandInteraction();
        String command = interaction.getCommandName();
        if (command.equals("search")){
            String name = interaction.getArgumentStringValueByName("name").get();

            if (name.isEmpty()){
                List<SelectMenuOption> options = new ArrayList<>();
                for (Item item : SortMethodItemsList.alphabeticMethod()) {
                        options.add(SelectMenuOption.create(item.getName(), item.getName()));
                }
                interaction.createImmediateResponder()
                        .setContent("Choisissez un item")
                        .addComponents(
                                ActionRow.of(
                                        SelectMenu.create(
                                                "menuListItems",
                                                "Choisissez une méthode de tri",
                                                options
                                        )
                                )
                        )
                        .setFlags(MessageFlag.EPHEMERAL)
                        .respond();
            }
            else {
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

    @Override
    public void onSelectMenuChoose(SelectMenuChooseEvent selectMenuChooseEvent) {
        SelectMenuInteraction selectMenuInteraction = selectMenuChooseEvent.getSelectMenuInteraction();
        String customId = selectMenuInteraction.getCustomId();
        if (customId.equals("menuListItems")) {
            List<String> selectedValues = selectMenuInteraction.getChosenOptions().stream()
                    .map(SelectMenuOption::getValue)
                    .toList();
            Item item = FileIOItemData.itemArrayList.get(FileIOItemData.searchItem(selectedValues.get(0)));

            selectMenuInteraction.createOriginalMessageUpdater()
                    .addEmbed(PresentationItemMessage.presentationItem(item))
                    .update();
        }
    }
}

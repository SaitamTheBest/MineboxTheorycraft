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
import org.mineboxtheorycraft.model.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DeleteCraftItemListener implements SlashCommandCreateListener, SelectMenuChooseListener {
    private Item itemToModify;
    @Override
    public void onSlashCommandCreate(SlashCommandCreateEvent slashCommandCreateEvent) {
        SlashCommandInteraction interaction = slashCommandCreateEvent.getSlashCommandInteraction();
        String command = interaction.getCommandName();
        if (command.equals("delete-craft-item")) {
            String nameItemToMofify = interaction.getArgumentStringValueByName("name").orElse(null);
            if (nameItemToMofify != null) {
                int indexItemToMofify = FileIOItemData.searchItem(nameItemToMofify);
                if (indexItemToMofify == -1) {
                    interaction.createImmediateResponder()
                            .setContent("Paramètre 1 : Introuvable")
                            .setFlags(MessageFlag.EPHEMERAL)
                            .respond();
                    return;
                }
                else {
                    itemToModify = FileIOItemData.itemArrayList.get(indexItemToMofify);
                    if (!itemToModify.getCraftItem().isEmpty()) {
                        List<SelectMenuOption> optionsCraftItems = new ArrayList<>();
                        for (HashMap.Entry<Item, Long> entry : itemToModify.getCraftItem().entrySet()) {
                            optionsCraftItems.add(
                                    SelectMenuOption.create(entry.getKey().getName(), entry.getKey().getName())
                            );
                        }

                        interaction.createImmediateResponder()
                                .setContent("Choisissez un item à supprimer du craft")
                                .addComponents(
                                        ActionRow.of(
                                                SelectMenu.create("menuItemsToRemove", "Choisissez un item", optionsCraftItems)
                                        )
                                )
                                .setFlags(MessageFlag.EPHEMERAL)
                                .respond();
                    } else {
                        interaction.createImmediateResponder()
                                .setContent("Aucun item à supprimer du craft")
                                .setFlags(MessageFlag.EPHEMERAL)
                                .respond();
                    }
                }
            }
            else{
                interaction.createImmediateResponder()
                        .setContent("Nom d'item manquant")
                        .setFlags(MessageFlag.EPHEMERAL)
                        .respond();
            }
        }
    }

    @Override
    public void onSelectMenuChoose(SelectMenuChooseEvent selectMenuChooseEvent) {
        SelectMenuInteraction selectMenuInteraction = selectMenuChooseEvent.getSelectMenuInteraction();
        String customId = selectMenuInteraction.getCustomId();
        List<String> selectedValues = selectMenuInteraction.getChosenOptions().stream()
                .map(SelectMenuOption::getValue)
                .toList();

        if (customId.equals("menuItemsToRemove")) {
            handleSelectMenuToRemove(selectMenuInteraction, selectedValues);
        }
    }

    private void handleSelectMenuToRemove(SelectMenuInteraction selectMenuInteraction, List<String> selectedValues) {
        if (!selectedValues.isEmpty()) {
            String selectedItemName = selectedValues.get(0);
            int index = FileIOItemData.searchItem(selectedItemName);
            Item selectedItem = FileIOItemData.itemArrayList.get(index);
            if (selectedItem != null && itemToModify.getCraftItem().containsKey(selectedItem)) {
                itemToModify.getCraftItem().remove(selectedItem);
                selectMenuInteraction.createOriginalMessageUpdater()
                        .setContent("Item " + selectedItemName + " supprimé du craft de " + itemToModify.getName())
                        .setFlags(MessageFlag.EPHEMERAL)
                        .update();
            }
        }
    }
}

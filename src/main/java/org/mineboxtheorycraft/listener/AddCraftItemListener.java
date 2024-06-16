package org.mineboxtheorycraft.listener;

import org.javacord.api.entity.message.MessageFlag;
import org.javacord.api.entity.message.component.*;
import org.javacord.api.event.interaction.ButtonClickEvent;
import org.javacord.api.event.interaction.ModalSubmitEvent;
import org.javacord.api.event.interaction.SelectMenuChooseEvent;
import org.javacord.api.event.interaction.SlashCommandCreateEvent;
import org.javacord.api.interaction.ButtonInteraction;
import org.javacord.api.interaction.ModalInteraction;
import org.javacord.api.interaction.SelectMenuInteraction;
import org.javacord.api.interaction.SlashCommandInteraction;
import org.javacord.api.listener.interaction.ButtonClickListener;
import org.javacord.api.listener.interaction.ModalSubmitListener;
import org.javacord.api.listener.interaction.SelectMenuChooseListener;
import org.javacord.api.listener.interaction.SlashCommandCreateListener;
import org.mineboxtheorycraft.filedata.FileIOItemData;
import org.mineboxtheorycraft.model.Item;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class AddCraftItemListener implements SlashCommandCreateListener, ModalSubmitListener {
    private Item itemToModify;
    private Item itemSelected;

    @Override
    public void onSlashCommandCreate(SlashCommandCreateEvent slashCommandCreateEvent) {
        SlashCommandInteraction interaction = slashCommandCreateEvent.getSlashCommandInteraction();
        String command = interaction.getCommandName();
        if (command.equals("add-craft-item")) {
            String nameItemToMofify = interaction.getArgumentStringValueByName("name-item-to-modify").orElse(null);
            String nameItemAdd = interaction.getArgumentStringValueByName("name-item-add").orElse(null);

            if (nameItemToMofify != null || nameItemAdd != null) {
                int indexItemToMofify = FileIOItemData.searchItem(nameItemToMofify);
                if (indexItemToMofify == -1) {
                    interaction.createImmediateResponder()
                            .setContent("Paramètre 1 : Introuvable")
                            .setFlags(MessageFlag.EPHEMERAL)
                            .respond();
                    return;
                }
                int indexAddItem = FileIOItemData.searchItem(nameItemAdd);
                if (indexAddItem == -1) {
                    interaction.createImmediateResponder()
                            .setContent("Paramètre 2 : Introuvable")
                            .setFlags(MessageFlag.EPHEMERAL)
                            .respond();
                } else {
                    itemToModify = FileIOItemData.itemArrayList.get(indexItemToMofify);
                    itemSelected = FileIOItemData.itemArrayList.get(indexAddItem);
                    interaction.respondWithModal("modalAddCraft",
                            truncate("Quantité de [" + itemSelected.getName() + "] pour craft [" + itemToModify.getName() + "]", 45),
                            ActionRow.of(TextInput.create(TextInputStyle.SHORT,
                                    "quantityCraft", truncate("Quantité nécessaire (Nombre entier uniquement)", 45),
                                    truncate("Entrez un nombre ici", 45), "", true))).join();
                }
            } else {
                interaction.createImmediateResponder()
                        .setContent("Nom d'item manquant")
                        .setFlags(MessageFlag.EPHEMERAL)
                        .respond();
            }
        }
    }

    @Override
    public void onModalSubmit(ModalSubmitEvent modalSubmitEvent) {
        ModalInteraction modalInteraction = modalSubmitEvent.getModalInteraction();
        if (modalInteraction.getCustomId().equals("modalAddCraft")) {
            Optional<String> quantityOpt = modalInteraction.getTextInputValueByCustomId("quantityCraft");
            String quantityString = quantityOpt.orElse("0");
            long quantity;
            try {
                quantity = Long.parseLong(quantityString);
            } catch (NumberFormatException e) {
                modalInteraction.createImmediateResponder()
                        .setContent("Quantité invalide")
                        .setFlags(MessageFlag.EPHEMERAL)
                        .respond();
                return;
            }

            if (itemToModify != null && itemSelected != null) {
                itemToModify.addItemForCraft(itemSelected, quantity);
                modalInteraction.createImmediateResponder()
                        .setContent("Item [**" + itemSelected.getName() +
                                "**] ajouté au craft de [**"+ itemToModify.getName()+"**] avec la quantité **" + quantity +"**")
                        .setFlags(MessageFlag.EPHEMERAL)
                        .respond();
                try {
                    FileIOItemData.writeObject(itemSelected);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else {
                modalInteraction.createImmediateResponder()
                        .setContent("Erreur lors de l'ajout de l'item au craft")
                        .setFlags(MessageFlag.EPHEMERAL)
                        .respond();
            }
        }
    }

    private String truncate(String input, int maxLength) {
        if (input.length() <= maxLength) {
            return input;
        }
        return input.substring(0, maxLength);
    }
}

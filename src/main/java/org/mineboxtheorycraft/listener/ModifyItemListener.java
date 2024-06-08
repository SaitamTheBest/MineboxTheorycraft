package org.mineboxtheorycraft.listener;

import org.javacord.api.entity.message.component.ActionRow;
import org.javacord.api.entity.message.component.TextInput;
import org.javacord.api.entity.message.component.TextInputStyle;
import org.javacord.api.event.interaction.ModalSubmitEvent;
import org.javacord.api.event.interaction.SlashCommandCreateEvent;
import org.javacord.api.interaction.ModalInteraction;
import org.javacord.api.interaction.SlashCommandInteraction;
import org.javacord.api.listener.interaction.ModalSubmitListener;
import org.javacord.api.listener.interaction.SlashCommandCreateListener;
import org.mineboxtheorycraft.filedata.FileIOItemData;
import org.mineboxtheorycraft.message.ModifyResultItemMessage;
import org.mineboxtheorycraft.model.Item;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class ModifyItemListener implements SlashCommandCreateListener, ModalSubmitListener {
    private int index;
    @Override
    public void onSlashCommandCreate(SlashCommandCreateEvent slashCommandCreateEvent) {
        SlashCommandInteraction interaction = slashCommandCreateEvent.getSlashCommandInteraction();
        String command = interaction.getCommandName();
        if (command.equals("modify")) {
            String name = interaction.getArgumentStringValueByName("name").get();

            index = FileIOItemData.searchItem(name);
            if (index == -1) {
                interaction.createImmediateResponder().setContent("Introuvable").respond();
            } else {
                Item item = FileIOItemData.itemArrayList.get(index);
                interaction.respondWithModal("modifyItemModal", "Modifier l'item " + item.getName(),
                        ActionRow.of(
                                TextInput.create(TextInputStyle.SHORT, "modifyName", "Nom de l'item", "Actuel = " + item.getName(), "")
                        )
                        ,ActionRow.of(
                                TextInput.create(TextInputStyle.SHORT, "modifyPrice", "Prix de l'item", "Actuel = " + item.getPrice(), "")
                        )
                        ,ActionRow.of(
                                TextInput.create(TextInputStyle.SHORT, "modifyImage", "Image de l'item (URL uniquement)", false)
                        )
                ).join();
            }
        }
    }

    @Override
    public void onModalSubmit(ModalSubmitEvent modalSubmitEvent) {
        ModalInteraction modalInteraction = modalSubmitEvent.getModalInteraction();
        if (modalInteraction.getCustomId().equals("modifyItemModal")) {
            String newName = modalInteraction.getTextInputValueByCustomId("modifyName").orElse("");
            String priceString = modalInteraction.getTextInputValueByCustomId("modifyPrice").orElse("0");
            String newImageUrl = modalInteraction.getTextInputValueByCustomId("modifyImage").orElse("");
            Long newPrice;
            if (index != -1) {
                Item item = FileIOItemData.itemArrayList.get(index);
                String previousName = item.getName();
                Long previousPrice = item.getPrice();

                if (priceString.isEmpty()){
                    newPrice = previousPrice;
                } else {
                    newPrice = Long.valueOf(priceString);
                }
                if (newName.isEmpty()) {
                    newName = previousName;
                }

                if(!newName.isEmpty()){
                    item.setName(newName);
                }
                if (!priceString.isEmpty() || !newPrice.equals(0L)) {
                    item.setPrice(newPrice);
                }

                if (!newImageUrl.isEmpty()){
                    try {
                        URL urlImage = new URL(newImageUrl);
                        item.setUrlImage(urlImage);
                    } catch (MalformedURLException e) {
                        throw new RuntimeException(e);
                    }
                }

                try {
                    FileIOItemData.writeObject(item);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                modalInteraction.createImmediateResponder().setContent("Item mis à jour avec succès!")
                        .addEmbed(ModifyResultItemMessage.SearchItemEmbed(previousName,newName,previousPrice,newPrice)).respond();
            } else {
                modalInteraction.createImmediateResponder().setContent("Erreur lors de la mise à jour de l'item.").respond();
            }
        }
    }
}

package org.mineboxtheorycraft.listener;

import org.javacord.api.entity.message.MessageFlag;
import org.javacord.api.entity.message.component.ActionRow;
import org.javacord.api.entity.message.component.TextInput;
import org.javacord.api.entity.message.component.TextInputStyle;
import org.javacord.api.event.interaction.ButtonClickEvent;
import org.javacord.api.event.interaction.ModalSubmitEvent;
import org.javacord.api.interaction.ButtonInteraction;
import org.javacord.api.interaction.ModalInteraction;
import org.javacord.api.listener.interaction.ButtonClickListener;
import org.javacord.api.listener.interaction.ModalSubmitListener;
import org.mineboxtheorycraft.filedata.FileIOMessageCommerce;

import java.io.IOException;

public class ModifyMessageCommerceListener implements ButtonClickListener, ModalSubmitListener {
    @Override
    public void onButtonClick(ButtonClickEvent buttonClickEvent) {
        ButtonInteraction button = buttonClickEvent.getButtonInteraction();
        String action = button.getCustomId();
        if(action.equals("modifyMessage")) {
            button.respondWithModal("modifyMessageCommerceModal", "Modifier le message commerce",
                    ActionRow.of(
                            TextInput.create(TextInputStyle.PARAGRAPH, "modifyMessage", "Message commerce :", "Entrez du texte", "", true)
                    )
            ).join();
        }
    }

    @Override
    public void onModalSubmit(ModalSubmitEvent modalSubmitEvent) {
        ModalInteraction modalInteraction = modalSubmitEvent.getModalInteraction();
        if (modalInteraction.getCustomId().equals("modifyMessageCommerceModal")) {
            String newMessage = modalInteraction.getTextInputValueByCustomId("modifyMessage").orElse("Pas de message enregistré");
            try {
                FileIOMessageCommerce.writeMessageInFile(newMessage);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            modalInteraction.createImmediateResponder()
                    .setContent("Message mis à jour avec succès!")
                    .setFlags(MessageFlag.EPHEMERAL)
                    .respond();
        }
    }
}

package org.mineboxtheorycraft.listener;

import org.javacord.api.entity.Attachment;
import org.javacord.api.entity.message.MessageFlag;
import org.javacord.api.event.interaction.SlashCommandCreateEvent;
import org.javacord.api.interaction.SlashCommandInteraction;
import org.javacord.api.listener.interaction.SlashCommandCreateListener;
import org.mineboxtheorycraft.filedata.FileIOItemData;
import org.mineboxtheorycraft.message.AddItemMessage;
import org.mineboxtheorycraft.model.Item;

import java.io.IOException;
import java.net.URL;

public class AddItemListener implements SlashCommandCreateListener {
    @Override
    public void onSlashCommandCreate(SlashCommandCreateEvent slashCommandCreateEvent) {
        SlashCommandInteraction interaction = slashCommandCreateEvent.getSlashCommandInteraction();
        String command = interaction.getCommandName();
        if (command.equals("add-item"))
        {
            String name = interaction.getArgumentStringValueByName("name").get();
            Long price = interaction.getArgumentLongValueByName("price").get();
            boolean haveImage = interaction.getArgumentAttachmentValueByName("image").isEmpty();
            Item item = null;
            if (!haveImage){
                Attachment image = interaction.getArgumentAttachmentValueByName("image").get();
                if (image.isImage()){
                    URL urlImage = image.getUrl();
                    item = new Item(name,price,urlImage);
                }
                else {
                    interaction.createImmediateResponder()
                            .setContent("Erreur, la pièce jointe doit être une image")
                            .setFlags(MessageFlag.EPHEMERAL)
                            .respond();
                }
            }
            else{
                item = new Item(name,price);
            }
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

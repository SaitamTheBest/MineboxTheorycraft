package org.mineboxtheorycraft.listener;

import org.javacord.api.entity.message.component.ActionRow;
import org.javacord.api.entity.message.component.Button;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.event.interaction.SlashCommandCreateEvent;
import org.javacord.api.interaction.SlashCommandInteraction;
import org.javacord.api.listener.interaction.SlashCommandCreateListener;
import org.mineboxtheorycraft.filedata.FileIOMessageCommerce;

public class MessageCommerceListener implements SlashCommandCreateListener {
    @Override
    public void onSlashCommandCreate(SlashCommandCreateEvent slashCommandCreateEvent) {
        SlashCommandInteraction interaction = slashCommandCreateEvent.getSlashCommandInteraction();
        String command = interaction.getCommandName();
        if (command.equals("message-commerce")){
            String messageCommerce = FileIOMessageCommerce.messageCommerce;
            if (messageCommerce.isEmpty()){
                messageCommerce = "Pas de message enregistré";
            }

            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setDescription("`"+messageCommerce+"`\n");

            interaction.createImmediateResponder().addEmbed(embedBuilder)
                    .addComponents(
                            ActionRow.of(Button.primary("modifyMessage","Modifier le message"))
                    ).respond();
        }
    }
}

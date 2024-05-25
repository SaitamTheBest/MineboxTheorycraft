package org.mineboxtheorycraft.message;

import org.javacord.api.entity.message.embed.EmbedBuilder;

public class AddItemMessage {
    public static EmbedBuilder AddItemEmbed(String name, Long price){
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("Ajout de l'item `"+name+"` avec succès");
        embedBuilder.addInlineField("Nom",name);
        embedBuilder.addInlineField("Prix",price.toString());
        embedBuilder.setFooter("Ajout d'un item");

        return embedBuilder;
    }
}

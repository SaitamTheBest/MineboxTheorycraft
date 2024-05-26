package org.mineboxtheorycraft.message;

import org.javacord.api.entity.message.embed.EmbedBuilder;

public class SearchItemMessage {
    public static EmbedBuilder SearchItemEmbed(String previousName, String newName, Long previousPrice, Long newPrice){
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("Modification de `"+previousName+"` avec succès");
        embedBuilder.addInlineField("Nom modifié :",previousName);
        embedBuilder.addInlineField("Nom actuel :",newName);
        embedBuilder.addInlineField("","");
        embedBuilder.addInlineField("Prix modifié :",previousPrice.toString());
        embedBuilder.addInlineField("Prix actuel :",newPrice.toString());
        embedBuilder.setFooter("Modification d'un item");

        return embedBuilder;
    }
}

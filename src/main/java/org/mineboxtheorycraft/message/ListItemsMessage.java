package org.mineboxtheorycraft.message;

import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.mineboxtheorycraft.model.Item;

import java.util.ArrayList;

public class ListItemsMessage {
    public static EmbedBuilder ListItemEmbed(ArrayList<Item> items){
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("Liste des items");
        for (Item item : items){
            embedBuilder.addField("`[ "+ item.getName() +" ]`",
                    "`Prix : "+ item.getPrice() + "Luxs, Bénéfice : "+ item.rentabilyCraftPercentage() +" `");
        }

        return embedBuilder;
    }
}

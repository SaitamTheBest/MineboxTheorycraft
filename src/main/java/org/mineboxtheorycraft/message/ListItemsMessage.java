package org.mineboxtheorycraft.message;

import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.mineboxtheorycraft.model.Item;

import java.util.ArrayList;

public class ListItemsMessage {
    public static EmbedBuilder ListItemEmbed(ArrayList<Item> items){
        String textEmbed = "```diff\n";
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("Liste des items");
        int index = 0;
        String even = "+ ";
        String odd = "- ";
        for (Item item : items){
            if (index % 2 == 0){
                textEmbed = textEmbed + even;
            }
            else {
                textEmbed = textEmbed + odd;
            }
            textEmbed += "[ "+ item.getName() +" ] => Prix : "+ item.getPrice() + "Luxs, Bénéfice : "+ item.rentabilyCraftPercentage() +"\n";
            index++;
        }
        textEmbed += "```";
        embedBuilder.setDescription(textEmbed);

        return embedBuilder;
    }
}

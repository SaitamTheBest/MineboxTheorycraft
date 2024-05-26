package org.mineboxtheorycraft.message;

import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.mineboxtheorycraft.model.Item;
import org.mineboxtheorycraft.model.RequestAverageRentabiltyItems;

import java.util.HashMap;

public class PresentationItemMessage {
    public static EmbedBuilder presentationItem(Item item){
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("Presentation ["+item.getName()+"]");

        embed.addInlineField("Nom :",item.getName());
        embed.addInlineField("Prix :",item.getPrice().toString()+" Luxs\uD83D\uDCB0");

        StringBuilder stringCraft = new StringBuilder();
        if (item.getCraftItem().isEmpty()){
            stringCraft.append("Pas de craft");
            embed.addField("Craft :", "```" + stringCraft + "```", false);
        }
        else {
            for (HashMap.Entry<Item, Long> entry : item.getCraftItem().entrySet()) {
                stringCraft.append(entry.getValue() +
                        "x " + entry.getKey().getName() +
                        "("+ entry.getKey().getPrice() * entry.getValue() +" Luxs\uD83D\uDCB0)\n");
            }
            embed.addField("Craft :", "```" + stringCraft + "```", false);
            double rentabilityPercentage = item.rentabilyCraftPercentage();
            String emoji;
            if (rentabilityPercentage > 0){
                emoji = "\uD83D\uDCC8";
            }
            else {
                emoji = "\uD83D\uDCC9";
            }
            embed.addInlineField("Bénéfice :", "[" +emoji + "] " + String.format("%.2f", rentabilityPercentage) +"%");

            String isRentable = RequestAverageRentabiltyItems.IsRentable(item);
            embed.addInlineField("Rentabilité :", isRentable);
        }
        embed.setTimestampToNow();
        embed.setFooter("Presentation ["+item.getName()+"]");
        if (item.getUrlImage() != null){
            embed.setThumbnail(item.getUrlImage().toString());
        }

        return embed;
    };
}

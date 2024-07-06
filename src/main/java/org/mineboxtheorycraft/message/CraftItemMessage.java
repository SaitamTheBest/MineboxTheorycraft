package org.mineboxtheorycraft.message;

import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.mineboxtheorycraft.model.Item;
import org.mineboxtheorycraft.model.RequestAverageRentabiltyItems;

import java.util.HashMap;

public class CraftItemMessage {
    public static EmbedBuilder craftItemPresentation(Item item, Long quantity){
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("Craft  [ x"+quantity+" "+item.getName()+" ]");

        embed.addInlineField("Nom :",item.getName());
        embed.addInlineField("Prix :",item.getPrice().toString()+" Luxs\uD83D\uDCB0");
        embed.addField("Total vente :", item.getPrice()*quantity +" Luxs\uD83D\uDCB0");
        embed.addInlineField("Total achat :", item.itemPriceCraft()*quantity +" Luxs\uD83D\uDCB0");

        StringBuilder stringCraft = new StringBuilder();
        if (item.getCraftItem().isEmpty()){
            stringCraft.append("Pas de craft");
            embed.addField("Craft :", "```" + stringCraft + "```", false);
        }
        else {
            for (HashMap.Entry<Item, Long> entry : item.getCraftItem().entrySet()) {
                stringCraft.append(entry.getValue()*quantity +
                        "x " + entry.getKey().getName() +
                        "("+ entry.getKey().getPrice() * entry.getValue() * quantity +" Luxs\uD83D\uDCB0)\n");
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
        embed.setFooter("Craft  [ x"+quantity+" "+item.getName()+" ]");
        if (item.getUrlImage() != null){
            embed.setThumbnail(item.getUrlImage().toString());
        }

        return embed;
    }
}

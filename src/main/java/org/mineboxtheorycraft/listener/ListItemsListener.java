package org.mineboxtheorycraft.listener;

import org.javacord.api.entity.message.MessageFlag;
import org.javacord.api.entity.message.component.ActionRow;
import org.javacord.api.entity.message.component.SelectMenu;
import org.javacord.api.entity.message.component.SelectMenuOption;
import org.javacord.api.event.interaction.SelectMenuChooseEvent;
import org.javacord.api.event.interaction.SlashCommandCreateEvent;
import org.javacord.api.interaction.SelectMenuInteraction;
import org.javacord.api.interaction.SlashCommandInteraction;
import org.javacord.api.listener.interaction.SelectMenuChooseListener;
import org.javacord.api.listener.interaction.SlashCommandCreateListener;
import org.mineboxtheorycraft.message.ListItemsMessage;
import org.mineboxtheorycraft.model.Item;
import org.mineboxtheorycraft.model.SortMethodItemsList;

import java.util.ArrayList;
import java.util.List;


public class ListItemsListener implements SlashCommandCreateListener, SelectMenuChooseListener {
    @Override
    public void onSlashCommandCreate(SlashCommandCreateEvent slashCommandCreateEvent) {
        SlashCommandInteraction interaction = slashCommandCreateEvent.getSlashCommandInteraction();
        String command = interaction.getCommandName();
        if (command.equals("list-items")) {
            List<SelectMenuOption> options = new ArrayList<>();
            options.add(SelectMenuOption.create("Ordre alphabétique","alphabetic"));
            options.add(SelectMenuOption.create("Prix décroissant","price"));
            options.add(SelectMenuOption.create("Bénéfice décroissant","rentability"));
            interaction.createImmediateResponder()
                    .setContent("Choisissez un type de tri pour l'affichage des items")
                    .addComponents(
                            ActionRow.of(
                                    SelectMenu.create(
                                            "menuListItems",
                                            "Choisissez une méthode de tri",
                                            options
                                    )
                            )
                    )
                    .setFlags(MessageFlag.EPHEMERAL)
                    .respond();
        }
    }

    @Override
    public void onSelectMenuChoose(SelectMenuChooseEvent selectMenuChooseEvent) {
        SelectMenuInteraction selectMenuInteraction = selectMenuChooseEvent.getSelectMenuInteraction();
        String customId = selectMenuInteraction.getCustomId();
        if (customId.equals("menuListItems")) {

            List<String> selectedValues = selectMenuInteraction.getChosenOptions().stream()
                    .map(SelectMenuOption::getValue)
                    .toList();

            switch (selectedValues.get(0)) {
                case "alphabetic":
                    alphabeticMethod(selectMenuInteraction);
                    break;
                case "price":
                    priceMethod(selectMenuInteraction);
                    break;
                case "rentability":
                    rentabilityMethod(selectMenuInteraction);
                    break;
                default:
                    selectMenuInteraction.createOriginalMessageUpdater().setContent("Erreur").update();
                    break;
            }
        }
    }

    private void alphabeticMethod(SelectMenuInteraction selectMenuInteraction) {
        ArrayList<Item> alphabeticList = SortMethodItemsList.alphabeticMethod();

        selectMenuInteraction.createOriginalMessageUpdater()
                .addEmbed(ListItemsMessage.ListItemEmbed(alphabeticList))
                .update();
    }

    private void priceMethod(SelectMenuInteraction selectMenuInteraction) {
        ArrayList<Item> priceList = SortMethodItemsList.priceMethod();

        selectMenuInteraction.createOriginalMessageUpdater()
                .addEmbed(ListItemsMessage.ListItemEmbed(priceList))
                .update();
    }

    private void rentabilityMethod(SelectMenuInteraction selectMenuInteraction) {
        ArrayList<Item> rentabilityList = SortMethodItemsList.rentabilityMethod();

        selectMenuInteraction.createOriginalMessageUpdater()
                .addEmbed(ListItemsMessage.ListItemEmbed(rentabilityList))
                .update();
    }
}

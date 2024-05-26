package org.mineboxtheorycraft.listener;

import org.javacord.api.entity.message.MessageFlag;
import org.javacord.api.entity.message.component.*;
import org.javacord.api.event.interaction.ButtonClickEvent;
import org.javacord.api.event.interaction.ModalSubmitEvent;
import org.javacord.api.event.interaction.SelectMenuChooseEvent;
import org.javacord.api.event.interaction.SlashCommandCreateEvent;
import org.javacord.api.interaction.ButtonInteraction;
import org.javacord.api.interaction.ModalInteraction;
import org.javacord.api.interaction.SelectMenuInteraction;
import org.javacord.api.interaction.SlashCommandInteraction;
import org.javacord.api.listener.interaction.ButtonClickListener;
import org.javacord.api.listener.interaction.ModalSubmitListener;
import org.javacord.api.listener.interaction.SelectMenuChooseListener;
import org.javacord.api.listener.interaction.SlashCommandCreateListener;
import org.mineboxtheorycraft.filedata.FileIOItemData;
import org.mineboxtheorycraft.model.Item;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class CraftItemListener implements SlashCommandCreateListener, ButtonClickListener, ModalSubmitListener, SelectMenuChooseListener {
    private Item itemToModify;
    private Item itemSelected;

    @Override
    public void onSlashCommandCreate(SlashCommandCreateEvent slashCommandCreateEvent) {
        SlashCommandInteraction interaction = slashCommandCreateEvent.getSlashCommandInteraction();
        String command = interaction.getCommandName();
        if (command.equals("craft-item")) {
            String name = interaction.getArgumentStringValueByName("name").orElse(null);

            if (name != null) {
                int index = FileIOItemData.searchItem(name);
                if (index == -1) {
                    interaction.createImmediateResponder()
                            .setContent("Introuvable")
                            .setFlags(MessageFlag.EPHEMERAL)
                            .respond();
                } else {
                    itemToModify = FileIOItemData.itemArrayList.get(index);

                    interaction.createImmediateResponder()
                            .setContent("Quelle action voulez-vous réaliser sur " + itemToModify.getName() + "?")
                            .addComponents(
                                    ActionRow.of(
                                            Button.success("addCraftItem", "Ajouter un item pour craft [" + itemToModify.getName() + "]"),
                                            Button.danger("removeCraftItem", "Supprimer un item pour le craft de [" + itemToModify.getName() + "]")
                                    )
                            )
                            .setFlags(MessageFlag.EPHEMERAL)
                            .respond();
                }
            } else {
                interaction.createImmediateResponder()
                        .setContent("Nom d'item manquant")
                        .setFlags(MessageFlag.EPHEMERAL)
                        .respond();
            }
        }
    }

    @Override
    public void onButtonClick(ButtonClickEvent buttonClickEvent) {
        ButtonInteraction button = buttonClickEvent.getButtonInteraction();
        String action = button.getCustomId();

        switch (action) {
            case "addCraftItem":
                handleAddCraftItem(button);
                break;
            case "removeCraftItem":
                handleRemoveCraftItem(button);
                break;
            default:
                button.createImmediateResponder()
                        .setContent("Action non reconnue")
                        .setFlags(MessageFlag.EPHEMERAL)
                        .respond();
                break;
        }
    }

    private void handleAddCraftItem(ButtonInteraction button) {
        List<SelectMenuOption> options = new ArrayList<>();
        for (Item item : FileIOItemData.itemArrayList) {
            if (!item.getName().equals(itemToModify.getName())
                    && !itemToModify.getCraftItem().containsKey(item)) {
                options.add(SelectMenuOption.create(item.getName(), item.getName()));
            }
        }

        if (options.isEmpty()) {
            button.createImmediateResponder()
                    .setContent("Aucun item disponible à ajouter")
                    .setFlags(MessageFlag.EPHEMERAL)
                    .respond();
        } else {
            button.createImmediateResponder()
                    .setContent("Choisissez un item pour le craft")
                    .addComponents(
                            ActionRow.of(
                                    SelectMenu.create("menuItemsCraftable", "Choisissez un item", options)
                            )
                    )
                    .setFlags(MessageFlag.EPHEMERAL)
                    .respond();
        }
    }

    private void handleRemoveCraftItem(ButtonInteraction button) {
        if (itemToModify != null && !itemToModify.getCraftItem().isEmpty()) {
            List<SelectMenuOption> optionsCraftItems = new ArrayList<>();
            for (HashMap.Entry<Item, Long> entry : itemToModify.getCraftItem().entrySet()) {
                optionsCraftItems.add(
                        SelectMenuOption.create(entry.getKey().getName(), entry.getKey().getName())
                );
            }

            button.createImmediateResponder()
                    .setContent("Choisissez un item à supprimer du craft")
                    .addComponents(
                            ActionRow.of(
                                    SelectMenu.create("menuItemsToRemove", "Choisissez un item", optionsCraftItems)
                            )
                    )
                    .setFlags(MessageFlag.EPHEMERAL)
                    .respond();
        } else {
            button.createImmediateResponder()
                    .setContent("Aucun item à supprimer du craft")
                    .setFlags(MessageFlag.EPHEMERAL)
                    .respond();
        }
    }

    @Override
    public void onSelectMenuChoose(SelectMenuChooseEvent event) {
        SelectMenuInteraction selectMenuInteraction = event.getSelectMenuInteraction();
        String customId = selectMenuInteraction.getCustomId();
        List<String> selectedValues = selectMenuInteraction.getChosenOptions().stream()
                .map(SelectMenuOption::getValue)
                .toList();

        if (customId.equals("menuItemsCraftable")) {
            handleSelectMenuCraftable(selectMenuInteraction, selectedValues);
        } else if (customId.equals("menuItemsToRemove")) {
            handleSelectMenuToRemove(selectMenuInteraction, selectedValues);
        }
    }

    private void handleSelectMenuCraftable(SelectMenuInteraction selectMenuInteraction, List<String> selectedValues) {
        if (!selectedValues.isEmpty()) {
            String selectedItemName = selectedValues.get(0);
            int index = FileIOItemData.searchItem(selectedItemName);
            Item selectedItem = FileIOItemData.itemArrayList.get(index);
            itemSelected = selectedItem;
            if (selectedItem != null) {
                selectMenuInteraction.respondWithModal("modalAddCraft",
                        truncate("Quantité de [" + selectedItem.getName() + "] pour craft [" + itemToModify.getName() + "]", 45),
                        ActionRow.of(TextInput.create(TextInputStyle.SHORT,
                                "quantityCraft", truncate("Quantité nécessaire (Nombre entier uniquement)", 45),
                                truncate("Entrez un nombre ici", 45), "", true))).join();
            }
        }
    }

    private void handleSelectMenuToRemove(SelectMenuInteraction selectMenuInteraction, List<String> selectedValues) {
        if (!selectedValues.isEmpty()) {
            String selectedItemName = selectedValues.get(0);
            int index = FileIOItemData.searchItem(selectedItemName);
            Item selectedItem = FileIOItemData.itemArrayList.get(index);
            if (selectedItem != null && itemToModify.getCraftItem().containsKey(selectedItem)) {
                // Remove the selected item from the craft
                itemToModify.getCraftItem().remove(selectedItem);
                selectMenuInteraction.createImmediateResponder()
                        .setContent("Item " + selectedItemName + " supprimé du craft de " + itemToModify.getName())
                        .setFlags(MessageFlag.EPHEMERAL)
                        .respond();
            }
        }
    }

    @Override
    public void onModalSubmit(ModalSubmitEvent modalSubmitEvent) {
        ModalInteraction modalInteraction = modalSubmitEvent.getModalInteraction();
        if (modalInteraction.getCustomId().equals("modalAddCraft")) {
            Optional<String> quantityOpt = modalInteraction.getTextInputValueByCustomId("quantityCraft");
            String quantityString = quantityOpt.orElse("0");
            long quantity;
            try {
                quantity = Long.parseLong(quantityString);
            } catch (NumberFormatException e) {
                modalInteraction.createImmediateResponder()
                        .setContent("Quantité invalide")
                        .setFlags(MessageFlag.EPHEMERAL)
                        .respond();
                return;
            }

            if (itemToModify != null && itemSelected != null) {
                itemToModify.addItemForCraft(itemSelected, quantity);
                modalInteraction.createImmediateResponder()
                        .setContent("Item [**" + itemSelected.getName() +
                                "**] ajouté au craft de [**"+ itemToModify.getName()+"**] avec la quantité **" + quantity +"**")
                        .setFlags(MessageFlag.EPHEMERAL)
                        .respond();
                try {
                    FileIOItemData.writeObject(itemSelected);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else {
                modalInteraction.createImmediateResponder()
                        .setContent("Erreur lors de l'ajout de l'item au craft")
                        .setFlags(MessageFlag.EPHEMERAL)
                        .respond();
            }
        }
    }

    private String truncate(String input, int maxLength) {
        if (input.length() <= maxLength) {
            return input;
        }
        return input.substring(0, maxLength);
    }
}

package Scenes;

import Pokemon.Bisasam;
import Pokemon.Evoli;
import Pokemon.Glumanda;
import Pokemon.Lektroball;
import Pokemon.Nebulak;
import Pokemon.Pokemon;
import Pokemon.Schiggy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class PokemonScene {

    public static Scene createScene(Stage stage) {
        return createScene(stage, new ArrayList<>());
    }

    public static Scene createScene(Stage stage, List<String> preselectedNames) {
        List<Pokemon> selectedPokemons = new ArrayList<>();
        Set<String> selected = new HashSet<>();

        Label badge = SceneStyler.createBadge("TEAM DRAFT");
        Label title = SceneStyler.createTitle("Choose Your Pokemon");
        Label subtitle = SceneStyler.createSubtitle("Pick three fighters for your opening team. Click a selected card again to remove it.");
        Label counter = SceneStyler.createBadge("0 / 3 selected");
        Label[] slotLabels = new Label[3];

        GridPane pokemonGrid = new GridPane();
        pokemonGrid.setHgap(18);
        pokemonGrid.setVgap(18);
        pokemonGrid.setPadding(new Insets(16));
        pokemonGrid.setAlignment(Pos.CENTER);

        VBox[] cards = {
            createPokemonBox(new Glumanda(), selectedPokemons, selected, counter),
            createPokemonBox(new Bisasam(), selectedPokemons, selected, counter),
            createPokemonBox(new Evoli(), selectedPokemons, selected, counter),
            createPokemonBox(new Schiggy(), selectedPokemons, selected, counter),
            createPokemonBox(new Lektroball(), selectedPokemons, selected, counter),
            createPokemonBox(new Nebulak(), selectedPokemons, selected, counter)
        };

        for (int i = 0; i < cards.length; i++) {
            pokemonGrid.add(cards[i], i % 3, i / 3);
        }

        Label slotsTitle = SceneStyler.createSectionLabel("Selected Team");
        GridPane selectionPreview = new GridPane();
        selectionPreview.setHgap(12);
        selectionPreview.setAlignment(Pos.CENTER);
        for (int i = 0; i < slotLabels.length; i++) {
            slotLabels[i] = SceneStyler.createBodyLabel("Empty");
            slotLabels[i].setTextAlignment(TextAlignment.CENTER);

            VBox slot = new VBox(slotLabels[i]);
            slot.setAlignment(Pos.CENTER);
            slot.setPrefWidth(180);
            slot.setMinHeight(62);
            slot.setPadding(new Insets(14));
            slot.setStyle(SceneStyler.CARD_STYLE);
            selectionPreview.add(slot, i, 0);
        }

        Button continueButton = new Button("Continue to Battle");
        SceneStyler.stylePrimaryButton(continueButton);
        continueButton.setDisable(true);
        continueButton.setOnAction(e -> goToBattlePrep(stage, selectedPokemons));

        updateSelectionPreview(selectedPokemons, counter, continueButton, slotLabels);

        VBox contentBox = SceneStyler.createPanel(
            Pos.CENTER,
            20,
            badge,
            title,
            subtitle,
            counter,
            pokemonGrid,
            slotsTitle,
            selectionPreview,
            continueButton
        );
        contentBox.setPadding(new Insets(28));
        contentBox.setMaxWidth(860);

        for (VBox card : cards) {
            card.setOnMouseClicked(e -> {
                Pokemon pokemon = (Pokemon) card.getUserData();
                togglePokemonSelection(pokemon, card, selectedPokemons, selected, counter, continueButton, slotLabels);
                if (selectedPokemons.size() == 3) {
                    goToBattlePrep(stage, selectedPokemons);
                }
            });
        }

        applyPreselectedTeam(preselectedNames, cards, selectedPokemons, selected, counter, continueButton, slotLabels);

        return SceneStyler.createScene(contentBox, 1000, 700);
    }

    private static VBox createPokemonBox(
        Pokemon pokemonInstance,
        List<Pokemon> selectedPokemons,
        Set<String> selected,
        Label counter
    ) {
        String name = pokemonInstance.getName();
        String imagePath = "file:" + System.getProperty("user.dir") + "/Images/" + pokemonInstance.getClass().getSimpleName() + ".png";
        ImageView imageView = new ImageView(new Image(imagePath));
        imageView.setFitWidth(150);
        imageView.setFitHeight(170);
        imageView.setPreserveRatio(true);
        imageView.setMouseTransparent(true);

        Button infoButton = new Button("i");
        infoButton.setOnAction(e -> showInfoPopup(pokemonInstance));
        SceneStyler.styleInfoButton(infoButton);

        StackPane imageCard = new StackPane(imageView, infoButton);
        imageCard.setPadding(new Insets(8));
        imageCard.setStyle(
            "-fx-background-color: rgba(255,255,255,0.08);" +
            "-fx-background-radius: 18;" +
            "-fx-border-color: rgba(255,255,255,0.08);" +
            "-fx-border-radius: 18;"
        );
        StackPane.setAlignment(infoButton, Pos.TOP_RIGHT);
        StackPane.setMargin(infoButton, new Insets(6));

        Label label = SceneStyler.createSectionLabel(name);
        label.setTextAlignment(TextAlignment.CENTER);
        Label elementLabel = SceneStyler.createBodyLabel("Type: " + pokemonInstance.getElementsLabel());
        elementLabel.setTextAlignment(TextAlignment.CENTER);
        Label hpLabel = SceneStyler.createBodyLabel("HP: " + pokemonInstance.getHp());
        hpLabel.setTextAlignment(TextAlignment.CENTER);

        VBox box = new VBox(10, imageCard, label, elementLabel, hpLabel);
        box.setAlignment(Pos.CENTER);
        box.setPrefWidth(220);
        box.setMinHeight(320);
        box.setPadding(new Insets(18));
        box.setUserData(pokemonInstance);
        SceneStyler.setSelectableCardStyle(box, false);

        return box;
    }

    private static void togglePokemonSelection(
        Pokemon pokemonInstance,
        VBox card,
        List<Pokemon> selectedPokemons,
        Set<String> selected,
        Label counter,
        Button continueButton,
        Label[] slotLabels
    ) {
        String name = pokemonInstance.getName();

        if (selected.contains(name)) {
            selected.remove(name);
            selectedPokemons.remove(pokemonInstance);
            SceneStyler.setSelectableCardStyle(card, false);
        } else if (selectedPokemons.size() < 3) {
            selected.add(name);
            selectedPokemons.add(pokemonInstance);
            SceneStyler.setSelectableCardStyle(card, true);
        }

        updateSelectionPreview(selectedPokemons, counter, continueButton, slotLabels);
    }

    private static void updateSelectionPreview(
        List<Pokemon> selectedPokemons,
        Label counter,
        Button continueButton,
        Label[] slotLabels
    ) {
        counter.setText(selectedPokemons.size() + " / 3 selected");
        continueButton.setDisable(selectedPokemons.size() != 3);

        for (int i = 0; i < slotLabels.length; i++) {
            if (i < selectedPokemons.size()) {
                slotLabels[i].setText(selectedPokemons.get(i).getName());
                slotLabels[i].getParent().setStyle(SceneStyler.SELECTED_CARD_STYLE);
            } else {
                slotLabels[i].setText("Empty");
                slotLabels[i].getParent().setStyle(SceneStyler.CARD_STYLE);
            }
        }
    }

    private static void applyPreselectedTeam(
        List<String> preselectedNames,
        VBox[] cards,
        List<Pokemon> selectedPokemons,
        Set<String> selected,
        Label counter,
        Button continueButton,
        Label[] slotLabels
    ) {
        if (preselectedNames == null) {
            return;
        }

        List<String> validNames = Arrays.asList("Glumanda", "Bisasam", "Evoli", "Schiggy", "Lektroball", "Nebulak");
        for (String name : preselectedNames) {
            if (!validNames.contains(name) || selected.contains(name) || selectedPokemons.size() >= 3) {
                continue;
            }

            for (VBox card : cards) {
                Pokemon pokemon = (Pokemon) card.getUserData();
                if (pokemon.getName().equals(name)) {
                    selected.add(name);
                    selectedPokemons.add(pokemon);
                    SceneStyler.setSelectableCardStyle(card, true);
                    break;
                }
            }
        }

        updateSelectionPreview(selectedPokemons, counter, continueButton, slotLabels);
    }

    private static void goToBattlePrep(Stage stage, List<Pokemon> selectedPokemons) {
        if (selectedPokemons.size() != 3) {
            return;
        }
        stage.setScene(BattlePrepScene.create(stage, new ArrayList<>(selectedPokemons)));
    }

    private static void showInfoPopup(Pokemon pokemon) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Pokemon Info");
        alert.setHeaderText(pokemon.getName());

        String content =
            "HP: " + pokemon.getHp() + "\n"
                + "Type: " + pokemon.getElementsLabel() + "\n"
                + "Hard battles won: " + pokemon.getSchwereKampfSiege();

        alert.setContentText(content);
        alert.showAndWait();
    }
}

package Scenes;

import Battle.Battle;
import Players.KI.Bot;
import Players.KI.BotManager;
import Pokemon.Pokemon;
import java.util.ArrayList;
import java.util.List;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class BattlePrepScene {

    public static Scene create(Stage stage, List<Pokemon> selectedPokemons) {
        Label badge = SceneStyler.createBadge("TEAM READY");
        Label title = SceneStyler.createTitle("Your Battle Lineup");
        Label subtitle = SceneStyler.createSubtitle("Review your three fighters, inspect their evolutions, then start the match.");

        HBox teamBox = new HBox(18);
        teamBox.setAlignment(Pos.CENTER);
        for (Pokemon p : selectedPokemons) {
            VBox pokeBox = new VBox(10);
            pokeBox.setAlignment(Pos.CENTER);
            pokeBox.setPadding(new Insets(18));
            pokeBox.setPrefWidth(180);
            pokeBox.setStyle(SceneStyler.CARD_STYLE);

            String imagePath = "file:" + System.getProperty("user.dir") + "/Images/" + p.getClass().getSimpleName() + ".png";
            ImageView imgView = new ImageView(new Image(imagePath));
            imgView.setFitWidth(120);
            imgView.setFitHeight(140);
            imgView.setPreserveRatio(true);

            Label nameLabel = SceneStyler.createSectionLabel(p.getClass().getSimpleName());
            Label typeLabel = SceneStyler.createBodyLabel("Type: " + p.getElementsLabel());

            pokeBox.getChildren().addAll(imgView, nameLabel, typeLabel);
            teamBox.getChildren().add(pokeBox);
        }

        Button startFightButton = new Button("Start Battle");
        SceneStyler.stylePrimaryButton(startFightButton);
        startFightButton.setPrefWidth(190);
        startFightButton.setOnAction(e -> {
            DifficultyScene.Difficulty def = DifficultyScene.getSelectedDifficulty();
            BotManager mBot = new BotManager();
            mBot.teamWahl();

            Bot bot = mBot.chooseBot(def);
            Battle battle = new Battle(selectedPokemons, bot);
            BattleView battleView = new BattleView(battle);
            stage.setScene(battleView.createScene(stage));
            stage.setTitle("Pokemon Battle");
        });

        Button myPokemonBtn = new Button("View Evolutions");
        SceneStyler.styleSecondaryButton(myPokemonBtn);
        myPokemonBtn.setPrefWidth(190);
        myPokemonBtn.setOnAction(e -> showTeamPopup(stage, selectedPokemons));

        Button editTeamButton = new Button("Edit Team");
        SceneStyler.styleSecondaryButton(editTeamButton);
        editTeamButton.setPrefWidth(190);
        editTeamButton.setOnAction(e -> stage.setScene(PokemonScene.createScene(stage, getSelectedNames(selectedPokemons))));

        HBox buttonBox = new HBox(16, editTeamButton, myPokemonBtn, startFightButton);
        buttonBox.setAlignment(Pos.CENTER);

        VBox content = SceneStyler.createPanel(Pos.CENTER, 18, badge, title, subtitle, teamBox, buttonBox);
        content.setMaxWidth(860);

        return SceneStyler.createScene(content, 940, 650);
    }

    private static void showTeamPopup(Stage owner, List<Pokemon> selectedPokemons) {
        Stage popupStage = new Stage();
        popupStage.initOwner(owner);
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.setTitle("Team Evolutions");

        VBox innerContent = new VBox(14);
        innerContent.setAlignment(Pos.CENTER);

        Label title = SceneStyler.createTitle("Evolution Preview");
        title.setFont(javafx.scene.text.Font.font("Verdana", javafx.scene.text.FontWeight.EXTRA_BOLD, 28));
        Label subtitle = SceneStyler.createSubtitle("Base form on the left, evolved form on the right.");

        innerContent.getChildren().addAll(title, subtitle);

        for (Pokemon p : selectedPokemons) {
            String baseName = p.getClass().getSimpleName();
            String evolvedName = switch (baseName) {
                case "Bisasam" -> "Bisaflor";
                case "Glumanda" -> "Glurak";
                case "Schiggy" -> "Turtok";
                case "Evoli" -> "Nachtara";
                case "Nebulak" -> "Gengar";
                case "Voltoball" -> "Lektroball";
                default -> baseName;
            };

            ImageView baseImage = new ImageView(
                new Image("file:" + System.getProperty("user.dir") + "/Images/" + baseName + ".png")
            );
            baseImage.setFitWidth(110);
            baseImage.setPreserveRatio(true);

            ImageView evolvedImage = new ImageView(
                new Image("file:" + System.getProperty("user.dir") + "/Images/" + evolvedName + ".png")
            );
            evolvedImage.setFitWidth(110);
            evolvedImage.setPreserveRatio(true);

            Label infoLabel = SceneStyler.createBodyLabel(baseName + " -> " + evolvedName + "   |   HP: " + p.getHp());
            HBox pokeBox = new HBox(18, baseImage, evolvedImage, infoLabel);
            pokeBox.setAlignment(Pos.CENTER_LEFT);
            pokeBox.setPadding(new Insets(16));
            pokeBox.setStyle(SceneStyler.CARD_STYLE);
            innerContent.getChildren().add(pokeBox);
        }

        Button closeButton = new Button("Close");
        SceneStyler.stylePrimaryButton(closeButton);
        closeButton.setOnAction(e -> popupStage.close());
        innerContent.getChildren().add(closeButton);

        VBox panel = SceneStyler.createPanel(Pos.CENTER, 16, innerContent);
        popupStage.setScene(SceneStyler.createScene(panel, 620, 560));
        popupStage.showAndWait();
    }

    private static List<String> getSelectedNames(List<Pokemon> selectedPokemons) {
        List<String> names = new ArrayList<>();
        for (Pokemon pokemon : selectedPokemons) {
            names.add(pokemon.getName());
        }
        return names;
    }
}

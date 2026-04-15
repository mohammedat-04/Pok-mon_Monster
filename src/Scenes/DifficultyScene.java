package Scenes;

import javafx.animation.PauseTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

public class DifficultyScene {

    public enum Difficulty {
        EASY, MEDIUM, HARD
    }

    private static Difficulty selectedDifficulty;

    public static Difficulty getSelectedDifficulty() {
        return selectedDifficulty;
    }

    public static Scene createScene(Stage primaryStage) {
        Runnable goToPokemonScene = () -> {
            primaryStage.setScene(
                SceneStyler.createLoadingScene("Setting the arena", "Getting your team selection ready...", 900, 650)
            );

            PauseTransition pause = new PauseTransition(Duration.seconds(1.2));
            pause.setOnFinished(ev -> primaryStage.setScene(PokemonScene.createScene(primaryStage)));
            pause.play();
        };

        Label badge = SceneStyler.createBadge("MATCH SETTINGS");
        Label title = SceneStyler.createTitle("Choose Your Difficulty");
        Label subtitle = SceneStyler.createSubtitle(
            "Start easy, take the balanced route, or push into a faster and tougher AI battle."
        );

        HBox difficultyCards = new HBox(
            18,
            createDifficultyCard(
                "Easy",
                "A calmer opening fight with more room to learn the flow.",
                Difficulty.EASY,
                goToPokemonScene
            ),
            createDifficultyCard(
                "Medium",
                "A balanced match for a solid challenge without chaos.",
                Difficulty.MEDIUM,
                goToPokemonScene
            ),
            createDifficultyCard(
                "Hard",
                "A sharper AI that punishes weak decisions quickly.",
                Difficulty.HARD,
                goToPokemonScene
            )
        );
        difficultyCards.setAlignment(Pos.CENTER);

        VBox content = new VBox(22, badge, title, subtitle, difficultyCards);
        content.setAlignment(Pos.CENTER);
        content.setPadding(new Insets(36));

        return SceneStyler.createScene(content, 900, 650);
    }

    private static VBox createDifficultyCard(
        String levelName,
        String description,
        Difficulty difficulty,
        Runnable goToPokemonScene
    ) {
        Label levelLabel = SceneStyler.createSectionLabel(levelName);
        Label descriptionLabel = SceneStyler.createBodyLabel(description);
        Button chooseButton = new Button("Play " + levelName);
        SceneStyler.stylePrimaryButton(chooseButton);
        chooseButton.setMaxWidth(Double.MAX_VALUE);
        chooseButton.setOnAction(e -> {
            selectedDifficulty = difficulty;
            goToPokemonScene.run();
        });

        VBox card = SceneStyler.createCard(Pos.TOP_LEFT, 14, levelLabel, descriptionLabel, chooseButton);
        card.setPrefWidth(220);
        card.setMinHeight(220);
        return card;
    }
}

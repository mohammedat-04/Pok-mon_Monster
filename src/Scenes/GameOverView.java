package Scenes;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class GameOverView {
    private Stage stage;
    private boolean playerWon;

    public GameOverView(Stage stage, boolean playerWon) {
        this.stage = stage;
        this.playerWon = playerWon;
    }

    public Scene createScene() {
        String victoryPath = System.getProperty("user.dir") + "/Images/victory.png";
        String gameOverPath = System.getProperty("user.dir") + "/Images/gameover.png";
        String imagePath = playerWon ? victoryPath : gameOverPath;
        Color accentColor = playerWon ? Color.web("#f7b733") : Color.web("#f87171");
        Color accentSoft = playerWon ? Color.web("#fde68a") : Color.web("#fca5a5");

        ImageView backgroundView = new ImageView(new Image("file:" + imagePath));
        backgroundView.setPreserveRatio(false);
        backgroundView.setSmooth(true);

        Region overlay = new Region();
        overlay.setStyle(
            "-fx-background-color: linear-gradient(to bottom, rgba(4, 12, 24, 0.30), rgba(4, 12, 24, 0.82));"
        );

        Label badge = new Label(playerWon ? "VICTORY" : "DEFEAT");
        badge.setFont(Font.font("Trebuchet MS", FontWeight.EXTRA_BOLD, 13));
        badge.setTextFill(accentSoft);
        badge.setStyle(
            "-fx-background-color: rgba(255,255,255,0.08);" +
            "-fx-background-radius: 999;" +
            "-fx-border-color: " + toRgba(accentColor, 0.42) + ";" +
            "-fx-border-radius: 999;" +
            "-fx-padding: 7 15;"
        );

        Label title = SceneStyler.createTitle(playerWon ? "You Won the Match" : "You Lost the Match");
        title.setFont(Font.font("Verdana", FontWeight.EXTRA_BOLD, 34));
        title.setTextFill(accentSoft);

        Label subtitle = SceneStyler.createSubtitle(
            playerWon
                ? "Your team held the line. Jump back in or switch the difficulty for a fresh challenge."
                : "Reset the matchup, rebuild your team, and try a stronger route."
        );
        subtitle.setMaxWidth(420);

        ImageView resultImage = new ImageView(new Image("file:" + imagePath));
        resultImage.setPreserveRatio(true);
        resultImage.setFitWidth(330);
        resultImage.setFitHeight(240);

        StackPane imageFrame = new StackPane(resultImage);
        imageFrame.setPadding(new Insets(14));
        imageFrame.setStyle(
            "-fx-background-color: linear-gradient(to bottom, rgba(255,255,255,0.10), rgba(255,255,255,0.04));" +
            "-fx-background-radius: 26;" +
            "-fx-border-color: " + toRgba(accentColor, 0.38) + ";" +
            "-fx-border-width: 2;" +
            "-fx-border-radius: 26;" +
            "-fx-effect: dropshadow(gaussian, " + toRgba(accentColor, 0.24) + ", 26, 0.16, 0, 10);"
        );

        Label statTitle = SceneStyler.createSectionLabel(playerWon ? "Trainer Status" : "Battle Status");
        statTitle.setTextFill(Color.WHITE);
        Label statLine1 = SceneStyler.createBodyLabel(playerWon ? "Result: clean finish" : "Result: team knocked out");
        Label statLine2 = SceneStyler.createBodyLabel(playerWon ? "Momentum: high" : "Next step: rebuild and retry");

        VBox statsCard = new VBox(8, statTitle, statLine1, statLine2);
        statsCard.setAlignment(Pos.CENTER_LEFT);
        statsCard.setPadding(new Insets(18));
        statsCard.setMaxWidth(360);
        statsCard.setStyle(
            "-fx-background-color: rgba(255,255,255,0.08);" +
            "-fx-background-radius: 22;" +
            "-fx-border-color: rgba(255,255,255,0.10);" +
            "-fx-border-radius: 22;"
        );

        Button playAgainButton = new Button("Play Again");
        SceneStyler.stylePrimaryButton(playAgainButton);
        playAgainButton.setOnAction(e -> stage.setScene(PokemonScene.createScene(stage)));

        Button backToDifficulty = new Button("Back to Difficulty Select");
        SceneStyler.styleSecondaryButton(backToDifficulty);
        backToDifficulty.setOnAction(e -> stage.setScene(DifficultyScene.createScene(stage)));

        HBox buttonRow = new HBox(12, playAgainButton, backToDifficulty);
        buttonRow.setAlignment(Pos.CENTER);

        VBox panel = new VBox(18, badge, title, subtitle, imageFrame, statsCard, buttonRow);
        panel.setAlignment(Pos.CENTER);
        panel.setPadding(new Insets(34));
        panel.setMaxWidth(520);
        panel.setStyle(
            "-fx-background-color: linear-gradient(to bottom right, rgba(12, 24, 42, 0.94), rgba(26, 34, 52, 0.90));" +
            "-fx-background-radius: 30;" +
            "-fx-border-color: " + toRgba(accentColor, 0.28) + ";" +
            "-fx-border-width: 2;" +
            "-fx-border-radius: 30;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.42), 34, 0.18, 0, 16);"
        );

        Region glow = new Region();
        glow.setStyle(
            "-fx-background-color: radial-gradient(center 50% 50%, radius 62%, " +
                toRgba(accentColor, 0.24) + " 0%, rgba(255,255,255,0.0) 72%);"
        );

        StackPane root = new StackPane(backgroundView, overlay, glow, panel);
        Scene scene = new Scene(root, 980, 700);
        backgroundView.fitWidthProperty().bind(scene.widthProperty());
        backgroundView.fitHeightProperty().bind(scene.heightProperty());
        overlay.prefWidthProperty().bind(scene.widthProperty());
        overlay.prefHeightProperty().bind(scene.heightProperty());
        glow.prefWidthProperty().bind(scene.widthProperty());
        glow.prefHeightProperty().bind(scene.heightProperty());

        return scene;
    }

    private String toRgba(Color color, double opacity) {
        int red = (int) Math.round(color.getRed() * 255);
        int green = (int) Math.round(color.getGreen() * 255);
        int blue = (int) Math.round(color.getBlue() * 255);
        return "rgba(" + red + "," + green + "," + blue + "," + opacity + ")";
    }

}

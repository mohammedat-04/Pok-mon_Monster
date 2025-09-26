package Scenes;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import Scenes.DifficultyScene.Difficulty;
import javafx.application.Application;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.paint.Color;
import javafx.animation.PauseTransition;
import javafx.util.Duration;


import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.geometry.Insets;


public class DifficultyScene {

    public enum Difficulty {
        EASY, MEDIUM, HARD
    }

    private static Difficulty selectedDifficulty;

    public static Difficulty getSelectedDifficulty() {
        return selectedDifficulty;
    }

   public static Scene createScene(Stage primaryStage) {
        Label title = new Label("Choose Difficulty Level");
        title.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, 28));
        title.setTextFill(Color.DARKORANGE);

        Button easyButton = new Button("Easy");
        Button mediumButton = new Button("Medium");
        Button hardButton = new Button("Hard");

        Runnable goToPokemonScene = () -> {
            Label loadingLabel = new Label("Loading...");
            VBox loadingLayout = new VBox(loadingLabel);
            loadingLayout.setAlignment(Pos.CENTER);
            Scene loadingScene = new Scene(loadingLayout, 800, 600);
            primaryStage.setScene(loadingScene);

            PauseTransition pause = new PauseTransition(Duration.seconds(2));
            pause.setOnFinished(ev -> primaryStage.setScene(PokemonScene.createScene(primaryStage)));
            pause.play();
        };

        easyButton.setOnAction(e -> {
            selectedDifficulty = Difficulty.EASY;
            goToPokemonScene.run();
        });

        mediumButton.setOnAction(e -> {
            selectedDifficulty = Difficulty.MEDIUM;
            goToPokemonScene.run();
        });

        hardButton.setOnAction(e -> {
            selectedDifficulty = Difficulty.HARD;
            goToPokemonScene.run();
        });
        VBox content = new VBox(15, title, easyButton, mediumButton, hardButton);
        content.setAlignment(Pos.CENTER);
        content.setPadding(new Insets(40));
        content.setStyle("-fx-background-color: rgba(255,255,255,0.6); -fx-background-radius: 10;");
        String path = System.getProperty("user.dir") + "/Images/background.png";
        Image backgroundImage = new Image("file:"+path);
        ImageView backgroundView = new ImageView(backgroundImage);
        backgroundView.setPreserveRatio(false);
        backgroundView.setFitWidth(800);
        backgroundView.setFitHeight(600);
        StackPane root = new StackPane();
        root.getChildren().addAll(backgroundView, content);
         Scene scene = new Scene(root, 800, 600);
        backgroundView.fitWidthProperty().bind(scene.widthProperty());
        backgroundView.fitHeightProperty().bind(scene.heightProperty());


        return scene;  
    }


       

        

}

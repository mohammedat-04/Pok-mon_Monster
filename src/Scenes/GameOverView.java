package Scenes;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import java.util.Stack;

public class GameOverView {
    private Stage stage;
    private boolean playerWon;

    public GameOverView(Stage stage, boolean playerWon) {
        this.stage = stage;
        this.playerWon = playerWon;
    }

    public Scene createScene() {
    // Hintergrundbild laden
    String path = System.getProperty("user.dir") + "/Images/victory.png";
    
    String path2 = System.getProperty("user.dir") + "/Images/gameover.png";
    
    String imagePath = playerWon
        ? path
        : path2;
    Image backgroundImage = new Image("file:"+imagePath);
    ImageView backgroundView = new ImageView(backgroundImage);
    backgroundView.setPreserveRatio(false);

    // Button erstellen
    Button backToDifficulty = new Button("Zurück zur Schwierigkeitsauswahl");
    backToDifficulty.setPrefWidth(250);
    backToDifficulty.setPrefHeight(40);
    backToDifficulty.setStyle("-fx-font-size: 16px;");
    backToDifficulty.setOnAction(e -> {
        DifficultyScene difficultyScene = new DifficultyScene();
        stage.setScene(difficultyScene.createScene(stage));
    });

    // Unten zentrieren
    HBox bottomBox = new HBox(backToDifficulty);
    bottomBox.setAlignment(Pos.CENTER);
    bottomBox.setPadding(new javafx.geometry.Insets(20));

    // BorderPane für das Layout mit Button unten
    BorderPane layout = new BorderPane();
    layout.setBottom(bottomBox);

    // StackPane: Hintergrund + Layout
    StackPane root = new StackPane();
    root.getChildren().addAll(backgroundView, layout);

    // Szene
    Scene scene = new Scene(root, 600, 400);

    // Hintergrundbild skalieren
    backgroundView.fitWidthProperty().bind(scene.widthProperty());
    backgroundView.fitHeightProperty().bind(scene.heightProperty());

    return scene;
}

}

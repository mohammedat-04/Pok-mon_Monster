
package Scenes;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import Pokemon.Pokemon;
import java.util.List;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.paint.Color;

import Battle.Battle;
import Players.KI.*;
import Scenes.BattleView;
import Pokemon.*;
import java.util.logging.Handler;
import java.util.ArrayList;

public class BattlePrepScene {

    public static Scene create(Stage stage, List<Pokemon> selectedPokemons) {
        BorderPane root = new BorderPane();
        
        // Set background image
        String path = System.getProperty("user.dir") + "/Images/background.png";
        Image bgImage = new Image("file:"+path);
        BackgroundImage backgroundImage = new BackgroundImage(
            bgImage,
            BackgroundRepeat.NO_REPEAT,
            BackgroundRepeat.NO_REPEAT,
            BackgroundPosition.CENTER,
            new BackgroundSize(
                BackgroundSize.AUTO,
                BackgroundSize.AUTO,
                false,
                false,
                true,
                true
            )
        );
        root.setBackground(new Background(backgroundImage));
        root.setPadding(new Insets(20));
        

        // Top: Display selected Pokémon
        HBox topBox = new HBox(20);
        topBox.setPadding(new Insets(20));
        topBox.setAlignment(Pos.CENTER);

        for (Pokemon p : selectedPokemons) {
            VBox pokeBox = new VBox(5);
            pokeBox.setAlignment(Pos.CENTER);

            // Load image
            String imagePath = "file:Images/" + p.getClass().getSimpleName() + ".png";
            ImageView imgView = new ImageView(new Image(imagePath));
            imgView.setFitWidth(100);
            imgView.setFitHeight(120);

            Label nameLabel = new Label(p.getClass().getSimpleName());

            pokeBox.getChildren().addAll(imgView, nameLabel);
            topBox.getChildren().add(pokeBox);
        }

        
        VBox topSection = new VBox(30);
        topSection.setAlignment(Pos.CENTER);
        topSection.setPadding(new Insets(20, 0, 0, 0));
        topSection.getChildren().add(topBox);


        // Center: Start Fight button
        Button startFightButton = new Button("Start Fight");
       startFightButton.setOnAction(e -> {
    System.out.println("Fight started!");
        DifficultyScene.Difficulty def = DifficultyScene.getSelectedDifficulty();
        List<Pokemon> meinTeam = selectedPokemons;
        if(meinTeam.isEmpty())System.out.println("emptyyyyyyyyyys");
        BotManager mBot = new BotManager();
        mBot.teamWahl();
    
          // Nur wenn du damit umgehen kannst
        Bot bot = mBot.chooseBot(def);
        Battle battle = new Battle(meinTeam, bot);

        BattleView battleView = new BattleView(battle);
        Scene kampfSzene = battleView.createScene(stage);  
        stage.setScene(kampfSzene);                       
        stage.setTitle("Pokémon Kampf"); 
    });
        BorderPane.setAlignment(startFightButton, Pos.CENTER);
        
        startFightButton.setPrefWidth(180);
        startFightButton.setPrefHeight(60);

    // Left: "Meine Pokémon" button
    Button myPokemonBtn = new Button("My Pokemons");
    myPokemonBtn.setOnAction(e -> {
    Stage popupStage = new Stage();
    VBox popupRoot = new VBox(10);  // renamed from root to avoid variable conflict
    popupRoot.setAlignment(Pos.CENTER);
    popupRoot.setPadding(new Insets(10));
    
    HBox headerBox = new HBox(100);  // spacing based on your image width
    headerBox.setAlignment(Pos.CENTER);
    Label baseLabel = new Label("Normal Form");
    baseLabel.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, 10));
    baseLabel.setTextFill(Color.DARKORANGE);
    Label evolvedLabel = new Label("Evolved Form");
    evolvedLabel.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, 10));
    evolvedLabel.setTextFill(Color.DARKORANGE);
    headerBox.getChildren().addAll(baseLabel, evolvedLabel);
    popupRoot.getChildren().add(headerBox);

    for (Pokemon p : selectedPokemons) {
        HBox pokeBox = new HBox(5);
        pokeBox.setAlignment(Pos.CENTER);

        String baseName = p.getClass().getSimpleName();
        String basePath = "file:Images/" + baseName + ".png";
        ImageView baseImage = new ImageView(new Image(basePath));
        baseImage.setFitWidth(120);
        baseImage.setPreserveRatio(true);

        String evolvedName = switch (baseName) {
            case "Bisasam" -> "Bisaflor";
            case "Glumanda" -> "Glurak";
            case "Schiggy" -> "Turtok";
            case "Evoli" -> "Nachtara";
            case "Nebulak" -> "Gengar";
            case "Voltoball" -> "Lektroball";
            default -> baseName;
        };
        String evolvedPath = "file:Images/" + evolvedName + ".png";
        ImageView evolvedImage = new ImageView(new Image(evolvedPath));
        evolvedImage.setFitWidth(120);
        evolvedImage.setPreserveRatio(true);

        Label infoLabel = new Label(baseName + " - HP: " + p.getHp());

        pokeBox.getChildren().addAll(baseImage, evolvedImage, infoLabel);
        popupRoot.getChildren().add(pokeBox);
    }

    Scene popupScene = new Scene(popupRoot, 400, 500);
    popupStage.setScene(popupScene);
    popupStage.setTitle("Your Pokémon Team");
    popupStage.show();
});

        myPokemonBtn.setPrefWidth(150);
        myPokemonBtn.setPrefHeight(50);

        HBox buttonBox = new HBox(40);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(myPokemonBtn, startFightButton);

        topSection.getChildren().add(buttonBox);
        root.setTop(topSection);

        VBox leftPane = new VBox(myPokemonBtn);
        leftPane.setAlignment(Pos.CENTER_LEFT);
        leftPane.setPadding(new Insets(20));
         root.setLeft(leftPane);

         return new Scene(root, 800, 600);
    }
}

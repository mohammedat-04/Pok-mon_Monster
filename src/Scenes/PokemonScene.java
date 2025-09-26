package Scenes;

import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.paint.Color;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;

import Pokemon.*;

import java.util.List;
import javafx.scene.control.Label; 
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class PokemonScene {

    public static Scene createScene(Stage stage) {
        List<Pokemon> selectedPokemons = new ArrayList<>();
        Set<String> selected = new HashSet<>();

        Label title = new Label("CHOOSE YOUR POKÉMON");
        title.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, 28));
        title.setTextFill(Color.DARKORANGE);

        VBox glumandaBox = createPokemonBox("Glumanda", new Glumanda(), selectedPokemons, selected, stage);
        VBox bisasamBox = createPokemonBox("Bisasam", new Bisasam(), selectedPokemons, selected, stage);
        VBox evoliBox = createPokemonBox("Evoli", new Evoli(), selectedPokemons, selected, stage);
        VBox schiggyBox = createPokemonBox("Schiggy", new Schiggy(), selectedPokemons, selected, stage);
        VBox lektroballBox = createPokemonBox("Lektroball", new Lektroball(), selectedPokemons, selected, stage);
        VBox nebulaBox = createPokemonBox("Nebulak", new Nebulak(), selectedPokemons, selected, stage);

        GridPane pokemonGrid = new GridPane();
        pokemonGrid.setAlignment(Pos.CENTER);
        pokemonGrid.setHgap(20);
        pokemonGrid.setVgap(20);
        pokemonGrid.setPadding(new Insets(20));

        VBox[] boxes = {glumandaBox, bisasamBox, evoliBox, schiggyBox, lektroballBox, nebulaBox};
        for (int i = 0; i < boxes.length; i++) {
            pokemonGrid.add(boxes[i], i % 3, i / 3);
        }

         VBox contentBox = new VBox(30, title, pokemonGrid);
        contentBox.setAlignment(Pos.TOP_CENTER);
        contentBox.setPadding(new Insets(40));
        contentBox.setStyle("-fx-background-color: rgba(255,255,255,0.6); -fx-background-radius: 10;");
        String path = System.getProperty("user.dir") + "/Images/background.png";
        Image bgImage = new Image("file:"+path);
        ImageView backgroundView = new ImageView(bgImage);
        backgroundView.setPreserveRatio(false);
        
        StackPane rootPane = new StackPane(backgroundView, contentBox);

        Scene scene = new Scene(rootPane, 1000, 600);
        backgroundView.fitWidthProperty().bind(scene.widthProperty());
        backgroundView.fitHeightProperty().bind(scene.heightProperty());

        return scene;
    }

    private static VBox createPokemonBox(String name, Pokemon pokemonInstance, List<Pokemon> selectedPokemons, Set<String> selected, Stage stage) {
        ImageView imageView = new ImageView(new Image("file:Images/" + name.toLowerCase() + ".png"));
        imageView.setFitWidth(150);
        imageView.setFitHeight(200);
        imageView.setMouseTransparent(true);

       // Info button on image
         Button infoButton = new Button("ℹ");
        infoButton.setOnAction(e -> showInfoPopup(pokemonInstance));
        infoButton.setStyle(
            "-fx-background-radius: 50em;" +
            "-fx-min-width: 30px;" +
            "-fx-min-height: 30px;" +
            "-fx-font-size: 16px;" +
            "-fx-text-fill: white;" +
            "-fx-background-color: #34495e;" +
            "-fx-border-color: white;" +
            "-fx-border-width: 2px;" +
            "-fx-border-radius: 50em;"
        );

      
        Pane overlay = new Pane(infoButton);
        overlay.setPickOnBounds(false); 

        StackPane.setAlignment(overlay, Pos.TOP_RIGHT);
        StackPane.setMargin(overlay, new Insets(8));

        StackPane imageStack = new StackPane(imageView, overlay);



        Label label = new Label(name);
        label.setFont(Font.font("Comic Sans MS", FontWeight.SEMI_BOLD, 18));
        label.setTextFill(Color.DARKGREEN);

     VBox box = new VBox(5, imageStack, label);
        box.setAlignment(Pos.CENTER);

        box.setOnMouseClicked(e -> {
            if (selectedPokemons.size() < 3 && !selected.contains(name)) {
                selectedPokemons.add(pokemonInstance);
                box.setStyle("-fx-border-color: green; -fx-border-width: 3px;");
                selected.add(name);
            }
            if (selectedPokemons.size() == 3) {
                Scene battleScene = BattlePrepScene.create(stage, selectedPokemons);
                stage.setScene(battleScene);
            }
        });

        return box;
    }
     private static void showInfoPopup(Pokemon pokemon) {
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle("Pokémon Info");
    alert.setHeaderText(pokemon.getName());

    String content =
                    "HP: " + pokemon.getHp() + "\n"
                   + "Elemente: " + pokemon.getElementsLabel() + "\n"
                   + "Schwere Kämpfe: " + pokemon.getSchwereKampfSiege();

    alert.setContentText(content);
    alert.showAndWait();
}

}

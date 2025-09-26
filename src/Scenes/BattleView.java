package Scenes;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.stage.Modality;
import javafx.geometry.*;

import Pokemon.*;
import Pokemon.helper.*;
import Battle.Battle;

import java.util.List;

import javafx.animation.PauseTransition;
import javafx.util.Duration;



public class BattleView {
    private Stage primaryStage;
    private Battle battle;

    private VBox root;

    private TextArea kampfLog;

    private VBox attackenBox;

    private Label spielerInfo;

    private Label gegnerInfo;

    private ImageView spielerImg;

    private ImageView gegnerImg;

    private Button wechselButton;

    private ProgressBar spielerHpBar;

    private ProgressBar gegnerHpBar;


    public BattleView(Battle battle) {
        this.battle = battle;
       
    }

   
    public Scene createScene(Stage primaryStage) {
    this.primaryStage = primaryStage;
    root = new VBox(10);
    
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
    root.setAlignment(Pos.CENTER);

    // Pokemon-Ansicht (oben)
    BorderPane pokemonArea = new BorderPane();
    pokemonArea.setPadding(new Insets(10));

    // Spieler-Box (links)
    VBox spielerBox = new VBox(10);
    spielerImg = new ImageView();
    spielerImg.setFitWidth(150);
    spielerImg.setFitHeight(150);
    spielerInfo = new Label();
    spielerHpBar = new ProgressBar();
    spielerHpBar.setPrefWidth(150);
    spielerHpBar.setStyle("-fx-accent: red;");
    spielerBox.getChildren().addAll(spielerImg, spielerInfo, spielerHpBar);
    spielerBox.setAlignment(Pos.CENTER_LEFT);
    pokemonArea.setLeft(spielerBox);
    BorderPane.setMargin(spielerBox, new Insets(0, 0, 0, 20));

    // Gegner-Box (rechts)
    VBox gegnerBox = new VBox(10);
    gegnerImg = new ImageView();
    gegnerImg.setFitWidth(150);
    gegnerImg.setFitHeight(150);
    gegnerInfo = new Label();
    gegnerHpBar = new ProgressBar();
    gegnerHpBar.setPrefWidth(150);
    gegnerHpBar.setStyle("-fx-accent: red;");
    gegnerBox.getChildren().addAll(gegnerImg, gegnerInfo, gegnerHpBar);
    gegnerBox.setAlignment(Pos.CENTER_RIGHT);
    pokemonArea.setRight(gegnerBox);
    BorderPane.setMargin(gegnerBox, new Insets(0, 20, 0, 0));

    // Kampflog & Aktionsbereich (unten)
    HBox bottomArea = new HBox(20);
    bottomArea.setAlignment(Pos.CENTER);

    kampfLog = new TextArea();
    kampfLog.setEditable(false);
    kampfLog.setWrapText(true);
    kampfLog.setPrefSize(350, 120); // kleiner als vorher für Platz

    attackenBox = new VBox(10);
    attackenBox.setAlignment(Pos.CENTER_RIGHT);

    wechselButton = new Button("Pokémon wechseln");
    wechselButton.setOnAction(e -> zeigeWechselDialog(primaryStage));

    VBox aktionen = new VBox(10, attackenBox, wechselButton);
    aktionen.setAlignment(Pos.CENTER_RIGHT);

    bottomArea.getChildren().addAll(kampfLog, aktionen);

    // Alles zusammensetzen
    root.getChildren().addAll(pokemonArea, bottomArea);

    aktualisiereAnsicht();

    return new Scene(root, 800, 600);
}


    /**
     * Aktualisiert die Ansicht basierend auf dem aktuellen Spielzustand.
     * Aktualisiert Bilder, Texte und Attackenbuttons.
     */
    private void aktualisiereAnsicht() {
        Pokemon spielerPokemon = battle.getPlayerPokemon();
        Pokemon gegnerPokemon = battle.getEnemyPokemon();

        double spielerHpRatio = (double) spielerPokemon.getHp() / spielerPokemon.getMaxHp();
        double gegnerHpRatio = (double) gegnerPokemon.getHp() / gegnerPokemon.getMaxHp();

        spielerHpBar.setProgress(spielerHpRatio);
        gegnerHpBar.setProgress(gegnerHpRatio);


        spielerInfo.setText(spielerPokemon.getName() +spielerPokemon.getElementsLabel()+ " HP: " + spielerPokemon.getHp()+ "/" +spielerPokemon.getMaxHp());
        gegnerInfo.setText(gegnerPokemon.getName() +gegnerPokemon.getElementsLabel()+ " HP: " + gegnerPokemon.getHp()+ "/" + gegnerPokemon.getMaxHp());

        spielerImg.setImage(spielerPokemon.getImage());
        gegnerImg.setImage(gegnerPokemon.getImage());

        attackenBox.getChildren().clear();
        List<Attacke> attacken = spielerPokemon.getVerfügbareAttacken();
        for (Attacke atk : attacken) {
            Button atkBtn = new Button(atk.getName()+" " + (atk.getSchaden()*gegnerPokemon.getMultiplikatorGegen(atk.getElement())));
            atkBtn.setOnAction(e -> fuehreAttackeAus(atk));
            attackenBox.getChildren().add(atkBtn);
        }
    }

    /**
     * Führt den Spielerzug mit der gewählten Attacke aus und aktualisiert die Anzeige.
     *
     * @param attacke Die ausgewählte Attacke des Spielers.
     */
    private void fuehreAttackeAus(Attacke attacke) {
        // Spieler greift an
        String log = battle.playerAttack(attacke);
        kampfLog.appendText(log + "\n");

        // Wenn Pokémon der KI K.O. ist, wird es jetzt schon ersetzt → aktualisieren!
        if (battle.getEnemyPokemon().isFainted()) {
            kampfLog.appendText("Das gegnerische Pokemon wurde besiegt!\n");
        }

        aktualisiereAnsicht(); 

        if (battle.isGameOver()) {
            zeigeGameOverScreen();
            return;
        }
        PauseTransition pause = new PauseTransition(Duration.seconds(0.4));

        pause.setOnFinished(e -> {
            String botLog = battle.KITurn();
            kampfLog.appendText(botLog + "\n");
            aktualisiereAnsicht();

            if (battle.isGameOver()) {
                zeigeGameOverScreen();
            }
        });
        pause.play();
    }

    private void zeigeGameOverScreen() {
            boolean playerWon = battle.getisPlayerWinner();
            GameOverView gameOverView = new GameOverView(primaryStage, playerWon);
            primaryStage.setScene(gameOverView.createScene());
    }

    
    private void zeigeWechselDialog(Stage ownerStage) {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(ownerStage);
        dialog.setTitle("Wähle ein Pokémon");

        HBox box = new HBox(10);
        box.setAlignment(Pos.CENTER);
        List<Pokemon> team = battle.getTeam1();

        for (Pokemon p : team) {
            if (p != battle.getPlayerPokemon() && !p.isFainted()){
                VBox pokeBox = new VBox(5);
                pokeBox.setAlignment(Pos.CENTER);
                ImageView img = new ImageView(p.getImage());
                img.setFitWidth(100);
                img.setFitHeight(100);

                Button btn = new Button(p.getName());
                btn.setOnAction(e -> {
                    battle.playerSwitchPokemon(p);
                    kampfLog.appendText(battle.playerSwitchPokemon(p));
                    dialog.close();
                    aktualisiereAnsicht();
                });

                pokeBox.getChildren().addAll(img, btn);
                box.getChildren().add(pokeBox);
            }
        }

        Scene scene = new Scene(box, 400, 200);
        dialog.setScene(scene);
        dialog.showAndWait();
    }
}

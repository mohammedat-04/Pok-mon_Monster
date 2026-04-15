package Scenes;

import Battle.Battle;
import Pokemon.Pokemon;
import Pokemon.helper.Attacke;
import Pokemon.helper.Element;
import java.util.ArrayList;
import java.util.List;
import javafx.application.Platform;
import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.ParallelTransition;
import javafx.animation.PauseTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.Cursor;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

public class BattleView {
    private Stage primaryStage;
    private final Battle battle;

    private VBox root;
    private VBox kampfLogEntries;
    private ScrollPane kampfLogPane;
    private VBox attackenBox;
    private Label spielerNameLabel;
    private Label spielerInfo;
    private Label spielerHpText;
    private Label gegnerNameLabel;
    private Label gegnerInfo;
    private Label gegnerHpText;
    private ImageView spielerImg;
    private ImageView gegnerImg;
    private Button wechselButton;
    private ProgressBar spielerHpBar;
    private ProgressBar gegnerHpBar;
    private StackPane spielerCard;
    private StackPane gegnerCard;
    private Label spielerDamageLabel;
    private Label gegnerDamageLabel;
    private Region spielerImpactOverlay;
    private Region gegnerImpactOverlay;
    private Region attackTrail;
    private Animation spielerIdleMotion;
    private Animation gegnerIdleMotion;
    private boolean inputLocked;

    public BattleView(Battle battle) {
        this.battle = battle;
    }

    public Scene createScene(Stage primaryStage) {
        this.primaryStage = primaryStage;
        root = new VBox(16);
        root.setPadding(new Insets(18, 22, 18, 22));
        root.setAlignment(Pos.CENTER);
        root.setMaxWidth(Double.MAX_VALUE);
        root.setFillWidth(true);

        Label badge = SceneStyler.createBadge("LIVE BATTLE");
        Label title = SceneStyler.createTitle("Arena Clash");
        title.setFont(Font.font("Verdana", FontWeight.EXTRA_BOLD, 24));

        spielerCard = createPokemonStatusCard(true);
        gegnerCard = createPokemonStatusCard(false);

        Label versus = SceneStyler.createBadge("VS");
        attackTrail = createAttackTrail();
        StackPane battlefield = new StackPane();
        HBox pokemonArea = new HBox(16, spielerCard, versus, gegnerCard);
        pokemonArea.setAlignment(Pos.CENTER);
        battlefield.getChildren().addAll(attackTrail, pokemonArea);

        kampfLogEntries = new VBox(6);
        kampfLogEntries.setFillWidth(true);
        kampfLogEntries.setPadding(new Insets(2));

        kampfLogPane = new ScrollPane(kampfLogEntries);
        kampfLogPane.setFitToWidth(true);
        kampfLogPane.setPannable(true);
        kampfLogPane.setPrefViewportHeight(176);
        kampfLogPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        kampfLogPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        kampfLogPane.setStyle(
            "-fx-background: transparent;" +
            "-fx-background-color: rgba(5, 12, 24, 0.92);" +
            "-fx-background-insets: 0;" +
            "-fx-border-color: rgba(255,255,255,0.12);" +
            "-fx-border-radius: 18;" +
            "-fx-background-radius: 18;"
        );

        Label logTitle = SceneStyler.createSectionLabel("Battle Log");
        VBox logCard = SceneStyler.createCard(Pos.TOP_LEFT, 10, logTitle, kampfLogPane);
        logCard.setPrefWidth(430);
        HBox.setHgrow(logCard, Priority.ALWAYS);

        attackenBox = new VBox(8);
        attackenBox.setAlignment(Pos.TOP_CENTER);
        attackenBox.setFillWidth(true);

        wechselButton = new Button("Switch Pokemon");
        SceneStyler.styleSecondaryButton(wechselButton);
        wechselButton.setMaxWidth(Double.MAX_VALUE);
        wechselButton.setOnAction(e -> zeigeWechselDialog(primaryStage));

        Label actionsTitle = SceneStyler.createSectionLabel("Actions");
        VBox aktionen = SceneStyler.createCard(Pos.TOP_LEFT, 10, actionsTitle, attackenBox, wechselButton);
        aktionen.setPrefWidth(310);

        FlowPane bottomArea = new FlowPane();
        bottomArea.setHgap(16);
        bottomArea.setVgap(16);
        bottomArea.setAlignment(Pos.TOP_CENTER);
        bottomArea.getChildren().addAll(logCard, aktionen);

        root.getChildren().addAll(badge, title, battlefield, bottomArea);

        aktualisiereAnsicht();
        startIdleMotion();

        ScrollPane scrollPane = new ScrollPane(root);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setPannable(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setStyle(
            "-fx-background: transparent;" +
            "-fx-background-color: transparent;" +
            "-fx-padding: 0;"
        );

        Scene scene = SceneStyler.createScene(scrollPane, 960, 640);
        configureResponsiveLayout(scene, title, battlefield, bottomArea, logCard, aktionen);
        return scene;
    }

    private StackPane createPokemonStatusCard(boolean isPlayer) {
        Label roleLabel = createSideBadge(isPlayer ? "YOUR ACTIVE" : "RIVAL ACTIVE", isPlayer);
        Color sideColor = getSideColor(isPlayer);

        ImageView imageView = new ImageView();
        imageView.setFitWidth(170);
        imageView.setFitHeight(170);
        imageView.setPreserveRatio(true);

        Label nameLabel = new Label();
        nameLabel.setFont(Font.font("Trebuchet MS", FontWeight.EXTRA_BOLD, 20));
        nameLabel.setTextFill(sideColor.brighter());
        nameLabel.setTextAlignment(TextAlignment.CENTER);
        nameLabel.setAlignment(Pos.CENTER);
        nameLabel.setMaxWidth(220);

        Label infoLabel = new Label();
        infoLabel.setFont(Font.font("Trebuchet MS", FontWeight.EXTRA_BOLD, 19));
        infoLabel.setTextFill(isPlayer ? Color.web("#dbeafe") : Color.web("#fee2e2"));
        infoLabel.setTextAlignment(TextAlignment.CENTER);
        infoLabel.setAlignment(Pos.CENTER);
        infoLabel.setMaxWidth(220);
        infoLabel.setStyle(
            "-fx-background-color: rgba(255,255,255,0.08);" +
            "-fx-background-radius: 999;" +
            "-fx-border-color: " + toRgba(sideColor, 0.28) + ";" +
            "-fx-border-radius: 999;" +
            "-fx-padding: 7 14;"
        );

        Label hpTextLabel = new Label();
        hpTextLabel.setFont(Font.font("Trebuchet MS", FontWeight.EXTRA_BOLD, 15));
        hpTextLabel.setTextFill(Color.web("#f8fafc"));
        hpTextLabel.setTextAlignment(TextAlignment.CENTER);
        hpTextLabel.setAlignment(Pos.CENTER);
        hpTextLabel.setMaxWidth(Double.MAX_VALUE);
        hpTextLabel.setPrefWidth(220);
        hpTextLabel.setPadding(new Insets(7, 14, 7, 14));

        ProgressBar hpBar = new ProgressBar(1);
        hpBar.setPrefWidth(220);

        VBox infoBox = new VBox(5, nameLabel, infoLabel, hpTextLabel);
        infoBox.setAlignment(Pos.CENTER);

        VBox content = SceneStyler.createCard(Pos.CENTER, 10, roleLabel, imageView, infoBox, hpBar);
        content.setPrefWidth(240);
        content.setMinHeight(292);
        content.setStyle(getSideCardStyle(isPlayer));

        Region impactOverlay = new Region();
        impactOverlay.setManaged(false);
        impactOverlay.setMouseTransparent(true);
        impactOverlay.setOpacity(0);
        impactOverlay.setPrefSize(240, 320);
        impactOverlay.setScaleX(0.7);
        impactOverlay.setScaleY(0.7);

        Label damageLabel = new Label();
        damageLabel.setManaged(false);
        damageLabel.setVisible(false);
        damageLabel.setFont(Font.font("Trebuchet MS", FontWeight.EXTRA_BOLD, 20));
        damageLabel.setTextFill(getSideColor(isPlayer));
        damageLabel.setStyle(
            "-fx-background-color: rgba(5, 12, 24, 0.86);" +
            "-fx-background-radius: 999;" +
            "-fx-border-color: " + toRgba(getSideColor(isPlayer), 0.42) + ";" +
            "-fx-border-radius: 999;" +
            "-fx-padding: 8 16;"
        );
        damageLabel.setContentDisplay(ContentDisplay.CENTER);

        StackPane card = new StackPane(content, impactOverlay, damageLabel);
        card.setAlignment(Pos.CENTER);
        card.setPrefWidth(240);
        StackPane.setAlignment(impactOverlay, Pos.CENTER);
        StackPane.setAlignment(damageLabel, Pos.TOP_CENTER);
        StackPane.setMargin(damageLabel, new Insets(18, 0, 0, 0));

        if (isPlayer) {
            spielerImg = imageView;
            spielerNameLabel = nameLabel;
            spielerInfo = infoLabel;
            spielerHpText = hpTextLabel;
            spielerHpBar = hpBar;
            spielerDamageLabel = damageLabel;
            spielerImpactOverlay = impactOverlay;
        } else {
            gegnerImg = imageView;
            gegnerNameLabel = nameLabel;
            gegnerInfo = infoLabel;
            gegnerHpText = hpTextLabel;
            gegnerHpBar = hpBar;
            gegnerDamageLabel = damageLabel;
            gegnerImpactOverlay = impactOverlay;
        }

        return card;
    }

    private void aktualisiereAnsicht() {
        renderPokemonCard(true, battle.getPlayerPokemon(), battle.getPlayerPokemon().getHp());
        renderPokemonCard(false, battle.getEnemyPokemon(), battle.getEnemyPokemon().getHp());

        attackenBox.getChildren().clear();
        List<Attacke> attacken = battle.getPlayerPokemon().getVerfügbareAttacken();
        for (Attacke atk : attacken) {
            int predictedDamage = (int) Math.round(
                atk.getSchaden() * battle.getEnemyPokemon().getMultiplikatorGegen(atk.getElement())
            );
            Button atkBtn = new Button(atk.getName() + "  |  " + atk.getElement().getLabel() + "  |  " + predictedDamage);
            styleAttackButton(atkBtn, atk);
            atkBtn.setMaxWidth(Double.MAX_VALUE);
            atkBtn.setDisable(inputLocked);
            atkBtn.setOnAction(e -> fuehreAttackeAus(atk));
            attackenBox.getChildren().add(atkBtn);
        }

        wechselButton.setDisable(inputLocked || getAvailableSwitchCount() == 0);
    }

    private void renderPokemonCard(boolean isPlayer, Pokemon pokemon, int hpToDisplay) {
        ImageView image = isPlayer ? spielerImg : gegnerImg;
        Label nameLabel = isPlayer ? spielerNameLabel : gegnerNameLabel;
        Label info = isPlayer ? spielerInfo : gegnerInfo;
        Label hpText = isPlayer ? spielerHpText : gegnerHpText;
        ProgressBar hpBar = isPlayer ? spielerHpBar : gegnerHpBar;

        image.setImage(pokemon.getImage());
        nameLabel.setText(pokemon.getName());
        info.setText(pokemon.getElementsLabel());

        double hpRatio = getHpRatio(hpToDisplay, pokemon.getMaxHp());
        hpText.setText("HP " + Math.max(0, hpToDisplay) + " / " + (int) Math.round(pokemon.getMaxHp()));
        hpText.setTextFill(getHpTextColor(hpRatio));
        styleHpBand(hpText, hpRatio, isPlayer);
        hpBar.setProgress(hpRatio);
        SceneStyler.styleHpBar(hpBar, hpRatio);
    }

    private double getHpRatio(int hp, double maxHp) {
        if (maxHp <= 0) {
            return 0;
        }
        return Math.max(0, hp) / maxHp;
    }

    private Color getHpTextColor(double hpRatio) {
        if (hpRatio > 0.6) {
            return Color.web("#86efac");
        }
        if (hpRatio > 0.3) {
            return Color.web("#fde68a");
        }
        return Color.web("#fca5a5");
    }

    private void styleHpBand(Label hpText, double hpRatio, boolean isPlayer) {
        Color bandColor = hpRatio > 0.6
            ? Color.web("#10b981")
            : hpRatio > 0.3
                ? Color.web("#f59e0b")
                : Color.web("#ef4444");
        Color edgeColor = isPlayer ? Color.web("#60a5fa") : Color.web("#f87171");
        hpText.setStyle(
            "-fx-background-color: linear-gradient(to right, " +
                toRgba(bandColor, 0.94) + ", " + toRgba(bandColor.deriveColor(0, 1, 0.72, 1), 0.94) + ");" +
            "-fx-background-radius: 999;" +
            "-fx-border-color: " + toRgba(edgeColor, 0.34) + ";" +
            "-fx-border-radius: 999;" +
            "-fx-border-width: 1.5;" +
            "-fx-effect: dropshadow(gaussian, " + toRgba(bandColor, 0.22) + ", 16, 0.12, 0, 4);"
        );
    }

    private String formatTypeText(Pokemon pokemon) {
        List<String> names = new ArrayList<>();
        for (Element element : pokemon.getElemente()) {
            names.add(prettyElementName(element));
        }
        return "TYPE  " + String.join(" / ", names);
    }

    private String prettyElementName(Element element) {
        if (element == null) {
            return "Unknown";
        }
        return switch (element.name()) {
            case "FEUER" -> "Fire";
            case "WASSER" -> "Water";
            case "PFLANZE" -> "Grass";
            case "BODEN" -> "Ground";
            case "KÄFER" -> "Bug";
            case "KAMPF" -> "Fight";
            case "FEE" -> "Fairy";
            case "NORMAL" -> "Normal";
            case "EIS" -> "Ice";
            case "PSYCHO" -> "Psychic";
            case "DRACHE" -> "Dragon";
            case "GIFT" -> "Poison";
            case "LICHT" -> "Light";
            case "UNLICHT" -> "Dark";
            case "ELEKTRO" -> "Electric";
            case "GESTEIN" -> "Rock";
            case "FLUG" -> "Flying";
            case "GEIST" -> "Ghost";
            case "STATUS" -> "Status";
            case "STAHL" -> "Steel";
            default -> element.name();
        };
    }

    private void fuehreAttackeAus(Attacke attacke) {
        if (inputLocked) {
            return;
        }

        inputLocked = true;
        aktualisiereAnsicht();

        Pokemon defenderBefore = battle.getEnemyPokemon();
        int hpBefore = defenderBefore.getHp();
        double ratioBefore = getHpRatio(hpBefore, defenderBefore.getMaxHp());

        String log = battle.playerAttack(attacke);
        int hpAfter = Math.max(0, defenderBefore.getHp());
        double ratioAfter = getHpRatio(hpAfter, defenderBefore.getMaxHp());
        int damage = Math.max(0, hpBefore - hpAfter);
        Color attackColor = getAttackColor(attacke);

        playAttackSequence(
            true,
            attackColor,
            () -> {
                appendLog(log, true);
                showDamageEffect(
                    gegnerCard,
                    gegnerDamageLabel,
                    gegnerImpactOverlay,
                    damage,
                    ratioBefore,
                    ratioAfter,
                    gegnerHpBar,
                    attackColor
                );
            },
            () -> {
                aktualisiereAnsicht();
                if (battle.isGameOver()) {
                    zeigeGameOverScreen();
                    return;
                }

                PauseTransition pause = new PauseTransition(Duration.millis(260));
                pause.setOnFinished(e -> fuehreBotZugAus());
                pause.play();
            }
        );
    }

    private void fuehreBotZugAus() {
        Pokemon enemyShownBefore = battle.getEnemyPokemon();
        Pokemon defenderBefore = battle.getPlayerPokemon();
        int hpBefore = defenderBefore.getHp();
        double ratioBefore = getHpRatio(hpBefore, defenderBefore.getMaxHp());

        String botLog = battle.KITurn();
        Pokemon attackingEnemy = battle.getEnemyPokemon();
        int hpAfter = Math.max(0, defenderBefore.getHp());
        double ratioAfter = getHpRatio(hpAfter, defenderBefore.getMaxHp());
        int damage = Math.max(0, hpBefore - hpAfter);
        Color attackColor = getAttackColor(battle.getLastEnemyAttack());

        if (attackingEnemy != null && attackingEnemy != enemyShownBefore) {
            renderPokemonCard(false, attackingEnemy, attackingEnemy.getHp());
        }

        playAttackSequence(
            false,
            attackColor,
            () -> {
                appendLog(botLog, false);
                showDamageEffect(
                    spielerCard,
                    spielerDamageLabel,
                    spielerImpactOverlay,
                    damage,
                    ratioBefore,
                    ratioAfter,
                    spielerHpBar,
                    attackColor
                );
            },
            () -> {
                aktualisiereAnsicht();
                if (battle.isGameOver()) {
                    zeigeGameOverScreen();
                } else {
                    inputLocked = false;
                    aktualisiereAnsicht();
                }
            }
        );
    }

    private void playAttackSequence(
        boolean playerAttack,
        Color attackColor,
        Runnable onHit,
        Runnable onFinished
    ) {
        stopIdleMotion();

        ImageView attacker = playerAttack ? spielerImg : gegnerImg;
        int direction = playerAttack ? 36 : -36;
        DropShadow attackGlow = new DropShadow(26, attackColor);
        attacker.setEffect(attackGlow);
        playAttackTrail(playerAttack, attackColor);

        TranslateTransition lunge = new TranslateTransition(Duration.millis(180), attacker);
        lunge.setByX(direction);
        lunge.setInterpolator(Interpolator.EASE_BOTH);

        ScaleTransition grow = new ScaleTransition(Duration.millis(180), attacker);
        grow.setToX(1.08);
        grow.setToY(1.08);
        grow.setInterpolator(Interpolator.EASE_BOTH);

        ParallelTransition windup = new ParallelTransition(lunge, grow);

        PauseTransition hitMoment = new PauseTransition(Duration.millis(30));
        hitMoment.setOnFinished(e -> onHit.run());

        TranslateTransition retreat = new TranslateTransition(Duration.millis(180), attacker);
        retreat.setToX(0);
        retreat.setInterpolator(Interpolator.EASE_BOTH);

        ScaleTransition settle = new ScaleTransition(Duration.millis(180), attacker);
        settle.setToX(1);
        settle.setToY(1);
        settle.setInterpolator(Interpolator.EASE_BOTH);

        PauseTransition holdAfterHit = new PauseTransition(Duration.millis(420));

        SequentialTransition sequence = new SequentialTransition(
            windup,
            hitMoment,
            new ParallelTransition(retreat, settle),
            holdAfterHit
        );
        sequence.setOnFinished(e -> {
            attacker.setTranslateX(0);
            attacker.setScaleX(1);
            attacker.setScaleY(1);
            attacker.setEffect(null);
            startIdleMotion();
            onFinished.run();
        });
        sequence.play();
    }

    private void showDamageEffect(
        StackPane targetCard,
        Label damageLabel,
        Region impactOverlay,
        int damage,
        double ratioBefore,
        double ratioAfter,
        ProgressBar hpBar,
        Color attackColor
    ) {
        String labelText = damage > 0 ? "-" + damage : "No Damage";
        damageLabel.setText(labelText);
        damageLabel.setTextFill(damage > 0 ? attackColor.brighter() : Color.web("#e5edf8"));
        damageLabel.setOpacity(1);
        damageLabel.setTranslateY(22);
        damageLabel.setVisible(true);
        damageLabel.setStyle(
            "-fx-background-color: rgba(5, 12, 24, 0.86);" +
            "-fx-background-radius: 999;" +
            "-fx-border-color: " + toRgba(attackColor, 0.42) + ";" +
            "-fx-border-radius: 999;" +
            "-fx-padding: 8 16;"
        );

        Timeline hpTimeline = new Timeline(
            new KeyFrame(Duration.ZERO, new KeyValue(hpBar.progressProperty(), ratioBefore)),
            new KeyFrame(
                Duration.millis(300),
                new KeyValue(hpBar.progressProperty(), ratioAfter, Interpolator.EASE_BOTH)
            )
        );
        SceneStyler.styleHpBar(hpBar, ratioAfter);
        styleImpactOverlay(impactOverlay, attackColor);

        TranslateTransition shake = new TranslateTransition(Duration.millis(55), targetCard);
        shake.setByX(10);
        shake.setCycleCount(6);
        shake.setAutoReverse(true);

        ScaleTransition impact = new ScaleTransition(Duration.millis(90), targetCard);
        impact.setToX(0.97);
        impact.setToY(0.97);
        impact.setCycleCount(2);
        impact.setAutoReverse(true);

        FadeTransition fade = new FadeTransition(Duration.millis(460), damageLabel);
        fade.setFromValue(1);
        fade.setToValue(0);

        TranslateTransition floatUp = new TranslateTransition(Duration.millis(460), damageLabel);
        floatUp.setFromY(22);
        floatUp.setToY(-16);

        FadeTransition overlayFade = new FadeTransition(Duration.millis(320), impactOverlay);
        overlayFade.setFromValue(0.7);
        overlayFade.setToValue(0);

        ScaleTransition overlayScale = new ScaleTransition(Duration.millis(320), impactOverlay);
        overlayScale.setFromX(0.72);
        overlayScale.setFromY(0.72);
        overlayScale.setToX(1.28);
        overlayScale.setToY(1.28);
        overlayScale.setInterpolator(Interpolator.EASE_OUT);

        ParallelTransition combined = new ParallelTransition(
            hpTimeline,
            shake,
            impact,
            fade,
            floatUp,
            overlayFade,
            overlayScale
        );
        combined.setOnFinished(e -> {
            damageLabel.setVisible(false);
            damageLabel.setOpacity(1);
            damageLabel.setTranslateY(0);
            impactOverlay.setOpacity(0);
            impactOverlay.setScaleX(0.7);
            impactOverlay.setScaleY(0.7);
            targetCard.setTranslateX(0);
            targetCard.setScaleX(1);
            targetCard.setScaleY(1);
        });
        combined.play();
    }

    private void zeigeGameOverScreen() {
        boolean playerWon = battle.getisPlayerWinner();
        GameOverView gameOverView = new GameOverView(primaryStage, playerWon);
        primaryStage.setScene(gameOverView.createScene());
    }

    private void zeigeWechselDialog(Stage ownerStage) {
        if (inputLocked) {
            return;
        }

        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(ownerStage);
        dialog.setTitle("Choose a Pokemon");

        HBox box = new HBox(14);
        box.setAlignment(Pos.CENTER);
        List<Pokemon> team = battle.getTeam1();

        for (Pokemon p : team) {
            if (p != battle.getPlayerPokemon() && !p.isFainted()) {
                VBox pokeBox = new VBox(8);
                pokeBox.setAlignment(Pos.CENTER);
                pokeBox.setPadding(new Insets(16));
                pokeBox.setStyle(SceneStyler.CARD_STYLE);

                ImageView img = new ImageView(p.getImage());
                img.setFitWidth(100);
                img.setFitHeight(100);
                img.setPreserveRatio(true);

                Button btn = new Button(p.getName());
                SceneStyler.stylePrimaryButton(btn);
                btn.setOnAction(e -> {
                    String switchLog = battle.playerSwitchPokemon(p);
                    appendLog(switchLog, true);
                    dialog.close();
                    aktualisiereAnsicht();
                    SceneStyler.playEntrance(spielerCard);
                });

                pokeBox.getChildren().addAll(img, btn);
                box.getChildren().add(pokeBox);
            }
        }

        if (box.getChildren().isEmpty()) {
            box.getChildren().add(SceneStyler.createBodyLabel("All other Pokemon are unable to fight."));
        }

        VBox panel = SceneStyler.createPanel(Pos.CENTER, 16, box);
        dialog.setScene(SceneStyler.createScene(panel, 460, 260));
        dialog.showAndWait();
    }

    private int getAvailableSwitchCount() {
        int available = 0;
        for (Pokemon pokemon : battle.getTeam1()) {
            if (pokemon != battle.getPlayerPokemon() && !pokemon.isFainted()) {
                available++;
            }
        }
        return available;
    }

    private Region createAttackTrail() {
        Region trail = new Region();
        trail.setManaged(false);
        trail.setMouseTransparent(true);
        trail.setVisible(false);
        trail.setPrefSize(140, 14);
        trail.setTranslateY(-10);
        return trail;
    }

    private void playAttackTrail(boolean playerAttack, Color attackColor) {
        if (attackTrail == null) {
            return;
        }

        attackTrail.setStyle(
            "-fx-background-color: linear-gradient(to right, " +
                toRgba(attackColor, playerAttack ? 0.08 : 0.92) + ", " +
                toRgba(attackColor, 0.98) + ", " +
                toRgba(attackColor, playerAttack ? 0.92 : 0.08) + ");" +
            "-fx-background-radius: 999;" +
            "-fx-effect: dropshadow(gaussian, " + toRgba(attackColor, 0.65) + ", 18, 0.3, 0, 0);"
        );
        attackTrail.setOpacity(0.95);
        attackTrail.setScaleX(0.72);
        attackTrail.setScaleY(1);
        attackTrail.setVisible(true);

        Bounds playerBounds = spielerCard.localToScene(spielerCard.getBoundsInLocal());
        Bounds enemyBounds = gegnerCard.localToScene(gegnerCard.getBoundsInLocal());
        double startX = playerAttack ? playerBounds.getMinX() + 120 : enemyBounds.getMinX() - 120;
        double endX = playerAttack ? enemyBounds.getMinX() - 120 : playerBounds.getMinX() + 120;
        double centerSceneX = (playerBounds.getMinX() + enemyBounds.getMaxX()) / 2;

        attackTrail.setTranslateX(startX - centerSceneX);

        Timeline trailTimeline = new Timeline(
            new KeyFrame(Duration.ZERO, new KeyValue(attackTrail.translateXProperty(), startX - centerSceneX)),
            new KeyFrame(
                Duration.millis(210),
                new KeyValue(attackTrail.translateXProperty(), endX - centerSceneX, Interpolator.EASE_BOTH)
            ),
            new KeyFrame(Duration.millis(210), new KeyValue(attackTrail.opacityProperty(), 0.1, Interpolator.EASE_OUT))
        );

        ScaleTransition trailStretch = new ScaleTransition(Duration.millis(210), attackTrail);
        trailStretch.setFromX(0.72);
        trailStretch.setToX(1.18);
        trailStretch.setInterpolator(Interpolator.EASE_OUT);

        ParallelTransition burst = new ParallelTransition(trailTimeline, trailStretch);
        burst.setOnFinished(e -> {
            attackTrail.setVisible(false);
            attackTrail.setOpacity(1);
            attackTrail.setTranslateX(0);
            attackTrail.setScaleX(1);
            attackTrail.setScaleY(1);
        });
        burst.play();
    }

    private Label createSideBadge(String text, boolean isPlayer) {
        Label badge = SceneStyler.createBadge(text);
        Color sideColor = getSideColor(isPlayer);
        badge.setTextFill(sideColor.brighter());
        badge.setStyle(
            "-fx-background-color: " + toRgba(sideColor, 0.18) + ";" +
            "-fx-background-radius: 999;" +
            "-fx-border-color: " + toRgba(sideColor, 0.42) + ";" +
            "-fx-border-radius: 999;" +
            "-fx-text-fill: " + toRgba(sideColor.brighter(), 1) + ";" +
            "-fx-padding: 7 14;"
        );
        return badge;
    }

    private String getSideCardStyle(boolean isPlayer) {
        Color sideColor = getSideColor(isPlayer);
        Color start = isPlayer ? Color.web("#0f2745") : Color.web("#381a23");
        Color end = isPlayer ? Color.web("#17385f") : Color.web("#4a1f26");
        return
            "-fx-background-color: linear-gradient(to bottom, " + toRgba(start, 0.96) + ", " + toRgba(end, 0.92) + ");" +
            "-fx-background-radius: 24;" +
            "-fx-border-color: " + toRgba(sideColor, 0.48) + ";" +
            "-fx-border-width: 2;" +
            "-fx-border-radius: 24;" +
            "-fx-effect: dropshadow(gaussian, " + toRgba(sideColor, 0.24) + ", 28, 0.16, 0, 10);";
    }

    private Color getSideColor(boolean isPlayer) {
        return isPlayer ? Color.web("#60a5fa") : Color.web("#f87171");
    }

    private void styleAttackButton(Button button, Attacke attacke) {
        Color attackColor = getAttackColor(attacke);
        Color darkColor = attackColor.deriveColor(0, 1, 0.62, 1);
        button.setStyle(
            "-fx-background-color: linear-gradient(to right, " + toRgba(attackColor, 1) + ", " + toRgba(darkColor, 1) + ");" +
            "-fx-background-radius: 18;" +
            "-fx-text-fill: white;" +
            "-fx-font-weight: bold;" +
            "-fx-effect: dropshadow(gaussian, " + toRgba(attackColor, 0.28) + ", 20, 0.16, 0, 7);"
        );
        button.setFont(Font.font("Trebuchet MS", FontWeight.BOLD, 13));
        button.setPadding(new Insets(11, 16, 11, 16));
        button.setTextFill(Color.WHITE);
        button.setCursor(Cursor.HAND);
        SceneStyler.addHoverLift(button);
    }

    private Color getAttackColor(Attacke attacke) {
        if (attacke == null || attacke.getElement() == null) {
            return Color.web("#f8fafc");
        }

        String element = attacke.getElement().name();
        return switch (element) {
            case "FEUER" -> Color.web("#fb923c");
            case "WASSER" -> Color.web("#38bdf8");
            case "PFLANZE" -> Color.web("#4ade80");
            case "ELEKTRO" -> Color.web("#facc15");
            case "GEIST" -> Color.web("#a78bfa");
            case "GIFT" -> Color.web("#c084fc");
            case "EIS" -> Color.web("#7dd3fc");
            case "KAMPF" -> Color.web("#f97316");
            case "DRACHE" -> Color.web("#818cf8");
            case "FLUG" -> Color.web("#93c5fd");
            case "GESTEIN" -> Color.web("#d6b36a");
            case "STAHL" -> Color.web("#cbd5e1");
            case "LICHT" -> Color.web("#fde68a");
            case "UNLICHT" -> Color.web("#78716c");
            case "BODEN" -> Color.web("#c08457");
            case "PSYCHO" -> Color.web("#f472b6");
            case "NORMAL" -> Color.web("#d1d5db");
            case "FEE" -> Color.web("#f9a8d4");
            case "STATUS" -> Color.web("#94a3b8");
            case "K\u00c4FER" -> Color.web("#a3e635");
            default -> Color.web("#f8fafc");
        };
    }

    private void styleImpactOverlay(Region overlay, Color attackColor) {
        overlay.setStyle(
            "-fx-background-color: radial-gradient(center 50% 50%, radius 70%, " +
                toRgba(attackColor, 0.95) + " 0%, " +
                toRgba(attackColor, 0.35) + " 45%, " +
                toRgba(attackColor, 0.0) + " 82%);" +
            "-fx-background-radius: 999;" +
            "-fx-border-color: " + toRgba(attackColor.brighter(), 0.5) + ";" +
            "-fx-border-radius: 999;" +
            "-fx-border-width: 2;"
        );
    }

    private String toRgba(Color color, double opacity) {
        int red = (int) Math.round(color.getRed() * 255);
        int green = (int) Math.round(color.getGreen() * 255);
        int blue = (int) Math.round(color.getBlue() * 255);
        return "rgba(" + red + "," + green + "," + blue + "," + opacity + ")";
    }

    private void appendLog(String text, boolean playerContext) {
        if (text == null || text.isBlank()) {
            return;
        }

        String[] lines = text.split("\\R");
        for (String rawLine : lines) {
            String line = rawLine.trim();
            if (line.isEmpty()) {
                continue;
            }

            Label lineLabel = new Label(line);
            lineLabel.setWrapText(true);
            lineLabel.setFont(Font.font("Menlo", FontWeight.SEMI_BOLD, 11));
            lineLabel.setMaxWidth(Double.MAX_VALUE);
            lineLabel.prefWidthProperty().bind(kampfLogPane.widthProperty().subtract(28));
            lineLabel.setTextFill(getLogLineColor(line, playerContext));
            lineLabel.setStyle(
                "-fx-background-color: " + toRgba(getLogLineColor(line, playerContext), 0.12) + ";" +
                "-fx-background-radius: 12;" +
                "-fx-border-color: " + toRgba(getLogLineColor(line, playerContext), 0.22) + ";" +
                "-fx-border-radius: 12;" +
                "-fx-padding: 7 10;"
            );
            kampfLogEntries.getChildren().add(lineLabel);
        }

        Platform.runLater(() -> kampfLogPane.setVvalue(1.0));
    }

    private Color getLogLineColor(String line, boolean playerContext) {
        String lower = line.toLowerCase();

        if (lower.contains("gewonnen") || lower.contains("verloren")) {
            return Color.web("#fde68a");
        }
        if (lower.contains("k.o")) {
            return Color.web("#fda4af");
        }
        if (lower.startsWith("bot ") || lower.startsWith("gegner ")) {
            return Color.web("#fca5a5");
        }
        if (lower.startsWith("du ")) {
            return Color.web("#93c5fd");
        }
        if (lower.contains("schaden")) {
            return playerContext ? Color.web("#86efac") : Color.web("#fca5a5");
        }
        return playerContext ? Color.web("#dbeafe") : Color.web("#fee2e2");
    }

    private void configureResponsiveLayout(
        Scene scene,
        Label title,
        StackPane battlefield,
        FlowPane bottomArea,
        VBox logCard,
        VBox actionsCard
    ) {
        Runnable updateLayout = () -> {
            double width = scene.getWidth();
            double height = scene.getHeight();

            root.setMaxWidth(Math.max(420, width - 40));
            bottomArea.setPrefWrapLength(Math.max(420, width - 90));

            double titleSize = width < 860 ? 20 : width < 1080 ? 24 : 28;
            title.setFont(Font.font("Verdana", FontWeight.EXTRA_BOLD, titleSize));

            double cardWidth = width < 860 ? 200 : width < 1080 ? 220 : 240;
            double cardHeight = height < 680 ? 252 : height < 760 ? 276 : 292;
            resizeBattleCard(spielerCard, cardWidth, cardHeight);
            resizeBattleCard(gegnerCard, cardWidth, cardHeight);

            double logWidth = width < 860 ? Math.max(300, width - 110) : width < 1080 ? 410 : 440;
            double actionWidth = width < 860 ? Math.max(300, width - 110) : width < 1080 ? 320 : 330;
            logCard.setPrefWidth(logWidth);
            actionsCard.setPrefWidth(actionWidth);

            double logHeight = height < 660 ? 132 : height < 760 ? 156 : 184;
            kampfLogPane.setPrefViewportHeight(logHeight);

            double imageSize = width < 860 ? 142 : width < 1080 ? 156 : 170;
            spielerImg.setFitWidth(imageSize);
            spielerImg.setFitHeight(imageSize);
            gegnerImg.setFitWidth(imageSize);
            gegnerImg.setFitHeight(imageSize);

            double nameSize = width < 860 ? 17 : width < 1080 ? 18 : 20;
            double metaSize = width < 860 ? 16 : width < 1080 ? 18 : 19;
            double hpSize = width < 860 ? 13 : 15;
            spielerNameLabel.setFont(Font.font("Trebuchet MS", FontWeight.EXTRA_BOLD, nameSize));
            gegnerNameLabel.setFont(Font.font("Trebuchet MS", FontWeight.EXTRA_BOLD, nameSize));
            spielerInfo.setFont(Font.font("Trebuchet MS", FontWeight.EXTRA_BOLD, metaSize));
            gegnerInfo.setFont(Font.font("Trebuchet MS", FontWeight.EXTRA_BOLD, metaSize));
            spielerHpText.setFont(Font.font("Trebuchet MS", FontWeight.EXTRA_BOLD, hpSize));
            gegnerHpText.setFont(Font.font("Trebuchet MS", FontWeight.EXTRA_BOLD, hpSize));

            double estimatedBattleWidth = (cardWidth * 2) + 150;
            double estimatedBattleHeight = cardHeight + 48;
            double battleScale = Math.min(
                1.0,
                Math.min((width - 50) / estimatedBattleWidth, (height - 230) / estimatedBattleHeight)
            );
            battleScale = Math.max(0.82, battleScale);
            battlefield.setScaleX(battleScale);
            battlefield.setScaleY(battleScale);
        };

        scene.widthProperty().addListener((obs, oldVal, newVal) -> updateLayout.run());
        scene.heightProperty().addListener((obs, oldVal, newVal) -> updateLayout.run());
        Platform.runLater(updateLayout);
    }

    private void resizeBattleCard(StackPane card, double width, double height) {
        card.setPrefWidth(width);
        if (card.getChildren().isEmpty()) {
            return;
        }
        if (card.getChildren().get(0) instanceof VBox content) {
            content.setPrefWidth(width);
            content.setMinHeight(height);
        }
    }

    private void startIdleMotion() {
        stopIdleMotion();
        spielerIdleMotion = createIdleTransition(spielerImg, 5);
        gegnerIdleMotion = createIdleTransition(gegnerImg, 7);
        spielerIdleMotion.play();
        gegnerIdleMotion.play();
    }

    private Animation createIdleTransition(ImageView imageView, double amplitude) {
        TranslateTransition idle = new TranslateTransition(Duration.seconds(1.5), imageView);
        idle.setFromY(-amplitude);
        idle.setToY(amplitude);
        idle.setAutoReverse(true);
        idle.setCycleCount(Animation.INDEFINITE);
        idle.setInterpolator(Interpolator.EASE_BOTH);
        return idle;
    }

    private void stopIdleMotion() {
        if (spielerIdleMotion != null) {
            spielerIdleMotion.stop();
        }
        if (gegnerIdleMotion != null) {
            gegnerIdleMotion.stop();
        }
        if (spielerImg != null) {
            spielerImg.setTranslateY(0);
        }
        if (gegnerImg != null) {
            gegnerImg.setTranslateY(0);
        }
    }
}

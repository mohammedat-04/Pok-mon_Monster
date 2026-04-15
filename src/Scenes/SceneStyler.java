package Scenes;

import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextInputControl;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;

public final class SceneStyler {
    public static final String PANEL_STYLE =
        "-fx-background-color: linear-gradient(to bottom right, rgba(13, 28, 48, 0.92), rgba(29, 45, 68, 0.88));" +
        "-fx-background-radius: 28;" +
        "-fx-border-color: rgba(255, 255, 255, 0.14);" +
        "-fx-border-radius: 28;" +
        "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.35), 36, 0.18, 0, 18);";

    public static final String CARD_STYLE =
        "-fx-background-color: linear-gradient(to bottom, rgba(255, 255, 255, 0.14), rgba(255, 255, 255, 0.08));" +
        "-fx-background-radius: 24;" +
        "-fx-border-color: rgba(255, 255, 255, 0.12);" +
        "-fx-border-radius: 24;";

    public static final String SELECTED_CARD_STYLE =
        "-fx-background-color: linear-gradient(to bottom, rgba(249, 194, 70, 0.34), rgba(247, 120, 79, 0.26));" +
        "-fx-background-radius: 24;" +
        "-fx-border-color: rgba(249, 194, 70, 0.92);" +
        "-fx-border-width: 2;" +
        "-fx-border-radius: 24;" +
        "-fx-effect: dropshadow(gaussian, rgba(249, 194, 70, 0.35), 24, 0.2, 0, 8);";

    public static final String PRIMARY_BUTTON_STYLE =
        "-fx-background-color: linear-gradient(to right, #f7b733, #fc4a1a);" +
        "-fx-background-radius: 18;" +
        "-fx-text-fill: white;" +
        "-fx-font-weight: bold;";

    public static final String SECONDARY_BUTTON_STYLE =
        "-fx-background-color: rgba(255, 255, 255, 0.12);" +
        "-fx-background-radius: 18;" +
        "-fx-border-color: rgba(255, 255, 255, 0.18);" +
        "-fx-border-radius: 18;" +
        "-fx-text-fill: #e8eef9;" +
        "-fx-font-weight: bold;";

    public static final String INFO_BUTTON_STYLE =
        "-fx-background-color: rgba(13, 28, 48, 0.88);" +
        "-fx-background-radius: 999;" +
        "-fx-border-color: rgba(255, 255, 255, 0.35);" +
        "-fx-border-width: 1.4;" +
        "-fx-border-radius: 999;" +
        "-fx-text-fill: #f8fafc;" +
        "-fx-font-weight: bold;";

    private static final String FIELD_STYLE =
        "-fx-background-color: rgba(255, 255, 255, 0.14);" +
        "-fx-background-radius: 14;" +
        "-fx-border-color: rgba(255, 255, 255, 0.12);" +
        "-fx-border-radius: 14;" +
        "-fx-text-fill: white;" +
        "-fx-prompt-text-fill: rgba(232, 238, 249, 0.7);" +
        "-fx-highlight-fill: rgba(247, 183, 51, 0.6);" +
        "-fx-padding: 14 16;";

    private static final String BADGE_STYLE =
        "-fx-background-color: rgba(247, 183, 51, 0.16);" +
        "-fx-background-radius: 999;" +
        "-fx-border-color: rgba(247, 183, 51, 0.36);" +
        "-fx-border-radius: 999;" +
        "-fx-text-fill: #f8e08e;" +
        "-fx-padding: 7 14;";

    private static final String LOG_STYLE =
        "-fx-control-inner-background: rgba(5, 12, 24, 0.9);" +
        "-fx-background-color: rgba(5, 12, 24, 0.9);" +
        "-fx-background-insets: 0;" +
        "-fx-background-radius: 20;" +
        "-fx-border-color: rgba(255, 255, 255, 0.12);" +
        "-fx-border-radius: 20;" +
        "-fx-text-fill: #e5edf8;" +
        "-fx-font-family: 'Menlo';" +
        "-fx-font-size: 13px;";

    private SceneStyler() {
    }

    public static Scene createScene(Node content, double width, double height) {
        ImageView backgroundView = createBackgroundView();
        Region overlay = new Region();
        overlay.setStyle(
            "-fx-background-color: linear-gradient(to bottom, rgba(6, 12, 24, 0.48), rgba(6, 12, 24, 0.82));"
        );

        StackPane root = new StackPane(backgroundView, overlay, content);
        StackPane.setAlignment(content, Pos.CENTER);

        Scene scene = new Scene(root, width, height);
        backgroundView.fitWidthProperty().bind(scene.widthProperty());
        backgroundView.fitHeightProperty().bind(scene.heightProperty());
        overlay.prefWidthProperty().bind(scene.widthProperty());
        overlay.prefHeightProperty().bind(scene.heightProperty());

        playEntrance(content);
        return scene;
    }

    public static Scene createLoadingScene(String titleText, String detailText, double width, double height) {
        ProgressIndicator indicator = new ProgressIndicator();
        indicator.setPrefSize(72, 72);

        Label title = createTitle(titleText);
        title.setFont(Font.font("Verdana", FontWeight.EXTRA_BOLD, 28));
        Label detail = createSubtitle(detailText);
        detail.setWrapText(true);
        detail.setMaxWidth(340);

        VBox panel = createPanel(Pos.CENTER, 18, indicator, title, detail);
        panel.setMaxWidth(420);
        return createScene(panel, width, height);
    }

    public static VBox createPanel(Pos alignment, double spacing, Node... children) {
        VBox box = new VBox(spacing, children);
        box.setAlignment(alignment);
        box.setPadding(new Insets(30));
        box.setStyle(PANEL_STYLE);
        box.setMaxWidth(Region.USE_PREF_SIZE);
        return box;
    }

    public static VBox createCard(Pos alignment, double spacing, Node... children) {
        VBox box = new VBox(spacing, children);
        box.setAlignment(alignment);
        box.setPadding(new Insets(18));
        box.setStyle(CARD_STYLE);
        addHoverLift(box);
        return box;
    }

    public static Label createTitle(String text) {
        Label label = new Label(text);
        label.setFont(Font.font("Verdana", FontWeight.EXTRA_BOLD, 34));
        label.setTextFill(Color.web("#f8e08e"));
        label.setWrapText(true);
        label.setTextAlignment(TextAlignment.CENTER);
        return label;
    }

    public static Label createSubtitle(String text) {
        Label label = new Label(text);
        label.setFont(Font.font("Trebuchet MS", FontWeight.NORMAL, 15));
        label.setTextFill(Color.web("#dbe7f7"));
        label.setWrapText(true);
        label.setTextAlignment(TextAlignment.CENTER);
        return label;
    }

    public static Label createSectionLabel(String text) {
        Label label = new Label(text);
        label.setFont(Font.font("Trebuchet MS", FontWeight.BOLD, 18));
        label.setTextFill(Color.WHITE);
        label.setWrapText(true);
        return label;
    }

    public static Label createBodyLabel(String text) {
        Label label = new Label(text);
        label.setFont(Font.font("Trebuchet MS", FontWeight.SEMI_BOLD, 14));
        label.setTextFill(Color.web("#e5edf8"));
        label.setWrapText(true);
        return label;
    }

    public static Label createBadge(String text) {
        Label label = new Label(text);
        label.setFont(Font.font("Trebuchet MS", FontWeight.BOLD, 12));
        label.setTextFill(Color.web("#f8e08e"));
        label.setStyle(BADGE_STYLE);
        return label;
    }

    public static void styleTextField(TextInputControl control, String promptText) {
        control.setPromptText(promptText);
        control.setStyle(FIELD_STYLE);
        control.setFont(Font.font("Trebuchet MS", FontWeight.SEMI_BOLD, 14));
        control.setPrefWidth(280);
    }

    public static void stylePrimaryButton(Button button) {
        button.setStyle(PRIMARY_BUTTON_STYLE);
        button.setFont(Font.font("Trebuchet MS", FontWeight.BOLD, 15));
        button.setPadding(new Insets(12, 20, 12, 20));
        button.setCursor(Cursor.HAND);
        addHoverLift(button);
    }

    public static void styleSecondaryButton(Button button) {
        button.setStyle(SECONDARY_BUTTON_STYLE);
        button.setFont(Font.font("Trebuchet MS", FontWeight.BOLD, 15));
        button.setPadding(new Insets(12, 20, 12, 20));
        button.setCursor(Cursor.HAND);
        addHoverLift(button);
    }

    public static void styleInfoButton(Button button) {
        button.setStyle(INFO_BUTTON_STYLE);
        button.setFont(Font.font("Trebuchet MS", FontWeight.BOLD, 13));
        button.setMinSize(32, 32);
        button.setPrefSize(32, 32);
        button.setCursor(Cursor.HAND);
        addHoverLift(button);
    }

    public static void styleFeedbackLabel(Label label, boolean isError) {
        label.setFont(Font.font("Trebuchet MS", FontWeight.BOLD, 13));
        label.setTextFill(isError ? Color.web("#fda4af") : Color.web("#86efac"));
    }

    public static void styleLog(TextArea textArea) {
        textArea.setStyle(LOG_STYLE);
        textArea.setFont(Font.font("Menlo", FontWeight.NORMAL, 13));
        textArea.setPrefRowCount(10);
    }

    public static void styleHpBar(ProgressBar bar, double ratio) {
        String accent = ratio > 0.6 ? "#34d399" : ratio > 0.3 ? "#fbbf24" : "#f87171";
        bar.setStyle(
            "-fx-accent: " + accent + ";" +
            "-fx-control-inner-background: rgba(255, 255, 255, 0.1);" +
            "-fx-background-radius: 999;"
        );
    }

    public static void setSelectableCardStyle(Region region, boolean selected) {
        region.setStyle(selected ? SELECTED_CARD_STYLE : CARD_STYLE);
    }

    public static ImageView createBackgroundView() {
        String path = System.getProperty("user.dir") + "/Images/background.png";
        ImageView view = new ImageView(new Image("file:" + path));
        view.setPreserveRatio(false);
        view.setSmooth(true);
        return view;
    }

    public static void playEntrance(Node node) {
        FadeTransition fade = new FadeTransition(Duration.millis(450), node);
        fade.setFromValue(0);
        fade.setToValue(1);

        TranslateTransition slide = new TranslateTransition(Duration.millis(450), node);
        slide.setFromY(24);
        slide.setToY(0);
        slide.setInterpolator(Interpolator.EASE_BOTH);

        fade.play();
        slide.play();
    }

    public static void addHoverLift(Node node) {
        node.setEffect(new DropShadow(20, Color.rgb(0, 0, 0, 0.16)));
        node.addEventHandler(MouseEvent.MOUSE_ENTERED, event -> scale(node, 1.03));
        node.addEventHandler(MouseEvent.MOUSE_EXITED, event -> scale(node, 1.0));
    }

    private static void scale(Node node, double scaleTarget) {
        ScaleTransition scale = new ScaleTransition(Duration.millis(120), node);
        scale.setToX(scaleTarget);
        scale.setToY(scaleTarget);
        scale.setInterpolator(Interpolator.EASE_BOTH);
        scale.play();
    }
}

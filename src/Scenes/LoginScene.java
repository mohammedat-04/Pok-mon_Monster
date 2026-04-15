package Scenes;

import Players.Users.User;
import Players.Users.UserManager;
import app.MainMenu;
import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

public class LoginScene {
    public static Scene create(Application app) {
        MainMenu menu = (MainMenu) app;
        Stage stage = menu.getPrimaryStage();
        UserManager userManager = menu.getUserManager();

        Label badge = SceneStyler.createBadge("TRAINER PORTAL");
        Label title = SceneStyler.createTitle("Pocket Monster Arena");
        Label subtitle = SceneStyler.createSubtitle(
            "Create your trainer, log in, and build a team that looks ready for battle."
        );

        Label nameLabel = SceneStyler.createBodyLabel("Trainer Name");
        TextField nameField = new TextField();
        SceneStyler.styleTextField(nameField, "Enter your name");

        Label passLabel = SceneStyler.createBodyLabel("Password");
        PasswordField passField = new PasswordField();
        SceneStyler.styleTextField(passField, "Enter your password");

        Button createBtn = new Button("Create Account");
        Button loginBtn = new Button("Login");
        SceneStyler.styleSecondaryButton(createBtn);
        SceneStyler.stylePrimaryButton(loginBtn);
        createBtn.setPrefWidth(170);
        loginBtn.setPrefWidth(170);

        Label feedback = new Label();
        feedback.setVisible(false);

        VBox fields = new VBox(8, nameLabel, nameField, passLabel, passField);
        fields.setAlignment(Pos.CENTER_LEFT);
        fields.setPadding(new Insets(8, 0, 0, 0));

        HBox buttonBox = new HBox(12, createBtn, loginBtn);
        buttonBox.setAlignment(Pos.CENTER_LEFT);

        VBox content = SceneStyler.createPanel(
            Pos.CENTER_LEFT,
            18,
            badge,
            title,
            subtitle,
            fields,
            buttonBox,
            feedback
        );
        content.setMaxWidth(460);

        createBtn.setOnAction(e -> {
            String name = nameField.getText().trim();
            String pass = passField.getText().trim();

            if (name.isEmpty() || pass.isEmpty()) {
                showFeedback(feedback, "Enter a trainer name and password.", true);
                return;
            }

            if (userManager.getUserByUsername(name) != null) {
                showFeedback(feedback, "That trainer name already exists.", true);
                return;
            }

            stage.setScene(SceneStyler.createLoadingScene("Creating account", "Preparing your trainer profile...", 800, 600));

            PauseTransition delay = new PauseTransition(Duration.seconds(1.2));
            delay.setOnFinished(ev -> {
                User newUser = new User(name, pass);
                userManager.addUser(newUser);
                userManager.speichereAlleUser();
                stage.setScene(LoginScene.create(menu));
            });
            delay.play();
        });

        loginBtn.setOnAction(e -> {
            String name = nameField.getText().trim();
            String pass = passField.getText().trim();
            User user = userManager.getUserByUsername(name);

            if (user == null || !user.getPassword().equals(pass)) {
                showFeedback(feedback, "Invalid login. Check the trainer name and password.", true);
                return;
            }

            stage.setScene(SceneStyler.createLoadingScene("Logging in", "Entering the battle lobby...", 800, 600));

            PauseTransition delay = new PauseTransition(Duration.seconds(1.2));
            delay.setOnFinished(ev -> {
                menu.setCurrentUser(user);
                stage.setScene(DifficultyScene.createScene(stage));
            });
            delay.play();
        });

        return SceneStyler.createScene(content, 800, 600);
    }

    private static void showFeedback(Label feedback, String message, boolean isError) {
        feedback.setVisible(true);
        feedback.setText(message);
        SceneStyler.styleFeedbackLabel(feedback, isError);
    }
}

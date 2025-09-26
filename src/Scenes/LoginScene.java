package Scenes;

import app.MainMenu;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.application.Application;
import javafx.stage.Stage;
import java.util.List;
import javafx.animation.PauseTransition;
import javafx.util.Duration;
import Players.Users.User;
import Players.Users.UserManager;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


public class LoginScene {
    public static Scene create(Application app) {
        MainMenu menu = (MainMenu) app;
        Stage stage = menu.getPrimaryStage();
        UserManager userManager = menu.getUserManager();

        // Title
        Label title = new Label("Welcome to Pocket Monster");
        title.setFont(new Font("Arial", 28));

        // Form labels and fields
        Label nameLabel = new Label("Name:");
        TextField nameField = new TextField();
        Label passLabel = new Label("Password:");
        PasswordField passField = new PasswordField();

        // Buttons
        Button createBtn = new Button("Create new Account");
        Button loginBtn = new Button("login");

        // Feedback label
        Label feedback = new Label();

        // Layout: Grid for form
        GridPane formGrid = new GridPane();
        formGrid.setAlignment(Pos.CENTER);
        formGrid.setVgap(10);
        formGrid.setHgap(10);
        formGrid.add(nameLabel, 0, 0);
        formGrid.add(nameField, 1, 0);
        formGrid.add(passLabel, 0, 1);
        formGrid.add(passField, 1, 1);

        // Layout: Buttons
        HBox buttonBox = new HBox(10, createBtn, loginBtn);
        buttonBox.setAlignment(Pos.CENTER);

        // Root layout
        VBox content = new VBox(20, title, formGrid, buttonBox, feedback);
        content.setStyle("-fx-background-color: rgba(255,255,255,0.6); -fx-background-radius: 10;");
        content.setAlignment(Pos.CENTER);
        content.setPadding(new Insets(40));

        //////
        String path = System.getProperty("user.dir") + "/Images/background.png";
        Image backgroundImage = new Image("file:"+path);
        ImageView backgroundView = new ImageView(backgroundImage);
        backgroundView.setPreserveRatio(false);
        backgroundView.setFitWidth(800);
        backgroundView.setFitHeight(600);


        
        StackPane root = new StackPane();
        root.getChildren().addAll(backgroundView, content);


        // Create Account button logic
       // Create Account button logic
createBtn.setOnAction(e -> {
    String name = nameField.getText().trim();
    String pass = passField.getText().trim();

    if (name.isEmpty() || pass.isEmpty()) {
        feedback.setText("Input Your Name and Password");
        return;
    }

    if (userManager.getUserByUsername(name) != null) {
        feedback.setText("User already exists");
    } else {
        // Show loading scene
        VBox loadingBox = new VBox(new Label("Account is being created..."));
        loadingBox.setAlignment(Pos.CENTER);
        Scene loadingScene = new Scene(loadingBox, 800, 600);
        stage.setScene(loadingScene);

        // Wait 1.5 seconds before creating user and returning to login screen
        PauseTransition delay = new PauseTransition(Duration.seconds(1.5));
       delay.setOnFinished(ev -> {
    User newUser = new User(name, pass);
    userManager.addUser(newUser);
    userManager.speichereAlleUser(); // saves it to disk
    stage.setScene(LoginScene.create(menu));
});

        delay.play();
    }
});

// Login button logic
loginBtn.setOnAction(e -> {
    String name = nameField.getText().trim();
    String pass = passField.getText().trim();
    User user = userManager.getUserByUsername(name);

    if (user == null || !user.getPassword().equals(pass)) {
        feedback.setText("Password Invalid or User doesn't exist");
    } else {
        // Show loading screen
        VBox loadingBox = new VBox(new Label("Logging in..."));
        loadingBox.setAlignment(Pos.CENTER);
        Scene loadingScene = new Scene(loadingBox, 800, 600);
        stage.setScene(loadingScene);

        // Wait 1.5 seconds before proceeding to Pokémon scene
        PauseTransition delay = new PauseTransition(Duration.seconds(1.5));
        delay.setOnFinished(ev -> {
            menu.setCurrentUser(user);
             stage.setScene(DifficultyScene.createScene(stage));
        });
        delay.play();
    }
});


        Scene scene = new Scene(root, 800, 600);
        backgroundView.fitWidthProperty().bind(scene.widthProperty());
        backgroundView.fitHeightProperty().bind(scene.heightProperty());
        return scene;

    }
}

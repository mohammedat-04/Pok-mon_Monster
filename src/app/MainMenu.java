package app;

import Scenes.LoginScene;
import Scenes.PokemonScene;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import Players.Users.User;
import Players.Users.UserManager;
import Pokemon.Glumanda;
import Pokemon.Bisasam;
import Pokemon.Evoli;
import Pokemon.Pokemon;

public class MainMenu extends Application {

    private Stage primaryStage;
    private UserManager userManager = new UserManager();
    private User currentUser;
    private Pokemon chosenPokemon;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        this.primaryStage = stage;
        primaryStage.setScene(LoginScene.create(this));
          primaryStage.show();      
    }
    public Pokemon getChosenPokemon() {
        return chosenPokemon;
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public UserManager getUserManager() {
        return userManager;
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    public void setChosenPokemon(Pokemon pokemon) {
        this.chosenPokemon = pokemon;
    }
}

package controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * MainMenuControl : Controller lié à la vue JavaFX du menu mainMenu.fxml
 * by Nicolas and Clément Jan 2019
 */
public class MainMenuControl {

    @FXML
    private Button buttonNewGame;

    /**
     * Permet de lancer une partie et d'aficher la page de sélection des joueurs
     * @throws IOException Si le fichier de ressource n'a pas été trouvé
     */
    public void launchPlayerPageButton() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/playersPage.fxml"));
        Parent playerPage = loader.load();
        Stage stage = (Stage) buttonNewGame.getScene().getWindow();
        stage.setScene(new Scene(playerPage,600, 600));

        Jeu.getInstance().setPlayersPageControl(loader.getController());
    }

    /***
     * Permet de quitter l'aplication
     */
    public void quitButtonAction(){
        Platform.exit();
    }
}

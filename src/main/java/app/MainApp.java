package app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApp extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    /***
     * Permet de créer les différentes vues
     * @param primaryStage Première vue à être lancée
     * @throws IOException Si le fichier ressource n'a pas été trouvé
     */
    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/mainMenu.fxml"));
        Parent root = loader.load();
        primaryStage.setScene(new Scene(root, 1200,800));
        primaryStage.show();
    }
}

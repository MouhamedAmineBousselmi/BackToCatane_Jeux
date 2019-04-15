package controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.CustomDialogs;
import model.ParamGame;
import view.ColorRectCell;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;

/**
 * PlayersPageControl : Controller lié à la vue JavaFX de création des joueurs playersPage.fxml
 * by Nicolas Jan 2019
 */
public class PlayersPageControl implements Initializable {

    @FXML
    private VBox vbox1PlayerPage; //vbox des textfields
    @FXML
    private VBox vbox2PlayerPage; //vbox des couleurs
    @FXML
    private RadioButton radioButton3; //3 joueurs
    @FXML
    private RadioButton radioButton4; //4 joueurs
    @FXML
    private Button buttonBackPlayerPage;

    /**
     * Méthode qui se lance lors de l'affichage de la vue
     * @param location URL Location
     * @param resources RessourceBundle ressoures
     */
    public void initialize(URL location, ResourceBundle resources) {
        creationPlayerPanel();
        autoCompleteDemo(); // Complète les noms et couleurs des joueurs pour la Démo
    }

    /***
     * Appelle la création des 2 vboxs
     */
    public void creationPlayerPanel(){
        addPlayers();
        addColors();
    }

    /**
     * Méthode qui permet d'ajouter autant de textfields que nécessaire pour le nombre de joueurs
     */
    private void addPlayers(){
        vbox1PlayerPage.getChildren().clear();
        if(radioButton3.isSelected()){
            for (int i = 1; i < 4; i++) {
                final TextField newPlayerField = new TextField();
                testLengthName(i, newPlayerField);
            }
        }
        if(radioButton4.isSelected()){
            for (int i = 1; i < 5; i++) {
                TextField newPlayerField = new TextField();
                testLengthName(i, newPlayerField);
            }
        }
    }

    /**
     * Permet de limiter les nombre de caractères dans les textfields des noms
     * @param i  numéro du joueur
     * @param newPlayerField  Le textfield à limiter
     */
    private void testLengthName(int i, final TextField newPlayerField) {
        newPlayerField.setPromptText("Joueur "+i);
        newPlayerField.lengthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if(newValue.intValue() > oldValue.intValue() && newPlayerField.getText().length() >= ParamGame.MAX_LENGTH_PLAYER_NAME){
                    newPlayerField.setText(newPlayerField.getText().substring(0, ParamGame.MAX_LENGTH_PLAYER_NAME));
                }
            }
        });
        vbox1PlayerPage.getChildren().add(newPlayerField);
    }

    /**
     * Méthode qui permet d'ajouter autant de colorPickers que nécessaire pour le nombre de joueurs
     */
    private void addColors(){
        vbox2PlayerPage.getChildren().clear();
        ObservableList<String> listeCouleurs = FXCollections.observableArrayList(
                "blue", "yellow", "green", "red");
        if(radioButton3.isSelected()){
            for (int i = 0; i < 3; i++) {
                generateComboBoxes(listeCouleurs);
            }
        }
        if(radioButton4.isSelected()){
            for (int i = 0; i < 4; i++) {
                generateComboBoxes(listeCouleurs);
            }
        }
    }

    /**
     * Permet de générer les comboboxs qui comprennent les couleurs.
     * @param listeCouleurs La liste des couleurs à mettrent dans chaque combobox
     */
    private void generateComboBoxes(ObservableList<String> listeCouleurs) {
        ComboBox<String> colorComboBox = new ComboBox<>();
        colorComboBox.setItems(listeCouleurs);
        Callback<ListView<String>, ListCell<String>> factory = new Callback<ListView<String>, ListCell<String>>() {
            @Override
            public ListCell<String> call(ListView<String> list) {
                return new ColorRectCell();
            }
        };
        colorComboBox.setCellFactory(factory);
        colorComboBox.setButtonCell(factory.call(null));
        vbox2PlayerPage.getChildren().add(colorComboBox);
    }

    /**
     * Vérifie si chaque joueur a mis son nom
     * @return faux si au moins un nom n'est pas remplit
     */
    private int verifyEntries(){
        ArrayList<String> listNoms = new ArrayList<>();
        for (Node node: vbox1PlayerPage.getChildren()) {
            if (node instanceof TextField){
                if (((TextField) node).getText().trim().isEmpty()){
                    return 1;
                } else {
                    listNoms.add(((TextField) node).getText());
                }
            }
        }
        for (Node node: vbox2PlayerPage.getChildren()){
            if(node instanceof ComboBox){
                if(((ComboBox) node).getSelectionModel().isEmpty()){
                    return 2;
                }
            }
        }

        ArrayList<String> listeCouleurs = getColors();
        Set<String> setCouleurs = new HashSet<>(listeCouleurs);
        if (setCouleurs.size() < listeCouleurs.size()){
            return 3;
        }

        for (int i=0;i<listNoms.size();i++)
            for (int k=i+1;k<listNoms.size();k++)
                if (k!=i && listNoms.get(k).equals(listNoms.get(i)))
                    return 4;

        return 0;
    }

    /**
     * Méthode de test pour autoCompléter les joueurs
     */
    private void autoCompleteDemo() {
        radioButton4.setSelected(true);
        addColors();

        String[] namesTest = {"Amine", "Clément", "Nicolas", "Tilan"};
        int i = 0;
        for (Node node: vbox1PlayerPage.getChildren()) {
            if (node instanceof TextField){
                ((TextField) node).setText(namesTest[i]);
                i++;
            }
        }
        i = 0;
        for (Node node: vbox2PlayerPage.getChildren()){
            if(node instanceof ComboBox){
                ((ComboBox) node).getSelectionModel().select(i);
                i++;
            }
        }
    }

    /**
     * Affiche un message warning à l'utilisateur
     */
    private void displayWarning(){
        switch (verifyEntries()){
            case 1:
                CustomDialogs.displayWarningPopup("Tous les noms de joueurs doivent être renseigné !",
                        this.vbox1PlayerPage.getScene().getWindow());
                break;
            case 2:
                CustomDialogs.displayWarningPopup("Tous les joueurs doivent sélectionner une couleur !",
                        this.vbox1PlayerPage.getScene().getWindow());
                break;
            case 3:
                CustomDialogs.displayWarningPopup("Tous les joueurs doivent avoir une couleur différente",
                        this.vbox1PlayerPage.getScene().getWindow());
                break;
            case 4:
                CustomDialogs.displayWarningPopup("Tous les joueurs doivent avoir une nom différent !",
                        this.vbox1PlayerPage.getScene().getWindow());
                break;
            default:
                break;
        }
    }

    /**
     * Permet de retourner au menu principal
     * @throws IOException Si le fichier ressource n'a pas été trouvé
     */
    public void backButton() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/mainMenu.fxml"));
        Stage stage = (Stage) buttonBackPlayerPage.getScene().getWindow();
        stage.setScene(new Scene(root,1200, 800));
    }

    /**
     * Permet de lancer une partie et d'aficher la page du jeu
     * @throws IOException Si le fichier ressource n'a pas été trouvé
     */
    public void launchgameview() throws IOException {
        displayWarning();
        if (verifyEntries() == 0) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/mainGame.fxml"));
            Parent gameView = loader.load();

            Jeu.getInstance().setMainGameControl(loader.getController());

            Stage stage = (Stage) vbox1PlayerPage.getScene().getWindow();
            stage.setScene(new Scene(gameView));
            stage.setFullScreen(true);
        }
    }

    ///////// GETTERS //////////

    /**
     * getter du nom des joueurs
     * @return une liste avec tous les noms
     */
    ArrayList<String> getNames(){
        ArrayList<String> listNames = new ArrayList<>();
        for (Node node: vbox1PlayerPage.getChildren()) {
            if (node instanceof TextField){
                listNames.add(((TextField) node).getText());
            }
        }
        return listNames;
    }

    /**
     * getter des couleurs sélectionées par les joueurs
     * @return une liste de String des couleurs
     */
    ArrayList<String> getColors() {
        ArrayList<String> listeCouleurs = new ArrayList<>();
        for (Node node: vbox2PlayerPage.getChildren()){
            if(node instanceof ComboBox){
                listeCouleurs.add(((ComboBox) node).getSelectionModel().getSelectedItem().toString());
            }
        }
        return listeCouleurs;
    }
}

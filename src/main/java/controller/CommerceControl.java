package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import model.CustomDialogs;
import model.Joueur;
import model.TypeRessource;
import model.customExceptions.NotEnoughResourceException;

import java.lang.reflect.Type;
import java.net.URL;
import java.util.*;

/**
 * CommerceControl : Controller lié à la vue JavaFX commerce.fxml
 * by Nicolas and Clément Jan 2019
 */
public class CommerceControl implements Initializable {

    @FXML
    private AnchorPane anchorPaneCommerce;
    @FXML
    private GridPane gridPaneCurrentPlayer;
    @FXML
    private GridPane gridPaneOtherPlayer;
    @FXML
    private Pane rightVBox;
    @FXML
    private Label labWarning;

    private Jeu monJeu = Jeu.getInstance();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        populateChoiceBox();
        createPlayersButtons();
    }

     /**
     * Permet de remplir tout les choiceBoxs de chiffres
     */
    private void populateChoiceBox() {
        Joueur activePlayer = this.monJeu.getCurrentPlayer();
        ArrayList<Integer> listInt;
        int resourceNum = 0;

        // Remplissage des combobox de chaque type de ressource en fonction du joueur courant
        for(Node node: this.gridPaneCurrentPlayer.getChildren()) {
            if (node instanceof ChoiceBox) {
                listInt = new ArrayList<>();
                for (int i = 0; i < activePlayer.getListeRessources().get(TypeRessource.values()[resourceNum]) + 1; i++) {
                    listInt.add(i);
                }
                ((ChoiceBox) node).setItems(FXCollections.observableList(listInt));
                ((ChoiceBox) node).getSelectionModel().selectFirst();
                resourceNum++;
            }
        }

        // Génére une liste de nombre de 0 à 20
        ArrayList<Integer> listNumbersInt = new ArrayList<>();
        for (int i = 0; i <= 20; i++) {
            listNumbersInt.add(i);
        }
        ObservableList listNumbers = FXCollections.observableList(listNumbersInt);

        // Remplissage des combobox de chaque type de ressource du joueur cible de l'échange
        for(Node node: this.gridPaneOtherPlayer.getChildren()){
            if (node instanceof ChoiceBox) {
                ((ChoiceBox) node).setItems(listNumbers);
                ((ChoiceBox) node).getSelectionModel().selectFirst();
            }
        }
    }

    /**
     * Creer un bouton pour chaque autre joueur. Le joueur actif peut choisir n'importe quel autre joueur pour procéder à un échange.
     */
    private void createPlayersButtons(){
        for (Joueur j : Jeu.getListeJoueurs()) {
            if (!j.getId().equals(this.monJeu.getCurrentPlayer().getId())) {
                Button buttonPlayer = new Button(j.getNom());

                buttonPlayer.setTranslateX(30);
                buttonPlayer.setTranslateY(20);

                rightVBox.getChildren().add(buttonPlayer);

                addEventsButton(buttonPlayer);
            }
        }
    }

    /**
     * Récupère le nombre de ressource que le joueur passé en paramètre propose
     * @return EnumMap<TypeRessource, Integer>
     */
    private EnumMap<TypeRessource, Integer> retrieveCommerceProposition(Joueur j) {
        EnumMap<TypeRessource, Integer> listeRessources = new EnumMap<>(TypeRessource.class);
        Integer nbRessources;
        int typeIndex = 0;

        GridPane gp;
        if (j == this.monJeu.getCurrentPlayer()) {
            gp = this.gridPaneCurrentPlayer;
        }
        else {
            gp = this.gridPaneOtherPlayer;
        }

        for(Node node: gp.getChildren()) {
            if (node instanceof ChoiceBox) {
                nbRessources = Integer.parseInt(((ChoiceBox) node).getValue().toString());
                listeRessources.put(TypeRessource.values()[typeIndex], nbRessources);
                typeIndex++;
            }
        }
        return listeRessources;
    }

    /**
     * Ajoute les listeners sur les boutons des joueurs cibles de l'échange
     */
    private void addEventsButton(Button b){
        String playerSelectedName = b.getText();
        Joueur activePlayer = this.monJeu.getCurrentPlayer();
        Joueur otherPlayerToVerify = this.monJeu.findPlayerByName(playerSelectedName);

        b.setOnAction(e -> {
            EnumMap<TypeRessource, Integer> ressCurPlayer = retrieveCommerceProposition(activePlayer);
            EnumMap<TypeRessource, Integer> ressOtherPlayer = retrieveCommerceProposition(otherPlayerToVerify);

            try {
                // Si les joueurs ont assez de ressources
                activePlayer.checkRessource(ressCurPlayer);
                otherPlayerToVerify.checkRessource(ressOtherPlayer);

                //affiche pop-up au joueur concerné avec accepter ou refuser
                Optional<ButtonType> answer = CustomDialogs.displayCommerceConfirmPopUp(ressCurPlayer, ressOtherPlayer,
                        activePlayer.getNom(), otherPlayerToVerify.getNom(),
                        this.anchorPaneCommerce.getScene().getWindow());

                // Si le joueur accepte, on lance l'echange des ressources
                if (answer.isPresent() && answer.get() == ButtonType.OK) {
                    activePlayer.commercer(ressOtherPlayer, otherPlayerToVerify, ressCurPlayer);
                    this.monJeu.updateView();
                    Jeu.getInstance().getMainGameControl().verifyBlockButtonProduction();
                    CustomDialogs.displayInfoPopUp("L'échange s'est bien passé !",
                            this.anchorPaneCommerce.getScene().getWindow());
                    anchorPaneCommerce.getScene().getWindow().hide();
                }
                // Sinon on affiche un message d'erreur
                else {
                    CustomDialogs.displayWarningPopup("L'échange a été annulé !",
                            this.anchorPaneCommerce.getScene().getWindow());
                }
            }
            // Si un des joueurs n'a pas assez de ressource, on affiche un message d'erreur
            catch (NotEnoughResourceException e1) {
                if (e1.getNameJoueur().equals(activePlayer.getNom())) {
                    CustomDialogs.displayWarningPopup("Vous n'avez pas assez de ressources !",
                            this.anchorPaneCommerce.getScene().getWindow());
                }
                else {
                    CustomDialogs.displayWarningPopup(e1.getNameJoueur() + " n'a pas assez de ressources !",
                            this.anchorPaneCommerce.getScene().getWindow());
                }
            }
        });
    }
}

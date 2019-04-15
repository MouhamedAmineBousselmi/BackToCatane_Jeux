package controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import model.ParamGame;
import model.TypeRessource;

import java.net.URL;
import java.util.ResourceBundle;
/**
 * ProductionControl : Controller lié à la vue JavaFX de la production production.fxml
 * by Nicolas Jan 2019
 */
public class ProductionControl implements Initializable {

    @FXML
    private ToggleGroup groupActivePlayer;
    @FXML
    private ToggleGroup groupOtherPlayer;
    @FXML
    private Button buttonOk;
    @FXML
    private Label labelActivePlayer;
    @FXML
    private Label labelOtherPlayer;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setLabel();
        verifySelection();
    }

    /**
     * Vérifie si une ressource est sélectionnée dans un premier groupe et change l'état du bouton OK en fonction
     */
    private void verifySelection(){
        groupActivePlayer.selectedToggleProperty().addListener(new ChangeListener<Toggle>(){
            public void changed(ObservableValue<? extends Toggle> ov, Toggle oldToggle, Toggle newToggle) {

                if (groupActivePlayer.getSelectedToggle() != null) {
                    verifyOtherGroup(1);
                } else {
                    buttonOk.setDisable(true);
                }

            }
        });

        groupOtherPlayer.selectedToggleProperty().addListener(new ChangeListener<Toggle>(){
            public void changed(ObservableValue<? extends Toggle> ov, Toggle oldToggle, Toggle newToggle) {

                if (groupOtherPlayer.getSelectedToggle() != null) {
                    verifyOtherGroup(2);
                } else {
                    buttonOk.setDisable(true);
                }

            }
        });
    }

    /**
     * Vérifie si une ressource est sélectionnée dans l'autre groupe
     * @param groupNumber Groupe ayant déjà été vérifié
     */
    private void verifyOtherGroup(int groupNumber){
        switch (groupNumber){
            case 1:
                if (groupOtherPlayer.getSelectedToggle() != null){
                    buttonOk.setDisable(false);
                }
                break;
            case 2:
                if (groupActivePlayer.getSelectedToggle() != null){
                    buttonOk.setDisable(false);
                }
                break;
        }
    }

    /**
     * Permet d'afficher du texte sur les labels de la fenêtre
     */
    private void setLabel(){
        labelActivePlayer.setText("Vous donnez "+ ParamGame.RESSOURCES_A_ENLEVER_PRODUCTION+" ressources : ");
        labelOtherPlayer.setText("Vous produisez " + ParamGame.RESSOURCE_A_AJOUTER_PRODUCTION + " ressource de base : ");
    }

    /**
     * Procède à la production lorsque le joueur clique sur le bouton ok
     */
    public void actionButtonOk(){
        TypeRessource typeRessourceActivePlayer = null;
        TypeRessource typeRessourceOtherPlayer = null;

        String sActive = groupActivePlayer.getSelectedToggle().toString();
        String[] fields = sActive.split("\\[");
        sActive = fields[1];
        fields = sActive.split("=");
        sActive = fields[1];
        fields = sActive.split(",");
        sActive = fields[0];

        String sOther = groupOtherPlayer.getSelectedToggle().toString();
        String[] fieldsOther = sOther.split("\\[");
        sOther = fieldsOther[1];
        fieldsOther = sOther.split("=");
        sOther = fieldsOther[1];
        fieldsOther = sOther.split(",");
        sOther = fieldsOther[0];

        switch (sActive){
            case "buttonOr1":
                typeRessourceActivePlayer = TypeRessource.OR;
                break;
            case "buttonOil1":
                typeRessourceActivePlayer = TypeRessource.PETROLE;
                break;
            case "buttonBrick1":
                typeRessourceActivePlayer = TypeRessource.BRIQUE;
                break;
            case "buttonPluto1":
                typeRessourceActivePlayer = TypeRessource.PLUTONIUM;
                break;
            case "buttonIron1":
                typeRessourceActivePlayer = TypeRessource.FER;
                break;
            case "buttonAlu1":
                typeRessourceActivePlayer = TypeRessource.ALUMINIUM;
                break;
        }

        switch (sOther){
            case "buttonBrick2":
                typeRessourceOtherPlayer = TypeRessource.BRIQUE;
                break;
            case "buttonIron2":
                typeRessourceOtherPlayer = TypeRessource.FER;
                break;
            case "buttonAlu2":
                typeRessourceOtherPlayer = TypeRessource.ALUMINIUM;
                break;
        }

        Jeu.getInstance().getCurrentPlayer().produire(typeRessourceActivePlayer, typeRessourceOtherPlayer);
        Jeu.getInstance().updateView();
        labelOtherPlayer.getScene().getWindow().hide();
    }

    public Label getLabelActivePlayer() {
        return labelActivePlayer;
    }
}

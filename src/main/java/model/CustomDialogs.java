package model;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Window;

import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;

/**
 * CustomDialogs : Contient les méthodes d'affichage de messages
 * pour l'utilisateur
 * by Clément Jan 2019
 */
public abstract class CustomDialogs {

    /**
     * Permet d'afficher un pop-up d'alerte
     * @param errorTexte Le texte à mettre dans le pop-up
     * @param parentWindow Windows du parent (fenêtre principale)
     */
    public static void displayWarningPopup(String errorTexte, Window parentWindow){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.initOwner(parentWindow);
        alert.setTitle("ATTENTION");
        alert.setHeaderText("Attention");
        alert.setContentText(errorTexte);
        alert.showAndWait();
    }

    /**
     * Affiche un pop-up d'information
     * @param message Message a afficher
     * @param parentWindow Windows du parent (fenêtre principale)
     */
    public static void displayInfoPopUp(String message, Window parentWindow){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.initOwner(parentWindow);
        alert.setTitle("INFORMATION");
        alert.setHeaderText("Information");
        alert.setContentText(message);

        alert.showAndWait();
    }

    /**
     * Affiche le résumé de la récolte des ressources
     * @param summary Liste des changelog des ressources en fonction de chaque joueur
     * @param parentWindow Windows du parent (fenêtre principale)
     */
    public static void displaySummaryRecolte(Map<Joueur, EnumMap<TypeRessource, Integer>> summary, Window parentWindow){
        StringBuilder message = new StringBuilder();
        int nbPlayerWithoutRessource = 0;

        for (Map.Entry<Joueur, EnumMap<TypeRessource, Integer>> entry : summary.entrySet()) {
            if (entry.getValue().size() > 0) {
                message.append("Le joueur " + entry.getKey().getNom() + " a récolté : \n");

                for (Map.Entry<TypeRessource, Integer> entryRessource : entry.getValue().entrySet()) {
                    message.append(" + " + entryRessource.getValue() + " " + entryRessource.getKey() + "\n");
                }
            }
            else {
                nbPlayerWithoutRessource++;
            }
        }

        if (nbPlayerWithoutRessource == summary.size()) {
            message.append("Aucun joueur n'a récolté de ressources ce tour");
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.initOwner(parentWindow);
        alert.setTitle("Résumé récolte ressource");
        alert.setHeaderText("Résumé récolte ressource");
        alert.setContentText(message.toString());

        alert.showAndWait();
    }

    /**
     * Affiche le résumé des ressources enlevées par Biff
     * @param summary Liste des changelog des ressources en fonction de chaque joueur
     * @param parentWindow Windows du parent (fenêtre principale)
     */
    public static void displaySummaryBiff(Map<Joueur, EnumMap<TypeRessource, Integer>> summary, Window parentWindow){
        StringBuilder message = new StringBuilder();

        for (Map.Entry<Joueur, EnumMap<TypeRessource, Integer>> entry : summary.entrySet()) {
            message.append("Le joueur " + entry.getKey().getNom() + " a perdu : \n");

            for (Map.Entry<TypeRessource, Integer> entryRessource : entry.getValue().entrySet()) {
                message.append(" - " + entryRessource.getValue() + " " + entryRessource.getKey() + "\n");
            }
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.initOwner(parentWindow);
        alert.setTitle("Résumé perte ressources BIFF");
        alert.setHeaderText("Résumé perte ressources BIFF");
        alert.setContentText(message.toString());

        alert.showAndWait();
    }

    /**
     * Affiche popup pour demander d'accepter l'échange ou non
     * @param ressourcesActivePlayer les ressources du joueur actif
     * @param ressourcesOtherPlayer  les ressources du joueur concerné par l'échange
     * @param nameCurrentPlayer Nom du joueur courant
     * @param nameOtherPlayer Nom du joueur cible de l'échange
     * @param parentWindow Windows du parent (fenêtre principale)
     * @return Statut du bouton de confirmation
     */
    public static Optional<ButtonType> displayCommerceConfirmPopUp(Map<TypeRessource, Integer> ressourcesActivePlayer,
                                                            Map<TypeRessource, Integer> ressourcesOtherPlayer,
                                                            String nameCurrentPlayer, String nameOtherPlayer,
                                                            Window parentWindow){

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initOwner(parentWindow);
        alert.setTitle("Demande d'echange pour " + nameOtherPlayer);

        StringBuilder message = new StringBuilder(nameCurrentPlayer + " vous propose d'échanger : \n");

        for (TypeRessource tp : TypeRessource.values()) {
            if (ressourcesOtherPlayer.get(tp) > 0) {
                message.append(" + ");
                message.append(ressourcesOtherPlayer.get(tp));
                message.append(" ");
                message.append(tp);
                message.append("\n");
            }
        }

        message.append("contre : \n");

        for (TypeRessource tp : TypeRessource.values()) {
            if (ressourcesActivePlayer.get(tp) > 0) {
                message.append(" + ");
                message.append(ressourcesActivePlayer.get(tp));
                message.append(" ");
                message.append(tp);
                message.append("\n");
            }
        }

        message.append("Est-ce que cela vous convient ?");

        alert.setHeaderText("Question");
        alert.setContentText(message.toString());

        Optional<ButtonType> result = alert.showAndWait();
        alert.hide();

        return result;
    }
}

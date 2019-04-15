package model;

import java.util.Map;

/**
 * MessagesHelp : Contient tous les messages affichés dans le jeu
 * by Clément Jan 2019
 */
public abstract class MessagesHelp {

    public static final String IMPOSSIBLE_CONSTRUIRE_DELO =
        "Impossible de construire ici !\n" +
        "Pour construire une Delorean, il faut qu'elle soit adjacente\n" +
        "à une de vos routes existantes, et les 3 croisements autour de\n" +
        "cet emplacement doivent être libres";

    public static final String IMPOSSIBLE_CONSTRUIRE_ROUTE =
            "Impossible de construire ici !\n" +
            "Pour construire une Route, il faut qu'elle soit adjacente\n" +
            "à une de vos constructions existantes";

    public static final String FORCE_CONSTRUCT_ROAD = "Vous devez construire une Route !";
    public static final String FORCE_CONSTRUCT_DELO = "Vous devez construire une Delorean !";

    public static final String BUSY_LOCATION =
            "Impossible de construire ici ! \n" +
            "L'emplacement est déjà occupé par un autre joueur !";

    public static final String PLATEAU_INACCESSIBLE_CONSTRUCT =
            "Vous ne pouvez pas construire ici car vous n'avez pas encore \n" +
            "débloqué ce plateau.\n" +
            "Pour le débloquer, il faut construire une Delorean sur ce plateau.";

    public static final String PLATEAU_INACCESSIBLE_LABO =
            "Vous ne pouvez pas améliorer de laboratoire ici car vous n'avez pas encore \n" +
            "débloqué ce plateau.\n" +
            "Pour le débloquer, il faut construire une Delorean sur ce plateau.";

    public static final String NOT_OWNER_DELOREAN =
            "Cette Delorean appartient à un autre joueur\n" +
            "Veuillez choisir une Delorean qui vous appartient";

    public static final String NOT_ENOUGH_RESSOURCE = "Ressources insuffisantes ";

    public static final String NOT_ENOUGH_RESSOURCE_PROD =
            "Ressources insuffisantes pour produire ! \n" +
            "Il vous faut " + ParamGame.RESSOURCES_A_ENLEVER_PRODUCTION + " ressources pour pouvoir produire\n" +
            ParamGame.RESSOURCE_A_AJOUTER_PRODUCTION + " ressource de base de votre choix";

    public static final String ERROR_BIFF_MOVE = "Impossible de déplacer biff ici !";

    public static String notEnoughRessourceMessage(Placeable p) {
        StringBuilder message = new StringBuilder(NOT_ENOUGH_RESSOURCE);

        if (p instanceof Delorean) {
            message.append("pour construire un LABORATOIRE");
            message.append("\nIl vous faut : \n");
            for (Map.Entry<TypeRessource, Integer> entryRessource : ParamGame.prixLabo.entrySet()) {
                message.append(" " + entryRessource.getValue() + " " + entryRessource.getKey() + "\n");
            }
        }
        else if (p instanceof Route) {
            message.append("pour construire une ROUTE");
            message.append("\nIl vous faut : \n");
            for (Map.Entry<TypeRessource, Integer> entryRessource : ParamGame.prixRoute.entrySet()) {
                message.append(" " + entryRessource.getValue() + " " + entryRessource.getKey() + "\n");
            }
        }
        else {
            message.append("pour construire une DELOREAN");
            message.append("\nIl vous faut : \n");
            for (Map.Entry<TypeRessource, Integer> entryRessource : ParamGame.prixDelorean.entrySet()) {
                message.append(" " + entryRessource.getValue() + " " + entryRessource.getKey() + "\n");
            }
        }
        return message.toString();
    }
}

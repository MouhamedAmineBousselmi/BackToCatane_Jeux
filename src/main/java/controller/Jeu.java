package controller;

import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import model.*;
import model.customExceptions.BuildingException;
import model.customExceptions.BusyLocation;
import model.customExceptions.NotEnoughResourceException;
import model.customExceptions.PlateauLockedException;
import view.BoardView;
import view.CroisementTile;
import view.HexagonalTile;
import view.RouteTile;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.*;
import java.util.List;

/**
 * Classe Jeu : Controller principal de l'application
 * by Clément Jan 2019
 */
public class Jeu {

    private HashMap<Integer, Plateau> listePlateaux;
    private HashMap<Integer, BoardView> listeVuesPlateaux;

    private static ArrayList<Joueur> listeJoueurs;
    private Random random;

    private static Jeu monJeu = null;
    private MainGameControl mainGameControl;
    private PlayersPageControl playersPageControl;
    private ProductionControl productionControl;

    private Integer nextIdJoueur;
    private Joueur currentPlayer;
    private boolean laboUpgradeMode;
    private boolean moveBiff;

    private boolean constructionMode;
    private boolean freeConstruction;
    private boolean routeOnly;

    private Jeu() {
        this.listePlateaux = new HashMap<>();
        this.listeVuesPlateaux = new HashMap<>();
        listeJoueurs = new ArrayList<>();

        this.nextIdJoueur = 1;
        this.freeConstruction = true;
        this.routeOnly = false;
        this.constructionMode = false;
        this.laboUpgradeMode = false;
        this.moveBiff = false;

        try {
            this.random = SecureRandom.getInstanceStrong();
        }
        catch (NoSuchAlgorithmException e) {}
    }

    public static Jeu getInstance()
    {
        if(monJeu == null){
            monJeu = new Jeu();
        }
        return monJeu;
    }

    /***
     * Initialise la partie
     * - Création des plateaux
     * - Génération des accesPlateaux
     * - Création des joueurs
     *   - Création du joueur coop
     *   - Assignation de la liste commune de ressource à tous les joueurs
     * - Création des cartes
     * @param nbJoueurs Nombre de joueurs
     * @param noms Liste des noms des joueurs
     * @param colors Liste des couleurs des joueurs
     */
    public void nouvellePartie(Integer nbJoueurs, String[] noms, Color[] colors) {
        // Génère les plateaux
        for (Map.Entry<Integer, Integer> line : ParamGame.sizePlateaux.entrySet()) {
            // Création du model
            Plateau p = new Plateau(line.getKey());
            this.listePlateaux.put(line.getKey(), p);

            // Configure l'echelle des hexagones en fonction de la taille du plateau
            int radius;
            if (line.getValue() == 3) { radius = ParamGame.RADIUS_PETIT_PLATEAU; }
            else { radius = ParamGame.RADIUS_GRAND_PLATEAU; }

            // Création de la vue
            BoardView bw = new BoardView(ParamGame.WIDTHPLATEAU, ParamGame.HEIGHTPLATEAU,
                    line.getValue(), radius, ParamGame.PADDINGHEXA, p);

            this.listeVuesPlateaux.put(line.getKey(), bw);

            assignClickEventsConstruction(bw);
            assignEventsHexagone(bw);
        }

        // Génère la liste des accès plateaux
        HashMap<Plateau, Boolean> accesPlateaux = new HashMap<>();
        for (Map.Entry<Integer, Plateau> p : this.listePlateaux.entrySet()) {
            // Autorise l'accès au plateau de départ
            // Interdit l'accès aux autres plateaux
            boolean accesP = p.getValue().getEpoque().equals(ParamGame.START_PLATEAU);
            accesPlateaux.put(p.getValue(), accesP);
        }

        // Création des joueurs
        for (int i = 0; i < nbJoueurs; i++) {
            Joueur j = new Joueur(nextIdJoueur, noms[i], colors[i], accesPlateaux);
            listeJoueurs.add(j);
            nextIdJoueur++;
        }

        // Assignation du premier joueur
        int indexPremierJoueur = new Random().nextInt(listeJoueurs.size() - 1);
        this.currentPlayer = listeJoueurs.get(indexPremierJoueur);

//        // Création du joueur co-op
//        Joueur coop = new Joueur(nextIdJoueur, "COOP", Color.BLACK, accesPlateaux);
//        coop.initRessources(0);
//
//        // Assignation des ressources communes a tous les joueurs
//        for (Joueur j : listeJoueurs) {
//            j.setListeRessources(coop.getListeRessources());
//        }
    }

    /**
     * Change le joueur courant vers le prochain joueur
     */
    void joueurSuivant() {
        int currentPlayerIndex = listeJoueurs.indexOf(this.currentPlayer);
        int nbJoueurs = listeJoueurs.size();
        this.currentPlayer = listeJoueurs.get((currentPlayerIndex + 1) % nbJoueurs);
    }

    /**
     * Permet de mettre à jour le panneau d'un joueur (ressources + score)
     */
    public void updateView(){
        for(Joueur j: listeJoueurs) {
            j.calculeScore();
            j.getListeLabels().get(TypeLabel.LABEL_ALUMINIUM).setText(j.getListeRessources().get(TypeRessource.ALUMINIUM).toString());
            j.getListeLabels().get(TypeLabel.LABEL_FER).setText(j.getListeRessources().get(TypeRessource.FER).toString());
            j.getListeLabels().get(TypeLabel.LABEL_PETROLE).setText(j.getListeRessources().get(TypeRessource.PETROLE).toString());
            j.getListeLabels().get(TypeLabel.LABEL_OR).setText(j.getListeRessources().get(TypeRessource.OR).toString());
            j.getListeLabels().get(TypeLabel.LABEL_BRIQUE).setText(j.getListeRessources().get(TypeRessource.BRIQUE).toString());
            j.getListeLabels().get(TypeLabel.LABEL_PLUTONIUM).setText(j.getListeRessources().get(TypeRessource.PLUTONIUM).toString());
            j.getListeLabels().get(TypeLabel.LABEL_SCORE).setText(j.getScore().toString());
        }
    }

    /**
     * Assigner les évènements aux croisements/routes vides
     * pour pouvoir en construire
     */
    private void assignClickEventsConstruction(BoardView bw) {
        // Assigner evenements aux Croisements
        for (CroisementTile ct : bw.getListeCroisements()) {
            if (ct.getPlaceable().isAvailable()) {
                ct.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                    // Si c'est les premières Delorean à poser
                    if (this.constructionMode && this.freeConstruction) {
                        if (!routeOnly) {
                            try {
                                this.currentPlayer.construire(ct.getPlaceable(), ct, bw.getPlateau(), freeConstruction);
                                assignLaboUpdateEvent(ct, bw.getPlateau());
                                this.routeOnly = true;
                                if (this.mainGameControl != null) {
                                    mainGameControl.getButtonConstruction().setDisable(false);
                                    mainGameControl.getButtonConstruction().fire();
                                    mainGameControl.getButtonConstruction().setDisable(true);
                                }
                            }
                            catch (BuildingException e1) {
                                CustomDialogs.displayWarningPopup(MessagesHelp.IMPOSSIBLE_CONSTRUIRE_DELO,
                                        mainGameControl.getAnchorPane().getScene().getWindow());

                            }
                            catch (NotEnoughResourceException e2) { }
                            catch (BusyLocation e3) {
                                CustomDialogs.displayWarningPopup(MessagesHelp.BUSY_LOCATION,
                                        mainGameControl.getAnchorPane().getScene().getWindow());
                            }
                            catch (PlateauLockedException e4) {
                                CustomDialogs.displayWarningPopup(MessagesHelp.PLATEAU_INACCESSIBLE_CONSTRUCT,
                                        mainGameControl.getAnchorPane().getScene().getWindow());
                            }
                        }
                        else {
                            CustomDialogs.displayWarningPopup(MessagesHelp.FORCE_CONSTRUCT_ROAD,
                                    mainGameControl.getAnchorPane().getScene().getWindow());
                        }
                    }
                    else if (this.constructionMode) {
                        try {
                            this.currentPlayer.construire(ct.getPlaceable(), ct, bw.getPlateau(), freeConstruction);
                        }
                        catch (BuildingException e1) {
                            CustomDialogs.displayWarningPopup(MessagesHelp.IMPOSSIBLE_CONSTRUIRE_DELO,
                                    mainGameControl.getAnchorPane().getScene().getWindow());

                        }
                        catch (NotEnoughResourceException e2) {
                            mainGameControl.getButtonConstruction().setStyle("");
                            this.setConstructionMode(false);
                            mainGameControl.setDisableOtherButtons(mainGameControl.getButtonConstruction(), false);

                            CustomDialogs.displayWarningPopup(MessagesHelp.notEnoughRessourceMessage(ct.getPlaceable()),
                                    mainGameControl.getAnchorPane().getScene().getWindow());
                        }
                        catch (BusyLocation e3) {
                            CustomDialogs.displayWarningPopup(MessagesHelp.BUSY_LOCATION,
                                    mainGameControl.getAnchorPane().getScene().getWindow());
                        }
                        catch (PlateauLockedException e4) {
                            CustomDialogs.displayWarningPopup(MessagesHelp.PLATEAU_INACCESSIBLE_CONSTRUCT,
                                    mainGameControl.getAnchorPane().getScene().getWindow());
                        }
                    }
                });
            }
        }

        // Assigner evenements aux Routes
        for (RouteTile rt : bw.getListeRoutes()) {
            if (rt.getRoute().isAvailable()) {
                rt.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                    // Si c'est les premières Delorean à poser
                    if (this.constructionMode && this.freeConstruction) {
                        if (routeOnly) {
                            try {
                                this.currentPlayer.construire(rt.getRoute(), null, bw.getPlateau(), freeConstruction);
                                this.routeOnly = false;
                                mainGameControl.nextPlayer();
                            }
                            catch (BuildingException e1) {
                                CustomDialogs.displayWarningPopup(MessagesHelp.IMPOSSIBLE_CONSTRUIRE_ROUTE,
                                        mainGameControl.getAnchorPane().getScene().getWindow());

                            }
                            catch (NotEnoughResourceException e2) { }
                            catch (BusyLocation e3) {
                                CustomDialogs.displayWarningPopup(MessagesHelp.BUSY_LOCATION,
                                        mainGameControl.getAnchorPane().getScene().getWindow());
                            }
                            catch (PlateauLockedException e4) {
                                CustomDialogs.displayWarningPopup(MessagesHelp.PLATEAU_INACCESSIBLE_CONSTRUCT,
                                        mainGameControl.getAnchorPane().getScene().getWindow());
                            }
                        }
                        else {
                            CustomDialogs.displayWarningPopup(MessagesHelp.FORCE_CONSTRUCT_DELO,
                                    mainGameControl.getAnchorPane().getScene().getWindow());
                        }
                    }
                    else if (this.constructionMode) {
                        try {
                            this.currentPlayer.construire(rt.getRoute(), null, bw.getPlateau(), freeConstruction);
                        }
                        catch (BuildingException e1) {
                            CustomDialogs.displayWarningPopup(MessagesHelp.IMPOSSIBLE_CONSTRUIRE_ROUTE,
                                    mainGameControl.getAnchorPane().getScene().getWindow());
                        }
                        catch (NotEnoughResourceException e2) {
                            mainGameControl.getButtonConstruction().setStyle("");
                            this.setConstructionMode(false);
                            mainGameControl.setDisableOtherButtons(mainGameControl.getButtonConstruction(), false);

                            CustomDialogs.displayWarningPopup(MessagesHelp.notEnoughRessourceMessage(rt.getRoute()),
                                    mainGameControl.getAnchorPane().getScene().getWindow());
                        }
                        catch (BusyLocation e3) {
                            CustomDialogs.displayWarningPopup(MessagesHelp.BUSY_LOCATION,
                                    mainGameControl.getAnchorPane().getScene().getWindow());
                        }
                        catch (PlateauLockedException e4) {
                            CustomDialogs.displayWarningPopup(MessagesHelp.PLATEAU_INACCESSIBLE_CONSTRUCT,
                                    mainGameControl.getAnchorPane().getScene().getWindow());
                        }
                    }
                });
            }
//                rt.addEventHandler(MouseEvent.MOUSE_ENTERED, event -> {
//                    if (hasOwner) {
//                        rt.setHeight(50);
//                        rt.setWidth(50);
//                    }
//                });
        }
    }

    private void assignLaboUpdateEvent(CroisementTile ct, Plateau p) {
        if (ct.getPlaceable() instanceof Delorean) {
            ct.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                if (this.laboUpgradeMode) {
                    try {
                        this.currentPlayer.updateDeloToLab((Delorean)ct.getPlaceable(), ct, p);
                    }
                    catch (NotEnoughResourceException e1) {
                        mainGameControl.getBtnUpdateToLabo().setStyle("");
                        this.setLaboUpgradeMode(false);
                        mainGameControl.setDisableOtherButtons(mainGameControl.getBtnUpdateToLabo(), false);

                        CustomDialogs.displayWarningPopup(MessagesHelp.notEnoughRessourceMessage(ct.getPlaceable()),
                                mainGameControl.getAnchorPane().getScene().getWindow());
                    }
                    catch (BusyLocation e2) {
                        CustomDialogs.displayWarningPopup(MessagesHelp.NOT_OWNER_DELOREAN,
                                mainGameControl.getAnchorPane().getScene().getWindow());
                    }
                    catch (PlateauLockedException e4) {
                        CustomDialogs.displayWarningPopup(MessagesHelp.PLATEAU_INACCESSIBLE_LABO,
                                mainGameControl.getAnchorPane().getScene().getWindow());
                    }
                }
            });
        }
    }

    private void assignEventsHexagone(BoardView bw) {
        for (HexagonalTile ht : bw.getListeHexagoneTile()) {
            // Ajout évènement sur l'hexagone
            ht.getTxtNum().addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
                if (this.moveBiff) {
                    try {
                        bw.moveBiff(ht);
                        this.moveBiff = false;
                        mainGameControl.setDisableOtherButtons(mainGameControl.getButtonDice(), false);
                        mainGameControl.verifyBlockButtonProduction();
                    }
                    catch (BusyLocation e1) {
                        CustomDialogs.displayWarningPopup(MessagesHelp.ERROR_BIFF_MOVE,
                                mainGameControl.getAnchorPane().getScene().getWindow());
                    }
                }
            });
        }
    }

    /***
     * Tirage de 2 dés
     * @return Somme des 2 dés
     */
    Integer tirageDes() {
        int low = 2;
        int high = 12;
        return random.nextInt(high - low) + low;
    }

    /**
     * Recolte les ressource en fonction du nombre tiré aux dés passé en paramètre
     * @param nb Nombre tiré au dé
     */
    void recolterRessources(Integer nb) {
        // Créer une liste qui contient le changelog des ressources
        HashMap<Joueur, EnumMap<TypeRessource, Integer>> playerUpdateResources = new HashMap<>();
        for (Joueur j : Jeu.getListeJoueurs()) {
            playerUpdateResources.put(j, new EnumMap<>(TypeRessource.class));
        }

        for (Map.Entry<Integer, Plateau> entry : this.getListePlateaux().entrySet()) {
            // Trouver les hexagones qui produisent
            ArrayList<Hexagone> listeHexaToProduce = new ArrayList<>();
            for (Hexagone h : entry.getValue().getHexagones()) {
                // Si il n'y a pas Biff
                if (h.getNum().equals(nb) && !h.isBrigand()) {
                    listeHexaToProduce.add(h);
                }
            }

            // Ajouter les ressources des hexagones producteurs
            // aux joueurs qui possèdent une Delorean/un labo autour de ceux-ci
            for (Hexagone h : listeHexaToProduce) {
                for (Placeable p : entry.getValue().getIntersections(h)) {
                    if (p.hasOwner()) {
                        if (p instanceof Delorean) {
                            p.getProprietaire().addRessource(ParamGame.typesTerrainsRessources.get(h.getTypeT()), ParamGame.ADD_RESSOURCE_PER_DELOREAN);

                            // Update changelog
                            updateChangelogRessources(playerUpdateResources, p,
                                    ParamGame.typesTerrainsRessources.get(h.getTypeT()),
                                    ParamGame.ADD_RESSOURCE_PER_DELOREAN);
                        }
                        else if (p instanceof Laboratoire) {
                            p.getProprietaire().addRessource(ParamGame.typesTerrainsRessources.get(h.getTypeT()), ParamGame.ADD_RESSOURCE_PER_LABO);

                            // Update changelog
                            updateChangelogRessources(playerUpdateResources, p,
                                    ParamGame.typesTerrainsRessources.get(h.getTypeT()),
                                    ParamGame.ADD_RESSOURCE_PER_LABO);
                        }
                    }
                }
            }
        }
        updateView();
        CustomDialogs.displaySummaryRecolte(playerUpdateResources,
                mainGameControl.getAnchorPane().getScene().getWindow());
    }

    /**
     * Met a jour le résumé de la collecte des ressources de la methode recolteRessource
     * @param playerUpdateResources Liste des changelog des ressources
     * @param p Placeable qui recolte
     * @param tr TypeRessource récoltée
     */
    private void updateChangelogRessources(HashMap<Joueur, EnumMap<TypeRessource, Integer>> playerUpdateResources, Placeable p, TypeRessource tr, Integer addRessource) {
        // Si l'entrée n'existe pas dans l'enumMap
        if (playerUpdateResources.get(p.getProprietaire()).get(tr) == null) {
            playerUpdateResources.get(p.getProprietaire()).put(tr, addRessource);
        }
        // Si elle existe, on additionne les quantités de ressources
        else {
            Integer val = playerUpdateResources.get(p.getProprietaire()).get(tr);
            playerUpdateResources.get(p.getProprietaire()).put(tr, val + addRessource);
        }
    }

    /**
     * Fait l'action Biff :
     * - Enlève des ressources aux joueurs qui en possède plus que 7
     * - Déplace Biff
     * @param removeOverRessources true si il faut enlever les ressources en trop des joueurs
     */
    void executeBiffAction(Boolean removeOverRessources) {
        HashMap<Joueur, EnumMap<TypeRessource, Integer>> playerUpdateResources = new HashMap<>();

        // Enlève les ressources en trop des joueurs
        if (removeOverRessources) {
            for (Joueur j : Jeu.getListeJoueurs()) {
                int nbRess = j.calculateTotalRessources();

                if (nbRess > 7) {
                    playerUpdateResources.put(j, removeOverRessources(j));
                }
            }
            // Afficher le résumé des ressources perdues si il y en a eu
            if (playerUpdateResources.size() > 0) {
                CustomDialogs.displaySummaryBiff(playerUpdateResources,
                        mainGameControl.getAnchorPane().getScene().getWindow());
            }
        }
        this.moveBiff = true;
    }

    /**
     * Enlève aléatoirement une ressource au joueur, tant qu'il en a plus que 7
     * @param j Joueur a traiter
     * @return Map<TypeRessource, Integer> Ressources perdues par le joueur
     */
    private EnumMap<TypeRessource, Integer> removeOverRessources(Joueur j) {
        EnumMap<TypeRessource, Integer> res = new EnumMap<>(TypeRessource.class);
        ArrayList<TypeRessource> ressourceOwnPlayer;

        while (j.calculateTotalRessources() > 7) {
            // Création de la liste de ressource possédée par le joueur (nbRessource > 0)
            ressourceOwnPlayer = new ArrayList<>();
            for (Map.Entry<TypeRessource, Integer> entry : j.getListeRessources().entrySet()) {
                if (entry.getValue() > 0) {
                    ressourceOwnPlayer.add(entry.getKey());
                }
            }

            // Sélectionne une ressource aléatoire parmis celles que le joueur possède
            int rand = new Random().nextInt(ressourceOwnPlayer.size());
            TypeRessource tr = ressourceOwnPlayer.get(rand);

            // Supprime une ressource aléatoire
            j.removeRessource(tr, 1);

            // Mets a jour la liste de changement retournee
            if (res.get(tr) == null) {
                res.put(tr, 1);
            }
            else {
                int val = res.get(tr);
                res.put(tr, val + 1);
            }
        }
        return res;
    }

    /**
     * Permet de récupérer un joueur grâce à son nom
     * @param name Nom du joueur que l'on veut récupérer
     * @return Le joueur auquel le nom correspond
     */
    Joueur findPlayerByName(String name) {
        for (Joueur j : Jeu.listeJoueurs) {
            if (j.getNom().equals(name)){
                return j;
            }
        }
        throw new NullPointerException("Name not found");
    }

    /**
     * Getter liste plateaux
     * @return la liste des plateaux
     */
    public Map<Integer, Plateau> getListePlateaux() {
        return listePlateaux;
    }

    /**
     * Getter liste des joueurs
     * @return la liste des joueurs
     */
    public static List<Joueur> getListeJoueurs() {
        return listeJoueurs;
    }

    public HashMap<Integer, BoardView> getListeVuesPlateaux() {
        return listeVuesPlateaux;
    }

    boolean isConstructionMode() {
        return constructionMode;
    }

    public MainGameControl getMainGameControl() {
        return this.mainGameControl;
    }

    void setMainGameControl(MainGameControl mainGameControl) {
        this.mainGameControl = mainGameControl;
    }

    PlayersPageControl getPlayersPageControl() {
        return this.playersPageControl;
    }

    void setPlayersPageControl(PlayersPageControl playersPageControl) {
        this.playersPageControl = playersPageControl;
    }

    public void setConstructionMode(boolean constructionMode) {
        this.constructionMode = constructionMode;
    }

    boolean isLaboUpgradeMode() {
        return laboUpgradeMode;
    }

    public void setLaboUpgradeMode(boolean laboUpgradeMode) {
        this.laboUpgradeMode = laboUpgradeMode;
    }

    boolean isFreeConstruction() {
        return freeConstruction;
    }

    void setFreeConstruction(boolean freeConstruction) {
        this.freeConstruction = freeConstruction;
    }

    public void setCurrentPlayer(Joueur currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public Joueur getCurrentPlayer() {
        return currentPlayer;
    }

    public ProductionControl getProductionControl() {
        return this.productionControl;
    }

    void setProductionControl(ProductionControl productionControl) {
        this.productionControl = productionControl;
    }

    @Override
    public String toString() {
        return "Jeu{" +
                "listePlateaux=" + listePlateaux +
                ", listeJoueurs=" + listeJoueurs +
                ", nextIdJoueur=" + nextIdJoueur +
                '}';
    }
}

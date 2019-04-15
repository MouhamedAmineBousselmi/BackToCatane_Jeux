package model;

import controller.Jeu;
import javafx.scene.control.Label;
import java.util.*;

import javafx.scene.paint.Color;
import model.customExceptions.BuildingException;
import model.customExceptions.BusyLocation;
import model.customExceptions.NotEnoughResourceException;
import model.customExceptions.PlateauLockedException;
import view.CroisementTile;

/**
 * Joueur : Classe du joueur avec tout ce qu'il détiendra pendant le jeu, toutes ces propriétés.
 * by Clément Jan 2019
 */
public class Joueur {
    private Integer id;
    private String nom;
    private Color couleur;
    private Integer score;
    private EnumMap<TypeRessource, Integer> listeRessources;
    private ArrayList<Delorean> listeDelorean;
    private Integer nbLabo;
    private boolean convertisseurTemporel;
    private boolean armeeLaPlusPuissante;
    private boolean routeLaPlusLongue;
    private boolean hasLabo;
    private HashMap<Plateau, Boolean> accesPlateaux;
    private EnumMap<TypeLabel, Label> listeLabels;

    /***
     * Constructeur de joueur
     * @param id Id du joueur
     * @param accesPlateaux Droit d'accès aux différents plateaux
     */
    private Joueur(Integer id, HashMap<Plateau, Boolean> accesPlateaux) {
        this.id = id;
        this.score = 0;
        this.listeDelorean = new ArrayList<>();
        this.nbLabo = 0;
        this.convertisseurTemporel = false;
        this.armeeLaPlusPuissante = false;
        this.routeLaPlusLongue = false;
        this.hasLabo = false;

        this.listeRessources = new EnumMap<>(TypeRessource.class);
        initRessources(ParamGame.NB_START_RESSOURCES);
        initListLabel();

        this.accesPlateaux = accesPlateaux;
    }

    /***
     * Constructeur de joueur en spécifiant son nom
     * @param id Id du joueur
     * @param nom Nom du joueur (affiché en jeu)
     * @param color Couleur du joueur en jeu
     * @param accesPlateaux Droit d'accès aux différents plateaux
     */
    public Joueur(Integer id, String nom, Color color, HashMap<Plateau, Boolean> accesPlateaux) {
        this(id, accesPlateaux);
        this.nom = nom;
        this.couleur = color;
    }

    /***
     * Commercer avec un autre joueur
     * Le joueur qui commerce propose des ressources "ressourcesGiven"
     * et recoit les ressources "ressourcesAsked" du "joueurCible"
     * @param ressourcesAsked Ressources demandées par le joueur
     * @param joueurCible Joueur avec qui commercer
     * @param ressourcesGiven Ressources données par le joueur
     */
    public void commercer(Map<TypeRessource, Integer> ressourcesAsked, Joueur joueurCible, Map<TypeRessource, Integer> ressourcesGiven) {
        try {
            checkRessource(ressourcesGiven);
            joueurCible.checkRessource(ressourcesAsked);

            // Ajoute les ressourcesGiven au joueur cible
            for (Map.Entry<TypeRessource, Integer> entry : ressourcesGiven.entrySet()) {
                joueurCible.addRessource(entry.getKey(), entry.getValue());
            }

            // Ajoute les ressourcesAsked au joueur courant
            for (Map.Entry<TypeRessource, Integer> entry : ressourcesAsked.entrySet()) {
                this.addRessource(entry.getKey(), entry.getValue());
            }

            // Retire les ressourcesAsked du joueur cible
            for (Map.Entry<TypeRessource, Integer> entry : ressourcesAsked.entrySet()) {
                joueurCible.removeRessource(entry.getKey(), entry.getValue());
            }

            // Retire les ressourcesGiven au joueur courant
            for (Map.Entry<TypeRessource, Integer> entry : ressourcesGiven.entrySet()) {
                this.removeRessource(entry.getKey(), entry.getValue());
            }
        }
        catch (NotEnoughResourceException e1) { }
    }

    /***
     * Produire une ressource avec un laboratoire du joueur
     * @param ressourceToConsume Ressources échangées pour produire la matière première
     * @param ressourceToProduce Ressource à produire
     */
    public void produire(TypeRessource ressourceToConsume, TypeRessource ressourceToProduce) {
        // Vérifie le nombre de ressources à consommer
        EnumMap<TypeRessource, Integer> resources = new EnumMap<>(TypeRessource.class);
        resources.put(ressourceToConsume, ParamGame.RESSOURCES_A_ENLEVER_PRODUCTION);

        try {
            // Vérifie les ressources du joueur avant de pouvoir produire
            checkRessource(resources);

            // Si le nombre de ressource à consommer est valide, alors on peut produire
            // Consomme les ressources passées en paramètre
            this.removeRessource(ressourceToConsume, ParamGame.RESSOURCES_A_ENLEVER_PRODUCTION);

            // Produit la nouvelle ressource
            this.addRessource(ressourceToProduce, ParamGame.RESSOURCE_A_AJOUTER_PRODUCTION);

        }
        catch (NotEnoughResourceException e1) {
            CustomDialogs.displayWarningPopup(MessagesHelp.NOT_ENOUGH_RESSOURCE_PROD,
                    Jeu.getInstance().getProductionControl().getLabelActivePlayer().getScene().getWindow());

        }
    }

    /**
     * Construire un objet (Route, Delorean, Labo
     * @param p Objet à construire
     * @param ct tuile croisement sur laquelle construire
     * @param pl Plateau sur lequel construire
     * @param freeConstruction true pour placer gratuitement à n'importe quel endroit la Delorean
     * @throws NotEnoughResourceException Si pas assez de ressource
     * @throws BuildingException Si impossible de construire ici
     * @throws BusyLocation Si l'emplacement est déjà occupé
     */
    public void construire(Placeable p, CroisementTile ct, Plateau pl, boolean freeConstruction) throws NotEnoughResourceException, BuildingException, BusyLocation, PlateauLockedException {
        boolean paid = false;

        if (this.getAccesPlateaux().get(pl)) {
            // Route
            if (p instanceof Route) {
                // Vérifier si la route est constructible (en fonction des règles de jeu)
                if (p.isAvailable()) {
                    if (pl.isBuildable(p, this, freeConstruction)) {
                        if (!freeConstruction) {
                            if (checkRessource(ParamGame.prixRoute)) {
                                payer(this, ParamGame.prixRoute);
                                paid = true;
                            }
                            else {
                                throw new NotEnoughResourceException();
                            }
                        }
                        else {
                            paid = true;
                        }
                    }
                    else {
                        throw new BuildingException("Impossible de construire la route ici");
                    }
                }
                else {
                    throw new BusyLocation("Ces emplacement est déjà occupé par un autre joueur");
                }
            }
            // Intersection
            else {
                if (p.isAvailable()) {
                    if (pl.isBuildable(p, this, freeConstruction)) {
                        if (!freeConstruction) {
                            if (checkRessource(ParamGame.prixDelorean)) {
                                payer(this, ParamGame.prixDelorean);
                                paid = true;
                            }
                            else {
                                throw new NotEnoughResourceException("Ressources insuffisantes ");
                            }
                        }
                        else {
                            paid = true;
                        }
                    }
                    else {
                        throw new BuildingException("Impossible de construire la Delorean ici");
                    }
                }
                else {
                    throw new BusyLocation("Ces emplacement est déjà occupé par un autre joueur");
                }
            }

            if (paid) {
                p.setProprietaire(this);
                // Construction d'une Delorean graphique
                if (!(p instanceof Route)) {
                    Delorean d = new Delorean(this, p.getPosX(), p.getPosY());
                    pl.getListeObjetsPlateau()[p.getPosY()][p.getPosX()] = d;
                    ct.setPlaceable(d);
                    this.listeDelorean.add(d);
                }
                Jeu.getInstance().setConstructionMode(false);
                if (Jeu.getInstance().getMainGameControl() != null) {
                    Jeu.getInstance().getMainGameControl().getButtonConstruction().setStyle("");
                    Jeu.getInstance().getMainGameControl().setDisableOtherButtons(Jeu.getInstance().getMainGameControl().getButtonConstruction(), false);
                    Jeu.getInstance().getMainGameControl().verifyBlockButtonProduction();
                    Jeu.getInstance().updateView();
                }
            }
        }
        else {
            throw new PlateauLockedException("Plateau inaccessible !");
        }
    }

    /**
     * Construire un laboratoire, qui remplace une Delorean déjà placée sur le plateau
     * @param d Delorean cible
     * @param ct Tuile croisement de la vue correspondante
     * @param pl Plateau cible
     * @throws NotEnoughResourceException Si pas assez de ressource
     * @throws BusyLocation Si l'emplacement est déjà occupé
     */
    public void updateDeloToLab(Delorean d, CroisementTile ct, Plateau pl) throws NotEnoughResourceException, BusyLocation, PlateauLockedException {
        if (this.getAccesPlateaux().get(pl)) {
            if (d.getProprietaire() == this) {
                if (this.checkRessource(ParamGame.prixLabo)) {
                    // Remplacer la Delorean par un laboratoire dans le tableau model
                    pl.getListeObjetsPlateau()[d.getPosY()][d.getPosX()] = new Laboratoire(this, d.getPosX(), d.getPosY());
                    // Afficher une laboratoire à la place d'une delorean
                    ct.updateToLabo();
                    // Le joueur possède un labo
                    this.hasLabo = true;

                    this.getListeDelorean().remove(d);
                    this.nbLabo = this.nbLabo + 1;

                    Jeu.getInstance().setLaboUpgradeMode(false);
                    if (Jeu.getInstance().getMainGameControl() != null) {
                        Jeu.getInstance().getMainGameControl().getBtnUpdateToLabo().setStyle("");
                        Jeu.getInstance().getMainGameControl().setDisableOtherButtons(Jeu.getInstance().getMainGameControl().getBtnUpdateToLabo(), false);
                        Jeu.getInstance().getMainGameControl().verifyBlockButtonProduction();
                        Jeu.getInstance().updateView();
                    }
                }
                else {
                    throw new NotEnoughResourceException("Ressources insuffisantes ");
                }
            }
            else {
                throw new BusyLocation("Cette Delorean appartient à un autre joueur");
            }
        }
        else {
            throw new PlateauLockedException("Plateau inaccessible !");
        }
    }

    /**
     * Vérifie si le joueur a assez de ressources
     * @param listeRessources La liste de ses ressources (Alu, brique, or, fer, essence, plutonium)
     * @return true si le joueur a assez de ressources
     * @throws NotEnoughResourceException Si le joueur n'a pas assez de ressources
     */
    public boolean checkRessource(Map<TypeRessource, Integer> listeRessources) throws NotEnoughResourceException {
        for (Map.Entry<TypeRessource, Integer> entry : listeRessources.entrySet()) {
            if (this.listeRessources.get(entry.getKey()) < entry.getValue()) {
                throw new NotEnoughResourceException("Pas assez de ressources ", this.nom);
            }
        }
        return true;
    }

    /**
     * Payer la liste de ressoure passée en paramètre
     * Retire les ressources au joueur passé en param
     * @param j Joueur cible
     * @param prix Prix a payer
     */
    private void payer(Joueur j, Map<TypeRessource, Integer> prix) {
        for (Map.Entry<TypeRessource, Integer> entry : prix.entrySet()) {
            j.removeRessource(entry.getKey(), entry.getValue());
        }
    }

    /***
     * Initialise la liste des ressource du joueur à "nb" de chaque type de ressource
     * @param nb Nombre de ressources
     */
    private void initRessources(Integer nb) {
        if (nb < 0) nb = 0;
        this.listeRessources = new EnumMap<>(TypeRessource.class);
        for (TypeRessource typeR : TypeRessource.values()) {
            this.listeRessources.put(typeR, nb);
        }
    }

    /**
     * Créer la liste de label du joueur (affiches dans la fenetre de jeu)
     */
    private void initListLabel() {
        this.listeLabels = new EnumMap<>(TypeLabel.class);
        int i = 0;
        // Add ressource label
        for (TypeRessource tr : TypeRessource.values()) {
            this.listeLabels.put(TypeLabel.values()[i], new Label(this.listeRessources.get(tr).toString()));
            i++;
        }
        // Add victory point label
        this.listeLabels.put(TypeLabel.LABEL_SCORE, new Label(this.score.toString()));
    }

    /**
     * Calcul le nombre total de ressources du joueur
     * @return int : nombre de ressources total du joueur
     */
    public int calculateTotalRessources() {
        int res = 0;

        for (Map.Entry<TypeRessource, Integer> entry : this.listeRessources.entrySet()) {
            res += entry.getValue();
        }
        return res;
    }

    /***
     * Ajouter la ressource en quantité passée en paramètre au joueur
     * (si la quantité est positive)
     * @param typeR Type de ressource
     * @param quantity Quantité à ajouter
     */
    public void addRessource(TypeRessource typeR, Integer quantity) {
        if (quantity > 0) {
            this.listeRessources.put(typeR, this.listeRessources.get(typeR) + quantity);
        }
    }

    /***
     * Enlever la ressource en quantité passée en paramètre au joueur
     * (si la quantité est négative)
     * La quantité minimale de ressource est de 0
     * @param typeR Type de ressource
     * @param quantity Quantité à ajouter
     */
    public void removeRessource(TypeRessource typeR, Integer quantity) {
        if (quantity > 0) {
            this.listeRessources.put(typeR, Math.max(0, this.listeRessources.get(typeR) - quantity));
        }
    }

    /**
     * Calcule le score du joueur
     */
    public void calculeScore() {
        this.score = this.listeDelorean.size() * ParamGame.POINT_PAR_DELOREAN
                + this.nbLabo * ParamGame.POINT_PAR_LABO;
    }

    /***
     * Set score
     * @param score Score voulu
     */
    public void setScore(Integer score) { this.score = score; }

    /**
     * get Liste labels
     * @return liste des labels du joueur
     */
    public EnumMap<TypeLabel, Label> getListeLabels() {
        return listeLabels;
    }

    /***
     * Obtenir le score du joueur
     * @return Retourne le score du joueur
     */
    public Integer getScore() {
        return score;
    }

    /***
     * Obtenir l'id du joueur
     * @return Retourne l'id du joueur
     */
    public Integer getId() { return id; }

    /***
     * Obtenir le nom du joueur
     * @return Retourne le nom du joueur
     */
    public String getNom() {
        return nom;
    }

    /**
     * Obtenir la couleur du joueur
     * @return Retourne la couleur du joueur
     */
    public Color getCouleur() {
        return couleur;
    }

    public ArrayList<Delorean> getListeDelorean() {
        return listeDelorean;
    }

    /***
     * Obtenir la liste des accès aux plateaux
     * @return Hashmap d'accès aux plateaux
     */
    public HashMap<Plateau, Boolean> getAccesPlateaux() {
        return accesPlateaux;
    }

    /***
     * Obtenir la liste des ressources du joueur
     * @return Retourne la liste des ressources du joueur
     */
    public EnumMap<TypeRessource, Integer> getListeRessources() {
        return listeRessources;
    }

    public boolean isConvertisseurTemporel() {
        return convertisseurTemporel;
    }

    public boolean isArmeeLaPlusPuissante() {
        return armeeLaPlusPuissante;
    }

    public boolean isRouteLaPlusLongue() {
        return routeLaPlusLongue;
    }

    public boolean isHasLabo() {
        return hasLabo;
    }

    /***
     * Assigner la liste des ressources
     * @param listeRessources Nouvelle liste de ressources
     */
    public void setListeRessources(EnumMap<TypeRessource, Integer> listeRessources) {
        this.listeRessources = listeRessources;
    }

    @Override
    public String toString() {
        return "Joueur{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", couleur=" + couleur +
                ", score=" + score +
                ", listeRessources=" + listeRessources +
                ", convertisseurTemporel=" + convertisseurTemporel +
                ", armeeLaPlusPuissante=" + armeeLaPlusPuissante +
                ", routeLaPlusLongue=" + routeLaPlusLongue +
                ", accesPlateaux=" + accesPlateaux +
                '}' + '\n';
    }
}

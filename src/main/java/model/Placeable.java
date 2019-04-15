package model;

import java.util.EnumMap;

/**
 * Placeable : Objet dont Delorean, Laboratoire et Route hérite
 * Corresponds à un objet placable sur le plateau de jeu
 * by Clément Jan 2019
 */
public class Placeable extends ObjetPlateau {
    private Joueur proprietaire;
    protected EnumMap<TypeRessource, Integer> prix;

    /***
     * Constructeur de placeable
     */
    public Placeable() {
        this.proprietaire = null;
        this.posX = 0;
        this.posY = 0;
    }

    /***
     * Constructeur de placeable appelé par les classes héritées
     * @param j Joueur propriétaire
     * @param posX Position x dans le tableau model
     * @param posY Position y dans le tableau model
     */
    protected Placeable(Joueur j, Integer posX, Integer posY) {
        this.proprietaire = j;
        this.posX = posX;
        this.posY = posY;
    }

    /***
     * Renvoi un booleen pour savoir si l'emplacement est pris
     * par un objet du plateau (Delorean, laboratoire...)
     * @return Boolean : true si l'emplacement est libre, false sinon
     */
    public boolean isAvailable() {
        return proprietaire == null;
    }

    /**
     * Renvoi si l'emplacement est occupé par un joueur
     * @return Boolean : true si l'emplacement est occupé
     */
    public boolean hasOwner() {
        return proprietaire != null;
    }

    public Joueur getProprietaire() {
        return proprietaire;
    }

    /**
     * Change le propriétaire de cet objet, et notifie les observateurs
     * pour mettre a jour la vue
     * @param proprietaire
     */
    void setProprietaire(Joueur proprietaire) {
        this.proprietaire = proprietaire;
        setChanged();
        notifyObservers();
    }
}

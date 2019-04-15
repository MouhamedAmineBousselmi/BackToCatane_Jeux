package model;

/**
 * Route : Représente un objet Route à Placer
 * by Clément Jan 2019
 */
public class Route extends Placeable {

    /***
     * Constructeur de route
     * @param j Joueur propriétaire
     * @param posX Position x dans le tableau model
     * @param posY Position y dans le tableau model
     */
    public Route(Joueur j, Integer posX, Integer posY) {
        super(j, posX, posY);
    }
}

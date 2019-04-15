package model;

/**
 * Laboratoire : Représente l'objet Laboratoire à placer
 * by Clément Jan 2019
 */
public class Laboratoire extends Placeable {

    /***
     * Constructeur de laboratoire
     * @param j Joueur propriétaire
     * @param posX Position x dans le tableau model
     * @param posY Position y dans le tableau model
     */
    public Laboratoire(Joueur j, Integer posX, Integer posY) {
        super(j, posX, posY);
    }
}

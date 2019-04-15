package model;

/**
 * Delorean : Représente l'objet à placer Delorean
 * by Clément Jan 2019
 */
public class Delorean extends Placeable {

    /***
     * Constructeur de laboratoire
     * @param j Joueur propriétaire
     * @param posX Position x dans le tableau model
     * @param posY Position y dans le tableau model
     */
    public Delorean(Joueur j, Integer posX, Integer posY) {
        super(j, posX, posY);
    }
}

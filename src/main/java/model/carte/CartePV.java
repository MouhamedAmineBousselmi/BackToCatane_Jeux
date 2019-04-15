package model.carte;

import model.Joueur;
/**
 * CartePV : Fait gagner au joueur 1 point de victoire
 * by Clément Jan 2019
 */
public class CartePV extends Carte {


    public CartePV(Joueur joueur) {
        super(joueur);
    }

    public void action() {
        // Ajoute 1 point de victoire a un joueur
        joueur.setScore(joueur.getScore() + 1);
    }
}

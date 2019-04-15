package model.carte;

import model.Joueur;
import model.TypeRessource;

/**
 * CarteInvention : Permet d'obtenir 2 ressources de son choix gratuitement
 * by Amine Jan 2019
 */
public class CarteInvention extends Carte {

    private TypeRessource choix1;
    private TypeRessource choix2;

    //-- CONSTRUCTEUR
    public CarteInvention(Joueur joueur){
        super(joueur);
    }
    //-- //

    //-- AJOUT RESSOURCE SELON CHOIX //
    public void action() {
        joueur.addRessource(choix1,1);
        joueur.addRessource(choix2,1);
    }
    //--//


    //-- GETTERS SETTERS //
    public TypeRessource getChoix1() {
        return choix1;
    }

    public TypeRessource getChoix2() {
        return choix2;
    }

    public void setChoix1(TypeRessource choix1) {
        this.choix1 = choix1;
    }

    public void setChoix2(TypeRessource choix2) {
        this.choix2 = choix2;
    }
    //-- //

    //-- ToString //
    @Override
    public String toString() {
        return "CarteInvention{" +
                "choix1=" + choix1 +
                ", choix2=" + choix2 +
                '}';
    }
    //-- //
}

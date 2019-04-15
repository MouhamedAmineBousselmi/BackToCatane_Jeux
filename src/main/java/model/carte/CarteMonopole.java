package model.carte;

import controller.Jeu;
import model.Joueur;
import model.TypeRessource;

import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

/**
 * CarteMonopole : Permet de récupérer chez tous les joueurs toutes leurs ressources
 * de la ressource sélectionnée
 * by Clément Jan 2019
 */
public class CarteMonopole extends Carte {

    private TypeRessource choix;
    private LinkedList<TypeRessource> ressourcesRecolter;
    public CarteMonopole(Joueur joueur) {
        super(joueur);
    }


    public void getRessourcesJoueurs(){
        for (Joueur j:Jeu.getListeJoueurs()) {
                // tester si les ressources existent pour chaque joueur ou non et l'affecter au joueur j
                Set<Map.Entry<TypeRessource,Integer>> entrySet =  j.getListeRessources().entrySet();
                for(Map.Entry<TypeRessource,Integer> entry : entrySet){
                    ressourcesRecolter.add(entry.getKey());
                }

        }
    }

    public void action() {
        // affecter les ressource au joueur
        for (TypeRessource t : ressourcesRecolter) {
            joueur.addRessource(t, 1);
        }
    }
}

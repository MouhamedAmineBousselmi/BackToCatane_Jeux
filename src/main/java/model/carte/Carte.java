package model.carte;

import model.Joueur;
import model.TypeRessource;

import java.util.EnumMap;

/**
 * Carte : Classe abstraite des Cartes achetable en jeu
 * by Clément Jan 2019
 */
public abstract class Carte {
    protected EnumMap<TypeRessource, Integer> prix;
    protected Joueur joueur;

    public Carte(Joueur joueur){
        this.joueur = joueur;
    }

    public abstract void action();
}

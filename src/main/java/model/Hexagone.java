package model;

/**
 * Hexagone : Représente l'objet Hexagone dans le tableau mémoire
 * des plateaux de jeu
 * by Clément Jan 2019
 */
public class Hexagone extends ObjetPlateau {
    private TypeTerrain typeT;
    private Integer num;
    private boolean brigand;

    /***
     * Constructeur d'hexagone
     * @param posX Ligne de l'hexagone
     * @param posY Colonne de l'hexagone
     */
    public Hexagone(Integer posX, Integer posY) {
        this.typeT = null;
        this.num = 0;
        this.brigand = false;
        this.posX = posX;
        this.posY = posY;
    }

    /***
     * Constructeur d'hexagone
     * @param typeT Type de terrain de l'hexagone
     * @param num Numéro associé (correspondant du résultat de dé pour l'activer)
     * @param posX Ligne de l'hexagone
     * @param posY Colonne de l'hexagone
     */
    public Hexagone(TypeTerrain typeT, Integer num, Integer posX, Integer posY) {
        this.typeT = typeT;
        this.num = num;
        this.brigand = false;
        this.posX = posX;
        this.posY = posY;
    }

    /***
     * Constructeur d'hexagone
     * @param typeT Type de terrain de l'hexagone
     * @param num Numéro associé (correspondant du résultat de dé pour l'activer)
     * @param posX Ligne de l'hexagone
     * @param posY Colonne de l'hexagone
     * @param brigand Présence ou non du brigand sur cet hexagone
     */
    public Hexagone(TypeTerrain typeT, Integer num, Integer posX, Integer posY, boolean brigand) {
        this(typeT, num, posX, posY);
        this.brigand = brigand;
    }

    public TypeTerrain getTypeT() {
        return typeT;
    }

    public Integer getNum() {
        return num;
    }

    public boolean isBrigand() {
        return brigand;
    }

    public void setBrigand(boolean brigand) {
        this.brigand = brigand;
    }
}

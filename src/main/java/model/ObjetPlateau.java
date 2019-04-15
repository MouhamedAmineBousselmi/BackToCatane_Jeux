package model;

import java.util.Observable;

/**
 * ObjetPlateau : Super objet, qui sera le type du tableau des objetsPlateau de chaque plateau
 * by Clément Jan 2019
 */
public abstract class ObjetPlateau extends Observable {
    protected Integer posX;
    protected Integer posY;

    public Integer getPosX() {
        return posX;
    }

    public Integer getPosY() {
        return posY;
    }
}

package view;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import model.ParamGame;
import model.Route;

import java.util.Observable;
import java.util.Observer;

/**
 * RouteTile : Classe vue qui permet d'afficher une Route
 * by Clément Jan 2019
 */
public class RouteTile extends Rectangle implements Observer {
    private HexagonalTile ht;
    private Route route;

    /**
     * Construire une tuile route, qui référence une route dans le tableau en mémoire
     * @param r Route model
     * @param ht Tuile hexagone (vue)
     * @param i Index (position autour de l'hexagone) de la route
     */
    public RouteTile(Route r, HexagonalTile ht, int i) {
        super();

        this.route = r;

        this.route.addObserver(this);
        this.ht = ht;

        updateCoord(i);
        updateSize();

        if (this.route.hasOwner()) {
            this.setFill(this.route.getProprietaire().getCouleur());
        }
        else {
            this.setFill(Color.WHITE);
        }
        this.setStroke(Color.BLACK);
    }

    /**
     * Calcul la l'echelle de la route (vue)
     */
    public void updateSize() {
        // Petit plateau
        if (this.ht.getRadius() == ParamGame.RADIUS_PETIT_PLATEAU) {
            this.setWidth(75);
            this.setHeight(15);
        }
        // Grand plateau
        else {
            this.setWidth(50);
            this.setHeight(12);
        }
    }

    /**
     * Positionner la route relativement à la tuile hexagone (vue)
     * @param i Index (position autour de l'hexagone) de la route
     */
    private void updateCoord(int i) {
        switch (i) {
            case 0:
                this.setX(this.ht.getCenter().x - (0.81 * this.ht.getRadius()));
                this.setY(this.ht.getCenter().y - (0.84 * this.ht.getRadius()));
                this.setRotate(149);
                break;
            case 1:
                this.setX(this.ht.getCenter().x + (0.08 * this.ht.getRadius()));
                this.setY(this.ht.getCenter().y - (0.84 * this.ht.getRadius()));
                this.setRotate(30);
                break;
            case 2:
                this.setX(this.ht.getCenter().x + (0.5 * this.ht.getRadius()));
                this.setY(this.ht.getCenter().y - (0.09 * this.ht.getRadius()));
                this.setRotate(90);
                break;
            case 3:
                this.setX(this.ht.getCenter().x + (0.08 * this.ht.getRadius()));
                this.setY(this.ht.getCenter().y + (0.7 * this.ht.getRadius()));
                this.setRotate(149);
                break;
            case 4:
                this.setX(this.ht.getCenter().x - (0.82 * this.ht.getRadius()));
                this.setY(this.ht.getCenter().y + (0.7 * this.ht.getRadius()));
                this.setRotate(30);
                break;
            case 5:
                this.setX(this.ht.getCenter().x - (1.28 * this.ht.getRadius()));
                this.setY(this.ht.getCenter().y - (0.09 * this.ht.getRadius()));
                this.setRotate(90);
                break;
        }
    }

    public Route getRoute() {
        return route;
    }

    /**
     * Mets a jour la couleur de la route quand le propriétaire
     * de la route du model change
     * @param o Observable
     * @param obj Object
     */
    public void update(Observable o, Object obj) {
        this.setFill(this.route.getProprietaire().getCouleur());
    }
}

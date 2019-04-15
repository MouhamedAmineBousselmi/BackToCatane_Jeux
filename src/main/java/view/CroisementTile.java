package view;

import javafx.scene.image.ImageView;
import model.ParamGame;
import model.Placeable;

import java.util.Observable;
import java.util.Observer;

/**
 * CroisementTile : Classe vue qui permet d'afficher un Placeable (Delorean, Laboratoire)
 * by Clément Jan 2019
 */
public class CroisementTile extends ImageView implements Observer {
    private HexagonalTile ht;
    private Placeable placeable;

    public CroisementTile(HexagonalTile ht, Placeable p, int i) {
        super();

        this.ht = ht;
        this.placeable = p;

        this.placeable.addObserver(this);

        updateCoord(i, this.placeable.hasOwner());
        updateSize(this.placeable.hasOwner());
        updateImage(this.placeable.hasOwner());
    }

    private void updateImage(boolean hasOwner) {
        if (!hasOwner) {
            this.setImage(ParamGame.croisementVide);
        }
        else {
            this.setImage(ParamGame.deloreanJoueur.get(this.placeable.getProprietaire().getCouleur()));
        }
    }

    private void updateSize(boolean hasOwner) {
        // Croisement vide
        if (!hasOwner) {
            // Petit plateau
            if (this.ht.getRadius() == ParamGame.RADIUS_PETIT_PLATEAU) {
                this.setFitHeight(30);
                this.setFitWidth(30);
            }
            // Grand plateau
            else {
                this.setFitHeight(23);
                this.setFitWidth(23);
            }
        }
        // Croisement occupé
        else {
            // Petit plateau
            if (this.ht.getRadius() == ParamGame.RADIUS_PETIT_PLATEAU) {
                this.setFitHeight(40);
                this.setFitWidth(60);
            }
            // Grand plateau
            else {
                this.setFitHeight(30);
                this.setFitWidth(40);
            }
        }
    }

    private void updateCoord(int i, boolean hasOwner) {
        // Croisement vide
        if (!hasOwner) {
            // Petit plateau
            if (this.ht.getRadius() == ParamGame.RADIUS_PETIT_PLATEAU) {
                this.setX(this.ht.getXpoints()[i] - 15);
                this.setY(this.ht.getYpoints()[i] - 15);
            }
            // Grand plateau
            else {
                this.setX(this.ht.getXpoints()[i] - 12);
                this.setY(this.ht.getYpoints()[i] - 13);
            }
        }
        // Croisement occupé
        else {
            // Petit plateau
            if (this.ht.getRadius() == ParamGame.RADIUS_PETIT_PLATEAU) {
                this.setX(this.ht.getXpoints()[i] - 20);
                this.setY(this.ht.getYpoints()[i] - 10);
            }
            // Grand plateau
            else {
                this.setX(this.ht.getXpoints()[i] - 20);
                this.setY(this.ht.getYpoints()[i] - 15);
            }
        }
    }

    public void updateToLabo() {
        this.setImage(ParamGame.laboJoueur.get(this.placeable.getProprietaire().getCouleur()));
        this.updateSize(false);
        this.toFront();
    }

    public Placeable getPlaceable() {
        return placeable;
    }

    public void setPlaceable(Placeable placeable) {
        this.placeable = placeable;
    }

    /**
     * Mets a jour l'image du croisement quand le propriétaire
     * de celui-ci change dans le model
     * @param o Observable
     * @param obj Object
     */
    public void update(Observable o, Object obj) {
        this.setImage(ParamGame.deloreanJoueur.get(this.placeable.getProprietaire().getCouleur()));
        updateSize(!this.placeable.isAvailable());
        this.toFront();
    }
}

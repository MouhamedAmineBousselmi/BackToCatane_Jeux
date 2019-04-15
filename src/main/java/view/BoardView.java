package view;

import javafx.scene.Group;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import model.*;
import model.customExceptions.BusyLocation;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

/**
 * BoardView : Classe vue, correspond au Plateau de jeu model
 * by Clément Jan 2019
 */
public class BoardView extends Group {

    private Plateau plateau;
    private Font font = new Font("Arial", 40);

    private HexagonalTile htBiff;

    private ArrayList<CroisementTile> listeCroisements;
    private ArrayList<RouteTile> listeRoutes;
    private ArrayList<HexagonalTile> listeHexagoneTile;

    /**
     * Constructeur de la vue plateau
     * @param width Largeur de la fenêtre plateau
     * @param height Hauteur de la fenêtre plateau
     * @param size Nombre de ligne d'hexagones
     * @param radiusHexa Rayon d'un hexagone (tuile sur le plateau)
     * @param paddingBwHexa Espacement entre les hexagones
     * @param p Plateau à afficher
     */
    public BoardView(int width, int height, int size, int radiusHexa, int paddingBwHexa, Plateau p) {
        this.plateau = p;
        this.listeCroisements = new ArrayList<>();
        this.listeRoutes = new ArrayList<>();
        this.listeHexagoneTile = new ArrayList<>();

        this.htBiff = null;

        Point origin = new Point(width / 2, height / 2);
        drawHexGridLoop(origin, size, radiusHexa, paddingBwHexa);

        // Poser biff aléatoirement
        poserBiff();
    }

    /**
     * Boucle de construction des hexagones
     * Algorithme de placement des hexagones inspiré et adapté d'un algo en ligne
     * https://stackoverflow.com/questions/20734438/algorithm-to-generate-a-hexagonal-grid-with-coordinate-system
     * @param origin Point du centre du plateau
     * @param rows Nombre de ligne d'hexagone
     * @param radius Rayon d'un hexagone (tuile sur le plateau)
     * @param padding Espacement entre les hexagones
     */
    private void drawHexGridLoop(Point origin, int rows, int radius, int padding) {
        double ang30 = Math.toRadians(30);
        double xOff = Math.cos(ang30) * (radius + padding);
        double yOff = Math.sin(ang30) * (radius + padding);
        int half = rows / 2;
        int indexHexa = 0;
        HexagonalTile ht;
        Hexagone h;
        Text t;
        Circle c;

        ArrayList<Hexagone> lh = this.plateau.getHexagonesOrdre();

        for (int row = 0; row < rows; row++) {
            int cols = rows - java.lang.Math.abs(row - half);

            for (int col = 0; col < cols; col++) {
                int x = (int) (origin.x + xOff * (col * 2 + 1 - cols));
                int y = (int) (origin.y + yOff * (row - half) * 3);

                h = lh.get(indexHexa);


                // Création cercle autour numéro hexagone
                c = new Circle(x, y, 30, javafx.scene.paint.Color.WHITE);

                // Affichage du numéro de l'hexagone
                String numeroAffiche;
                if (h.getNum() < 10) { numeroAffiche = " " + h.getNum().toString(); }
                else { numeroAffiche = h.getNum().toString();}

                t = new Text(x - 23, y + 13, numeroAffiche);
                t.setFont(font);
                t.setFill(javafx.scene.paint.Color.BLACK);

                // Création tuile hexagone
                ht = new HexagonalTile(x, y, radius, h, row, col, c, t);
                this.listeHexagoneTile.add(ht);

                // Ajoute la tuile hexagone au tableau de jeu
                this.getChildren().add(ht);
                ht.toBack();
                // Ajoute le cercle autour du numéro d'hexagone au tableau de jeu
                this.getChildren().add(c);
                // Ajoute le numéro de l'hexagone au tableau de jeu
                this.getChildren().add(t);

                // Création des routes
                genereRoutes(ht, h, row, rows, col, cols);

                // Création des croisements
                genereCroisements(ht, h, row, rows, col, cols);

                indexHexa++;
            }
        }
    }

    /**
     * Génère les croisements (emplacement possible pour poser un laboratoire ou une Delorean)
     * @param ht Tuile hexagone sur laquelle générer les croisements
     * @param h Hexagone model associé à la tuile hexagone
     * @param row Ligne courante de la tuile hexagone
     * @param rows Nombre de lignes total de tuiles hexagone
     * @param col Colonne courante de la tuile hexagone
     * @param cols Nombre de colonnes total de tuiles hexagone
     */
    private void genereCroisements(HexagonalTile ht, Hexagone h, int row, int rows, int col, int cols) {
        ArrayList<Placeable> croisements = this.plateau.getIntersections(h);
        CroisementTile ct;

        // Croisement 0
        ct = new CroisementTile(ht, croisements.get(0), 0);
        this.getChildren().add(ct);
        ct.toFront();
        this.listeCroisements.add(ct);

        // Croisement 1
        ct = new CroisementTile(ht, croisements.get(1), 1);
        this.getChildren().add(ct);
        ct.toFront();
        this.listeCroisements.add(ct);

        // Croisement 2
        if (row >= (rows/2) && col == (cols - 1) || row == (rows - 1)) {
            ct = new CroisementTile(ht, croisements.get(2), 2);
            this.getChildren().add(ct);
            ct.toFront();
            this.listeCroisements.add(ct);
        }

        // Croisement 3
        if (row >= (rows/2) && col == 0 || row == (rows - 1)) {
            ct = new CroisementTile(ht, croisements.get(3), 3);
            this.getChildren().add(ct);
            ct.toFront();
            this.listeCroisements.add(ct);
        }

        // Croisement 4
        if (row >= (rows/2) && col == 0) {
            ct = new CroisementTile(ht, croisements.get(4), 4);
            this.getChildren().add(ct);
            ct.toFront();
            this.listeCroisements.add(ct);
        }

        // Croisement 5
        if (col == 0 && row <= (rows / 2)) {
            ct = new CroisementTile(ht, croisements.get(5), 5);
            this.getChildren().add(ct);
            ct.toFront();
            this.listeCroisements.add(ct);
        }
    }

    /**
     *
     * Génère les routes (emplacement possible pour poser une route)
     * @param ht Tuile hexagone sur laquelle générer les croisements
     * @param h Hexagone model associé à la tuile hexagone
     * @param row Ligne courante de la tuile hexagone
     * @param rows Nombre de lignes total de tuiles hexagone
     * @param col Colonne courante de la tuile hexagone
     * @param cols Nombre de colonnes total de tuiles hexagone
     */
    private void genereRoutes(HexagonalTile ht, Hexagone h, int row, int rows, int col, int cols) {
        ArrayList<Route> routes = this.plateau.getRoutes(h);
        RouteTile rt;

        // Route 0
        rt = new RouteTile(routes.get(0), ht, 0);
        this.getChildren().add(rt);
        this.listeRoutes.add(rt);

        // Route 1
        rt = new RouteTile(routes.get(1), ht, 1);
        this.getChildren().add(rt);
        this.listeRoutes.add(rt);

        // Route 2
        rt = new RouteTile(routes.get(2), ht, 2);
        this.getChildren().add(rt);
        this.listeRoutes.add(rt);

        // Route 3
        if (col == (cols - 1) && row >= (rows / 2) || row == (rows - 1)) {
            rt = new RouteTile(routes.get(3), ht, 3);
            this.getChildren().add(rt);
            this.listeRoutes.add(rt);
        }

        // Route 4
        if (row >= (rows / 2) && col == 0 ||row == (rows - 1)) {
            rt = new RouteTile(routes.get(4), ht, 4);
            this.getChildren().add(rt);
            this.listeRoutes.add(rt);
        }

        // Route 5
        if (col == 0) {
            rt = new RouteTile(routes.get(5), ht, 5);
            this.getChildren().add(rt);
            this.listeRoutes.add(rt);
        }
    }

    /**
     * Poser Biff aléatoirement (en début de jeu)
     */
    private void poserBiff() {
        int numHexa = new Random().nextInt(this.listeHexagoneTile.size());

        try {
            moveBiff(this.listeHexagoneTile.get(numHexa));
        }
        catch (BusyLocation e) {}
    }

    /**
     * Déplace Biff sur l'hexagone ciblé
     * @param ht HexagoneTile ciblé
     * @throws BusyLocation Si l'emplacement est déja occupé
     */
    public void moveBiff(HexagonalTile ht) throws BusyLocation {
        if (ht != null && !ht.getHexagone().isBrigand()) {
            if (this.htBiff != null) {
                // Supprime l'ancien emplacement de Biff
                this.htBiff.getHexagone().setBrigand(false);

                // Affiche a nouveau le numéro de l'hexagone
                this.htBiff.getTxtNum().setVisible(true);
                // Supprimer la texture de l'ancien hexagone
                this.getChildren().remove(this.htBiff.getIvBiff());
                this.htBiff.setIvBiff(null);
            }

            // Set new Biff location
            ht.getHexagone().setBrigand(true);
            this.htBiff = ht;

            // Cacher le numéro de l'hexagone
            this.htBiff.getTxtNum().setVisible(false);
            // Affiche la nouvelle texture
            ImageView iv = new ImageView(ParamGame.biffTile);
            iv.setFitHeight(64);
            iv.setFitWidth(64);
            this.htBiff.setIvBiff(iv);
            this.getChildren().add(iv);
            // Déplace la texture au bon endroit
            iv.relocate(this.htBiff.getCenter().x - 31, this.htBiff.getCenter().y - 31);
        }
        else {
            throw new BusyLocation("Impossible de déplacer Biff ici");
        }
    }

    public ArrayList<CroisementTile> getListeCroisements() {
        return listeCroisements;
    }

    public ArrayList<RouteTile> getListeRoutes() {
        return listeRoutes;
    }

    public Plateau getPlateau() {
        return plateau;
    }

    public ArrayList<HexagonalTile> getListeHexagoneTile() {
        return listeHexagoneTile;
    }

    public HexagonalTile getHtBiff() {
        return htBiff;
    }

    public void setHtBiff(HexagonalTile htBiff) {
        this.htBiff = htBiff;
    }
}

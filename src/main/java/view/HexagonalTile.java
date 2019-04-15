package view;

import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Text;
import model.Hexagone;
import model.ParamGame;
import java.awt.*;

/**
 * by Clément 10/01/2019
 * Algorithme de calcul des points des hexagones inspiré et adapté d'un algo en ligne
 * https://stackoverflow.com/questions/20734438/algorithm-to-generate-a-hexagonal-grid-with-coordinate-system
 */
public class HexagonalTile extends Polygon {

    private static final int SIDES = 6;

    private Hexagone hexagone;
    private Circle circleNum;
    private Text txtNum;
    private ImageView ivBiff;

    private Point[] points;
    private Point center;
    private int radius;
    private int rotation;

    // Position sur le plateau
    private int row;
    private int col;

    private int npoints;
    private int[] xpoints;
    private int[] ypoints;

    private HexagonalTile(Point center, int radius, Hexagone h, int row, int col, Circle cNum, Text txtNum) {
        super();

        this.hexagone = h;
        this.circleNum = cNum;
        this.txtNum = txtNum;
        this.ivBiff = null;

        this.points = new Point[SIDES];
        this.npoints = SIDES;
        this.xpoints = new int[SIDES];
        this.ypoints = new int[SIDES];

        this.row = row;
        this.col = col;

        this.center = center;
        this.radius = radius;
        this.rotation = 90;

        updatePoints();
        this.getPoints().addAll(initHexagonPoints());

        this.setFill(ParamGame.couleursTerrain.get(this.hexagone.getTypeT()));
        this.setStroke(Color.BLACK);
    }

    HexagonalTile(int x, int y, int radius, Hexagone h, int row, int col, Circle cNum, Text txtNum) {
        this(new Point(x, y), radius, h, row, col, cNum, txtNum);
    }

    private double findAngle(double fraction) {
        return fraction * Math.PI * 2 + Math.toRadians((rotation + 180) % 360);
    }

    private Point findPoint(double angle) {
        int x = (int) (center.x + Math.cos(angle) * radius);
        int y = (int) (center.y + Math.sin(angle) * radius);

        return new Point(x, y);
    }

    private void updatePoints() {
        for (int p = 0; p < SIDES; p++) {
            double angle = findAngle((double) p / SIDES);
            Point point = findPoint(angle);
            this.xpoints[p] = point.x;
            this.ypoints[p] = point.y;
            this.points[p] = point;
        }
    }

    private Double[] initHexagonPoints() {
        Double[] pointsXY = new Double[SIDES * 2];
        int i = 0;
        for (Point p : this.points) {
            pointsXY[i] = (double)p.x;
            pointsXY[i + 1] = (double)p.y;
            i+=2;
        }
        return pointsXY;
    }

    int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;

        updatePoints();
    }

    public int getRotation() {
        return rotation;
    }

    public Point getCenter() {
        return center;
    }

    int[] getXpoints() {
        return xpoints;
    }

    int[] getYpoints() {
        return ypoints;
    }

    public void setRotation(int rotation) {
        this.rotation = rotation;

        updatePoints();
    }

    private void setCenter(Point center) {
        this.center = center;

        updatePoints();
    }

    public void setCenter(int x, int y) {
        setCenter(new Point(x, y));
    }

    public Hexagone getHexagone() {
        return hexagone;
    }

    public Circle getCircleNum() {
        return circleNum;
    }

    public Text getTxtNum() {
        return txtNum;
    }

    public ImageView getIvBiff() {
        return ivBiff;
    }

    public void setIvBiff(ImageView ivBiff) {
        this.ivBiff = ivBiff;
    }
}

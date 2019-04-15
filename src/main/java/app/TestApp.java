package app;


import controller.Jeu;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import model.*;
import view.CroisementTile;

import java.io.IOException;
import java.util.ArrayList;

public class TestApp extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        primaryStage.setTitle("Test BackToCatane");

        Jeu monJeu = Jeu.getInstance();
        monJeu.nouvellePartie(3, new String[]{"Name1", "Name2", "Name3"}, new Color[]{Color.RED, Color.BLUE, Color.GREEN});

        Jeu.getListeJoueurs().get(0).addRessource(TypeRessource.BRIQUE, 5);
        Jeu.getListeJoueurs().get(0).addRessource(TypeRessource.FER, 5);
        Jeu.getListeJoueurs().get(0).addRessource(TypeRessource.ALUMINIUM, 10);

        monJeu.setCurrentPlayer(Jeu.getListeJoueurs().get(0));

//        monJeu.assignClickEventsConstruction();

//        Plateau p = new Plateau(1885);

            // CHECK ROUTE
//        System.out.println(p.getListeObjetsPlateau()[1][3]);
//        System.out.println(p.getListeObjetsPlateau()[1][5]);
//        System.out.println(p.getListeObjetsPlateau()[3][6]);
//        System.out.println(p.getListeObjetsPlateau()[5][5]);
//        System.out.println(p.getListeObjetsPlateau()[5][3]);
//        System.out.println(p.getListeObjetsPlateau()[3][2]);
//
//        System.out.println(p.getListeObjetsPlateau()[1][7]);
//        System.out.println(p.getListeObjetsPlateau()[1][9]);
//        System.out.println(p.getListeObjetsPlateau()[3][10]);
//        System.out.println(p.getListeObjetsPlateau()[5][9]);
//        System.out.println(p.getListeObjetsPlateau()[5][7]);
//
//        System.out.println(p.getListeObjetsPlateau()[5][1]);
//        System.out.println(p.getListeObjetsPlateau()[7][4]);
//        System.out.println(p.getListeObjetsPlateau()[9][3]);
//        System.out.println(p.getListeObjetsPlateau()[9][1]);
//        System.out.println(p.getListeObjetsPlateau()[7][0]);
//
//        System.out.println(p.getListeObjetsPlateau()[7][8]);
//        System.out.println(p.getListeObjetsPlateau()[9][7]);
//        System.out.println(p.getListeObjetsPlateau()[9][5]);
//
//        System.out.println(p.getListeObjetsPlateau()[5][11]);
//        System.out.println(p.getListeObjetsPlateau()[7][12]);
//        System.out.println(p.getListeObjetsPlateau()[9][11]);
//        System.out.println(p.getListeObjetsPlateau()[9][9]);
//
//        System.out.println(p.getListeObjetsPlateau()[11][6]);
//        System.out.println(p.getListeObjetsPlateau()[13][5]);
//        System.out.println(p.getListeObjetsPlateau()[13][3]);
//        System.out.println(p.getListeObjetsPlateau()[11][2]);
//
//        System.out.println(p.getListeObjetsPlateau()[11][10]);
//        System.out.println(p.getListeObjetsPlateau()[13][9]);
//        System.out.println(p.getListeObjetsPlateau()[13][7]);


//
//        Hexagone h1 = (Hexagone)monJeu.getListePlateaux().get(1885).getListeObjetsPlateau()[3][4];
//        Hexagone h2 = (Hexagone)monJeu.getListePlateaux().get(1885).getListeObjetsPlateau()[3][8];
//        Hexagone h3 = (Hexagone)monJeu.getListePlateaux().get(1885).getListeObjetsPlateau()[7][2];
//        Hexagone h4 = (Hexagone)monJeu.getListePlateaux().get(1885).getListeObjetsPlateau()[7][6];
//        Hexagone h5 = (Hexagone)monJeu.getListePlateaux().get(1885).getListeObjetsPlateau()[7][10];
//        Hexagone h6 = (Hexagone)monJeu.getListePlateaux().get(1885).getListeObjetsPlateau()[11][4];
//        Hexagone h7 = (Hexagone)monJeu.getListePlateaux().get(1885).getListeObjetsPlateau()[11][8];

//        System.out.println("TEST : " + monJeu.getListePlateaux().get(1885).getListeObjetsPlateau()[14][8]);

//        monJeu.getListePlateaux().get(1985).getIntersections((Hexagone)monJeu.getListePlateaux().get(1985).getListeObjetsPlateau()[19][6]);

//        System.out.printf("%d %d %d %d %d %d %d %n", h1.getNum(), h2.getNum(), h3.getNum(), h4.getNum(), h5.getNum(), h6.getNum(), h7.getNum());

//        System.out.println(monJeu.getListePlateaux().get(1885).getListeObjetsPlateau()[14][4]);

//        for (Map.Entry<Integer, BoardView> line : monJeu.getListeVuesPlateaux().entrySet()) {
//            System.out.println(line.getKey() + " " + line.getValue());
//        }

        CroisementTile ctCible = null;
        for (CroisementTile ct : monJeu.getListeVuesPlateaux().get(1885).getListeCroisements()) {
            if (ct.getPlaceable().getPosX() == 4 && ct.getPlaceable().getPosY() == 6) {
                ctCible = ct;
                break;
            }
        }
        System.out.println("Croisement tile : " + ctCible);

        try {
//            monJeu.construire(((Placeable)monJeu.getListePlateaux().get(1885).getListeObjetsPlateau()[6][4]), ctCible, monJeu.getListePlateaux().get(1885), true);
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }

        for (CroisementTile ct : monJeu.getListeVuesPlateaux().get(1985).getListeCroisements()) {
            if (ct.getPlaceable().getPosX() == 6 && ct.getPlaceable().getPosY() == 6) {
                ctCible = ct;
                break;
            }
        }

        try {
            monJeu.getCurrentPlayer().construire(((Placeable)monJeu.getListePlateaux().get(1985).getListeObjetsPlateau()[6][6]), ctCible, monJeu.getListePlateaux().get(1985), true);
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }

        monJeu.setConstructionMode(true);

//        System.out.println("BEFORE");
//        for (Map.Entry<TypeRessource, Integer> resource : Jeu.getListeJoueurs().get(0).getListeRessources().entrySet()) {
//            System.out.println(resource.getKey() + " " + resource.getValue());
//        }

        Pane p = new Pane();
        Button b = new Button("TEST");
        ChoiceBox<Integer> cbo = new ChoiceBox<>();
        ArrayList<Integer> listNumbersInt = new ArrayList<>();
        for (int i = 0; i <= 20; i++) {
            listNumbersInt.add(i);
        }
        ObservableList listNumbers = FXCollections.observableList(listNumbersInt);

        cbo.setItems(listNumbers);

        b.setOnAction(e -> {
            System.out.println("Button clicked");
            System.out.println(cbo.getValue());
//            monJeu.assignEventsLabo();
//            monJeu.setLaboUpgradeMode(true);
//            monJeu.recolterRessources(8);
//            System.out.println("AFTER");
//            for (Map.Entry<TypeRessource, Integer> resource : Jeu.getListeJoueurs().get(0).getListeRessources().entrySet()) {
//                System.out.println(resource.getKey() + " " + resource.getValue());
//            }
        });

        // PETIT PLATEAU
        p.getChildren().add(monJeu.getListeVuesPlateaux().get(1885));
        // GRAND PLATEAU
//        p.getChildren().add(monJeu.getListeVuesPlateaux().get(1985));

        p.getChildren().add(b);
        p.getChildren().add(cbo);

        cbo.setTranslateX(30);
        cbo.setTranslateY(30);
        Scene mainScene = new Scene(p);
        primaryStage.setScene(mainScene);
        primaryStage.show();
//        Platform.exit();
    }
}


package model;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

import java.util.*;

import static model.TypeRessource.*;
import static model.TypeTerrain.*;


/**
 * ParamGame : Contient tous les paramètres de jeu (modifiables à souhait)
 * by Clément Jan 2019
 */
public abstract class ParamGame {

    public static final Integer MAX_LENGTH_PLAYER_NAME = 10;

    ////// Joueur /////////////////////////////////////////
    public static final Integer NB_START_RESSOURCES = 0;
    ///////////////////////////////////////////////////////

    ///// Plateau de jeu ////////////////////////////
    public static final Integer WIDTHPLATEAU = 1607;
    public static final Integer HEIGHTPLATEAU = 788;
    public static final Integer PADDINGHEXA = 2;
    /////////////////////////////////////////////////

    ///// Hexagones /////////////////////////////////
    public static final Integer RADIUS_GRAND_PLATEAU = 70;
    public static final Integer RADIUS_PETIT_PLATEAU = 100;
    /////////////////////////////////////////////////

    ///// Production ////////////////////////////////////////////////
    public static final Integer RESSOURCES_A_ENLEVER_PRODUCTION = 4;
    public static final Integer RESSOURCE_A_AJOUTER_PRODUCTION = 1;
    ////////////////////////////////////////////////////////////////

    ///// Construction //////////////////////////////////////////
    public static final Integer ADD_RESSOURCE_PER_DELOREAN = 1;
    public static final Integer ADD_RESSOURCE_PER_LABO = 2;

    public static final Integer NB_START_DELO = 2;
    /////////////////////////////////////////////////////////////

    ///// Score ////////////////////////////////////////
    static final Integer POINT_PAR_DELOREAN = 1;
    static final Integer POINT_PAR_LABO = 2;

    public static final Integer SCORE_VICTOIRE = 10;
    ////////////////////////////////////////////////////

    ////// COUT DES CONSTRUCTIONS ////////////////////////////////
    public static EnumMap<TypeRessource, Integer> prixRoute =
            new EnumMap<> (TypeRessource.class); static { prixRoute.put(BRIQUE, 1);}

    public static EnumMap<TypeRessource, Integer> prixDelorean =
            new EnumMap<> (TypeRessource.class); static { prixDelorean.put(FER, 1); prixDelorean.put(ALUMINIUM, 2);}

    public static EnumMap<TypeRessource, Integer> prixLabo =
            new EnumMap<> (TypeRessource.class); static { prixLabo.put(FER, 3); prixLabo.put(BRIQUE, 2);}
    //////////////////////////////////////////////////////////////

    public static final Integer[] plateaux = {1885, 1955, 1985, 2015};
    public static final Integer START_PLATEAU = 1985;

    public static final Map<Integer, Integer> sizePlateaux =
            new HashMap<>(); static {
                sizePlateaux.put(1885, 3);
                sizePlateaux.put(1955, 5);
                sizePlateaux.put(1985, 5);
                sizePlateaux.put(2015, 3);}

    // Liste des numéros des hexagones possibles pour un grand plateau
    static List<Integer> hexaNumbersBig = Arrays.asList(2, 3, 3, 4, 4, 4, 5, 5, 6, 6, 8, 8, 9, 9, 10, 10, 11, 11, 12);

    // Liste des numéros des hexagones possibles pour un petit plateau
    static List<Integer> hexaNumbersSmall = Arrays.asList(2, 5, 6, 8, 9, 10, 12);


    public static final Image croisementVide = new Image("/images/croisement_vide.png");
    public static final Image biffTile = new Image("/images/biff.png");

    public static final Image iconPv = new Image("icons/victory.png");
    public static final Image iconRoadMaster = new Image("icons/roadMaster.png");
    public static final Image iconBiffMaster = new Image("icons/biffMaster.png");

    // Image Delorean par joueur
    public static final Map<Color, Image> deloreanJoueur =
            new HashMap<>(); static {
                deloreanJoueur.put(Color.BLUE, new Image("/images/delorean/delorean_blue.png"));
                deloreanJoueur.put(Color.YELLOW, new Image("/images/delorean/delorean_yellow.png"));
                deloreanJoueur.put(Color.GREEN, new Image("/images/delorean/delorean_green.png"));
                deloreanJoueur.put(Color.RED, new Image("/images/delorean/delorean_red.png")); }

    // Image labo par joueur
    public static final Map<Color, Image> laboJoueur =
            new HashMap<>(); static {
        laboJoueur.put(Color.BLUE, new Image("/images/labo/labo_blue.png"));
        laboJoueur.put(Color.YELLOW, new Image("/images/labo/labo_yellow.png"));
        laboJoueur.put(Color.GREEN, new Image("/images/labo/labo_green.png"));
        laboJoueur.put(Color.RED, new Image("/images/labo/labo_red.png")); }


    // Texture hexagone terrain
    public static final Map<TypeTerrain, ImageView> imagesTerrains =
            new EnumMap<>(TypeTerrain.class); static {
                imagesTerrains.put(MINE_ALU, new ImageView("/images/terrains/alu.jpg"));
                imagesTerrains.put(MINE_FER, new ImageView("/images/terrains/fer.jpg"));
                imagesTerrains.put(GISEMENT, new ImageView("/images/terrains/gisement.jpg"));
                imagesTerrains.put(MINE_OR, new ImageView("/images/terrains/or.jpg"));
                imagesTerrains.put(CARRIERE, new ImageView("/images/terrains/carriere.jpg"));}

    // Couleur hexagone terrain (provisoir)
    public static final Map<TypeTerrain, Color> couleursTerrain =
            new EnumMap<>(TypeTerrain.class); static {
                couleursTerrain.put(MINE_ALU, Color.rgb(234,240,249));
                couleursTerrain.put(MINE_FER, Color.rgb(144,148,155));
                couleursTerrain.put(GISEMENT, Color.rgb(59,61,66));
                couleursTerrain.put(MINE_OR, Color.rgb(255,225,0));
                couleursTerrain.put(CARRIERE, Color.rgb(255,183,218));
                couleursTerrain.put(CENTRALE, Color.rgb(66,244,140));}

    // Image dés //////////////////////////////////////////////////////
    private static final Image dice2 = new Image("icons/dices/dice2.png");
    private static final Image dice3 = new Image("icons/dices/dice3.png");
    private static final Image dice4 = new Image("icons/dices/dice4.png");
    private static final Image dice5 = new Image("icons/dices/dice5.png");
    private static final Image dice6 = new Image("icons/dices/dice6.png");
    private static final Image dice7 = new Image("icons/dices/dice7.png");
    private static final Image dice8 = new Image("icons/dices/dice8.png");
    private static final Image dice9 = new Image("icons/dices/dice9.png");
    private static final Image dice10 = new Image("icons/dices/dice10.png");
    private static final Image dice11 = new Image("icons/dices/dice11.png");
    private static final Image dice12 = new Image("icons/dices/dice12.png");

    public static final List<Image> imagesDices = Arrays.asList(null, null, dice2, dice3, dice4, dice5, dice6, dice7, dice8, dice9, dice10, dice11, dice12);
    /////////////////////////////////////////////////////////////////////

    ///// Icons ressource ////////////////////////////////////////////////////////////////
    private static final Image iconOr = new Image("icons/gold.png");
    private static final Image iconPetrole = new Image("icons/oil.png");
    private static final Image iconBrique = new Image("icons/brick.png");
    private static final Image iconPluto = new Image("icons/pluto.png");
    private static final Image iconFer = new Image("icons/iron.png");
    private static final Image iconAlu = new Image("icons/alu.png");

    public static final List<Image> iconsRessources = Arrays.asList(iconOr, iconPetrole, iconBrique, iconPluto, iconFer, iconAlu);
    //////////////////////////////////////////////////////////////////////////////////////

    // Types de terrain disponibles en 1985
    static Map<TypeTerrain, Integer> types1985 =
            new EnumMap<> (TypeTerrain.class); static {
                types1985.put(MINE_ALU, 7);
                types1985.put(MINE_FER, 6);
                types1985.put(CARRIERE, 6);}

    // Types de terrain disponibles en 1955
    static Map<TypeTerrain, Integer> types1955 =
            new EnumMap<> (TypeTerrain.class); static {
        types1955.put(MINE_ALU, 6);
        types1955.put(MINE_FER, 7);
        types1955.put(GISEMENT, 6);}

    // Types de terrain disponibles en 1885
    static Map<TypeTerrain, Integer> types1885 =
            new EnumMap<> (TypeTerrain.class); static {
        types1885.put(MINE_OR, 4);
        types1885.put(MINE_FER, 2);
        types1885.put(CARRIERE, 1);}

    // Types de terrain disponibles en 2015
    static Map<TypeTerrain, Integer> types2015 =
            new EnumMap<> (TypeTerrain.class); static {
        types2015.put(MINE_OR, 2);
        types2015.put(MINE_ALU, 1);
        types2015.put(CENTRALE, 4);}

    public static final EnumMap<TypeTerrain, TypeRessource> typesTerrainsRessources =
            new EnumMap<> (TypeTerrain.class); static {
            int i = 0;
            for (TypeTerrain tt : TypeTerrain.values()) {
                typesTerrainsRessources.put(tt, TypeRessource.values()[i]);
                i++;
            }
    }
}

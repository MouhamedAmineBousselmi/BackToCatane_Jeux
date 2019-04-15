package model;

import java.util.*;

/**
 * Plateau : Représente un plateau de jeu, et contient un tableau 2 dim
 * qui contient tous les objets du plateau (hexagones, Delorean, Route...)
 * by Clément Jan 2019
 */
public class Plateau {
    private Integer epoque;
    private ObjetPlateau[][] listeObjetsPlateau;

    private Integer XMax;
    private Integer YMax;

    /***
     * Constructeur de plateau de taille passée en paramètre
     * @param epoque Epoque du plateau
     */
    public Plateau(Integer epoque) {
        this.epoque = epoque;

        // Calcule de la taille du tableau en mémoire
        this.XMax = calculXMax(ParamGame.sizePlateaux.get(epoque));
        this.YMax = calculYMax(ParamGame.sizePlateaux.get(epoque));

        // Création du tableau
        this.listeObjetsPlateau = new ObjetPlateau[this.YMax][this.XMax];

        // Génère les emplacements des objets sur le plateau
        initPlateau();
    }

    /***
     * Calcule de la taille horizontale du tableau "physique" de jeu
     * @param nbLignes Nombre de ligne d'hexagone du plateau "physique"
     * @return Integer : Xmax
     */
    private Integer calculXMax(Integer nbLignes) {
        return (nbLignes * 4) + 1;
    }

    /***
     * Calcule de la taille verticale du tableau "physique" de jeu
     * @param nbLignes Nombre de lignes d'hexagone du plateau "physique"
     * @return Integer : Ymax
     */
    private Integer calculYMax(Integer nbLignes) {
        return (((nbLignes + 1) / 2) * 7) + ((nbLignes - 1) / 2);
    }

    /**
     * Génère un nombre dans l'interval [borneInf, borneSup]
     * @param borneInf Borne inférieure
     * @param borneSup Borne supérieure
     * @return Nombre pseudo aléatoire
     */
    public Integer generateNumber(Integer borneInf, Integer borneSup)
    {
        return (int) (Math.random() * (borneSup - borneInf)) + borneInf;
    }

    /***
     * Génère la liste des type de terrains (des hexagones) en fonction de l'époque du plateau
     * et en respectant les quantités de chaque terrain définies dans la classe ParamGame
     * @return Liste de type de terrain aléatoire
     */
    private ArrayList<TypeTerrain> generateTypesTerrain() {
        ArrayList<TypeTerrain> res = new ArrayList<>();

        // Genère la liste des terrains en fonctions des quantités
        // récupérées dans la classe ParamGame
        switch (this.epoque) {
            case 1885:
                for (Map.Entry<TypeTerrain, Integer> t : ParamGame.types1885.entrySet()) {
                    for (int i = 0; i < t.getValue(); i++) {
                        res.add(t.getKey());
                    }
                }
                break;
            case 1955:
                for (Map.Entry<TypeTerrain, Integer> t : ParamGame.types1955.entrySet()) {
                    for (int i = 0; i < t.getValue(); i++) {
                        res.add(t.getKey());
                    }
                }
                break;
            case 1985:
                for (Map.Entry<TypeTerrain, Integer> t : ParamGame.types1985.entrySet()) {
                    for (int i = 0; i < t.getValue(); i++) {
                        res.add(t.getKey());
                    }
                }
                break;
            case 2015:
                for (Map.Entry<TypeTerrain, Integer> t : ParamGame.types2015.entrySet()) {
                    for (int i = 0; i < t.getValue(); i++) {
                        res.add(t.getKey());
                    }
                }
                break;
        }
        // Mélanger la liste des type de terrain
        Collections.shuffle(res);
        return res;
    }

    /***
     * Génère la liste des numéros des hexagones en fonction de l'époque du plateau
     * @return Liste de numéros d'hexagones
     */
    private ArrayList<Integer> generateNumeros() {
        ArrayList<Integer> res = new ArrayList<>();

        // Petit plateau
        if (this.epoque == 1885 || this.epoque == 2015) {
            // Melange les numéros
            Collections.shuffle(ParamGame.hexaNumbersSmall);
            res.addAll(ParamGame.hexaNumbersSmall);
        }
        // Grand plateau
        else {
            // Melange les numéros
            Collections.shuffle(ParamGame.hexaNumbersBig);
            res.addAll(ParamGame.hexaNumbersBig);
        }
        return res;
    }

    /***
     * Construit le plateau
     * Génère les emplacements des routes, intersections et les hexagones
     */
    private void initPlateau(){
        initHexagones();
        initIntersections();
        initRoutes();
    }

    /***
     * Génère les objets Hexagone aux centre des hexagone dans le tableau en mémoire
     */
    private void initHexagones() {

        // Initialise types terrain et numéros des hexagones
        ArrayList<TypeTerrain> typesT = generateTypesTerrain();
        ArrayList<Integer> numeros = generateNumeros();

        int offset = 0;
        int xDecal = 0;
        int indexTabTypeNum = 0;

        // Génére hexagone (centre hexagone)
          // Partie haute au tableau
        for (int y = (this.YMax/2); y >= 3; y -= 4) {
            xDecal = (2 * offset);
            for (int x = 2 + xDecal; x < this.XMax - xDecal; x += 4) {
                this.listeObjetsPlateau[y][x] = new Hexagone(typesT.get(indexTabTypeNum), numeros.get(indexTabTypeNum), x, y);
                indexTabTypeNum++;
            }
            offset++;
        }

          // Partie basse au tableau
        offset = 1;
        for (int y = (this.YMax/2) + 4; y < this.YMax; y += 4) {
            xDecal = (2 * offset);
            for (int x = 2 + xDecal; x < this.XMax - xDecal; x += 4) {
                this.listeObjetsPlateau[y][x] = new Hexagone(typesT.get(indexTabTypeNum), numeros.get(indexTabTypeNum), x, y);
                indexTabTypeNum++;
            }
            offset++;
        }
    }

    /***
     * Génère les positions des intersections des hexagones dans le tableau en mémoire
     */
    private void initIntersections() {
        int offset = 0;
        int xDecal = 0;
        // Génère intersections des hexagones
          // Partie haute au tableau
        for (int y = (this.YMax/2) - 1; y >= 0; y -= 2) {
            if (offset % 2 == 1)
                xDecal = (offset + 1);

            for (int x = xDecal; x < this.XMax - xDecal; x += 4) {
                this.listeObjetsPlateau[y][x] = new Placeable(null, x, y);
            }
            offset++;
        }

        // Partie basse au tableau
        offset = 0;
        xDecal = 0;
        for (int y = (this.YMax/2) + 1; y < this.YMax; y += 2) {
            if (offset % 2 == 1)
                xDecal = (offset + 1);

            for (int x = xDecal; x < this.XMax - xDecal; x += 4) {
                this.listeObjetsPlateau[y][x] = new Placeable(null, x, y);
            }
            offset++;
        }
    }

    /***
     * Génète l'emplacement des routes dans le tableau en mémoire
     */
    private void initRoutes() {
        int offset = 0;
        int yOffset = 0;
        // Génère routes
          // Partie haute au tableau
        for (int y = (this.YMax/2); y > 0; y -= 2) {
            if (offset%2 == 1)
                yOffset = 2;
            else
                yOffset = 4;

            for (int x = offset; x < this.XMax - offset; x += yOffset) {
                this.listeObjetsPlateau[y][x] = new Route(null, x, y);
            }
            offset++;
        }

        // Partie basse au tableau
        offset = 0;
        yOffset = 0;
        for (int y = (this.YMax/2); y < this.YMax; y += 2) {
            if (offset%2 == 1)
                yOffset = 2;
            else
                yOffset = 4;

            for (int x = offset; x < this.XMax - offset; x += yOffset) {
                this.listeObjetsPlateau[y][x] = new Route(null, x, y);
            }
            offset++;
        }
    }

    /**
     * Vérifier les règles de construction d'un "placeable" Route ou Delorean
     * @param p Objet à construire (Route, Delorean)
     * @param j Joueur qui veut construire
     * @param freeConstruction mettre true pour pouvoir poser la première Delorean
     * @return Boolean : true si constructible
     */
    boolean isBuildable(Placeable p, Joueur j, boolean freeConstruction) {
        boolean res = false;

        // Construction d'une route
        if (p instanceof Route) {
            // Si la route n'a pas de propriétaire
            if (p.isAvailable()) {
                ArrayList<Placeable> croisementsAdj = this.getCroisementsAdjacentsARoute((Route)p);
                // Vérifie si un des 2 croisements adjacents à la route
                // m'appartient
                for (Placeable pl : croisementsAdj) {
                    if (pl.getProprietaire() == j) {
                        res = true;
                        break;
                    }
                }
                // Ou si une route adjacente m'appartient
                if (!res) {
                    ArrayList<Route> routesAdj = this.getRoutesAdjacentes((Route)p);

                    for (Route r : routesAdj) {
                        if (r.getProprietaire() == j) {
                            res = true;
                            break;
                        }
                    }
                }
            }
        }
        // Construction d'une Delorean
        else {
            // Si ce n'est pas les Delorean placées au départ
            if (!freeConstruction) {
                // Avoir une route adjacente à soit
                ArrayList<Route> routesAdj = this.getRoutesAdjacentes(p);
                for (Route r : routesAdj) {
                    if (r.getProprietaire() == j) {
                        res = true;
                        break;
                    }
                }
            }
            else {
                res = true;
            }

            if (res) {
                // Les intersections autour de la construction doivent être vide (propriétaire == null)
                ArrayList<Placeable> croisementsAdj = this.getCroisementAdjacentsAPlaceable(p);
                for (Placeable pl : croisementsAdj) {
                    if (!pl.isAvailable()) {
                        res = false;
                        break;
                    }
                }
            }
        }
        return res;
    }

    /**
     * Retourne la liste de tous les hexagones du plateau
     * dans l'ordre de la construction du plateau
     * @return Liste des hexagones du plateau
     */
    public List<Hexagone> getHexagones() {
        ArrayList<Hexagone> res = new ArrayList<>();

        Integer offset = 0;
        Integer xDecal = 0;
        // Partie haute au tableau
        for (int y = (this.YMax/2); y >= 3; y -= 4) {
            xDecal = (2 * offset);
            for (int x = 2 + xDecal; x < this.XMax - xDecal; x += 4) {
                res.add((Hexagone)this.listeObjetsPlateau[y][x]);
            }
            offset++;
        }

        // Partie basse au tableau
        offset = 1;
        for (int y = (this.YMax/2) + 4; y < this.YMax; y += 4) {
            xDecal = (2 * offset);
            for (int x = 2 + xDecal; x < this.XMax - xDecal; x += 4) {
                res.add((Hexagone)this.listeObjetsPlateau[y][x]);
            }
            offset++;
        }
        return res;
    }

    /**
     * Retourne la liste de tous les hexagones (centre) du plateau
     * dans l'ordre (1er hexagone en haut a gauche du plateau)
     * @return Liste des hexagones du plateau
     */
    public ArrayList<Hexagone> getHexagonesOrdre() {
        ArrayList<Hexagone> res = new ArrayList<>();

        for (int i = 0; i < this.listeObjetsPlateau.length; i++) {
            for(ObjetPlateau o : this.listeObjetsPlateau[i]) {
                if (o instanceof Hexagone) {
                    res.add((Hexagone)o);
                }
            }
        }
        return res;
    }

    /***
     * Obtenir la liste des routes en fonction de la position d'un hexagone dans le tableau
     * La liste commence par la route en haut à gauche de l'hexagone cible
     * @param h Hexagone cible
     * @return Liste des routes autour de l'hexagone cible
     */
    public ArrayList<Route> getRoutes(Hexagone h) {
        ArrayList<Route> res = new ArrayList<>();
        // Ajouter les 6 emplacements potentiels de route
            // Haut gauche
        res.add((Route)this.listeObjetsPlateau[h.getPosY() - 2][h.getPosX() - 1]);
            // Haut droite
        res.add((Route)this.listeObjetsPlateau[h.getPosY() - 2][h.getPosX() + 1]);
        // Droite
        res.add((Route)this.listeObjetsPlateau[h.getPosY()][h.getPosX() + 2]);
        // Bas gauche
        res.add((Route)this.listeObjetsPlateau[h.getPosY() + 2][h.getPosX() + 1]);
        // Bas droite
        res.add((Route)this.listeObjetsPlateau[h.getPosY() + 2][h.getPosX() - 1]);
        // Gauche
        res.add((Route)this.listeObjetsPlateau[h.getPosY()][h.getPosX() - 2]);

        return res;
    }

    /***
     * Obtenir la liste des croisements en fonction de la position d'un hexagone dans le tableau
     * La liste commence par le croisement en haut à gauche de l'hexagone cible
     * @param h Hexagone cible
     * @return Liste des croisements autour de l'hexagone cible
     */
    public ArrayList<Placeable> getIntersections(Hexagone h) {
        ArrayList<Placeable> res = new ArrayList<>();
        // Ajouter les 6 emplacements potentiels de route
        // Haut
        res.add((Placeable)this.listeObjetsPlateau[h.getPosY() - 3][h.getPosX()]);
        // Haut Droite
        res.add((Placeable)this.listeObjetsPlateau[h.getPosY() - 1][h.getPosX() + 2]);
        // Bas droite
        res.add((Placeable)this.listeObjetsPlateau[h.getPosY() + 1][h.getPosX() + 2]);
        // Bas
        res.add((Placeable)this.listeObjetsPlateau[h.getPosY() + 3][h.getPosX()]);
        // Bas gauche
        res.add((Placeable)this.listeObjetsPlateau[h.getPosY() + 1][h.getPosX() - 2]);
        // Haut gauche
        res.add((Placeable) this.listeObjetsPlateau[h.getPosY() - 1][h.getPosX() - 2]);

        return res;
    }

    /***
     * Obtenir la liste des croisements adjacents à la route "r" en param
     * Ordre des croisements : haut/bas gauche, puis dans le sens des aiguilles d'une montre
     * @param r Route
     * @return Liste des croisements
     */
    private ArrayList<Placeable> getCroisementsAdjacentsARoute(Route r) {
        ArrayList<Placeable> res = new ArrayList<>();
        Placeable pt;

        boolean smallBoard = ParamGame.sizePlateaux.get(this.epoque) == 3;

        /* Schéma 2
            i
            R
            i
         */

        if ((r.getPosX() % 2) == 0) {
            if (r.getPosY() - 1 >= 0) {
                pt = (Placeable) this.listeObjetsPlateau[r.getPosY() - 1][r.getPosX()];
                if (pt != null) res.add(pt);
            }

            if (r.getPosY() + 1 < this.listeObjetsPlateau.length) {
                pt = (Placeable) this.listeObjetsPlateau[r.getPosY() + 1][r.getPosX()];
                if (pt != null) res.add(pt);
            }
        }
        else {
            int x = ((r.getPosX() - 1) / 2) % 2;
            int y = ((r.getPosY() - 1) / 4) % 2;
            int sum = x + y;

            /* Schéma 3
               i
                R
                 i
             */
            if ((sum % 2 == 0 && smallBoard) || (sum % 2 != 0 && !smallBoard)) {
                if (r.getPosX() - 1 >= 0 && r.getPosY() - 1 >= 0) {
                    pt = (Placeable) this.listeObjetsPlateau[r.getPosY() - 1][r.getPosX() - 1];
                    if (pt != null) res.add(pt);
                }

                if (r.getPosY() + 1 < this.listeObjetsPlateau.length && r.getPosX() + 1 < this.listeObjetsPlateau[0].length) {
                    pt = (Placeable) this.listeObjetsPlateau[r.getPosY() + 1][r.getPosX() + 1];
                    if (pt != null) res.add(pt);
                }
            }
            /* Schéma 1
                  i
                R
              i
             */
            else {
                if (r.getPosX() - 1 >= 0 && r.getPosY() + 1 < this.listeObjetsPlateau.length) {
                    pt = (Placeable) this.listeObjetsPlateau[r.getPosY() + 1][r.getPosX() - 1];
                    if (pt != null) res.add(pt);
                }
                if (r.getPosY() - 1 >= 0 && r.getPosX() + 1 < this.listeObjetsPlateau[0].length) {
                    pt = (Placeable) this.listeObjetsPlateau[r.getPosY() - 1][r.getPosX() + 1];
                    if (pt != null) res.add(pt);
                }
            }
        }
        return res;
    }

    /***
     * Obtenir la liste des croisements adjacents au croisement "p" en param
     * Ordre des croisements : haut gauche, puis dans le sens des aiguilles d'une montre
     * @param p Placeable (correspondant à un croisement)
     * @return Liste des croisements
     */
    private ArrayList<Placeable> getCroisementAdjacentsAPlaceable(Placeable p) {
        ArrayList<Placeable> res = new ArrayList<>();

        int line = (p.getPosY()/2) % 2;
        Placeable pt;
        /*
        Schéma en Y
         I   I
          R R
           I
           R
           I
         */
        if (line == 1) {
            if (p.getPosY() - 2 >= 0 && p.getPosX() - 2 >= 0) {
                pt = (Placeable)this.listeObjetsPlateau[p.getPosY() - 2][p.getPosX() - 2];
                if (pt != null) res.add(pt);
            }

            if (p.getPosY() - 2 >= 0 && p.getPosX() + 2 < this.listeObjetsPlateau[0].length) {
                pt = (Placeable)this.listeObjetsPlateau[p.getPosY() - 2][p.getPosX() + 2];
                if (pt != null) res.add(pt);
            }

            if (p.getPosY() + 2 < this.listeObjetsPlateau[0].length) {
                pt = (Placeable)this.listeObjetsPlateau[p.getPosY() + 2][p.getPosX()];
                if (pt != null) res.add(pt);
            }
        }
        /*
        Schéma en lambda
           I
           R
           I
          R R
         I   I
         */
        else {
            if (p.getPosY() - 2 >= 0) {
                pt = (Placeable) this.listeObjetsPlateau[p.getPosY() - 2][p.getPosX()];
                if (pt != null) res.add(pt);
            }

            if (p.getPosX() + 2 < this.listeObjetsPlateau[0].length && p.getPosY() + 2 < this.listeObjetsPlateau[0].length) {
                pt = (Placeable) this.listeObjetsPlateau[p.getPosY() + 2][p.getPosX() + 2];
                if (pt != null) res.add(pt);
            }

            if (p.getPosX() - 2 >= 0 && p.getPosY() + 2 < this.listeObjetsPlateau[0].length) {
                pt = (Placeable) this.listeObjetsPlateau[p.getPosY() + 2][p.getPosX() - 2];
                if (pt != null) res.add(pt);
            }
        }
        return res;
    }

    /***
     * Obtenir la liste des routes adjacentes a un croisement (Placeable)
     * @param p Croisement (Placeable)
     * @return Liste des routes adjacentes au croisement (Placeable) p
     */
    private ArrayList<Route> getRoutesAdjacentes(Placeable p) {
        ArrayList<Route> res = new ArrayList<>();

        int line = (p.getPosY()/2) % 2;
        Placeable pt;
        /*
        Schéma en Y
          R R
           I
           R
         */
        if (line == 1) {
            if (p.getPosY() - 1 >= 0 && p.getPosX() - 1 >= 0) {
                pt = (Placeable)this.listeObjetsPlateau[p.getPosY() - 1][p.getPosX() - 1];
                if (pt != null) res.add((Route)pt);
            }

            if (p.getPosY() - 1 >= 0 && p.getPosX() + 1 < this.listeObjetsPlateau[0].length) {
                pt = (Placeable)this.listeObjetsPlateau[p.getPosY() - 1][p.getPosX() + 1];
                if (pt != null) res.add((Route)pt);
            }

            if (p.getPosY() + 1 < this.listeObjetsPlateau[0].length) {
                pt = (Placeable)this.listeObjetsPlateau[p.getPosY() + 1][p.getPosX()];
                if (pt != null) res.add((Route)pt);
            }
        }
        /*
        Schéma en lambda
           R
           I
          R R
         */
        else {
            if (p.getPosY() - 1 >= 0) {
                pt = (Placeable) this.listeObjetsPlateau[p.getPosY() - 1][p.getPosX()];
                if (pt != null) res.add((Route) pt);
            }

            if (p.getPosX() + 1 < this.listeObjetsPlateau[0].length && p.getPosY() + 1 < this.listeObjetsPlateau[0].length) {
                pt = (Placeable) this.listeObjetsPlateau[p.getPosY() + 1][p.getPosX() + 1];
                if (pt != null) res.add((Route) pt);
            }

            if (p.getPosX() - 1 >= 0 && p.getPosY() + 1 < this.listeObjetsPlateau[0].length) {
                pt = (Placeable) this.listeObjetsPlateau[p.getPosY() + 1][p.getPosX() - 1];
                if (pt != null) res.add((Route) pt);
            }
        }
        return res;
    }

    /***
     * Obtenir la liste des routes adjacentes a une route
     * @param r Route cible
     * @return Liste des routes adjacentes à la route r
     */
    private ArrayList<Route> getRoutesAdjacentes(Route r) {
        ArrayList<Route> res = new ArrayList<>();
        Route rt;

        boolean smallBoard = ParamGame.sizePlateaux.get(this.epoque) == 3;

        /* Schéma 2
           R R
            i
            R
            i
           R R
         */
        if ((r.getPosX() % 2) == 0) {

            if (r.getPosY() - 2 >= 0 && r.getPosX() - 1 >= 0) {
                rt = (Route) this.listeObjetsPlateau[r.getPosY() - 2][r.getPosX() - 1];
                if (rt != null) res.add(rt);
            }

            if (r.getPosY() - 2 >= 0 && r.getPosX() + 1 < this.listeObjetsPlateau[0].length) {
                rt = (Route) this.listeObjetsPlateau[r.getPosY() - 2][r.getPosX() + 1];
                if (rt != null) res.add(rt);
            }

            if (r.getPosY() + 2 < this.listeObjetsPlateau[0].length && r.getPosX() + 1 < this.listeObjetsPlateau[0].length) {
                rt = (Route) this.listeObjetsPlateau[r.getPosY() + 2][r.getPosX() + 1];
                if (rt != null) res.add(rt);
            }

            if (r.getPosX() - 1 >= 0 && r.getPosY() + 2 < this.listeObjetsPlateau[0].length) {
                rt = (Route) this.listeObjetsPlateau[r.getPosY() + 2][r.getPosX() - 1];
                if (rt != null) res.add(rt);
            }
        }
        else {
            int x = ((r.getPosX() - 1) / 2) % 2;
            int y = ((r.getPosY() - 1) / 4) % 2;
            int sum = x + y;

            /* Schéma 3
               R
             R i
                R
                 i R
                 R
             */
            if ((sum % 2 == 0 && smallBoard) || (sum % 2 != 0 && !smallBoard)) {
                if (r.getPosX() - 2 >= 0) {
                    rt = (Route) this.listeObjetsPlateau[r.getPosY()][r.getPosX() - 2];
                    if (rt != null) res.add(rt);
                }

                if (r.getPosY() - 2 >= 0 && r.getPosX() - 1 >= 0) {
                    rt = (Route) this.listeObjetsPlateau[r.getPosY() - 2][r.getPosX() - 1];
                    if (rt != null) res.add(rt);
                }

                if (r.getPosX() + 2 < this.listeObjetsPlateau[0].length) {
                    rt = (Route) this.listeObjetsPlateau[r.getPosY()][r.getPosX() + 2];
                    if (rt != null) res.add(rt);
                }

                if (r.getPosX() + 1 < this.listeObjetsPlateau[0].length && r.getPosY() + 2 < this.listeObjetsPlateau[0].length) {
                    rt = (Route) this.listeObjetsPlateau[r.getPosY() + 2][r.getPosX() + 1];
                    if (rt != null) res.add(rt);
                }
            }

            /* Schéma 1
                   R
                  i R
                R
            R i
              R
             */
            else {
                if (r.getPosX() - 2 >= 0) {
                    rt = (Route) this.listeObjetsPlateau[r.getPosY()][r.getPosX() - 2];
                    if (rt != null) res.add(rt);
                }

                if (r.getPosY() - 2 >= 0 && r.getPosX() + 1 < this.listeObjetsPlateau[0].length) {
                    rt = (Route) this.listeObjetsPlateau[r.getPosY() - 2][r.getPosX() + 1];
                    if (rt != null) res.add(rt);
                }

                if (r.getPosX() + 2 < this.listeObjetsPlateau[0].length) {
                    rt = (Route) this.listeObjetsPlateau[r.getPosY()][r.getPosX() + 2];
                    if (rt != null) res.add(rt);
                }

                if (r.getPosX() - 1 >= 0 && r.getPosY() + 2 < this.listeObjetsPlateau[0].length) {
                    rt = (Route) this.listeObjetsPlateau[r.getPosY() + 2][r.getPosX() - 1];
                    if (rt != null) res.add(rt);
                }
            }
        }
        return res;
    }

    /***
     * Retourne la liste des objets du plateau
     * @return Liste d'objets du plateau
     */
    public ObjetPlateau[][] getListeObjetsPlateau() {
        return listeObjetsPlateau;
    }

    /***
     * Retourne l'epoque du plateau (integer)
     * @return Integer : epoque du plateau
     */
    public Integer getEpoque() { return epoque; }

    /***
     * Affiche le layout du tableau de jeu en mémoire
     */
    public void printTable() {
        System.out.println("\n---------------------------------------------------------------------------------------");
        System.out.printf("   ");
        for (int k = 0; k < this.XMax; k++) {
            System.out.printf("| %d ", k%10);
        }
        System.out.printf("|%n");
        int i = 0, j = 0;
        for (i = 0; i < this.YMax; i++) {
            System.out.printf("%2d ", i);
            for (j = 0; j < this.XMax; j++) {
                if (this.listeObjetsPlateau[i][j] instanceof Hexagone)
                    System.out.print("| H ");
                else if (this.listeObjetsPlateau[i][j] instanceof Route)
                    System.out.print("| R ");
                else if (this.listeObjetsPlateau[i][j] instanceof Placeable)
                    System.out.print("| P ");
                else
                    System.out.print("| - ");
            }
            System.out.print("|\n");
        }
        System.out.println("--------------------------------------------------------------------------------------");
    }

    @Override
    public String toString() {
        return "Plateau{" +
                "epoque=" + epoque +
                ", listeObjetsPlateau=" + Arrays.toString(listeObjetsPlateau) +
                '}' + '\n';
    }
}

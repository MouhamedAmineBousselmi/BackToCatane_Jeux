package controller;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.*;
import view.BoardView;

import java.io.IOException;
import java.net.URL;
import java.util.*;

/**
 * CommerceControl : Controller lié à la vue JavaFX principale du jeu mainGame.fxml
 * by Nicolas and Clément Jan 2019
 */
public class MainGameControl implements Initializable {
//    @FXML
//    private StackPane stackPaneSlide;
//    @FXML
//    private Button buttonSlide;

    private int iterateurRefus;

    @FXML
    private TabPane tabPanePlateau;
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private HBox hBoxNoms;

    @FXML
    private Button buttonDice;
    @FXML
    private Button buttonCommerce;
    @FXML
    private Button buttonProduction;
    @FXML
    private Button buttonConstruction;
    @FXML
    private Button btnUpdateToLabo;
    @FXML
    private Button buttonNextPlayer;

    private ArrayList<GridPane> gridPaneList;

    private List<Button> actionButtons;

    @FXML
    private ImageView imageViewDice;

    /**
     * Génère la fenetre de jeu
     * @param location Location
     * @param resources Ressource
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.gridPaneList = new ArrayList<>();

        launchNewGame();
        creerEtatsJoueurs();

        actionButtons = Arrays.asList(buttonDice, buttonCommerce, buttonConstruction, buttonProduction, btnUpdateToLabo, buttonNextPlayer);

        nextPlayer();
        tabPanePlateau.getSelectionModel().select(2);
    }

    /**
     * Réinitialise le tour pour le prochain joueur
     */
    private void initTour() {
        iterateurRefus = 0;
        setDisableOtherButtons(buttonDice, true);

        imageViewDice.setVisible(false);
        buttonDice.setDisable(false);
    }

    /**
     * Passe au joueur suivant
     */
    public void nextPlayer() {
        Jeu.getInstance().joueurSuivant();
        // Cache les ressources des autres joueurs
        hideGridPane();

        // Si les Delorean de départ sont déjà posées
        if (!Jeu.getInstance().isFreeConstruction()) {
            initTour();
        }
        // Si un joueur doit encore poser ses Delorean de départ
        else {
            boolean startDeloOk = true;
            for (Joueur j : Jeu.getListeJoueurs()) {
                if (j.getListeDelorean().size() != 2) {
                    startDeloOk  = false;
                    break;
                }
            }
            Jeu.getInstance().setFreeConstruction(!startDeloOk);
            if (!Jeu.getInstance().isFreeConstruction()) {
                initTour();
            }
            else {
                buttonConstruction.setDisable(false);
                buttonConstruction.fire();
                buttonConstruction.setDisable(true);
            }
        }
    }

    /**
     * Bloque/Débloque le bouton production si le joueur possède ou non
     * au moins un laboratoire
     */
    public void verifyBlockButtonProduction(){
        buttonProduction.setDisable(!Jeu.getInstance().getCurrentPlayer().isHasLabo());
    }

//    /**
//     * Permet de gérer le slider pour afficher les cartes. (rip)
//     */
//    public void addSlider(){
//
//        Region sliderContent = new Region();
//        sliderContent.setPrefWidth(700);
//        sliderContent.setStyle("-fx-background-color: red; -fx-border-color: orange; -fx-border-width: 5;");
//
//
//        HBox slider = new HBox(sliderContent, buttonSlide);
//        slider.setAlignment(Pos.CENTER);
//        slider.setPrefWidth(Region.USE_COMPUTED_SIZE);
//        slider.setMaxWidth(Region.USE_PREF_SIZE);
//
//        slider.setTranslateX(-sliderContent.getPrefWidth());
//        StackPane.setAlignment(slider, Pos.BOTTOM_LEFT);
//
//        Timeline timeline = new Timeline(
//                new KeyFrame(Duration.ZERO, new KeyValue(slider.translateXProperty(), -sliderContent.getPrefWidth())),
//                new KeyFrame(Duration.millis(500), new KeyValue(slider.translateXProperty(), 0d))
//        );
//
//        buttonSlide.setOnAction(evt -> {
//            // adjust the direction of play and start playing, if not already done
//            String text = buttonSlide.getText();
//            boolean playing = timeline.getStatus() == Animation.Status.RUNNING;
//            if (">".equals(text)) {
//                timeline.setRate(2);
//                if (!playing) {
//                    timeline.playFromStart();
//                }
//                buttonSlide.setText("<");
//            } else {
//                timeline.setRate(-2);
//                if (!playing) {
//                    timeline.playFrom("end");
//                }
//                buttonSlide.setText(">");
//            }
//        });
//
//        stackPaneSlide.getChildren().add(slider);
//    }

    /**
     * Permet de créer un pane pour chaque joueur ainsi que le nom de chaque joueur et sa couleur.
     */
    private void creerEtatsJoueurs(){
        int iterateur = 0;
        ArrayList<String> listColors = Jeu.getInstance().getPlayersPageControl().getColors();
        List<Joueur> listPlayers = Jeu.getListeJoueurs();
        for(Joueur joueur: listPlayers) {
            Label labelName = new Label(joueur.getNom());
            labelName.setTranslateX(10);
            labelName.setTranslateY(5);
            labelName.setFont(Font.font("AR DARLING", 30));

            Circle colorCircle = new Circle(300, 25, 20, Color.web(listColors.get(iterateur)));

            GridPane gpJoueur = createGridPaneSummary(joueur);
            Pane paneJoueur = createBottomSummary(joueur);

            paneJoueur.getStyleClass().add("paneJoueur");
            paneJoueur.setPrefSize(400, Double.MAX_VALUE);

            paneJoueur.getChildren().addAll(labelName, colorCircle);
            paneJoueur.getChildren().add(gpJoueur);
            hBoxNoms.getChildren().add(paneJoueur);

            iterateur++;
        }
    }


    /**
     * Permet de créer le grid Pane récapitulatif de chaque joueur qui contient toutes ses ressources.
     */
    private GridPane createGridPaneSummary(Joueur j){
        GridPane gridPaneJoueur = new GridPane();

        // Generate constaints
        for (int i = 0; i < 6; i++) {
            gridPaneJoueur.getColumnConstraints().add(new ColumnConstraints(60 + (i%2) * 10));
        }

        // Ajout des icons dans le tableau de chaque joueur
        int colIndex = 0;
        int rowIndex = 0;
        for (Image image : ParamGame.iconsRessources) {
            gridPaneJoueur.add(new ImageView(image), colIndex, rowIndex);
            colIndex += 2;
            colIndex %= 6;
            if (colIndex == 0) {
                rowIndex = 1;
            }
        }

        // Ajout des labels du joueur
        colIndex = 1;
        rowIndex = 0;
        for (Map.Entry<TypeLabel, Label> entry : j.getListeLabels().entrySet()) {
            if (entry.getKey() != TypeLabel.LABEL_SCORE) {
                gridPaneJoueur.add(entry.getValue(),colIndex, rowIndex);
                colIndex += 2;
                colIndex %= 6;
                if (colIndex == 1) {
                    rowIndex = 1;
                }
            }
        }

        gridPaneJoueur.setTranslateX(30);
        gridPaneJoueur.setTranslateY(90);

        this.gridPaneList.add(gridPaneJoueur);

        return gridPaneJoueur;
    }

    /**
     * Permet de créer le pied de page du récapitulatif de chaque joueur avec son nombre de points de victoire et ses titres si il en a.
     */
    private Pane createBottomSummary(Joueur j){
        Pane paneJoueur = new Pane();

        // Icon PV
        ImageView ivPV = new ImageView(ParamGame.iconPv);
        ivPV.setTranslateX(30);
        ivPV.setTranslateY(210);

        // Label PV
        Label labelPV = j.getListeLabels().get(TypeLabel.LABEL_SCORE);
        labelPV.setTranslateX(80);
        labelPV.setTranslateY(230);

        paneJoueur.getChildren().addAll(ivPV, labelPV);

        // Icon Master Road
        Pane paneRouteMaster = new Pane();
        paneRouteMaster.setTranslateX(250);
        paneRouteMaster.setTranslateY(210);
        paneRouteMaster.getChildren().add(new ImageView(ParamGame.iconRoadMaster));

        // Icon Master Biff
        Pane paneBiffMaster = new Pane();
        paneBiffMaster.setTranslateX(320);
        paneBiffMaster.setTranslateY(210);
        paneBiffMaster.getChildren().add(new ImageView(ParamGame.iconBiffMaster));

        paneJoueur.getChildren().addAll(paneRouteMaster,paneBiffMaster);

        return paneJoueur;
    }

    /**
     * Permet de cacher les ressources des joueurs qui ne jouent pas
     */
    private void hideGridPane(){
        for(GridPane gridPane: gridPaneList){
            gridPane.setVisible(false);
        }
        gridPaneList.get(Jeu.getInstance().getCurrentPlayer().getId()-1).setVisible(true);
    }

    /**
     * Fonction appellée quand le joueur clique sur "lancer les dés"
     */
    public void launchDice(){
        Integer num = Jeu.getInstance().tirageDes();

        imageViewDice.setImage(ParamGame.imagesDices.get(num));
        imageViewDice.setVisible(true);

        buttonDice.setDisable(true);

        // Executer l'action de Biff
        if (num == 7) {
            // Enlever le surplus de ressource aux joueurs
            Jeu.getInstance().executeBiffAction(true);

            // Afficher un message pour demander de déplacer Biff
            CustomDialogs.displayInfoPopUp("Vous devez cliquer sur une hexagone pour déplacer Biff",
                    this.anchorPane.getScene().getWindow());
        }
        // Recolte les ressources
        else {
            Jeu.getInstance().recolterRessources(num);
            setDisableOtherButtons(buttonDice, false);
            verifyBlockButtonProduction();
        }
    }


    /**
     * Appele la fonction nouvellePartie dans le controlleur Jeu
     */
    private void launchNewGame(){
        ArrayList<String> listNames = Jeu.getInstance().getPlayersPageControl().getNames();
        ArrayList<String> listColorsString = Jeu.getInstance().getPlayersPageControl().getColors();
        ArrayList<Color> listColors = new ArrayList<>();
        for(String colorString: listColorsString){
            listColors.add(Color.web(colorString));
        }

        String[] tabNames = new String[Jeu.getInstance().getPlayersPageControl().getNames().size()];
        Color[] tabColors = new Color[Jeu.getInstance().getPlayersPageControl().getColors().size()];

        tabNames = listNames.toArray(tabNames);
        tabColors = listColors.toArray(tabColors);

        Jeu monJeu = Jeu.getInstance();

        // Créer une nouvelle partie (initialisation du model)
        monJeu.nouvellePartie(tabNames.length, tabNames, tabColors);

        // Attacher les vues des plateaux de jeu
        for (int i = 0; i < monJeu.getListeVuesPlateaux().size(); i++) {
            this.tabPanePlateau.getTabs().get(i).setContent(monJeu.getListeVuesPlateaux().get(ParamGame.plateaux[i]));
            monJeu.getListeVuesPlateaux().get(ParamGame.plateaux[i]).setTranslateX(450);
            monJeu.getListeVuesPlateaux().get(ParamGame.plateaux[i]).setTranslateY(100);
        }
    }

    /**
     * Lancer l'action de construction
     */
    public void construction() {
        if (!Jeu.getInstance().isConstructionMode()) {
            Jeu.getInstance().setConstructionMode(true);
            buttonConstruction.setStyle("-fx-background-color: #00ff00");
            setDisableOtherButtons(this.buttonConstruction, true);
        }
        else {
            buttonConstruction.setStyle("");
            Jeu.getInstance().setConstructionMode(false);
            setDisableOtherButtons(this.buttonConstruction, false);
        }
    }

    /**
     * Lancer l'action d'amélioration de Delorean en laboratoire
     */
    public void updateToLabo() {
        if (!Jeu.getInstance().isLaboUpgradeMode()) {
            Jeu.getInstance().setLaboUpgradeMode(true);
            btnUpdateToLabo.setStyle("-fx-background-color: #00ff00");
            setDisableOtherButtons(this.btnUpdateToLabo, true);
        }
        else {
            btnUpdateToLabo.setStyle("");
            Jeu.getInstance().setLaboUpgradeMode(false);
            setDisableOtherButtons(this.btnUpdateToLabo, false);
        }
    }

    /**
     * Fonction qui se lance lorsque le joueur appuie sur le bouton commerce
     * @throws IOException Si le fichier ressource n'a pas été trouvé
     */
    public void createCommerce() throws IOException {
        iterateurRefus++;
        if(iterateurRefus >= 3){
            buttonCommerce.setDisable(true);
        }

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/commerce.fxml"));
        createStage(loader);
    }

    /**
     * Fonction qui se lance lorsque le joueur appuie sur le bouton production
     * @throws IOException Si fichier non trouvé
     */
    public void createProduction() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/production.fxml"));
        createStage(loader);
        Jeu.getInstance().setProductionControl(loader.getController());
    }

    /**
     * Permet de creer un stage qui s'affiche par dessus le jeu principal
     * @param loader Le fxml du nouveau stage
     * @throws IOException Si fichier non trouvé
     */
    private void createStage(FXMLLoader loader) throws IOException {
        Parent gameView = loader.load();
        final Stage myDialog = new Stage();
        myDialog.initOwner(anchorPane.getScene().getWindow());
        myDialog.initModality(Modality.APPLICATION_MODAL);
        myDialog.setScene(new Scene(gameView));
        myDialog.show();
    }

    /**
     * Désactiver/Activer tous les boutons action sauf "btnClicked"
     * @param btnClicked Bouton à ne pas modifier
     * @param isDisable true pour désactiver les boutons, false pour les activer
     */
    public void setDisableOtherButtons(Button btnClicked, boolean isDisable) {
        for (Button b : actionButtons) {
            if (btnClicked == null || !b.getId().equals(btnClicked.getId())) {
                b.setDisable(isDisable);
            }
        }
    }

    /**
     * Getter anchorPane
     * @return l'anchorPane de la scene
     */
    AnchorPane getAnchorPane() {
        return anchorPane;
    }

    /**
     * Permet de quiter le jeu
     */
    public void quitMainGame(){
        anchorPane.getScene().getWindow().hide();
    }

    public Button getButtonConstruction() {
        return buttonConstruction;
    }

    public Button getBtnUpdateToLabo() {
        return btnUpdateToLabo;
    }

    public Button getButtonDice() {
        return buttonDice;
    }
}

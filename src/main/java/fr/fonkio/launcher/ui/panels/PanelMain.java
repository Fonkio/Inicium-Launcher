package fr.fonkio.launcher.ui.panels;

import fr.fonkio.launcher.Main;
import fr.fonkio.launcher.MvWildLauncher;
import fr.fonkio.launcher.ui.PanelManager;
import fr.fonkio.launcher.ui.panel.Panel;
import fr.fonkio.launcher.utils.HttpRecup;
import fr.fonkio.launcher.utils.MainPanel;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.geometry.*;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class PanelMain extends Panel {

    private final HomeGridPane home = new HomeGridPane(this);
    private final SettingsGridPane settings = new SettingsGridPane(this);
    private final PlayerListGridPane playerList = new PlayerListGridPane(this);

    private final GridPane centerPane = new GridPane();

    private final Label pseudo = new Label("");
    private VBox vBoxMv;
    private VBox vBoxSettings;
    private VBox vBoxPlayerList;
    private final ScrollPane scrollPane = new ScrollPane();
    private final Rectangle rectangleSelect = new Rectangle();

    ImageView imageViewTete = new ImageView();
    private Label playerListLabel;


    public PanelMain(Stage stage, PanelManager panelManager) {
        super(stage);
        this.panelManager = panelManager;
    }

    @Override
    public void init(PanelManager panelManager) {
        super.init(panelManager);
        setPseudo(panelManager.getMvAuth().getAuthInfos().getUsername());
        //Affichage principal
        ColumnConstraints mainPainConstraint = new ColumnConstraints();
        mainPainConstraint.setHalignment(HPos.LEFT);
        mainPainConstraint.setMinWidth(300);
        mainPainConstraint.setMaxWidth(300);
        this.layout.getColumnConstraints().addAll(mainPainConstraint, new ColumnConstraints());

        GridPane leftBar = new GridPane();
        GridPane.setVgrow(leftBar, Priority.ALWAYS);
        GridPane.setHgrow(leftBar, Priority.ALWAYS);

        Separator rightSeparator = new Separator();
        GridPane.setVgrow(rightSeparator, Priority.ALWAYS);
        GridPane.setHgrow(rightSeparator, Priority.ALWAYS);
        GridPane.setHalignment(rightSeparator, HPos.RIGHT);
        rightSeparator.setOrientation(Orientation.VERTICAL);
        rightSeparator.setTranslateY(1);
        rightSeparator.setTranslateX(4);
        rightSeparator.setMinWidth(2);
        rightSeparator.setMaxWidth(2);
        rightSeparator.setOpacity(0.30d);

        GridPane bottomGridPane = new GridPane();
        GridPane.setVgrow(bottomGridPane, Priority.ALWAYS);
        GridPane.setHgrow(bottomGridPane, Priority.ALWAYS);
        GridPane.setHalignment(bottomGridPane, HPos.LEFT);
        GridPane.setValignment(bottomGridPane, VPos.TOP);
        bottomGridPane.setTranslateY(30);
        bottomGridPane.setMinHeight(60);
        bottomGridPane.setMaxHeight(60);
        bottomGridPane.setMinWidth(300);
        bottomGridPane.setMaxWidth(300);

        showLeftBar(bottomGridPane);

        leftBar.getChildren().addAll(rightSeparator, bottomGridPane);
        leftBar.setStyle("-fx-background-color: black; -fx-opacity: 90%;");

        this.layout.add(this.centerPane, 1, 0);
        this.layout.add(leftBar, 0, 0);
        GridPane.setVgrow(this.centerPane, Priority.ALWAYS);
        GridPane.setHgrow(this.centerPane, Priority.ALWAYS);

        GridPane.setVgrow(this.scrollPane, Priority.ALWAYS);
        GridPane.setHgrow(this.scrollPane, Priority.ALWAYS);
        this.scrollPane.getStylesheets().addAll(Main.class.getResource("/css/scrollbar.css").toExternalForm());
        //PLAY
        this.vBoxMv = new VBox();
        GridPane.setVgrow(this.vBoxMv, Priority.ALWAYS);
        GridPane.setHgrow(this.vBoxMv, Priority.ALWAYS);
        this.vBoxMv.setMinHeight(200);
        this.vBoxMv.setMinWidth(900);
        this.vBoxMv.setMaxWidth(900);
        this.vBoxMv.setAlignment(Pos.CENTER);
        this.vBoxMv.setTranslateX(30);

        GridPane topPanel = new GridPane();
        GridPane.setVgrow(topPanel, Priority.ALWAYS);
        GridPane.setHgrow(topPanel, Priority.ALWAYS);
        GridPane.setValignment(topPanel, VPos.TOP);
        topPanel.setMinWidth(880);
        topPanel.setMaxWidth(880);
        topPanel.setMinHeight(340);
        topPanel.setMaxHeight(340);


        //SETTINGS
        this.vBoxSettings = new VBox();
        GridPane.setVgrow(this.vBoxSettings, Priority.ALWAYS);
        GridPane.setHgrow(this.vBoxSettings, Priority.ALWAYS);
        this.vBoxSettings.setMinHeight(200);
        this.vBoxSettings.setMinWidth(900);
        this.vBoxSettings.setMaxWidth(900);
        this.vBoxSettings.setAlignment(Pos.CENTER);
        this.vBoxSettings.setTranslateX(30);

        GridPane topPanelSettings = new GridPane();
        GridPane.setVgrow(topPanelSettings, Priority.ALWAYS);
        GridPane.setHgrow(topPanelSettings, Priority.ALWAYS);
        GridPane.setValignment(topPanelSettings, VPos.TOP);
        topPanelSettings.setMinWidth(880);
        topPanelSettings.setMaxWidth(880);
        topPanelSettings.setMinHeight(340);
        topPanelSettings.setMaxHeight(340);

        //PLAYERLIST
        this.vBoxPlayerList = new VBox();
        GridPane.setVgrow(this.vBoxPlayerList, Priority.ALWAYS);
        GridPane.setHgrow(this.vBoxPlayerList, Priority.ALWAYS);

        this.vBoxPlayerList.setAlignment(Pos.TOP_CENTER);
        this.vBoxPlayerList.setTranslateX(30);

        GridPane topPanelPlayerList = new GridPane();
        GridPane.setVgrow(topPanelPlayerList, Priority.ALWAYS);
        GridPane.setHgrow(topPanelPlayerList, Priority.ALWAYS);
        GridPane.setValignment(topPanelPlayerList, VPos.TOP);
        topPanelPlayerList.setMinWidth(880);
        topPanelPlayerList.setMaxWidth(880);
        topPanelPlayerList.setMinHeight(340);
        topPanelPlayerList.setMaxHeight(340);

        //Chargement des panels
        this.home.addTopPanel(topPanel);
        this.settings.addTopPanel(topPanelSettings);
        this.playerList.addTopPanel(topPanelPlayerList);

        this.centerPane.getChildren().add(this.scrollPane);
        this.scrollPane.setContent(this.vBoxMv);
        this.vBoxMv.getChildren().add(0, topPanel);
        this.vBoxSettings.getChildren().add(0, topPanelSettings);
        VBox.setVgrow(vBoxPlayerList, Priority.ALWAYS);
        this.vBoxPlayerList.getChildren().add(0, topPanelPlayerList);
    }


    //Partie gauche du home panel
    private void showLeftBar(GridPane pane) {
        // Affichage de la tête du joueur avec son pseudo
        GridPane.setVgrow(this.pseudo, Priority.ALWAYS);
        GridPane.setHgrow(this.pseudo, Priority.ALWAYS);
        GridPane.setValignment(this.pseudo, VPos.BOTTOM);
        this.pseudo.setStyle("-fx-font-size: 26px; -fx-text-fill: white; -fx-font-weight: bold");
        this.pseudo.setTranslateX(110);
        this.pseudo.setTranslateY(550);
        GridPane.setVgrow(this.imageViewTete, Priority.ALWAYS);
        GridPane.setHgrow(this.imageViewTete, Priority.ALWAYS);
        GridPane.setValignment(this.imageViewTete, VPos.TOP);
        GridPane.setHalignment(this.imageViewTete, HPos.LEFT);
        this.imageViewTete.setTranslateX(34);
        this.imageViewTete.setTranslateY(570);
        this.imageViewTete.setFitHeight(60);
        this.imageViewTete.setFitWidth(60);
        this.imageViewTete.setOnMouseEntered(e-> this.layout.setCursor(Cursor.HAND));
        this.imageViewTete.setOnMouseExited(e->this.layout.setCursor(Cursor.DEFAULT));
        this.pseudo.setTooltip(new Tooltip("Se déconnecter"));
        this.pseudo.setOnMouseEntered(e-> {
            this.layout.setCursor(Cursor.HAND);
            this.pseudo.setTooltip(new Tooltip("Se déconnecter"));

        });
        this.pseudo.setOnMouseExited(e->this.layout.setCursor(Cursor.DEFAULT));
        this.imageViewTete.setOnMouseClicked(e-> this.panelManager.deconnexion());
        this.pseudo.setOnMouseClicked(e-> this.panelManager.deconnexion());
        //Fin affichage pseudo + tete joueur

        //Selection de l'onglet
        //Play
        this.rectangleSelect.setX(0);
        this.rectangleSelect.setWidth(300);
        this.rectangleSelect.setHeight(70);
        this.rectangleSelect.setFill(Color.rgb(82, 135, 47));

        Image logoImageMvWild = new Image(Main.class.getResource("/logoPlay.png").toExternalForm());
        ImageView imageViewMvWild = new ImageView(logoImageMvWild);
        GridPane.setVgrow(imageViewMvWild, Priority.ALWAYS);
        GridPane.setHgrow(imageViewMvWild, Priority.ALWAYS);
        GridPane.setValignment(imageViewMvWild, VPos.CENTER);
        imageViewMvWild.setTranslateX(34);
        imageViewMvWild.setFitHeight(60);
        imageViewMvWild.setFitWidth(60);
        imageViewMvWild.setOnMouseEntered(e->this.layout.setCursor(Cursor.HAND));
        imageViewMvWild.setOnMouseExited(e->this.layout.setCursor(Cursor.DEFAULT));
        Label jouerLabel = new Label("Jouer");
        GridPane.setVgrow(jouerLabel, Priority.ALWAYS);
        GridPane.setHgrow(jouerLabel, Priority.ALWAYS);
        GridPane.setValignment(jouerLabel, VPos.CENTER);
        jouerLabel.setTranslateX(110);
        jouerLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: white; -fx-font-weight: bold");
        jouerLabel.setOnMouseEntered(e->this.layout.setCursor(Cursor.HAND));
        jouerLabel.setOnMouseExited(e->this.layout.setCursor(Cursor.DEFAULT));

        //Parametres

        Image settingImage = new Image(Main.class.getResource("/setting.png").toExternalForm());
        ImageView imageSetting = new ImageView(settingImage);
        imageSetting.setFitHeight(50);
        imageSetting.setFitWidth(50);
        Button buttonSetting = new Button();
        buttonSetting.setBackground(Background.EMPTY);
        buttonSetting.setGraphic(imageSetting);
        GridPane.setVgrow(buttonSetting, Priority.ALWAYS);
        GridPane.setHgrow(buttonSetting, Priority.ALWAYS);
        GridPane.setValignment(buttonSetting, VPos.CENTER);
        buttonSetting.setTranslateX(34);
        buttonSetting.setTranslateY(140);
        buttonSetting.setOnMouseEntered(e->this.layout.setCursor(Cursor.HAND));
        buttonSetting.setOnMouseExited(e->this.layout.setCursor(Cursor.DEFAULT));
        Label settingLabel = new Label("Paramètres");
        GridPane.setVgrow(settingLabel, Priority.ALWAYS);
        GridPane.setHgrow(settingLabel, Priority.ALWAYS);
        GridPane.setValignment(settingLabel, VPos.CENTER);
        settingLabel.setTranslateX(110);
        settingLabel.setTranslateY(140);
        settingLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: white; -fx-font-weight: bold");
        settingLabel.setOnMouseEntered(e->this.layout.setCursor(Cursor.HAND));
        settingLabel.setOnMouseExited(e->this.layout.setCursor(Cursor.DEFAULT));

        // ListeJoueurs

        Image playersImage = new Image(Main.class.getResource("/group.png").toExternalForm());
        ImageView imagePlayer = new ImageView(playersImage);
        imagePlayer.setFitHeight(50);
        imagePlayer.setFitWidth(50);
        Button buttonPlayer = new Button();
        buttonPlayer.setBackground(Background.EMPTY);
        buttonPlayer.setGraphic(imagePlayer);
        GridPane.setVgrow(buttonPlayer, Priority.ALWAYS);
        GridPane.setHgrow(buttonPlayer, Priority.ALWAYS);
        GridPane.setValignment(buttonPlayer, VPos.CENTER);
        buttonPlayer.setTranslateX(34);
        buttonPlayer.setTranslateY(70);
        buttonPlayer.setOnMouseEntered(e->this.layout.setCursor(Cursor.HAND));
        buttonPlayer.setOnMouseExited(e->this.layout.setCursor(Cursor.DEFAULT));
        this.playerListLabel = new Label("Joueurs connectés ("+ HttpRecup.getNbCo() +")");
        GridPane.setVgrow(playerListLabel, Priority.ALWAYS);
        GridPane.setHgrow(playerListLabel, Priority.ALWAYS);
        GridPane.setValignment(playerListLabel, VPos.CENTER);
        playerListLabel.setTranslateX(110);
        playerListLabel.setTranslateY(70);
        playerListLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: white; -fx-font-weight: bold");
        playerListLabel.setOnMouseEntered(e->this.layout.setCursor(Cursor.HAND));
        playerListLabel.setOnMouseExited(e->this.layout.setCursor(Cursor.DEFAULT));

        //Update

        Image updateImage = new Image(Main.class.getResource("/download.png").toExternalForm());
        ImageView imageUpdate = new ImageView(updateImage);
        imageUpdate.setFitHeight(50);
        imageUpdate.setFitWidth(50);
        Button buttonUpdate = new Button();
        buttonUpdate.setBackground(Background.EMPTY);
        buttonUpdate.setGraphic(imageUpdate);
        GridPane.setVgrow(buttonUpdate, Priority.ALWAYS);
        GridPane.setHgrow(buttonUpdate, Priority.ALWAYS);
        GridPane.setValignment(buttonUpdate, VPos.CENTER);
        buttonUpdate.setTranslateX(34);
        buttonUpdate.setTranslateY(210);
        buttonUpdate.setOnMouseEntered(e->this.layout.setCursor(Cursor.HAND));
        buttonUpdate.setOnMouseExited(e->this.layout.setCursor(Cursor.DEFAULT));
        Label updateLabel = new Label("Mettre à jour ! (");
        GridPane.setVgrow(updateLabel, Priority.ALWAYS);
        GridPane.setHgrow(updateLabel, Priority.ALWAYS);
        GridPane.setValignment(updateLabel, VPos.CENTER);
        updateLabel.setTranslateX(110);
        updateLabel.setTranslateY(210);
        updateLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: white; -fx-font-weight: bold");
        updateLabel.setOnMouseEntered(e->this.layout.setCursor(Cursor.HAND));
        updateLabel.setOnMouseExited(e->this.layout.setCursor(Cursor.DEFAULT));


        //Control affichage

        jouerLabel.setOnMouseClicked(e-> afficher(MainPanel.HOME));
        imageViewMvWild.setOnMouseClicked(e-> afficher(MainPanel.HOME));
        buttonSetting.setOnMouseClicked(e-> afficher(MainPanel.PARAMETRES));
        settingLabel.setOnMouseClicked(e-> afficher(MainPanel.PARAMETRES));
        buttonPlayer.setOnMouseClicked(e-> afficher(MainPanel.PLAYER_LIST));
        playerListLabel.setOnMouseClicked(e-> afficher(MainPanel.PLAYER_LIST));
        buttonUpdate.setOnMouseClicked(e-> update());
        updateLabel.setOnMouseClicked(e-> update());

        //Fin selection onglet

        //Ajout des éléments
        pane.getChildren().addAll(rectangleSelect, imageViewMvWild, jouerLabel, buttonSetting, settingLabel, pseudo, imageViewTete, buttonPlayer, playerListLabel);
        String checkVersion = panelManager.checkLauncherVersion();
        if (checkVersion != null) {
            updateLabel.setText(updateLabel.getText()+"v"+checkVersion+")");
            pane.getChildren().addAll(updateLabel, buttonUpdate);
        }
    } //Fin showLeftBar

    private void update () {
        try {
            Desktop.getDesktop().browse(new URI(MvWildLauncher.SITE_URL+"mvlauncher"));
        } catch (IOException | URISyntaxException ioException) {
            ioException.printStackTrace();
        }
    }

    public void setPseudo(String pseudo) {
        this.pseudo.setText(pseudo);
        String path = "https://cravatar.eu/head/"+pseudo+"/100.png";
        Thread t = new Thread(() -> {
            Image teteJ = new Image(path);
            Platform.runLater(() -> this.imageViewTete.setImage(teteJ));
        });
        t.start();
    }

    public String getRAM() {
        return this.panelManager.getRAM();
    }

    public Boolean isDRPDisabled() {
        return this.panelManager.isDRPDisabled();
    }

    public GridPane getLayout() {
        return this.layout;
    }

    public void setRAM(double ramD) {
        this.panelManager.setRAM(ramD);
    }

    public void setDisableDRP(boolean selected) {
        this.panelManager.setDisableDRP(selected);
    }

    public void install() {
        this.panelManager.install();
    }

    public void setStatus(String s) {
        this.home.setStatus(s);
    }

    public void setProgress(float avancee, float fin) {
        this.home.setProgress(avancee, fin);
    }

    public void setDisableInstall(boolean b) {
        this.home.setDisableInstall(b);
    }

    public void setInstallButtonText(String s) {
        this.home.setInstallButtonText(s);
    }

    public String getVersion() {
        return this.panelManager.getVersion();
    }

    public String getFabricVersion() {
        return this.panelManager.getFabricVersion();
    }

    public void afficher(MainPanel mainPanel) {
        TranslateTransition tt = new TranslateTransition(Duration.millis(100), this.rectangleSelect);
        switch (mainPanel) {
            case HOME -> {
                this.scrollPane.setContent(vBoxMv);
                tt.setToY(0);
            }
            case PARAMETRES -> {
                this.scrollPane.setContent(vBoxSettings);
                tt.setToY(140);
            }
            case PLAYER_LIST -> {
                this.scrollPane.setContent(vBoxPlayerList);
                tt.setToY(70);
            }
        }
        tt.play();
    }


    public void refreshList() {
        Platform.runLater(() -> this.playerListLabel.setText("Récupération..."));
        playerList.setRefreshButtonVisible(false);
        playerList.refreshList();
        Platform.runLater(() -> this.playerListLabel.setText("Joueurs connectés ("+ HttpRecup.getNbCo(true) +")"));
        playerList.setRefreshButtonVisible(true);
    }

    public void resetLauncher() {
        this.panelManager.resetLauncher();
    }

    public boolean containsModsFolder() {
        return this.panelManager.containsModsFolder();
    }

}

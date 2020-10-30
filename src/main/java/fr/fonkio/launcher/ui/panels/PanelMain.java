package fr.fonkio.launcher.ui.panels;

import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import fr.fonkio.launcher.Main;
import fr.fonkio.launcher.ui.PanelManager;
import fr.fonkio.launcher.ui.panel.Panel;
import fr.fonkio.launcher.utils.HttpRecup;
import fr.fonkio.launcher.utils.MainPanel;
import javafx.animation.TranslateTransition;
import javafx.geometry.*;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

public class PanelMain extends Panel {

    private HomeGridPane home = new HomeGridPane(this);
    private SettingsGridPane settings = new SettingsGridPane(this);
    private PlayerListGridPane playerList = new PlayerListGridPane(this);

    private GridPane centerPane = new GridPane();

    private Label pseudo = new Label("");
    private Image teteJ;
    private VBox vBoxMv;
    private VBox vBoxSettings;
    private VBox vBoxPlayerList;
    private ScrollPane scrollPane = new ScrollPane();
    private Rectangle rectangleSelect = new Rectangle();

    ImageView imageViewTete = new ImageView();
    private Label playerListLabel;


    public PanelMain(Stage stage, PanelManager panelManager) {
        super(stage);
        this.panelManager = panelManager;
    }

    @Override
    public void init(PanelManager panelManager) {
        super.init(panelManager);
        setPseudo(panelManager.getPseudo());
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

        //Chargement des panels
        this.home.addTopPanel(topPanel);
        this.settings.addTopPanel(topPanelSettings);
        this.playerList.addTopPanel(topPanelPlayerList);

        this.centerPane.getChildren().add(this.scrollPane);
        this.scrollPane.setContent(this.vBoxMv);
        this.vBoxMv.getChildren().add(0, topPanel);
        this.vBoxSettings.getChildren().add(0, topPanelSettings);
        VBox.setVgrow(vBoxPlayerList, Priority.ALWAYS);
        vBoxPlayerList.setMinHeight(5000);
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
        this.imageViewTete.setOnMouseEntered(e->{
            this.layout.setCursor(Cursor.HAND);
        });
        this.imageViewTete.setOnMouseExited(e->this.layout.setCursor(Cursor.DEFAULT));
        this.pseudo.setTooltip(new Tooltip("Se déconnecter"));
        this.pseudo.setOnMouseEntered(e-> {
            this.layout.setCursor(Cursor.HAND);
            this.pseudo.setTooltip(new Tooltip("Se déconnecter"));

        });
        this.pseudo.setOnMouseExited(e->this.layout.setCursor(Cursor.DEFAULT));
        this.imageViewTete.setOnMouseClicked(e-> {
            this.panelManager.showPanel(fr.fonkio.launcher.utils.MainPanel.LOGIN);
        });
        this.pseudo.setOnMouseClicked(e-> {
            this.panelManager.showPanel(fr.fonkio.launcher.utils.MainPanel.LOGIN);
        });
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

        MaterialDesignIconView logoSetting = new MaterialDesignIconView(MaterialDesignIcon.SETTINGS);
        GridPane.setVgrow(logoSetting, Priority.ALWAYS);
        GridPane.setHgrow(logoSetting, Priority.ALWAYS);
        GridPane.setValignment(logoSetting, VPos.CENTER);
        logoSetting.setTranslateX(34);
        logoSetting.setTranslateY(70);
        logoSetting.setFill(Color.rgb(255, 255, 255));
        logoSetting.setSize("60px");
        logoSetting.setStyle("-fx-background-color: white");
        logoSetting.setOnMouseEntered(e->this.layout.setCursor(Cursor.HAND));
        logoSetting.setOnMouseExited(e->this.layout.setCursor(Cursor.DEFAULT));
        Label settingLabel = new Label("Paramètres");
        GridPane.setVgrow(settingLabel, Priority.ALWAYS);
        GridPane.setHgrow(settingLabel, Priority.ALWAYS);
        GridPane.setValignment(settingLabel, VPos.CENTER);
        settingLabel.setTranslateX(110);
        settingLabel.setTranslateY(70);
        settingLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: white; -fx-font-weight: bold");
        settingLabel.setOnMouseEntered(e->this.layout.setCursor(Cursor.HAND));
        settingLabel.setOnMouseExited(e->this.layout.setCursor(Cursor.DEFAULT));

        // ListeJoueurs

        MaterialDesignIconView logoUsers = new MaterialDesignIconView(MaterialDesignIcon.ACCOUNT_MULTIPLE);
        GridPane.setVgrow(logoUsers, Priority.ALWAYS);
        GridPane.setHgrow(logoUsers, Priority.ALWAYS);
        GridPane.setValignment(logoUsers, VPos.CENTER);
        logoUsers.setTranslateX(34);
        logoUsers.setTranslateY(140);
        logoUsers.setFill(Color.rgb(255, 255, 255));
        logoUsers.setSize("60px");
        logoUsers.setStyle("-fx-background-color: white");
        logoUsers.setOnMouseEntered(e->this.layout.setCursor(Cursor.HAND));
        logoUsers.setOnMouseExited(e->this.layout.setCursor(Cursor.DEFAULT));
        this.playerListLabel = new Label("Joueurs connectés ("+ HttpRecup.getNbCo() +")");
        GridPane.setVgrow(playerListLabel, Priority.ALWAYS);
        GridPane.setHgrow(playerListLabel, Priority.ALWAYS);
        GridPane.setValignment(playerListLabel, VPos.CENTER);
        playerListLabel.setTranslateX(110);
        playerListLabel.setTranslateY(140);
        playerListLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: white; -fx-font-weight: bold");
        playerListLabel.setOnMouseEntered(e->this.layout.setCursor(Cursor.HAND));
        playerListLabel.setOnMouseExited(e->this.layout.setCursor(Cursor.DEFAULT));

        //Control affichage

        jouerLabel.setOnMouseClicked(e->{
            afficher(MainPanel.HOME);
        });
        imageViewMvWild.setOnMouseClicked(e->{
            afficher(MainPanel.HOME);
        });
        logoSetting.setOnMouseClicked(e->{
            afficher(MainPanel.PARAMETRES);
        });
        settingLabel.setOnMouseClicked(e->{
            afficher(MainPanel.PARAMETRES);
        });
        logoUsers.setOnMouseClicked(e->{
            afficher(MainPanel.PLAYER_LIST);
        });
        playerListLabel.setOnMouseClicked(e->{
            afficher(MainPanel.PLAYER_LIST);
        });

        //Fin selection onglet

        //Ajout des éléments
        pane.getChildren().addAll(rectangleSelect, imageViewMvWild, jouerLabel, logoSetting, settingLabel, pseudo, imageViewTete, logoUsers, playerListLabel);
    } //Fin showLeftBar

    public void setPseudo(String pseudo) {
        this.pseudo.setText(pseudo);
        String path = "https://minotar.net/avatar/"+pseudo+"/100.png";
        this.teteJ = new Image(path);
        this.imageViewTete.setImage(teteJ);
    }

    public String getRAM() {
        return this.panelManager.getRAM();
    }

    public Boolean getDRP() {
        return this.panelManager.getDRP();
    }

    public GridPane getLayout() {
        return this.layout;
    }

    public void setRAM(double ramD) {
        this.panelManager.setRAM(ramD);
    }

    public void setDRP(boolean selected) {
        this.panelManager.setDRP(selected);
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

    public String getMCPVersion() {
        return this.panelManager.getMCPVersion();
    }

    public String getForgeVersion() {
        return this.panelManager.getForgeVersion();
    }

    public void afficher(MainPanel mainPanel) {
        TranslateTransition tt = new TranslateTransition(Duration.millis(100), this.rectangleSelect);
        switch (mainPanel) {
            case HOME:
                this.scrollPane.setContent(vBoxMv);
                tt.setToY(0);
                break;
            case PARAMETRES:
                this.scrollPane.setContent(vBoxSettings);
                tt.setToY(70);
                break;
            case PLAYER_LIST:
                this.scrollPane.setContent(vBoxPlayerList);
                refreshList();
                tt.setToY(140);
                break;
        }
        tt.play();
    }

    public void refreshList() {
        this.playerListLabel.setText("Joueurs connectés ("+ HttpRecup.getNbCo() +")");
        GridPane topPanelPlayerList = new GridPane();
        GridPane.setVgrow(topPanelPlayerList, Priority.ALWAYS);
        GridPane.setHgrow(topPanelPlayerList, Priority.ALWAYS);
        GridPane.setValignment(topPanelPlayerList, VPos.TOP);
        topPanelPlayerList.setMinWidth(880);
        topPanelPlayerList.setMaxWidth(880);
        topPanelPlayerList.setMinHeight(340);
        topPanelPlayerList.setMaxHeight(340);
        this.playerList.addTopPanel(topPanelPlayerList);
        this.vBoxPlayerList.getChildren().remove(0);
        this.vBoxPlayerList.getChildren().add(0, topPanelPlayerList);
    }
}

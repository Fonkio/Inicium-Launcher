package fr.fonkio.launcher.ui.panels;

import com.sun.webkit.WebPage;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import fr.arinonia.arilibfx.ui.component.AProgressBar;
import fr.flowarg.flowupdater.utils.builderapi.BuilderException;
import fr.fonkio.launcher.Main;
import fr.fonkio.launcher.MvWildLauncher;
import fr.fonkio.launcher.ui.PanelManager;
import fr.fonkio.launcher.ui.panel.Panel;
import fr.fonkio.launcher.utils.HttpRecup;
import fr.fonkio.launcher.utils.MainPanel;
import javafx.collections.ListChangeListener;
import javafx.geometry.HPos;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.lang.reflect.Field;
import java.net.*;
import java.util.*;

public class HomePanel extends Panel {


    private GridPane centerPane = new GridPane();
    private Label status = new Label("");
    private Label pseudo = new Label("");
    private Image teteJ;
    private VBox vBoxMv;
    private VBox vBoxSettings;
    private ScrollPane scrollPane = new ScrollPane();
    private Button installButton = new Button("Jouer");
    ImageView imageViewTete = new ImageView();
    AProgressBar progressBar = new AProgressBar(400, 20);

    public HomePanel(Stage stage, PanelManager panelManager) {
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

        GridPane.setVgrow(scrollPane, Priority.ALWAYS);
        GridPane.setHgrow(scrollPane, Priority.ALWAYS);
        scrollPane.getStylesheets().addAll(Main.class.getResource("/css/scrollbar.css").toExternalForm());

        vBoxMv = new VBox();
        GridPane.setVgrow(vBoxMv, Priority.ALWAYS);
        GridPane.setHgrow(vBoxMv, Priority.ALWAYS);
        vBoxMv.setMinHeight(200);
        vBoxMv.setMinWidth(900);
        vBoxMv.setMaxWidth(900);
        vBoxMv.setAlignment(Pos.CENTER);
        vBoxMv.setTranslateX(30);

        vBoxSettings = new VBox();
        GridPane.setVgrow(vBoxSettings, Priority.ALWAYS);
        GridPane.setHgrow(vBoxSettings, Priority.ALWAYS);
        vBoxSettings.setMinHeight(200);
        vBoxSettings.setMinWidth(900);
        vBoxSettings.setMaxWidth(900);
        vBoxSettings.setAlignment(Pos.CENTER);
        vBoxSettings.setTranslateX(30);

        GridPane topPanel = new GridPane();
        GridPane.setVgrow(topPanel, Priority.ALWAYS);
        GridPane.setHgrow(topPanel, Priority.ALWAYS);
        GridPane.setValignment(topPanel, VPos.TOP);
        topPanel.setMinWidth(880);
        topPanel.setMaxWidth(880);
        topPanel.setMinHeight(340);
        topPanel.setMaxHeight(340);

        try {
            addTopPanel(topPanel);
        } catch (BuilderException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }


        GridPane topPanelSettings = new GridPane();
        GridPane.setVgrow(topPanelSettings, Priority.ALWAYS);
        GridPane.setHgrow(topPanelSettings, Priority.ALWAYS);
        GridPane.setValignment(topPanelSettings, VPos.TOP);
        topPanelSettings.setMinWidth(880);
        topPanelSettings.setMaxWidth(880);
        topPanelSettings.setMinHeight(340);
        topPanelSettings.setMaxHeight(340);
        addTopPanelSettings(topPanelSettings);

        this.centerPane.getChildren().add(scrollPane);
        scrollPane.setContent(vBoxMv);
        vBoxMv.getChildren().add(0, topPanel);
        vBoxSettings.getChildren().add(0, topPanelSettings);
    }

    //Affichage onglet paramètres
    private void addTopPanelSettings(GridPane topPanelSettings) {
        Label settingsTitle = new Label("Paramètres");
        GridPane.setVgrow(settingsTitle, Priority.ALWAYS);
        GridPane.setHgrow(settingsTitle, Priority.ALWAYS);
        GridPane.setValignment(settingsTitle, VPos.TOP);
        settingsTitle.setStyle("-fx-font-size: 26px; -fx-text-fill: white; -fx-font-weight: bold");
        settingsTitle.setTranslateY(20);

        Label ramTitle = new Label("RAM attribuée à votre minecraft (en GB)");
        GridPane.setVgrow(ramTitle, Priority.ALWAYS);
        GridPane.setHgrow(ramTitle, Priority.ALWAYS);
        GridPane.setValignment(ramTitle, VPos.TOP);
        ramTitle.setStyle("-fx-font-size: 20px; -fx-text-fill: white;  -fx-font-weight: bold");
        ramTitle.setTranslateY(80);

        Label ram0 = new Label("0 = par défaut");
        GridPane.setVgrow(ram0, Priority.ALWAYS);
        GridPane.setHgrow(ram0, Priority.ALWAYS);
        GridPane.setValignment(ram0, VPos.TOP);
        ram0.setStyle("-fx-font-size: 14px; -fx-text-fill: white;");
        ram0.setTranslateY(110);

        String ramStr = panelManager.getRAM();
        int ram = 0;
        if (ramStr != null) {
            ram = Integer.parseInt(ramStr);
        }
        Slider slider = new Slider(0, 16, ram/1024.0);
        GridPane.setVgrow(slider, Priority.ALWAYS);
        GridPane.setHgrow(slider, Priority.ALWAYS);
        GridPane.setValignment(slider, VPos.TOP);
        slider.setTranslateY(130);
        slider.setShowTickMarks(true);
        slider.setShowTickLabels(true);
        slider.setMajorTickUnit(1);
        slider.setMinorTickCount(1);
        slider.setSnapToTicks(true);

        CheckBox checkBox = new CheckBox();
        if(this.panelManager.getDRP() != null) {
            checkBox.setSelected(this.panelManager.getDRP());
        }
        checkBox.setTranslateY(25);
        Label checkText = new Label("Désactiver le Discord Rich Presence");
        checkText.setTranslateY(25);
        checkText.setTranslateX(20);
        checkText.setOnMouseEntered(e->{
            this.layout.setCursor(Cursor.HAND);
        });
        checkText.setOnMouseExited(e->{
            this.layout.setCursor(Cursor.DEFAULT);
        });
        checkBox.setOnMouseEntered(e->{
            this.layout.setCursor(Cursor.HAND);
        });
        checkBox.setOnMouseExited(e->{
            this.layout.setCursor(Cursor.DEFAULT);
        });

        checkText.setStyle("-fx-font-size: 14px; -fx-text-fill: white;");

        Button save = new Button("Sauvegarder");
        GridPane.setVgrow(save, Priority.ALWAYS);
        GridPane.setHgrow(save, Priority.ALWAYS);
        GridPane.setValignment(save, VPos.BOTTOM);
        GridPane.setHalignment(save, HPos.RIGHT);
        save.setMinWidth(140);
        save.setMaxHeight(40);
        save.setStyle("-fx-background-color: #2a4c13; -fx-background-insets: 0; -fx-font-size: 14px; -fx-text-fill: white;");
        save.setOnMouseEntered(e->this.layout.setCursor(Cursor.HAND));
        save.setOnMouseExited(e->this.layout.setCursor(Cursor.DEFAULT));
        save.setOnMouseClicked(e-> {
            double ramD = slider.getValue()*1024;
            panelManager.setRAM(ramD);
            panelManager.setDRP(checkBox.isSelected());
            save.setText("Sauvegardé !");
            save.setStyle("-fx-background-color: #52872F; -fx-background-insets: 0; -fx-font-size: 14px; -fx-text-fill: white;");
        });
        slider.setOnDragDetected(e-> {
            save.setText("Sauvegarder");
            save.setStyle("-fx-background-color: #2a4c13; -fx-background-insets: 0; -fx-font-size: 14px; -fx-text-fill: white;");
        });
        checkText.setOnMouseClicked(e->{
            checkBox.setSelected(!checkBox.isSelected());
            save.setText("Sauvegarder");
            save.setStyle("-fx-background-color: #2a4c13; -fx-background-insets: 0; -fx-font-size: 14px; -fx-text-fill: white;");
        });
        checkBox.setOnMouseClicked(e -> {
            save.setText("Sauvegarder");
            save.setStyle("-fx-background-color: #2a4c13; -fx-background-insets: 0; -fx-font-size: 14px; -fx-text-fill: white;");
        });
        topPanelSettings.getChildren().addAll(settingsTitle, ramTitle, ram0, slider, save, checkBox, checkText);
    }

    //Affichage onglet jouer par defaut
    private void addTopPanel(GridPane pane) throws BuilderException, URISyntaxException, MalformedURLException {
        //Titre et description
        Label mvwildTitle = new Label(MvWildLauncher.SERVEUR_NAME);
        GridPane.setVgrow(mvwildTitle, Priority.ALWAYS);
        GridPane.setHgrow(mvwildTitle, Priority.ALWAYS);
        GridPane.setValignment(mvwildTitle, VPos.TOP);
        mvwildTitle.setStyle("-fx-font-size: 26px; -fx-text-fill: white; -fx-font-weight: bold");
        mvwildTitle.setTranslateY(20);

        Label survie = new Label("Le serveur 100% survie");
        GridPane.setVgrow(survie, Priority.ALWAYS);
        GridPane.setHgrow(survie, Priority.ALWAYS);
        GridPane.setValignment(survie, VPos.TOP);
        survie.setStyle("-fx-font-size: 14px; -fx-text-fill: #ffffff; -fx-opacity: 70%;");
        survie.setTranslateY(70);

        Label desc = new Label(MvWildLauncher.SERVEUR_NAME+", le serveur minecraft où le Gamemode est, et restera sur 0 !\n" +
                "Rejoignez une communauté de survivants se battant contre la faim, les autres joueurs,\n" +
                "l'économie et des monstres divers parfois étranges et avec de redoutables pouvoirs...\n" +
                "Tout cela dans un seul et unique but : rester en vie !\n" +
                "Parviendrez-vous à survivre selon les règles d'origine de minecraft ?\n" +
                "Rejoignez-nous dès maintenant avec l'adresse ip : "+MvWildLauncher.SERVEUR_IP);
        GridPane.setVgrow(desc, Priority.ALWAYS);
        GridPane.setHgrow(desc, Priority.ALWAYS);
        GridPane.setValignment(desc, VPos.TOP);
        desc.setStyle("-fx-font-size: 14px; -fx-text-fill: #bcc6e7; -fx-opacity: 70%;");
        desc.setTranslateY(130);

        //ListeConnectés
        Image playerImage = new Image(Main.class.getResource("/users.png").toExternalForm());
        ImageView playerImageView = new ImageView(playerImage);
        playerImageView.setFitHeight(40);
        playerImageView.setFitWidth(40);
        Button buttonPlayer = new Button();
        GridPane.setVgrow(buttonPlayer, Priority.ALWAYS);
        GridPane.setHgrow(buttonPlayer, Priority.ALWAYS);
        GridPane.setValignment(buttonPlayer, VPos.TOP);
        GridPane.setHalignment(buttonPlayer, HPos.LEFT);
        buttonPlayer.setTranslateY(10);
        buttonPlayer.setTranslateX(450);
        buttonPlayer.setBackground(Background.EMPTY);
        buttonPlayer.setGraphic(playerImageView);
        buttonPlayer.setStyle("-fx-font-size: 26px; -fx-text-fill: white; -fx-font-weight: bold");;
        Tooltip tt = new Tooltip(HttpRecup.getList());
        buttonPlayer.setTooltip(tt);

        //NbConnectés
        Label nbCo = new Label(HttpRecup.getNbCo());
        GridPane.setVgrow(nbCo, Priority.ALWAYS);
        GridPane.setHgrow(nbCo, Priority.ALWAYS);
        GridPane.setValignment(nbCo, VPos.TOP);
        nbCo.setStyle("-fx-font-size: 26px; -fx-text-fill: white; -fx-font-weight: bold");
        nbCo.setTranslateX(520);
        nbCo.setTranslateY(20);
        nbCo.setTooltip(tt);

        //IFRAME Twitter
        GridPane twitter = new GridPane();
        GridPane.setVgrow(twitter, Priority.ALWAYS);
        GridPane.setHgrow(twitter, Priority.ALWAYS);
        GridPane.setValignment(twitter, VPos.CENTER);
        GridPane.setHalignment(twitter, HPos.RIGHT);
        twitter.setMinWidth(350);
        twitter.setMaxWidth(350);
        twitter.setMinHeight(1500);
        twitter.setTranslateY(20);
        twitter.setTranslateX(30);
        String content_url = "<a class=\"twitter-timeline\" data-lang=\"fr\" data-theme=\"dark\" href=\""+MvWildLauncher.TWITTER_URL+"?ref_src=twsrc%5Etfw\">Chargement des tweets ...</a> <script async src=\"https://platform.twitter.com/widgets.js\" charset=\"utf-8\"></script>";
        WebView webView = new WebView();
        webView.setStyle("overflow-x: hidden; overflow-y: hidden");
        WebEngine webEngine = webView.getEngine();
        webEngine.loadContent(content_url);
        twitter.getChildren().add(webView);
        webView.setOnMouseClicked(e-> {
            webEngine.loadContent(content_url);
            try {
                Desktop.getDesktop().browse(new URI(MvWildLauncher.TWITTER_URL));
            } catch (IOException ioException) {
                ioException.printStackTrace();
            } catch (URISyntaxException uriSyntaxException) {
                uriSyntaxException.printStackTrace();
            }
        });
        webView.getChildrenUnmodifiable().addListener((ListChangeListener<Node>) change ->{
            Set<Node> deadSeaScroll = webView.lookupAll(".scroll-bar");
            for(Node scroll : deadSeaScroll) {
                scroll.setVisible(false);
            }
        });
        try {
            Field field = webEngine.getClass().getDeclaredField("page");
            field.setAccessible(true);
            WebPage page = (WebPage)field.get(webEngine);
            SwingUtilities.invokeLater(()->page.setBackgroundColor(new java.awt.Color(255, 255, 255, 0).getRGB()));
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }

        //Install et dlBar
        GridPane.setVgrow(installButton, Priority.ALWAYS);
        GridPane.setHgrow(installButton, Priority.ALWAYS);
        GridPane.setValignment(installButton, VPos.TOP);
        GridPane.setHalignment(installButton, HPos.LEFT);

        progressBar.setBackgroundColor(Color.rgb(92, 92, 92));
        Stop[] stops = new Stop[]{new Stop(0, Color.rgb(48, 85, 22)), new Stop(1, Color.rgb(82, 135, 47))};
        LinearGradient lg = new LinearGradient(0,0,1,0,true, CycleMethod.NO_CYCLE, stops);
        progressBar.setForegroundColor(lg);
        GridPane.setVgrow(progressBar, Priority.ALWAYS);
        GridPane.setHgrow(progressBar, Priority.ALWAYS);
        GridPane.setValignment(progressBar, VPos.TOP);
        GridPane.setHalignment(progressBar, HPos.LEFT);
        progressBar.setTranslateY(280);

        GridPane.setVgrow(this.status, Priority.ALWAYS);
        GridPane.setHgrow(this.status, Priority.ALWAYS);
        GridPane.setValignment(this.status, VPos.TOP);
        this.status.setStyle("-fx-font-size: 14px; -fx-text-fill: white; -fx-opacity: 70%;");
        this.status.setTranslateY(300);

        installButton.setTranslateY(330);
        installButton.setMinWidth(140);
        installButton.setMaxHeight(40);
        installButton.setStyle("-fx-background-color: #52872F; -fx-background-insets: 0; -fx-font-size: 14px; -fx-text-fill: white;");
        installButton.setOnMouseEntered(e->this.layout.setCursor(Cursor.HAND));
        installButton.setOnMouseExited(e->this.layout.setCursor(Cursor.DEFAULT));

        //Socials
        Image siteImage = new Image(Main.class.getResource("/site.png").toExternalForm());
        ImageView imageViewSite = new ImageView(siteImage);
        imageViewSite.setFitHeight(60);
        imageViewSite.setFitWidth(60);
        Button buttonSite = new Button();
        GridPane.setVgrow(buttonSite, Priority.ALWAYS);
        GridPane.setHgrow(buttonSite, Priority.ALWAYS);
        GridPane.setValignment(buttonSite, VPos.TOP);
        GridPane.setHalignment(buttonSite, HPos.LEFT);
        buttonSite.setTranslateY(450);
        buttonSite.setBackground(Background.EMPTY);
        buttonSite.setGraphic(imageViewSite);
        buttonSite.setOnMouseEntered(e->this.layout.setCursor(Cursor.HAND));
        buttonSite.setOnMouseExited(e->this.layout.setCursor(Cursor.DEFAULT));
        buttonSite.setTooltip(new Tooltip("Site WEB"));
        buttonSite.setOnMouseClicked(e-> {
            try {
                Desktop.getDesktop().browse(new URI(MvWildLauncher.SITE_URL));
            } catch (IOException ioException) {
                ioException.printStackTrace();
            } catch (URISyntaxException uriSyntaxException) {
                uriSyntaxException.printStackTrace();
            }
        });
        Image voteImage = new Image(Main.class.getResource("/vote.png").toExternalForm());
        ImageView voteImageView = new ImageView(voteImage);
        voteImageView.setFitHeight(60);
        voteImageView.setFitWidth(60);
        Button buttonVote = new Button();
        GridPane.setVgrow(buttonVote, Priority.ALWAYS);
        GridPane.setHgrow(buttonVote, Priority.ALWAYS);
        GridPane.setValignment(buttonVote, VPos.TOP);
        GridPane.setHalignment(buttonVote, HPos.LEFT);
        buttonVote.setTranslateY(450);
        buttonVote.setTranslateX(100);
        buttonVote.setBackground(Background.EMPTY);
        buttonVote.setGraphic(voteImageView);
        buttonVote.setOnMouseEntered(e->this.layout.setCursor(Cursor.HAND));
        buttonVote.setOnMouseExited(e->this.layout.setCursor(Cursor.DEFAULT));
        buttonVote.setTooltip(new Tooltip("Voter"));
        buttonVote.setOnMouseClicked(e-> {
            try {
                Desktop.getDesktop().browse(new URI(MvWildLauncher.VOTE_URL));
            } catch (IOException ioException) {
                ioException.printStackTrace();
            } catch (URISyntaxException uriSyntaxException) {
                uriSyntaxException.printStackTrace();
            }
        });
        Image discordImage = new Image(Main.class.getResource("/discord.png").toExternalForm());
        ImageView imageViewdiscord = new ImageView(discordImage);
        imageViewdiscord.setFitHeight(60);
        imageViewdiscord.setFitWidth(60);
        Button buttonDiscord = new Button();
        GridPane.setVgrow(buttonDiscord, Priority.ALWAYS);
        GridPane.setHgrow(buttonDiscord, Priority.ALWAYS);
        GridPane.setValignment(buttonDiscord, VPos.TOP);
        GridPane.setHalignment(buttonDiscord, HPos.LEFT);
        buttonDiscord.setTranslateY(550);
        buttonDiscord.setTranslateX(200);
        buttonDiscord.setBackground(Background.EMPTY);
        buttonDiscord.setGraphic(imageViewdiscord);
        buttonDiscord.setOnMouseEntered(e->this.layout.setCursor(Cursor.HAND));
        buttonDiscord.setOnMouseExited(e->this.layout.setCursor(Cursor.DEFAULT));
        buttonDiscord.setTooltip(new Tooltip("Discord"));
        buttonDiscord.setOnMouseClicked(e-> {
            try {
                Desktop.getDesktop().browse(new URI(MvWildLauncher.DISCORD_URL));
            } catch (IOException ioException) {
                ioException.printStackTrace();
            } catch (URISyntaxException uriSyntaxException) {
                uriSyntaxException.printStackTrace();
            }
            });
        Image twitterImage = new Image(Main.class.getResource("/twitter.png").toExternalForm());
        ImageView twitterImageView = new ImageView(twitterImage);
        twitterImageView.setFitHeight(60);
        twitterImageView.setFitWidth(60);
        Button buttonTwitter = new Button();
        GridPane.setVgrow(buttonTwitter, Priority.ALWAYS);
        GridPane.setHgrow(buttonTwitter, Priority.ALWAYS);
        GridPane.setValignment(buttonTwitter, VPos.TOP);
        GridPane.setHalignment(buttonTwitter, HPos.LEFT);
        buttonTwitter.setTranslateY(550);
        buttonTwitter.setTranslateX(100);
        buttonTwitter.setBackground(Background.EMPTY);
        buttonTwitter.setGraphic(twitterImageView);
        buttonTwitter.setOnMouseEntered(e->this.layout.setCursor(Cursor.HAND));
        buttonTwitter.setOnMouseExited(e->this.layout.setCursor(Cursor.DEFAULT));
        buttonTwitter.setTooltip(new Tooltip("Twitter"));
        buttonTwitter.setOnMouseClicked(e-> {
            try {
                Desktop.getDesktop().browse(new URI(MvWildLauncher.TWITTER_URL));
            } catch (IOException ioException) {
                ioException.printStackTrace();
            } catch (URISyntaxException uriSyntaxException) {
                uriSyntaxException.printStackTrace();
            }
        });
        Image instagramImage = new Image(Main.class.getResource("/instagram.png").toExternalForm());
        ImageView instagramImageView = new ImageView(instagramImage);
        instagramImageView.setFitHeight(60);
        instagramImageView.setFitWidth(60);
        Button buttonInstagram = new Button();
        GridPane.setVgrow(buttonInstagram, Priority.ALWAYS);
        GridPane.setHgrow(buttonInstagram, Priority.ALWAYS);
        GridPane.setValignment(buttonInstagram, VPos.TOP);
        GridPane.setHalignment(buttonInstagram, HPos.LEFT);
        buttonInstagram.setTranslateY(550);
        buttonInstagram.setTranslateX(0);
        buttonInstagram.setBackground(Background.EMPTY);
        buttonInstagram.setGraphic(instagramImageView);
        buttonInstagram.setOnMouseEntered(e->this.layout.setCursor(Cursor.HAND));
        buttonInstagram.setOnMouseExited(e->this.layout.setCursor(Cursor.DEFAULT));
        buttonInstagram.setTooltip(new Tooltip("Instagram"));
        buttonInstagram.setOnMouseClicked(e-> {
            try {
                Desktop.getDesktop().browse(new URI(MvWildLauncher.INSTAGRAM_URL));
            } catch (IOException ioException) {
                ioException.printStackTrace();
            } catch (URISyntaxException uriSyntaxException) {
                uriSyntaxException.printStackTrace();
            }
        });
        Image facebookImage = new Image(Main.class.getResource("/facebook.png").toExternalForm());
        ImageView facebookImageView = new ImageView(facebookImage);
        facebookImageView.setFitHeight(60);
        facebookImageView.setFitWidth(60);
        Button buttonFacebook = new Button();
        GridPane.setVgrow(buttonFacebook, Priority.ALWAYS);
        GridPane.setHgrow(buttonFacebook, Priority.ALWAYS);
        GridPane.setValignment(buttonFacebook, VPos.TOP);
        GridPane.setHalignment(buttonFacebook, HPos.LEFT);
        buttonFacebook.setTranslateY(550);
        buttonFacebook.setTranslateX(300);
        buttonFacebook.setBackground(Background.EMPTY);
        buttonFacebook.setGraphic(facebookImageView);
        buttonFacebook.setOnMouseEntered(e->this.layout.setCursor(Cursor.HAND));
        buttonFacebook.setOnMouseExited(e->this.layout.setCursor(Cursor.DEFAULT));
        buttonFacebook.setTooltip(new Tooltip("Facebook"));
        buttonFacebook.setOnMouseClicked(e-> {
            try {
                Desktop.getDesktop().browse(new URI(MvWildLauncher.FACEBOOK_URL));
            } catch (IOException ioException) {
                ioException.printStackTrace();
            } catch (URISyntaxException uriSyntaxException) {
                uriSyntaxException.printStackTrace();
            }
        });

        installButton.setOnMouseClicked(e-> {
            panelManager.install();
        });
        //Ajout des éléments
        pane.getChildren().addAll(mvwildTitle, survie, desc, buttonPlayer, nbCo, twitter, installButton, buttonSite, buttonDiscord, buttonTwitter, buttonFacebook, buttonInstagram, buttonVote, progressBar, status);
    }

    //Modification texte barre de dl
    public void setStatus(String status) {
        this.status.setText(status);
    }

    //Partie gauche du home panel
    private void showLeftBar(GridPane pane) {
        // Affichage de la tête du joueur avec son pseudo
        GridPane.setVgrow(pseudo, Priority.ALWAYS);
        GridPane.setHgrow(pseudo, Priority.ALWAYS);
        GridPane.setValignment(pseudo, VPos.BOTTOM);
        pseudo.setStyle("-fx-font-size: 26px; -fx-text-fill: white; -fx-font-weight: bold");
        pseudo.setTranslateX(110);
        pseudo.setTranslateY(560);


        GridPane.setVgrow(imageViewTete, Priority.ALWAYS);
        GridPane.setHgrow(imageViewTete, Priority.ALWAYS);
        GridPane.setValignment(imageViewTete, VPos.TOP);
        GridPane.setHalignment(imageViewTete, HPos.LEFT);
        imageViewTete.setTranslateX(34);
        imageViewTete.setTranslateY(570);
        imageViewTete.setFitHeight(60);
        imageViewTete.setFitWidth(60);
        imageViewTete.setOnMouseEntered(e->{
            this.layout.setCursor(Cursor.HAND);
        });
        imageViewTete.setOnMouseExited(e->this.layout.setCursor(Cursor.DEFAULT));
        pseudo.setTooltip(new Tooltip("Se déconnecter"));
        pseudo.setOnMouseEntered(e-> {
            this.layout.setCursor(Cursor.HAND);
            pseudo.setTooltip(new Tooltip("Se déconnecter"));

        });
        pseudo.setOnMouseExited(e->this.layout.setCursor(Cursor.DEFAULT));
        imageViewTete.setOnMouseClicked(e-> {
            this.panelManager.showPanel(MainPanel.LOGIN);
        });
        pseudo.setOnMouseClicked(e-> {
            this.panelManager.showPanel(MainPanel.LOGIN);
        });
        //Fin affichage pseudo + tete joueur

        //Selection de l'onglet
        //Play
        Separator bluePlaySelSeparator = new Separator();
        GridPane.setVgrow(bluePlaySelSeparator, Priority.ALWAYS);
        GridPane.setHgrow(bluePlaySelSeparator, Priority.ALWAYS);
        bluePlaySelSeparator.setOrientation(Orientation.VERTICAL);
        bluePlaySelSeparator.setMinWidth(3);
        bluePlaySelSeparator.setMaxWidth(3);
        bluePlaySelSeparator.setMinHeight(60);
        bluePlaySelSeparator.setMaxHeight(60);
        bluePlaySelSeparator.setStyle("-fx-background-color: #52872F; -fx-border-width: 3 3 3 0; -fx-border-color: #52872F;");
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
        jouerLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: white");
        jouerLabel.setOnMouseEntered(e->this.layout.setCursor(Cursor.HAND));
        jouerLabel.setOnMouseExited(e->this.layout.setCursor(Cursor.DEFAULT));

        //Parametres
        Separator blueSettingSelSeparator = new Separator();
        GridPane.setVgrow(blueSettingSelSeparator, Priority.ALWAYS);
        GridPane.setHgrow(blueSettingSelSeparator, Priority.ALWAYS);
        blueSettingSelSeparator.setOrientation(Orientation.VERTICAL);
        blueSettingSelSeparator.setMinWidth(3);
        blueSettingSelSeparator.setMaxWidth(3);
        blueSettingSelSeparator.setMinHeight(60);
        blueSettingSelSeparator.setMaxHeight(60);
        blueSettingSelSeparator.setTranslateY(70);
        blueSettingSelSeparator.setStyle("-fx-background-color: #52872F; -fx-border-width: 3 3 3 0; -fx-border-color: #52872F;");
        blueSettingSelSeparator.setVisible(false);
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
        settingLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: white");
        settingLabel.setOnMouseEntered(e->this.layout.setCursor(Cursor.HAND));
        settingLabel.setOnMouseExited(e->this.layout.setCursor(Cursor.DEFAULT));

        //Control affichage
        jouerLabel.setOnMouseClicked(e->{
            scrollPane.setContent(vBoxMv);
            bluePlaySelSeparator.setVisible(true);
            blueSettingSelSeparator.setVisible(false);
        });
        imageViewMvWild.setOnMouseClicked(e->{
            scrollPane.setContent(vBoxMv);
            bluePlaySelSeparator.setVisible(true);
            blueSettingSelSeparator.setVisible(false);
        });
        logoSetting.setOnMouseClicked(e->{
            scrollPane.setContent(vBoxSettings);
            bluePlaySelSeparator.setVisible(false);
            blueSettingSelSeparator.setVisible(true);
        });
        settingLabel.setOnMouseClicked(e->{
            scrollPane.setContent(vBoxSettings);
            bluePlaySelSeparator.setVisible(false);
            blueSettingSelSeparator.setVisible(true);
        });
        //Fin selection onglet

        //Ajout des éléments
        pane.getChildren().addAll(bluePlaySelSeparator, imageViewMvWild, jouerLabel, blueSettingSelSeparator, logoSetting, settingLabel, pseudo, imageViewTete);
    } //Fin showLeftBar


    public void setInstallButtonText(String s) {
        installButton.setText(s);
    }

    public void setDisableInstall(boolean disabled) {
        installButton.setDisable(disabled);
    }

    public void setProgress(float avancee, float fin) {
        progressBar.setProgress(avancee, fin);
    }

    public void setPseudo(String pseudo) {
        this.pseudo.setText(pseudo);
        String path = "https://minotar.net/avatar/"+pseudo+"/100.png";
        teteJ = new Image(path);
        imageViewTete.setImage(teteJ);
    }
}

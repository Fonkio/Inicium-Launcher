package fr.fonkio.launcher.ui.panels;

import com.sun.webkit.WebPage;
import fr.fonkio.launcher.Main;
import fr.fonkio.launcher.MvWildLauncher;
import fr.fonkio.launcher.launcher.Launcher;
import fr.fonkio.launcher.utils.HttpRecup;
import javafx.collections.ListChangeListener;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.Effect;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Set;

public class HomeGridPane {

    private final PanelMain panelMain;
    private Label status;
    private Button installButton;
    ProgressBar progressBar = new ProgressBar();

    public HomeGridPane(PanelMain panelMain) {
        this.panelMain = panelMain;
    }

    //Affichage onglet jouer par defaut
    public void addTopPanel(GridPane pane) {
        if (Launcher.offline) {
            installButton = new Button("Mode Hors-Ligne");
        } else {
            installButton = new Button("Jouer");
        }
        status = new Label("Version "+this.panelMain.getVersion() + "/ Fabric : "+this.panelMain.getFabricVersion());

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
                "Rejoignez une communauté de survivants se battant contre la faim, les autres\n" +
                "joueurs, l'économie et des monstres divers parfois étranges et avec de redoutables\n" +
                "pouvoirs... Tout cela dans un seul et unique but : rester en vie !\n" +
                "Parviendrez-vous à survivre selon les règles d'origine de minecraft ?\n" +
                "Rejoignez-nous dès maintenant avec l'adresse ip : "+MvWildLauncher.SERVEUR_IP);
        GridPane.setVgrow(desc, Priority.ALWAYS);
        GridPane.setHgrow(desc, Priority.ALWAYS);
        GridPane.setValignment(desc, VPos.TOP);
        desc.setStyle("-fx-font-size: 14px; -fx-text-fill: #bcc6e7; -fx-opacity: 70%; ");
        desc.setTranslateY(130);

        Image twitterImage = new Image(Main.class.getResource("/twitter.png").toExternalForm());

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
        String content_url = "<a class=\"twitter-timeline\" data-lang=\"fr\" data-theme=\"dark\" href=\""+MvWildLauncher.TWITTER_URL+"\">Chargement des tweets de MvWild_Serveur</a> <script async src=\"https://platform.twitter.com/widgets.js\" charset=\"utf-8\"></script>";
        //String content_url = "<a class=\"twitter-timeline\" data-lang=\"fr\" data-theme=\"dark\" href=\""+MvWildLauncher.TWITTER_URL+"?ref_src=twsrc%5Etfw\">Chargement des tweets ...</a> <script async src=\"https://platform.twitter.com/widgets.js\" charset=\"utf-8\"></script>";
        boolean webEnabled = true;
        try {
            WebView webView = new WebView();
            webView.setStyle("overflow-x: hidden; overflow-y: hidden");
            WebEngine webEngine = webView.getEngine();
            webEngine.loadContent(content_url);
            twitter.getChildren().add(webView);
            webView.setOnMouseClicked(e-> {
                webEngine.loadContent(content_url);
                try {
                    Desktop.getDesktop().browse(new URI(MvWildLauncher.TWITTER_URL));
                } catch (IOException | URISyntaxException ioException) {
                    ioException.printStackTrace();
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
                SwingUtilities.invokeLater(()->page.setBackgroundColor(Color.TRANSLUCENT));
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
        } catch (NoClassDefFoundError e) {
            webEnabled = false;
        }

        //Install et dlBar
        GridPane.setVgrow(installButton, Priority.ALWAYS);
        GridPane.setHgrow(installButton, Priority.ALWAYS);
        GridPane.setValignment(installButton, VPos.TOP);
        GridPane.setHalignment(installButton, HPos.LEFT);

        //progressBar.setBackgroundColor(javafx.scene.paint.Color.rgb(92, 92, 92));
        Stop[] stops = new Stop[]{new Stop(0, javafx.scene.paint.Color.rgb(48, 85, 22)), new Stop(1, javafx.scene.paint.Color.rgb(82, 135, 47))};
        LinearGradient lg = new LinearGradient(0,0,1,0,true, CycleMethod.NO_CYCLE, stops);
        //progressBar.setForegroundColor(lg);
        GridPane.setVgrow(progressBar, Priority.ALWAYS);
        GridPane.setHgrow(progressBar, Priority.ALWAYS);
        GridPane.setValignment(progressBar, VPos.TOP);
        GridPane.setHalignment(progressBar, HPos.LEFT);
        progressBar.setTranslateY(280);
        progressBar.setMinWidth(500);
        progressBar.setStyle("-fx-accent: #52872F");

        GridPane.setVgrow(this.status, Priority.ALWAYS);
        GridPane.setHgrow(this.status, Priority.ALWAYS);
        GridPane.setValignment(this.status, VPos.TOP);
        this.status.setStyle("-fx-font-size: 14px; -fx-text-fill: white; -fx-opacity: 70%;");
        this.status.setTranslateY(300);

        installButton.setTranslateY(330);
        installButton.setMinWidth(140);
        installButton.setMaxHeight(40);
        installButton.setStyle("-fx-background-color: #52872F; -fx-background-insets: 0; -fx-font-size: 14px; -fx-text-fill: white;");
        installButton.setOnMouseEntered(e->this.panelMain.getLayout().setCursor(javafx.scene.Cursor.HAND));
        installButton.setOnMouseExited(e->this.panelMain.getLayout().setCursor(javafx.scene.Cursor.DEFAULT));

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
        buttonSite.setOnMouseEntered(e->this.panelMain.getLayout().setCursor(javafx.scene.Cursor.HAND));
        buttonSite.setOnMouseExited(e->this.panelMain.getLayout().setCursor(javafx.scene.Cursor.DEFAULT));
        buttonSite.setTooltip(new Tooltip("Site WEB"));
        buttonSite.setOnMouseClicked(e-> {
            try {
                Desktop.getDesktop().browse(new URI(MvWildLauncher.SITE_URL));
            } catch (IOException | URISyntaxException ioException) {
                ioException.printStackTrace();
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
        buttonVote.setOnMouseEntered(e->this.panelMain.getLayout().setCursor(javafx.scene.Cursor.HAND));
        buttonVote.setOnMouseExited(e->this.panelMain.getLayout().setCursor(javafx.scene.Cursor.DEFAULT));
        buttonVote.setTooltip(new Tooltip("Voter"));
        buttonVote.setOnMouseClicked(e-> {
            try {
                Desktop.getDesktop().browse(new URI(MvWildLauncher.VOTE_URL));
            } catch (IOException | URISyntaxException ioException) {
                ioException.printStackTrace();
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
        buttonDiscord.setOnMouseEntered(e->this.panelMain.getLayout().setCursor(javafx.scene.Cursor.HAND));
        buttonDiscord.setOnMouseExited(e->this.panelMain.getLayout().setCursor(javafx.scene.Cursor.DEFAULT));
        buttonDiscord.setTooltip(new Tooltip("Discord"));
        buttonDiscord.setOnMouseClicked(e-> {
            try {
                Desktop.getDesktop().browse(new URI(MvWildLauncher.DISCORD_URL));
            } catch (IOException | URISyntaxException ioException) {
                ioException.printStackTrace();
            }
        });
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
        buttonTwitter.setOnMouseEntered(e->this.panelMain.getLayout().setCursor(javafx.scene.Cursor.HAND));
        buttonTwitter.setOnMouseExited(e->this.panelMain.getLayout().setCursor(javafx.scene.Cursor.DEFAULT));
        buttonTwitter.setTooltip(new Tooltip("Twitter"));
        buttonTwitter.setOnMouseClicked(e-> {
            try {
                Desktop.getDesktop().browse(new URI(MvWildLauncher.TWITTER_URL));
            } catch (IOException | URISyntaxException ioException) {
                ioException.printStackTrace();
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
        buttonInstagram.setOnMouseEntered(e->this.panelMain.getLayout().setCursor(javafx.scene.Cursor.HAND));
        buttonInstagram.setOnMouseExited(e->this.panelMain.getLayout().setCursor(javafx.scene.Cursor.DEFAULT));
        buttonInstagram.setTooltip(new Tooltip("Instagram"));
        buttonInstagram.setOnMouseClicked(e-> {
            try {
                Desktop.getDesktop().browse(new URI(MvWildLauncher.INSTAGRAM_URL));
            } catch (IOException | URISyntaxException ioException) {
                ioException.printStackTrace();
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
        buttonFacebook.setOnMouseEntered(e->this.panelMain.getLayout().setCursor(javafx.scene.Cursor.HAND));
        buttonFacebook.setOnMouseExited(e->this.panelMain.getLayout().setCursor(Cursor.DEFAULT));
        buttonFacebook.setTooltip(new Tooltip("Facebook"));
        buttonFacebook.setOnMouseClicked(e-> {
            try {
                Desktop.getDesktop().browse(new URI(MvWildLauncher.FACEBOOK_URL));
            } catch (IOException | URISyntaxException ioException) {
                ioException.printStackTrace();
            }
        });

        installButton.setOnMouseClicked(e-> this.panelMain.install());
        //Ajout des éléments
        pane.getChildren().addAll(mvwildTitle, survie, desc, installButton, buttonSite, buttonDiscord, buttonTwitter, buttonFacebook, buttonInstagram, buttonVote, progressBar, status);
        if (webEnabled) {
            pane.getChildren().add(twitter);
        }
    }

    //Modification texte barre de dl
    public void setStatus(String status) {
        this.status.setText(status);
    }

    public void setInstallButtonText(String s) {
        installButton.setText(s);
    }

    public void setDisableInstall(boolean disabled) {
        installButton.setDisable(disabled);
    }

    public void setProgress(float avancee, float fin) {
        progressBar.setProgress(avancee/fin);
    }

    public void setLoading() {
        progressBar.setProgress(0);
    }
}

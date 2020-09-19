package fr.fonkio.launcher.ui.panels;

import com.sun.webkit.WebPage;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import fr.arinonia.arilibfx.ui.component.AProgressBar;
import fr.flowarg.flowlogger.ILogger;
import fr.flowarg.flowlogger.Logger;
import fr.flowarg.flowupdater.FlowUpdater;
import fr.flowarg.flowupdater.download.IProgressCallback;
import fr.flowarg.flowupdater.download.Step;
import fr.flowarg.flowupdater.download.json.Mod;
import fr.flowarg.flowupdater.utils.UpdaterOptions;
import fr.flowarg.flowupdater.utils.builderapi.BuilderException;
import fr.flowarg.flowupdater.versions.NewForgeVersion;
import fr.flowarg.flowupdater.versions.VanillaVersion;
import fr.flowarg.flowupdater.versions.VersionType;
import fr.flowarg.openlauncherlib.NewForgeVersionDiscriminator;
import fr.fonkio.launcher.Main;
import fr.fonkio.launcher.MvWildLauncher;
import fr.fonkio.launcher.files.FileManager;
import fr.fonkio.launcher.ui.PanelManager;
import fr.fonkio.launcher.ui.panel.Panel;
import fr.theshark34.openlauncherlib.LaunchException;
import fr.theshark34.openlauncherlib.external.ExternalLaunchProfile;
import fr.theshark34.openlauncherlib.external.ExternalLauncher;
import fr.theshark34.openlauncherlib.minecraft.*;
import fr.theshark34.openlauncherlib.util.Saver;
import javafx.application.Platform;
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
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
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
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.List;
import java.util.Timer;

public class HomePanel extends Panel {

    private final FileManager fileManager = new FileManager(MvWildLauncher.SERVEUR_NAME.toLowerCase());
    private GridPane centerPane = new GridPane();
    private AProgressBar leftDownloadBar;
    private Label status = new Label("");
    private String pseudo;
    private File dir = fileManager.getGameFolder();
    private Saver saver = new Saver(fileManager.getLauncherProperties());
    private VBox vBoxMv;
    private VBox vBoxSettings;
    private ScrollPane scrollPane = new ScrollPane();
    private Button installButton;

    public HomePanel(Stage stage) {
        super(stage);
        this.pseudo = "";
    }

    @Override
    public void init(PanelManager panelManager) {
        super.init(panelManager);

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
            JOptionPane.showMessageDialog(null, "Erreur de mise à jour de la version minecraft", "Erreur updater", JOptionPane.ERROR_MESSAGE);
            MvWildLauncher.stopRP();
            System.exit(0);
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

        String ramStr = saver.get("RAM");
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


        Button save = new Button("Sauvegarder");
        GridPane.setVgrow(save, Priority.ALWAYS);
        GridPane.setHgrow(save, Priority.ALWAYS);
        GridPane.setValignment(save, VPos.BOTTOM);
        GridPane.setHalignment(save, HPos.RIGHT);
        save.setMinWidth(140);
        save.setMaxHeight(40);
        save.setStyle("-fx-background-color: dodgerblue; -fx-background-insets: 0; -fx-font-size: 14px; -fx-text-fill: white;");
        save.setOnMouseEntered(e->this.layout.setCursor(Cursor.HAND));
        save.setOnMouseExited(e->this.layout.setCursor(Cursor.DEFAULT));
        save.setOnMouseClicked(e-> {
            double ramD = slider.getValue()*1024;
            saver.set("RAM", (Math.round(ramD)+""));
            saver.save();
            save.setText("Sauvegardé !");
            save.setStyle("-fx-background-color: green; -fx-background-insets: 0; -fx-font-size: 14px; -fx-text-fill: white;");
        });
        slider.setOnDragDetected(e-> {
            save.setText("Sauvegarder");
            save.setStyle("-fx-background-color: dodgerblue; -fx-background-insets: 0; -fx-font-size: 14px; -fx-text-fill: white;");
        });
        topPanelSettings.getChildren().addAll(settingsTitle, ramTitle, ram0, slider, save);
    }

    //Affichage onglet jouer par defaut
    private void addTopPanel(GridPane pane) throws BuilderException {
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
        survie.setStyle("-fx-font-size: 14px; -fx-text-fill: white; -fx-opacity: 70%;");
        survie.setTranslateY(70);

        Label desc = new Label(MvWildLauncher.SERVEUR_NAME+", le serveur minecraft ou le Gamemode est, et restera sur 0 !\n" +
                "Rejoignez une communauté de survivant se battant contre la faim, les autres joueurs,\n" +
                "l'économies et des monstres divers parfois étranges et avec de redoutables pouvoirs...\n" +
                "Tout cela dans un seul et unique but : rester en vie !\n" +
                "Parviendrez vous à survivre selon les règles d'origines de minecraft ?\n" +
                "Rejoignez nous dès maintenant avec l'adresse ip : "+MvWildLauncher.SERVEUR_IP);
        GridPane.setVgrow(desc, Priority.ALWAYS);
        GridPane.setHgrow(desc, Priority.ALWAYS);
        GridPane.setValignment(desc, VPos.TOP);
        desc.setStyle("-fx-font-size: 14px; -fx-text-fill: #bcc6e7; -fx-opacity: 70%;");
        desc.setTranslateY(130);

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
        webView.setOnMouseClicked(e-> {
            try {
                Desktop.getDesktop().browse(new URI(MvWildLauncher.TWITTER_URL));
            } catch (IOException ioException) {
                ioException.printStackTrace();
            } catch (URISyntaxException uriSyntaxException) {
                uriSyntaxException.printStackTrace();
            }
        });
        webView.setStyle("overflow-x: hidden; overflow-y: hidden");
        WebEngine webEngine = webView.getEngine();
        webEngine.loadContent(content_url);
        twitter.getChildren().add(webView);
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

        //Recuperation des versions
        String strVersion = getVersion(MvWildLauncher.SITE_URL +"version.php");
        if (strVersion == null) return;
        String strForgeVersion = getVersion(MvWildLauncher.SITE_URL +"launcher/forgeVersion.php");
        if (strForgeVersion == null) {
            return;
        }
        String strMCPVersion = getVersion(MvWildLauncher.SITE_URL +"launcher/mcpVersion.php");
        if (strForgeVersion == null) {
            return;
        }

        if (!fileManager.createGameDir().exists()) {
            fileManager.createGameDir().mkdir();
        }
        IProgressCallback dlCallback = new IProgressCallback() {
            private String status = "";
            @Override
            public void init(ILogger logger) {
                Main.logger.log("Création callback "+this.getClass().getName());
            }

            @Override
            public void step(Step step) {
                switch (step.toString()) {
                    case "READ":
                        this.status = "Verification des fichiers ";
                        break;
                    case "DL_LIBS":
                        this.status = "Téléchargement des librairies ";
                        break;
                    case "DL_ASSETS":
                        this.status = "Téléchargement des assets ";
                        break;
                    case "EXTRACT_NATIVES":
                        this.status = "Extraction en cours, veuillez patienter ";
                        break;
                    case "MODS":
                        this.status = "Récupération des mods ";
                        break;
                    case "FORGE":
                        this.status = "Installation de forge ";
                        break;
                    case "INTERNAL_FORGE_HACKS":
                        this.status = "Forge installé, lancement ";
                        break;
                    case "END":
                        this.status = "Le jeu a été lancé !";
                        break;
                    default:
                        this.status = "Chargement ";
                        Main.logger.warn(step.toString());
                        break;
                }
                Platform.runLater(()-> {
                    setStatus(this.status+"...");
                });
            }
            @Override
            public void update(int downloaded, int max) {
                Platform.runLater(()-> {
                    setStatus(this.status + downloaded+"/"+max +"...");
                });
            }
        };//Fin déclaration Callback

        //Recuperation updater
        //Version vanilla
        //final FlowUpdater updater = updateVanilla(dir, dlCallback, strVersion);
        //Version forge
        final FlowUpdater updater = updateForge(dir, dlCallback, strVersion, strForgeVersion);
        if (updater == null) {
            JOptionPane.showMessageDialog(null, "Erreur de mise à jour de la version minecraft (null)", "Erreur updater", JOptionPane.ERROR_MESSAGE);
            MvWildLauncher.stopRP();
            System.exit(0);
        }

        //Install et dlBar
        installButton = new Button("Jouer");
        GridPane.setVgrow(installButton, Priority.ALWAYS);
        GridPane.setHgrow(installButton, Priority.ALWAYS);
        GridPane.setValignment(installButton, VPos.TOP);
        GridPane.setHalignment(installButton, HPos.LEFT);

        AProgressBar progressBar = new AProgressBar(400, 20);
        progressBar.setBackgroundColor(Color.rgb(3, 48, 90));
        Stop[] stops = new Stop[]{new Stop(0, Color.rgb(7, 85, 136)), new Stop(1, Color.rgb(3, 163, 219))};
        LinearGradient lg = new LinearGradient(0,0,1,0,true, CycleMethod.NO_CYCLE, stops);
        progressBar.setForegroundColor(lg);
        GridPane.setVgrow(progressBar, Priority.ALWAYS);
        GridPane.setHgrow(progressBar, Priority.ALWAYS);
        GridPane.setValignment(progressBar, VPos.TOP);
        GridPane.setHalignment(progressBar, HPos.LEFT);
        progressBar.setTranslateY(330);
        progressBar.setProgress(100,150);

        this.status = new Label("Version "+strVersion + "/ Forge : "+strForgeVersion);
        GridPane.setVgrow(this.status, Priority.ALWAYS);
        GridPane.setHgrow(this.status, Priority.ALWAYS);
        GridPane.setValignment(this.status, VPos.TOP);
        this.status.setStyle("-fx-font-size: 14px; -fx-text-fill: white; -fx-opacity: 70%;");
        this.status.setTranslateY(350);

        installButton.setTranslateY(280);
        installButton.setMinWidth(140);
        installButton.setMaxHeight(40);
        installButton.setStyle("-fx-background-color: #115faa; -fx-background-insets: 0; -fx-font-size: 14px; -fx-text-fill: white;");
        installButton.setOnMouseEntered(e->this.layout.setCursor(Cursor.HAND));
        installButton.setOnMouseExited(e->this.layout.setCursor(Cursor.DEFAULT));

        //Socials
        Image siteImage = new Image(Main.class.getResource("/site.png").toExternalForm());
        ImageView imageViewSite = new ImageView(siteImage);
        GridPane.setVgrow(imageViewSite, Priority.ALWAYS);
        GridPane.setHgrow(imageViewSite, Priority.ALWAYS);
        GridPane.setValignment(imageViewSite, VPos.TOP);
        GridPane.setHalignment(imageViewSite, HPos.LEFT);
        imageViewSite.setTranslateY(500);
        imageViewSite.setFitHeight(60);
        imageViewSite.setFitWidth(60);
        imageViewSite.setOnMouseEntered(e->this.layout.setCursor(Cursor.HAND));
        imageViewSite.setOnMouseExited(e->this.layout.setCursor(Cursor.DEFAULT));
        imageViewSite.setOnMouseClicked(e-> {
            try {
                Desktop.getDesktop().browse(new URI(MvWildLauncher.SITE_URL));
            } catch (IOException ioException) {
                ioException.printStackTrace();
            } catch (URISyntaxException uriSyntaxException) {
                uriSyntaxException.printStackTrace();
            }
        });
        Image discordImage = new Image(Main.class.getResource("/discord.png").toExternalForm());
        ImageView imageViewdiscord = new ImageView(discordImage);
        GridPane.setVgrow(imageViewdiscord, Priority.ALWAYS);
        GridPane.setHgrow(imageViewdiscord, Priority.ALWAYS);
        GridPane.setValignment(imageViewdiscord, VPos.TOP);
        GridPane.setHalignment(imageViewdiscord, HPos.LEFT);
        imageViewdiscord.setTranslateY(500);
        imageViewdiscord.setTranslateX(100);
        imageViewdiscord.setFitHeight(60);
        imageViewdiscord.setFitWidth(60);
        imageViewdiscord.setOnMouseEntered(e->this.layout.setCursor(Cursor.HAND));
        imageViewdiscord.setOnMouseExited(e->this.layout.setCursor(Cursor.DEFAULT));
        imageViewdiscord.setOnMouseClicked(e-> {
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
        GridPane.setVgrow(twitterImageView, Priority.ALWAYS);
        GridPane.setHgrow(twitterImageView, Priority.ALWAYS);
        GridPane.setValignment(twitterImageView, VPos.TOP);
        GridPane.setHalignment(twitterImageView, HPos.LEFT);
        twitterImageView.setTranslateY(500);
        twitterImageView.setTranslateX(200);
        twitterImageView.setFitHeight(60);
        twitterImageView.setFitWidth(60);
        twitterImageView.setOnMouseEntered(e->this.layout.setCursor(Cursor.HAND));
        twitterImageView.setOnMouseExited(e->this.layout.setCursor(Cursor.DEFAULT));
        twitterImageView.setOnMouseClicked(e-> {
            try {
                Desktop.getDesktop().browse(new URI(MvWildLauncher.TWITTER_URL));
            } catch (IOException ioException) {
                ioException.printStackTrace();
            } catch (URISyntaxException uriSyntaxException) {
                uriSyntaxException.printStackTrace();
            }
        });
        Image facebookImage = new Image(Main.class.getResource("/facebook.png").toExternalForm());
        ImageView facebookImageView = new ImageView(facebookImage);
        GridPane.setVgrow(facebookImageView, Priority.ALWAYS);
        GridPane.setHgrow(facebookImageView, Priority.ALWAYS);
        GridPane.setValignment(facebookImageView, VPos.TOP);
        GridPane.setHalignment(facebookImageView, HPos.LEFT);
        facebookImageView.setTranslateY(500);
        facebookImageView.setTranslateX(300);
        facebookImageView.setFitHeight(60);
        facebookImageView.setFitWidth(60);
        facebookImageView.setOnMouseEntered(e->this.layout.setCursor(Cursor.HAND));
        facebookImageView.setOnMouseExited(e->this.layout.setCursor(Cursor.DEFAULT));
        facebookImageView.setOnMouseClicked(e-> {
            try {
                Desktop.getDesktop().browse(new URI(MvWildLauncher.FACEBOOK_URL));
            } catch (IOException ioException) {
                ioException.printStackTrace();
            } catch (URISyntaxException uriSyntaxException) {
                uriSyntaxException.printStackTrace();
            }
        });

        progressBar.setProgress(0,100);
        leftDownloadBar.setProgress(0,100);

        installButton.setOnMouseClicked(e-> {
            MvWildLauncher.updatePresence(strVersion, "Lancement du jeu", "mvwildlogo", pseudo);
            installButton.setDisable(true);
            TimerTask updateBar = new TimerTask() {
                public void run() {
                    float dl = updater.getDownloadInfos().getDownloaded()*1.0f;
                    float dlTot = updater.getDownloadInfos().getTotalToDownload()*1.0f;
                    leftDownloadBar.setProgress(dl, dlTot);
                    progressBar.setProgress(dl, dlTot);
                }
            };
            Timer timerUpdateBar = new Timer("timerUpdateBar");
            long delay  = 50L;
            long period = 50L;
            timerUpdateBar.scheduleAtFixedRate(updateBar, delay, period);
            Thread t = new Thread() {
                @Override
                public void run() {
                    try {
                        updater.update(dir, false);
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                        installButton.setText("Erreur");
                        installButton.setDisable(true);
                    } finally {
                        timerUpdateBar.cancel();
                    }
                    Platform.runLater(() -> {
                            status.setText("Lancement ...");
                            try {
                                launch(strVersion, strForgeVersion, strMCPVersion);
                            } catch (LaunchException l) {
                                getStage().setIconified(false);
                                MvWildLauncher.updatePresence(null, "Dans le launcher", "mvwildlogo", pseudo);
                                installButton.setText("Relancer");
                                installButton.setDisable(false);
                                l.printStackTrace();
                            }
                        });
                }
            };
            t.start();
        });
        //Ajout des éléments
        pane.getChildren().addAll(mvwildTitle, survie, desc, twitter, installButton, imageViewSite, imageViewdiscord, twitterImageView, facebookImageView, progressBar, status);

    }

    //Modification texte barre de dl
    public void setStatus(String status) {
        this.status.setText(status);
    }

    //Modif pseudo
    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    //Récupération du texte sur un URL pour les versions
    private String getVersion(String url) {
        String inputline = null;
        try{
            URLConnection connection = (new URL(url).openConnection());
            connection.setRequestProperty("User-Agent", MvWildLauncher.CONFIG_WEB);
            connection.connect();
            InputStream is = connection.getInputStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            inputline = in.readLine();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erreur de récupération de la version minecraft. Merci de nous contacter si cela se reproduit.", "Erreur url connection", JOptionPane.ERROR_MESSAGE);
            MvWildLauncher.stopRP();
            System.exit(0);
        }
        if (inputline == null) {
            return null;
        }
        Main.logger.log("Version recupérée : "+inputline);
        return inputline;
    }

    //Partie gauche du home panel
    private void showLeftBar(GridPane pane) {
        // Affichage de la tête du joueur avec son pseudo
        Label pseudo = new Label(this.pseudo);
        GridPane.setVgrow(pseudo, Priority.ALWAYS);
        GridPane.setHgrow(pseudo, Priority.ALWAYS);
        GridPane.setValignment(pseudo, VPos.BOTTOM);
        pseudo.setStyle("-fx-font-size: 26px; -fx-text-fill: white; -fx-font-weight: bold");
        pseudo.setTranslateX(110);
        pseudo.setTranslateY(560);
        String path = "https://minotar.net/avatar/"+this.pseudo+"/100.png";
        Image teteJ = new Image(path);
        ImageView imageViewTete = new ImageView(teteJ);
        GridPane.setVgrow(imageViewTete, Priority.ALWAYS);
        GridPane.setHgrow(imageViewTete, Priority.ALWAYS);
        GridPane.setValignment(imageViewTete, VPos.TOP);
        GridPane.setHalignment(imageViewTete, HPos.LEFT);
        imageViewTete.setTranslateX(34);
        imageViewTete.setTranslateY(570);
        imageViewTete.setFitHeight(60);
        imageViewTete.setFitWidth(60);
        imageViewTete.setOnMouseEntered(e->this.layout.setCursor(Cursor.HAND));
        imageViewTete.setOnMouseExited(e->this.layout.setCursor(Cursor.DEFAULT));
        Tooltip tt = new Tooltip("Se déconnecter");
        pseudo.setTooltip(tt);
        pseudo.setOnMouseEntered(e->this.layout.setCursor(Cursor.HAND));
        pseudo.setOnMouseExited(e->this.layout.setCursor(Cursor.DEFAULT));
        imageViewTete.setOnMouseClicked(e->this.panelManager.showPanel(new PanelLogin(getStage())));
        pseudo.setOnMouseClicked(e->this.panelManager.showPanel(new PanelLogin(getStage())));
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
        bluePlaySelSeparator.setStyle("-fx-background-color: rgb(5,179,242); -fx-border-width: 3 3 3 0; -fx-border-color: rgb(5,179,242);");
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
        leftDownloadBar = new AProgressBar(150, 3);
        leftDownloadBar.setBackgroundColor(Color.rgb(222, 222, 222, 0.3d));
        leftDownloadBar.setForegroundColor(Color.rgb(255 ,255,255));
        leftDownloadBar.setTranslateX(110.0d);
        leftDownloadBar.setTranslateY(12.0d);
        leftDownloadBar.setProgress(0,100);
        leftDownloadBar.setOnMouseEntered(e->this.layout.setCursor(Cursor.HAND));
        leftDownloadBar.setOnMouseExited(e->this.layout.setCursor(Cursor.DEFAULT));

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
        blueSettingSelSeparator.setStyle("-fx-background-color: rgb(5,179,242); -fx-border-width: 3 3 3 0; -fx-border-color: rgb(5,179,242);");
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
        leftDownloadBar.setOnMouseClicked(e->{
            scrollPane.setContent(vBoxMv);
            bluePlaySelSeparator.setVisible(true);
            blueSettingSelSeparator.setVisible(false);
        });
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
        pane.getChildren().addAll(bluePlaySelSeparator, imageViewMvWild, jouerLabel, leftDownloadBar, blueSettingSelSeparator, logoSetting, settingLabel, pseudo, imageViewTete);
    } //Fin showLeftBar

    /*private FlowUpdater updateVanilla(File dir, IProgressCallback callback, String strVersion) throws IOException, BuilderArgumentException {
        final IVanillaVersion.Builder versionBuilder = new IVanillaVersion.Builder(strVersion);
        final IVanillaVersion version = versionBuilder.build(false, VersionType.VANILLA);
        final FlowUpdater updater = new FlowUpdater.FlowUpdaterBuilder().withVersion(version).withLogger(new Logger("["+MvWildLauncher.SERVEUR_NAME+"]", fileManager.getLauncherLog())).withProgressCallback(callback).build();
        return updater;
    }*/

    private FlowUpdater updateForge(File dir, IProgressCallback callback, String versionMc, String versionForge) throws BuilderException {

        //Pas de mod pour l'instant
        List<Mod> mods = new ArrayList<>();
        //List<Mod> mods = Mod.getModsFromJson(MvWildLauncher.URL+"launcher/mods.json");

        Logger logger = new Logger("["+MvWildLauncher.SERVEUR_NAME+"]", fileManager.getLauncherLog());

        final VanillaVersion version = new VanillaVersion.VanillaVersionBuilder().withName(versionMc).withSnapshot(false).withVersionType(VersionType.FORGE).build();
        NewForgeVersion forge = new NewForgeVersion(versionForge, version, logger, callback, mods, false );
        final FlowUpdater updater = new FlowUpdater.FlowUpdaterBuilder().withVersion(version).withForgeVersion(forge).withLogger(logger).withUpdaterOptions(new UpdaterOptions(false, true)).withProgressCallback(callback).build();
        return updater;
    }

    public void launch(String version, String versionForge, String versionMCP) throws LaunchException {
        GameVersion gameVersion = new GameVersion(version, GameType.V1_13_HIGER_FORGE.setNewForgeVersionDiscriminator(new NewForgeVersionDiscriminator(versionForge, version, "net.minecraftforge", versionMCP)));
        GameInfos gameInfos = new GameInfos(MvWildLauncher.SERVEUR_NAME, gameVersion, new GameTweak[0]);
        GameFolder gameFolder = new GameFolder("/assets/", "/libraries/", "/natives/", "/client.jar");
        AuthInfos authInfos = new AuthInfos(pseudo, "compte", "crack");

        ExternalLaunchProfile profile = MinecraftLauncher.createExternalProfile(gameInfos, gameFolder, authInfos);
        ExternalLauncher launcher = new ExternalLauncher(profile);
        //Lancement
        MvWildLauncher.updatePresence(version, "En jeu", "mvwildlogo", pseudo);
        Process p = launcher.launch();
        try {
            Thread.sleep(5000L);
        } catch (InterruptedException e) {}
        getStage().setIconified(true);
        try {
            p.waitFor(); //Attente fermeture du jeu
        } catch (InterruptedException e) {
        }
        getStage().setIconified(false);
        installButton.setDisable(false);
        installButton.setText("Relancer");
        MvWildLauncher.updatePresence(version, "Retour sur le launcher", "mvwildlogo", pseudo);
        setStatus("");
    }

}

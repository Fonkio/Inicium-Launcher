package fr.fonkio.launcher.ui.panels;

import fr.fonkio.launcher.MvWildLauncher;
import fr.fonkio.launcher.files.FileManager;
import fr.fonkio.launcher.ui.PanelManager;
import fr.fonkio.launcher.ui.panel.Panel;
import fr.fonkio.launcher.utils.MainPanel;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.io.*;
import java.util.Timer;
import java.util.TimerTask;


public class PanelLogin extends Panel {
    private final Button validate = new Button();
    private GridPane loginPanel;

    public PanelLogin(Stage stage) throws IOException {
        super(stage);
        File dir = FileManager.createGameDir();
        if(!dir.exists()) {
            boolean created = dir.mkdir();
            if (!created) {
                MvWildLauncher.logger.err("Le dossier n'a pas pu être créé");
            }
        }

    }

    @Override
    public void init(PanelManager panelManager) {
        super.init(panelManager);

        this.loginPanel = new GridPane();
        GridPane mainPanel = new GridPane();

        loginPanel.setMaxWidth(1280);
        loginPanel.setMinWidth(1280);
        loginPanel.setMinHeight(1000);
        loginPanel.setMaxHeight(1080);

        GridPane.setVgrow(loginPanel, Priority.ALWAYS);
        GridPane.setHgrow(loginPanel, Priority.ALWAYS);
        GridPane.setValignment(loginPanel, VPos.TOP);
        GridPane.setHalignment(loginPanel, HPos.LEFT);

        RowConstraints bottomConstraints = new RowConstraints();
        bottomConstraints.setValignment(VPos.BOTTOM);
        bottomConstraints.setMaxHeight(55);
        loginPanel.getRowConstraints().addAll(new RowConstraints(), bottomConstraints);
        loginPanel.add(mainPanel, 0, 0);

        GridPane.setVgrow(mainPanel, Priority.ALWAYS);
        GridPane.setHgrow(mainPanel, Priority.ALWAYS);

        mainPanel.setStyle("-fx-background-color: #181818;");

        Label bienvenue = new Label("MvWild Launcher");
        GridPane.setVgrow(bienvenue, Priority.ALWAYS);
        GridPane.setHgrow(bienvenue, Priority.ALWAYS);
        GridPane.setValignment(bienvenue, VPos.TOP);
        GridPane.setHalignment(bienvenue, HPos.CENTER);
        bienvenue.setTranslateY(270);

        bienvenue.setStyle("-fx-text-fill: white; -fx-font-size: 35px; -fx-font-style: bold");

        Separator connectSeparator = new Separator();
        GridPane.setVgrow(connectSeparator, Priority.ALWAYS);
        GridPane.setHgrow(connectSeparator, Priority.ALWAYS);
        GridPane.setValignment(connectSeparator, VPos.TOP);
        GridPane.setHalignment(connectSeparator, HPos.CENTER);
        connectSeparator.setTranslateY(335);
        connectSeparator.setMinWidth(325);
        connectSeparator.setMaxWidth(325);
        connectSeparator.setStyle("-fx-background-color: white; -fx-opacity: 50%;");

        ImageView view = new ImageView(new Image("microsoft.png"));
        view.setPreserveRatio(true);
        view.setFitHeight(30d);
        GridPane.setVgrow(validate, Priority.ALWAYS);
        GridPane.setHgrow(validate, Priority.ALWAYS);
        GridPane.setValignment(validate, VPos.TOP);
        GridPane.setHalignment(validate, HPos.CENTER);
        validate.setGraphic(view);
        validate.setTranslateY(370);
        validate.setMinWidth(325);
        validate.setMaxHeight(50);
        validate.setStyle("-fx-background-color: #52872F; -fx-border-radius: 0px; -fx-background-insets: 0; -fx-font-size: 20px; -fx-font-style: bold; -fx-text-fill: white");
        validate.setOnMouseEntered(e->this.layout.setCursor(Cursor.HAND));
        validate.setOnMouseExited(e->this.layout.setCursor(Cursor.DEFAULT));
        validate.setOnMouseClicked(e-> {
            connexionEnCours();
            panelManager.connexion();
        });
        if (panelManager.isConnected()) {
            connexionEnCours();
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    connexionAnimation(panelManager);
                }
            };
            Timer timer = new Timer("Timer");
            long delay = 1000L;
            timer.schedule(task, delay);
        } else {
            seConnecter();
        }

        mainPanel.getChildren().addAll(bienvenue, connectSeparator, validate);
        this.layout.getChildren().add(loginPanel);
    }

    private void connexionEnCours() {
        validate.setDisable(true);
        Platform.runLater(() -> validate.setText(" Connexion..."));
    }

    private void seConnecter() {
        validate.setDisable(false);
        Platform.runLater(() -> validate.setText(" Connexion à Microsoft"));
    }

    public void connected(PanelManager panelManager) {
        connexionAnimation(panelManager);
    }

    public void disconnected() {
        seConnecter();
    }

    private void connexionAnimation(PanelManager panelManager) {
        TimerTask task = new TimerTask() {
            public void run() {
                Platform.runLater(() -> panelManager.showPanel(MainPanel.HOME)
                );
            }
        };

        Timer timer = new Timer("Timer");
        long delay = 1000L;
        TranslateTransition tt = new TranslateTransition(Duration.millis(500), loginPanel);
        tt.setToX(1280);
        tt.play();

        timer.schedule(task, delay);
    }

    public void showPanel() {
        seConnecter();
        TranslateTransition tt = new TranslateTransition(Duration.millis(500), loginPanel);
        tt.setToX(0);
        tt.play();
    }

}

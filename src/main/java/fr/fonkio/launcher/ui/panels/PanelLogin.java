package fr.fonkio.launcher.ui.panels;


import fr.fonkio.launcher.Main;
import fr.fonkio.launcher.MvWildLauncher;
import fr.fonkio.launcher.files.FileManager;
import fr.fonkio.launcher.ui.PanelManager;
import fr.fonkio.launcher.ui.panel.Panel;
import fr.theshark34.openlauncherlib.util.Saver;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.io.*;
import java.util.Timer;
import java.util.TimerTask;


public class PanelLogin extends Panel {
    private final Button validate = new Button("Valider");
    private final TextField usernameTextField = new TextField();
    private final Saver saver;
    private GridPane loginPanel;
    private final CheckBox saveName = new CheckBox();

    Label saveNameLabel = new Label("Se connecter automatiquement");

    public PanelLogin(Stage stage) throws IOException {
        super(stage);
        FileManager fileManager = new FileManager(MvWildLauncher.SERVEUR_NAME.toLowerCase());
        File dir = fileManager.createGameDir();
        if(!dir.exists()) {
            boolean created = dir.mkdir();
            if (!created) {
                MvWildLauncher.logger.err("Le dossier n'a pas pu être créé");
            }
        }
        if(!fileManager.getLauncherProperties().exists()){
            boolean created = fileManager.getLauncherProperties().createNewFile();
            if (!created) {
                MvWildLauncher.logger.err("Le dossier n'a pas pu être créé");
            }
        }
        saver = new Saver(fileManager.getLauncherProperties());
    }

    @Override
    public void init(PanelManager panelManager) {
        super.init(panelManager);
        String savePseudo = saver.get("name");
        if (savePseudo != null) { //pseudo save
            connexionAnimation(panelManager, true);
            setDisableAll(true);
        } else {
            setDisableAll(false);
        }


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

        Label bienvenue = new Label("SE CONNECTER !");
        GridPane.setVgrow(bienvenue, Priority.ALWAYS);
        GridPane.setHgrow(bienvenue, Priority.ALWAYS);
        GridPane.setValignment(bienvenue, VPos.TOP);
        GridPane.setHalignment(bienvenue, HPos.CENTER);
        bienvenue.setTranslateY(27);

        bienvenue.setStyle("-fx-text-fill: white; -fx-font-size: 20px;");

        Separator connectSeparator = new Separator();
        GridPane.setVgrow(connectSeparator, Priority.ALWAYS);
        GridPane.setHgrow(connectSeparator, Priority.ALWAYS);
        GridPane.setValignment(connectSeparator, VPos.TOP);
        GridPane.setHalignment(connectSeparator, HPos.CENTER);
        connectSeparator.setTranslateY(60);
        connectSeparator.setMinWidth(325);
        connectSeparator.setMaxWidth(325);
        connectSeparator.setStyle("-fx-background-color: white; -fx-opacity: 50%;");

        Label usernameLabel = new Label("Pseudo");
        GridPane.setVgrow(usernameLabel, Priority.ALWAYS);
        GridPane.setHgrow(usernameLabel, Priority.ALWAYS);
        GridPane.setValignment(usernameLabel, VPos.TOP);
        GridPane.setHalignment(usernameLabel, HPos.CENTER);
        usernameLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");
        usernameLabel.setTranslateY(270);
        usernameLabel.setTranslateX(-135);


        if (savePseudo != null) {
            usernameTextField.setText(savePseudo);
        }
        GridPane.setVgrow(usernameTextField, Priority.ALWAYS);
        GridPane.setHgrow(usernameTextField, Priority.ALWAYS);
        GridPane.setValignment(usernameTextField, VPos.TOP);
        GridPane.setHalignment(usernameTextField, HPos.CENTER);
        usernameTextField.setStyle("-fx-background-color: #1e1e1e; -fx-font-size: 16px; -fx-text-fill: #e5e5e5");
        usernameTextField.setMaxWidth(325);
        usernameTextField.setMaxHeight(40);
        usernameTextField.setTranslateY(300);
        usernameTextField.setOnKeyReleased(e->{

            if(usernameTextField.getText().length() >= 3) {
                if (e.getCode().equals(KeyCode.ENTER)) {
                    connexionAnimation(panelManager, false);
                } else {
                    validate.setDisable(false);
                }
            } else {
                validate.setDisable(true);
            }

        });

        Separator usernameSeparator = new Separator();
        GridPane.setVgrow(usernameSeparator, Priority.ALWAYS);
        GridPane.setHgrow(usernameSeparator, Priority.ALWAYS);
        GridPane.setValignment(usernameSeparator, VPos.CENTER);
        GridPane.setHalignment(usernameSeparator, HPos.CENTER);
        usernameSeparator.setTranslateY(-20);
        usernameSeparator.setMinWidth(325);
        usernameSeparator.setMaxWidth(325);
        usernameSeparator.setMaxHeight(1);
        usernameSeparator.setStyle("-fx-background-color: white; -fx-opacity: 50%;");


        Separator validateSeparator = new Separator();
        GridPane.setVgrow(validateSeparator, Priority.ALWAYS);
        GridPane.setHgrow(validateSeparator, Priority.ALWAYS);
        GridPane.setValignment(validateSeparator, VPos.TOP);
        GridPane.setHalignment(validateSeparator, HPos.CENTER);
        validateSeparator.setTranslateY(60);
        validateSeparator.setMinWidth(325);
        validateSeparator.setMaxWidth(325);
        validateSeparator.setStyle("-fx-background-color: white; -fx-opacity: 50%;");


        saveName.setSelected(savePseudo != null);
        GridPane.setVgrow(saveName, Priority.ALWAYS);
        GridPane.setHgrow(saveName, Priority.ALWAYS);
        GridPane.setValignment(saveName, VPos.TOP);
        GridPane.setHalignment(saveName, HPos.CENTER);
        saveName.setTranslateY(400);
        saveName.setTranslateX(-150);

        GridPane.setVgrow(saveNameLabel, Priority.ALWAYS);
        GridPane.setHgrow(saveNameLabel, Priority.ALWAYS);
        GridPane.setValignment(saveNameLabel, VPos.TOP);
        GridPane.setHalignment(saveNameLabel, HPos.CENTER);
        saveNameLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");
        saveNameLabel.setTranslateY(400);
        saveNameLabel.setTranslateX(-40);
        saveNameLabel.setOnMouseClicked(e -> saveName.setSelected(!saveName.isSelected()));


        GridPane.setVgrow(validate, Priority.ALWAYS);
        GridPane.setHgrow(validate, Priority.ALWAYS);
        GridPane.setValignment(validate, VPos.CENTER);
        GridPane.setHalignment(validate, HPos.CENTER);
        validate.setTranslateY(20);
        validate.setMinWidth(325);
        validate.setMaxHeight(50);
        validate.setStyle("-fx-background-color: #52872F; -fx-border-radius: 0px; -fx-background-insets: 0; -fx-font: 14px; -fx-text-fill: white");
        validate.setOnMouseEntered(e->this.layout.setCursor(Cursor.HAND));
        validate.setOnMouseExited(e->this.layout.setCursor(Cursor.DEFAULT));
        validate.setOnMouseClicked(e-> connexionAnimation(panelManager, false));
        if(usernameTextField.getText().length() < 3) {
            validate.setDisable(true);
        }

        mainPanel.getChildren().addAll(bienvenue, connectSeparator, usernameLabel, usernameTextField, usernameSeparator, saveName, saveNameLabel, validate);
        this.layout.getChildren().add(loginPanel);
    }

    private void connexionAnimation(PanelManager panelManager, boolean quickStart) {
        TimerTask task = new TimerTask() {
            public void run() {
                Platform.runLater(panelManager::connexion);
            }
        };
        if (!quickStart) {
            if (saveName.isSelected()) {
                saver.set("name", usernameTextField.getText());
            } else {
                saver.remove("name");
                System.out.println(validate.getText());
            }
        }
        saver.save();
        Timer timer = new Timer("Timer");

        long delay = 1000L;
        TranslateTransition tt = new TranslateTransition(Duration.millis(500), loginPanel);
        tt.setToX(1280);
        tt.play();

        timer.schedule(task, delay);
    }

    public void showPanel() {
        TranslateTransition tt = new TranslateTransition(Duration.millis(500), loginPanel);
        tt.setToX(0);
        tt.play();
        setDisableAll(false);
    }

    private void setDisableAll(boolean disabled) {
        saveName.setDisable(disabled);
        usernameTextField.setDisable(disabled);
        saveNameLabel.setDisable(disabled);
        if (disabled) {
            validate.setText("Connexion en cours ...");
        } else {
            validate.setText("Valider");
        }
        validate.setDisable(disabled);
    }

    public String getPseudoTextField() {
        return this.usernameTextField.getText();
    }
}

package fr.fonkio.launcher.ui.panels;

import fr.fonkio.launcher.Main;
import fr.fonkio.launcher.MvWildLauncher;
import fr.fonkio.launcher.files.FileManager;
import fr.fonkio.launcher.ui.PanelManager;
import fr.fonkio.launcher.ui.panel.Panel;
import fr.theshark34.openlauncherlib.util.Saver;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Stage;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;


public class PanelLogin extends Panel {
    private final FileManager fileManager = new FileManager(MvWildLauncher.SERVEUR_NAME.toLowerCase());
    private File dir = fileManager.createGameDir();
    private HomePanel hp;
    private Button validate = new Button("Valider");
    private TextField usernameTextField = new TextField();
    private Saver saver;

    public PanelLogin(Stage stage) {
        super(stage);

        File f = fileManager.getLauncherProperties();
        if(!dir.exists()) {
            dir.mkdir();
        }
        if (!f.exists()) {
            try {
                f.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        saver = new Saver(f);
        hp = new HomePanel(getStage());

    }

    @Override
    public void init(PanelManager panelManager) {
        super.init(panelManager);

        try{
            URLConnection connection = (new URL(MvWildLauncher.SITE_URL +"launcher/version.php").openConnection());
            connection.setRequestProperty("User-Agent", MvWildLauncher.CONFIG_WEB);
            connection.connect();
            InputStream is = connection.getInputStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            String version = in.readLine();
            if (!version.equals(MvWildLauncher.LAUNCHER_VERSION)) {
                Thread t = new Thread() {
                    @Override
                    public void run() {
                        switch (JOptionPane.showConfirmDialog(null, "Une nouvelle version est disponible !\nVersion actuelle : " + MvWildLauncher.LAUNCHER_VERSION + "\nNouvelle version : " + version)) {
                            case JOptionPane.OK_OPTION:
                                try {
                                    Desktop.getDesktop().browse(new URI(MvWildLauncher.SITE_URL + "launcher/"));
                                } catch (IOException e) {
                                    e.printStackTrace();
                                } catch (URISyntaxException e) {
                                    e.printStackTrace();
                                }
                                MvWildLauncher.stopRP();
                                System.exit(0);
                                break;
                            default:
                                break;
                        }
                    }
                };
                t.start();
            }
        } catch (Exception e) {

        }

        MvWildLauncher.updatePresence(null, "Connexion au launcher ...", "mvwildlogo", null);
        GridPane loginPanel = new GridPane();
        GridPane mainPanel = new GridPane();

        loginPanel.setMaxWidth(400);
        loginPanel.setMinWidth(400);
        loginPanel.setMinHeight(580);
        loginPanel.setMaxHeight(580);

        GridPane.setVgrow(loginPanel, Priority.ALWAYS);
        GridPane.setHgrow(loginPanel, Priority.ALWAYS);
        GridPane.setValignment(loginPanel, VPos.CENTER);
        GridPane.setHalignment(loginPanel, HPos.CENTER);

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
        GridPane.setValignment(usernameLabel, VPos.CENTER);
        GridPane.setHalignment(usernameLabel, HPos.LEFT);
        usernameLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");
        usernameLabel.setTranslateY(-70);
        usernameLabel.setTranslateX(37.5);

        String savePseudo = saver.get("pseudo");
        if (savePseudo != null) {
            usernameTextField.setText(savePseudo);
        }
        GridPane.setVgrow(usernameTextField, Priority.ALWAYS);
        GridPane.setHgrow(usernameTextField, Priority.ALWAYS);
        GridPane.setValignment(usernameTextField, VPos.CENTER);
        GridPane.setHalignment(usernameTextField, HPos.LEFT);
        usernameTextField.setStyle("-fx-background-color: #1e1e1e; -fx-font-size: 16px; -fx-text-fill: #e5e5e5");
        usernameTextField.setMaxWidth(325);
        usernameTextField.setMaxHeight(40);
        usernameTextField.setTranslateX(37.5);
        usernameTextField.setTranslateY(-40);
        usernameTextField.setOnKeyReleased(e->{
            if(e.getCode().equals(KeyCode.ENTER)) {
                connexion();
            }
            if(usernameTextField.getText().length() < 3) {
                validate.setDisable(true);
            } else {
                validate.setDisable(false);
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
        validate.setOnMouseClicked(e->{
            connexion();
        });
        if(usernameTextField.getText().length() < 3) {
            validate.setDisable(true);
        }

        mainPanel.getChildren().addAll(bienvenue, connectSeparator, usernameLabel, usernameTextField, usernameSeparator, validate);
        this.layout.getChildren().add(loginPanel);
    }

    private void connexion() {
        String pseudo = this.usernameTextField.getText();
        if (pseudo.length()<3) {
            JOptionPane.showMessageDialog(null, "Le pseudo est trop court !", "Erreur pseudo", JOptionPane.ERROR_MESSAGE);
        } else {
            MvWildLauncher.updatePresence(null, "Dans le launcher", "mvwildlogo", pseudo);
            this.validate.setText("Récupération des versions ...");
            Main.logger.log("Connexion avec le pseudo : "+pseudo);
            saver.set("pseudo", pseudo);
            saver.save();
            hp.setPseudo(pseudo);
            this.panelManager.showPanel(hp);
        }
    }
}

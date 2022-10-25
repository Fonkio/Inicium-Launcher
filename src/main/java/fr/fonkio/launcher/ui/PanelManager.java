package fr.fonkio.launcher.ui;

import fr.flowarg.flowupdater.utils.builderapi.BuilderException;
import fr.fonkio.launcher.Main;
import fr.fonkio.launcher.MvWildLauncher;
import fr.fonkio.launcher.launcher.Launcher;
import fr.fonkio.launcher.launcher.MvAuth;
import fr.fonkio.launcher.ui.panel.Panel;
import fr.fonkio.launcher.ui.panels.PanelMain;
import fr.fonkio.launcher.ui.panels.PanelLogin;
import fr.fonkio.launcher.utils.MainPanel;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.*;

public class PanelManager {

    private final Stage stage;
    private final GridPane centerPanel = new GridPane();
    PanelLogin panelLogin;
    boolean loginInit = false;
    PanelMain panelMain;
    boolean homeInit = false;
    MvAuth mvAuth;
    fr.fonkio.launcher.utils.MainPanel currentPanel;
    Launcher launcher;

    public PanelManager(Stage stage) throws URISyntaxException, BuilderException, MalformedURLException {
        this.stage = stage;
        launcher = new Launcher(this);
        panelMain = new PanelMain(stage, this);
        mvAuth = new MvAuth(this);
        try {
            panelLogin = new PanelLogin(stage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void init() {
        this.stage.setTitle(MvWildLauncher.SERVEUR_NAME + " Launcher");
        this.stage.setMinWidth(1280);
        this.stage.setWidth(1280);
        this.stage.setMinHeight(720);
        this.stage.setHeight(720);
        this.stage.initStyle(StageStyle.DECORATED);
        this.stage.centerOnScreen();
        this.stage.getIcons().add(new Image(Main.class.getResource("/logoNBG.png").toExternalForm()));
        this.stage.show();
        stage.setOnCloseRequest(we ->
                System.exit(0));

        GridPane layout = new GridPane();
        layout.setStyle(
                "-fx-background-image: url('"+Main.class.getResource("/fondLauncher.png").toExternalForm()+"');"
                +"-fx-backgound-repeat: skretch;"+"-fx-backgound-position: center center;"
                +"-fx-background-size: cover;");
        this.stage.setScene(new Scene(layout));
        this.stage.setResizable(false);


        layout.add(this.centerPanel, 0, 0);
        GridPane.setVgrow(this.centerPanel, Priority.ALWAYS);
        GridPane.setHgrow(this.centerPanel, Priority.ALWAYS);

        checkLauncherVersion();
        showPanel(fr.fonkio.launcher.utils.MainPanel.LOGIN);

        MvWildLauncher.updatePresence(null, "Connexion au launcher ...", "mvwildlogo", null);
    }

    public void showPanel(fr.fonkio.launcher.utils.MainPanel name) {
        Panel panel = null;
        boolean changementPanel = true;
        boolean initPanel = true;
        switch (name) {
            case HOME -> {
                panel = panelMain;
                changementPanel = currentPanel.equals(fr.fonkio.launcher.utils.MainPanel.LOGIN);
                initPanel = !homeInit;
                homeInit = true;
                currentPanel = fr.fonkio.launcher.utils.MainPanel.HOME;
            }
            case PARAMETRES -> {
                panel = panelMain;
                changementPanel = currentPanel.equals(fr.fonkio.launcher.utils.MainPanel.LOGIN);
                initPanel = !homeInit;
                homeInit = true;
                currentPanel = fr.fonkio.launcher.utils.MainPanel.PARAMETRES;
            }
            case LOGIN -> {
                currentPanel = fr.fonkio.launcher.utils.MainPanel.LOGIN;
                initPanel = !loginInit;
                loginInit = true;
                panel = panelLogin;
                panelLogin.showPanel();
            }
        }
        if (panel != null){
            if(changementPanel) {
                this.centerPanel.getChildren().clear();
                this.centerPanel.getChildren().add(panel.getLayout());
            }
            if(initPanel) {
                panel.init(this);
            }
            panel.onShow();
        }

    }

    public Stage getStage() {
        return stage;
    }
    public void setInstallButtonText(String s) {
        if (homeInit) {
            panelMain.setInstallButtonText(s);
        }
    }

    public void setDisableInstallButton(boolean b) {
        if (homeInit) {
            panelMain.setDisableInstall(b);
        }
    }
    public void setProgress(float avancee, float fin) {
        if (homeInit) {
            panelMain.setProgress(avancee, fin);
        }
    }

    public void setStatus(String s) {
        if(homeInit) {
            panelMain.setStatus(s);
        }
    }

    public String getRAM() {
        return this.launcher.getRAM();
    }

    public void setRAM(double ramD) {
        this.launcher.setRAM(ramD);
    }

    public void connexion() {
        this.mvAuth.connexion();
    }

    public String checkLauncherVersion() {
        return this.launcher.checkVersion();
    }

    public void install() {
        this.launcher.install();
    }

    public String getVersion() {
        return this.launcher.getVersion();
    }

    public String getFabricVersion() {
        return this.launcher.getFabricVersion();
    }

    public void setDisableDRP(boolean selected) {
        this.launcher.setDisableDRP(selected);
    }

    public Boolean isDRPDisabled() {
        return this.launcher.isDRPDisabled();
    }

    public void resetLauncher() {
        launcher.resetLauncher();
    }

    public boolean containsModsFolder() {
        return launcher.containsModsFolder();
    }

    public boolean isConnected() {
        return mvAuth.isConnected();
    }

    public MvAuth getMvAuth() {
        return mvAuth;
    }

    public void deconnexion() {
        mvAuth.deconnexion();
        showPanel(MainPanel.LOGIN);
    }

    public void connected() {
        panelLogin.connected(this);
    }
}

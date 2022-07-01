package fr.fonkio.launcher.launcher;

import fr.flowarg.flowupdater.download.IProgressCallback;
import fr.flowarg.flowupdater.download.json.Mod;
import fr.flowarg.flowupdater.utils.builderapi.BuilderException;
import fr.fonkio.launcher.MvWildLauncher;
import fr.fonkio.launcher.files.FileManager;
import fr.fonkio.launcher.files.MvSaver;
import fr.fonkio.launcher.ui.PanelManager;
import fr.fonkio.launcher.utils.HttpRecup;
import fr.fonkio.launcher.utils.MainPanel;
import fr.theshark34.openlauncherlib.LaunchException;
import fr.theshark34.openlauncherlib.external.ExternalLaunchProfile;
import fr.theshark34.openlauncherlib.external.ExternalLauncher;
import fr.theshark34.openlauncherlib.minecraft.*;
import fr.theshark34.openlauncherlib.util.Saver;
import javafx.application.Platform;
import javafx.scene.control.Alert;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class Launcher {

    private final MvSaver saver = new MvSaver();
    private final PanelManager panelManager;
    String pseudo;
    String strVersion;
    String strForgeVersion = null;
    String strFabricVersion;
    IProgressCallback mvCallback;
    Updater updater;

    public static boolean offline = false;
    


    public Launcher(PanelManager panelManager) throws BuilderException, URISyntaxException, MalformedURLException {
        this.panelManager = panelManager;

        if (!FileManager.createGameDir().exists()) {
            boolean created = FileManager.createGameDir().mkdir();
            if (!created) {
                MvWildLauncher.logger.err("Le dossier n'a pas pu être créé");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Le dossier n'a pas pu être créé");
                alert.show();
            }
        }

        mvCallback = new MvCallback(this.panelManager);

        strVersion = getVersion("launcher/versionMc.php", "mcVersion");
        strFabricVersion = getVersion("launcher/fabricVersion.php", "fabricVersion");

        try {
            updater = new Updater(strVersion, strFabricVersion, mvCallback);
        } catch (Exception e) {
            if (!offline) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur création de la mise à jour");
                alert.setContentText(e.getMessage());
                alert.setOnCloseRequest(event -> stop());
                alert.show();
            }
        }
    }

    private void stop() {
        MvWildLauncher.stopRP();
        System.exit(0);
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public String getPseudo() {
        return this.pseudo;
    }

    private String getVersion(String path, String versionName) {
        String version = null;
        if (!offline) {
            version = HttpRecup.getVersion(MvWildLauncher.SITE_URL + path);
        }
        if (version == null) {
            if (saver.get(versionName) == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Impossible de lancer le jeu");
                alert.setContentText("Le mode hors ligne du launcher est disponible uniquement quand le jeu a été lancé au moins une fois.");
                alert.setOnCloseRequest(event -> stop());
                alert.show();
            } else {
                goOffiline();
                version = saver.get(versionName);
                MvWildLauncher.logger.info("Mode hors ligne ... Version "+ versionName +" recuperee : "+ version);
            }
        }
        saver.set(versionName, version);
        saver.save();
        return version;
    }

    private void goOffiline() {
        this.panelManager.setInstallButtonText("Jouer hors ligne");
        offline = true;
        MvWildLauncher.logger.info("Mode hors ligne ...");
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Passage en mode hors ligne");
        alert.setContentText("Impossible d'établir la connexion avec le serveur, passage en mode hors ligne");
        alert.show();
    }

    public void connexion() {
        String pseudo = this.panelManager.getPseudoTextField();
        if (pseudo.length()<3) {
            JOptionPane.showMessageDialog(null, "Le pseudo est trop court !", "Erreur pseudo", JOptionPane.ERROR_MESSAGE);
        } else {
            MvWildLauncher.updatePresence(null, "Dans le launcher", "mvwildlogo", pseudo);
            MvWildLauncher.logger.info("Connexion avec le pseudo : "+pseudo);
            this.panelManager.setPseudo(pseudo);
            this.panelManager.showPanel(MainPanel.HOME);
        }
    }

    public void install() {
        MvWildLauncher.updatePresence(strVersion, "Lancement du jeu", "mvwildlogo", pseudo);
        this.panelManager.setDisableInstallButton(true);
        if (offline) {
            this.panelManager.setProgress(100, 100);
        }
        Thread t = new Thread(() -> {
            if (!offline) {
                try {
                    File modFolder = new File(String.valueOf(FileManager.getModFolderPath()));
                    if (modFolder.isDirectory()) {
                        List<String> name = new ArrayList<>();
                        for (Mod mod : Updater.getMods()) {
                            name.add(mod.getName());
                        }
                        for (File mod : modFolder.listFiles()) {
                            if (mod.getName().startsWith("AI_")) {
                                if(!name.contains(mod.getName())) {
                                    mod.delete();
                                }
                            }
                        }
                    }
                    updater.update();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                    panelManager.setInstallButtonText("Erreur");
                    panelManager.setDisableInstallButton(true);
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
                System.out.println("Fin update");
            }
            Platform.runLater(() -> {
                panelManager.setStatus("Lancement ...");
                try {
                    launch(strVersion);
                } catch (Exception e) {
                    e.printStackTrace();
                    panelManager.getStage().setIconified(false);
                    MvWildLauncher.updatePresence(null, "Dans le launcher", "mvwildlogo", pseudo);
                    panelManager.setInstallButtonText("Relancer");
                    panelManager.setDisableInstallButton(false);
                    if(offline) {
                        JOptionPane.showMessageDialog(null, "Impossible de lancer le jeu ! L'installation n'est pas complète, il est nécessaire d'être en ligne pour lancer le jeu !", "Erreur", JOptionPane.ERROR_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(null, "Impossible de lancer le jeu !", "Erreur", JOptionPane.ERROR_MESSAGE);
                    }
                    panelManager.setStatus("");

                }
            });
        });
        t.start();
    }

    public void launch(String version) throws LaunchException {

        GameVersion gameVersion = new GameVersion(version, GameType.FABRIC);
        GameInfos gameInfos = new GameInfos(MvWildLauncher.SERVEUR_NAME, gameVersion, new GameTweak[0]);
        GameFolder gameFolder = GameFolder.FLOW_UPDATER_1_19_SUP;
        AuthInfos authInfos = new AuthInfos(pseudo, "compte", "crack");

        ExternalLaunchProfile profile = MinecraftLauncher.createExternalProfile(gameInfos, gameFolder, authInfos);

        //Gestion param RAM
        if(saver.get("RAM") != null) {
            profile.getVmArgs().add("-Xmx" + saver.get("RAM") + "M");
        }

        ExternalLauncher launcher = new ExternalLauncher(profile);

        //Lancement
        MvWildLauncher.updatePresence(version, "En jeu", "mvwildlogo", pseudo);
        this.panelManager.setStatus("Jeu lancé");
        Process p = launcher.launch();
        this.panelManager.getStage().setIconified(true);

        //Thread attente fermeture du jeu
        Thread t = new Thread(() -> {
            try {
                p.waitFor();
                Platform.runLater(()->{
                    panelManager.getStage().setIconified(false);
                    panelManager.setDisableInstallButton(false);
                    panelManager.setInstallButtonText("Relancer");
                    panelManager.setStatus("");
                });

                MvWildLauncher.updatePresence(version, "Retour sur le launcher", "mvwildlogo", pseudo);

            } catch (InterruptedException ignored) {}
        });
        t.start();
    }

    public String getRAM() {
        return saver.get("RAM");
    }

    public void setRAM(double ramD) {
        if (ramD == 0) {
            saver.remove("RAM");
        } else {
            saver.set("RAM", (Math.round(ramD)+""));
        }
        saver.save();
    }

    public void setDisableDRP(boolean selected) {
        if(selected) {
            MvWildLauncher.stopRP();
        }
        saver.set("disableDRP", selected+"");
        saver.save();
    }

    public Boolean isDRPDisabled() {
        return Boolean.parseBoolean(saver.get("disableDRP"));
    }

    public String getVersion() {
        return strVersion;
    }

    public String getForgeVersion() {
        return strForgeVersion;
    }

    public String getFabricVersion() {
        return strFabricVersion;
    }

    public boolean containsModsFolder() {
        File modFolder = FileManager.getModFolderPath().toFile();
        return modFolder.exists();
    }

    public void resetLauncher() {
        File launcherFolder = FileManager.getGameFolder();
        System.out.println(launcherFolder.getPath());
        deleteDirectory(launcherFolder);
    }

    private void deleteDirectory(File directoryToBeDeleted) {
        File[] allContents = directoryToBeDeleted.listFiles();
        if (allContents != null) {
            for (File file : allContents) {
                if (!file.getName().startsWith("launcher.")) {
                    deleteDirectory(file);
                }
            }
        }
        directoryToBeDeleted.delete();
    }

    public String checkVersion() {
        String version = HttpRecup.getVersion(MvWildLauncher.SITE_URL +"launcher/version.php");
        if (version != null && (!Launcher.offline) && !version.equals(MvWildLauncher.LAUNCHER_VERSION)) {
            return version;
        }
        return null;
    }

}

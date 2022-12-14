package fr.fonkio.launcher.launcher;

import fr.flowarg.flowupdater.download.IProgressCallback;
import fr.flowarg.flowupdater.download.json.Mod;
import fr.flowarg.flowupdater.utils.builderapi.BuilderException;
import fr.flowarg.openlauncherlib.NoFramework;
import fr.fonkio.launcher.MvWildLauncher;
import fr.fonkio.launcher.files.FileManager;
import fr.fonkio.launcher.files.MvSaver;
import fr.fonkio.launcher.ui.PanelManager;
import fr.fonkio.launcher.utils.EnumSaver;
import fr.fonkio.launcher.utils.HttpRecup;
import fr.theshark34.openlauncherlib.minecraft.*;
import javafx.application.Platform;
import javafx.scene.control.Alert;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Launcher {

    private final MvSaver saver = new MvSaver();
    private final PanelManager panelManager;
    String pseudo;
    String minecraftVersion;
    String strForgeVersion = null;
    String fabricVersion;
    IProgressCallback mvCallback;
    Updater updater;

    public static boolean offline = false;
    


    public Launcher(PanelManager panelManager) throws BuilderException {
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

        minecraftVersion = getVersion("launcher/versionMc.php", EnumSaver.VERSION_MC);
        fabricVersion = getVersion("launcher/fabricVersion.php", EnumSaver.VERSION_FABRIC);

        try {
            updater = new Updater(minecraftVersion, fabricVersion, mvCallback);
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

    private String getVersion(String path, EnumSaver versionProp) {
        String version = null;
        if (!offline) {
            version = HttpRecup.getVersion(MvWildLauncher.SITE_URL + path);
        }
        if (version == null) {
            if (saver.get(versionProp) == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Impossible de lancer le jeu");
                alert.setContentText("Le mode hors ligne du launcher est disponible uniquement quand le jeu a été lancé au moins une fois.");
                alert.setOnCloseRequest(event -> stop());
                alert.show();
            } else {
                goOffiline();
                version = saver.get(versionProp);
                MvWildLauncher.logger.info("Mode hors ligne ... Version "+ versionProp.getKey() +" recuperee : "+ version);
            }
        }
        saver.set(versionProp, version);
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

    public void install() {
        MvWildLauncher.updatePresence(minecraftVersion, "Lancement du jeu", "mvwildlogo", pseudo);
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
                        if (modFolder.listFiles() != null) {
                            for (File mod : modFolder.listFiles()) {
                                if (mod.getName().startsWith("AI_")) {
                                    if(!name.contains(mod.getName())) {
                                        mod.delete();
                                    }
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
                    launch();
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

    public void launch() throws Exception {

        GameFolder gameFolder = GameFolder.FLOW_UPDATER_1_19_SUP;
        AuthInfos authInfos = panelManager.getMvAuth().getAuthInfos();



        //Gestion param RAM
        List<String> args = new ArrayList<>();
        if(saver.get(EnumSaver.RAM) != null) {
            args.add("-Xmx" + saver.get(EnumSaver.RAM) + "M");
        }

        NoFramework noFramework = new NoFramework(FileManager.getGameFolderPath(), authInfos, gameFolder, args, NoFramework.Type.VM);

        //Lancement
        MvWildLauncher.updatePresence(minecraftVersion, "En jeu", "mvwildlogo", pseudo);
        this.panelManager.setStatus("Jeu lancé");
        Process p = noFramework.launch(minecraftVersion, fabricVersion, NoFramework.ModLoader.FABRIC);
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

                MvWildLauncher.updatePresence(minecraftVersion, "Retour sur le launcher", "mvwildlogo", pseudo);

            } catch (InterruptedException ignored) {}
        });
        t.start();
    }

    public String getRAM() {
        return saver.get(EnumSaver.RAM);
    }

    public void setRAM(double ramD) {
        if (ramD == 0) {
            saver.remove(EnumSaver.RAM);
        } else {
            saver.set(EnumSaver.RAM, (Math.round(ramD)+""));
        }
        saver.save();
    }

    public void setDisableDRP(boolean selected) {
        if(selected) {
            MvWildLauncher.stopRP();
        } else {
            MvWildLauncher.startRP();
        }
        saver.set(EnumSaver.DISABLE_DRP, Boolean.toString(selected));
        saver.save();
    }

    public Boolean isDRPDisabled() {
        return Boolean.parseBoolean(saver.get(EnumSaver.DISABLE_DRP));
    }

    public String getVersion() {
        return minecraftVersion;
    }

    public String getForgeVersion() {
        return strForgeVersion;
    }

    public String getFabricVersion() {
        return fabricVersion;
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

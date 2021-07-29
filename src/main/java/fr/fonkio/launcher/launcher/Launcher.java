package fr.fonkio.launcher.launcher;

import fr.flowarg.flowupdater.FlowUpdater;
import fr.flowarg.flowupdater.download.IProgressCallback;
import fr.flowarg.flowupdater.download.json.ExternalFile;
import fr.flowarg.flowupdater.download.json.Mod;
import fr.flowarg.flowupdater.utils.UpdaterOptions;
import fr.flowarg.flowupdater.utils.builderapi.BuilderException;
import fr.flowarg.flowupdater.versions.*;
import fr.flowarg.openlauncherlib.NewForgeVersionDiscriminator;
import fr.fonkio.launcher.MvWildLauncher;
import fr.fonkio.launcher.files.FileManager;
import fr.fonkio.launcher.ui.PanelManager;
import fr.fonkio.launcher.utils.HttpRecup;
import fr.fonkio.launcher.utils.MainPanel;
import fr.theshark34.openlauncherlib.LaunchException;
import fr.theshark34.openlauncherlib.external.ExternalLaunchProfile;
import fr.theshark34.openlauncherlib.external.ExternalLauncher;
import fr.theshark34.openlauncherlib.minecraft.*;
import fr.theshark34.openlauncherlib.util.Saver;
import javafx.application.Platform;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Launcher {

    private final FileManager fileManager = new FileManager(MvWildLauncher.SERVEUR_NAME.toLowerCase());
    private final Saver saver = new Saver(fileManager.getLauncherProperties());
    private final PanelManager panelManager;
    String pseudo;
    String strVersion;
    String strForgeVersion = null;
    String strFabricVersion = null;
    String strMCPVersion = null;
    boolean offline = false;
    IProgressCallback dlCallback;
    FlowUpdater updater;
    private final Path dir;

    public Launcher(PanelManager panelManager) throws BuilderException, URISyntaxException, MalformedURLException {
        this.panelManager = panelManager;

        if (!fileManager.createGameDir().exists()) {
            boolean created = fileManager.createGameDir().mkdir();
            if (!created) {
                MvWildLauncher.logger.err("Le dossier n'a pas pu être créé");
            }
        }

        dlCallback = new MvCallback(this.panelManager);

        //Recuperation des versions
        strVersion = HttpRecup.getVersion(MvWildLauncher.SITE_URL +"launcher/versionMc.php");
        MvWildLauncher.logger.info("Version MC : "+ strVersion);
        if (HttpRecup.offline || strVersion == null) {
            if (saver.get("mcVersion") == null) {
                MvWildLauncher.stopRP();
                System.exit(0);
            } else {

                HttpRecup.offline = true;
                strVersion = saver.get("mcVersion");
                MvWildLauncher.logger.warn("Mode hors ligne ... Version mc recuperee : "+strVersion);
            }
        }
        saver.set("mcVersion", strVersion);
        dir = fileManager.getGameFolder(strVersion);

        if (!HttpRecup.offline) {
            strForgeVersion = HttpRecup.getVersion(MvWildLauncher.SITE_URL +"launcher/forgeVersion.php");
            MvWildLauncher.logger.info("Version Forge : "+ strForgeVersion);
        }
        if (strForgeVersion == null) {
            if (saver.get("forgeVersion") == null) {
                MvWildLauncher.stopRP();
                System.exit(0);
            } else {
                this.panelManager.setInstallButtonText("Jouer hors ligne");
                HttpRecup.offline = true;
                strForgeVersion = saver.get("forgeVersion");
                MvWildLauncher.logger.info("Mode hors ligne ... Version Forge recuperee : "+strForgeVersion);
            }

        }
        saver.set("forgeVersion", strForgeVersion);

        if (!HttpRecup.offline) {
            strFabricVersion = HttpRecup.getVersion(MvWildLauncher.SITE_URL +"launcher/fabricVersion.php");
            MvWildLauncher.logger.info("Version Fabric : "+ strFabricVersion);
        }
        if (strFabricVersion == null) {
            if (saver.get("fabricVersion") == null) {
                MvWildLauncher.stopRP();
                System.exit(0);
            } else {
                this.panelManager.setInstallButtonText("Jouer hors ligne");
                HttpRecup.offline = true;
                strFabricVersion = saver.get("fabricVersion");
                MvWildLauncher.logger.info("Mode hors ligne ... Version Fabric recuperee : "+strFabricVersion);
            }

        }
        saver.set("fabricVersion", strFabricVersion);

        if (!HttpRecup.offline) {
            strMCPVersion = HttpRecup.getVersion(MvWildLauncher.SITE_URL +"launcher/mcpVersion.php");
            MvWildLauncher.logger.info("Version MCP : "+ strMCPVersion);
        }
        if (strMCPVersion == null) {
            if (saver.get("mcpVersion") == null) {
                MvWildLauncher.stopRP();
                System.exit(0);
            } else {
                this.panelManager.setInstallButtonText("Jouer hors ligne");
                HttpRecup.offline = true;
                strMCPVersion = saver.get("mcpVersion");
                MvWildLauncher.logger.info("Mode hors ligne ... Version MCP recuperee : "+strMCPVersion);
            }
        }
        saver.set("mcpVersion", strMCPVersion);
        saver.save();

        //strVersion = "1.16.4";
        //strForgeVersion = "35.1.4";
        //strMCPVersion = "20201102.104115";

        //Recuperation updater
        //Version vanilla
        //final FlowUpdater updater = updateVanilla(dir, dlCallback, strVersion);
        //Version forge
        updater = updateFabric(dlCallback, strVersion, strFabricVersion);

        if (updater == null && !HttpRecup.offline) {
            JOptionPane.showMessageDialog(null, "Erreur de mise à jour de la version minecraft (null)", "Erreur updater", JOptionPane.ERROR_MESSAGE);
        }

    }
    /*private FlowUpdater updateVanilla(File dir, IProgressCallback callback, String strVersion) throws IOException, BuilderArgumentException {
        final IVanillaVersion.Builder versionBuilder = new IVanillaVersion.Builder(strVersion);
        final IVanillaVersion version = versionBuilder.build(false, VersionType.VANILLA);
        final FlowUpdater updater = new FlowUpdater.FlowUpdaterBuilder().withVersion(version).withLogger(new Logger("["+MvWildLauncher.SERVEUR_NAME+"]", fileManager.getLauncherLog())).withProgressCallback(callback).build();
        return updater;
    }*/

    private FlowUpdater updateForge(IProgressCallback callback, String versionMc, String versionForge) throws BuilderException, URISyntaxException, MalformedURLException {
        if (HttpRecup.offline) {
            return null;
        }
        //Pas de mod pour l'instant
        //List<Mod> mods = new ArrayList<>();
        List<Mod> mods = Mod.getModsFromJson(MvWildLauncher.SITE_URL+"launcher/mods.php");

        final VanillaVersion version = new VanillaVersion.VanillaVersionBuilder()
                .withName(versionMc)
                .withSnapshot(false)
                .withVersionType(VersionType.FORGE).build();
        AbstractForgeVersion forgeVersion = new ForgeVersionBuilder(ForgeVersionBuilder.ForgeVersionType.NEW)
                .withForgeVersion(versionForge)
                .withMods(mods)
                .build();
        UpdaterOptions options = new UpdaterOptions.UpdaterOptionsBuilder()
                .withSilentRead(false)
                .withReExtractNatives(false)
                .build();
        return new FlowUpdater.FlowUpdaterBuilder().
                withVersion(version).
                withForgeVersion(forgeVersion).
                withLogger(MvWildLauncher.logger).
                withUpdaterOptions(options)
                .withExternalFiles(ExternalFile.getExternalFilesFromJson(new URI(MvWildLauncher.SITE_URL+"launcher/externalfiles/externalfiles.php").toURL()))
                .withProgressCallback(callback)
                .build();
    }

    private FlowUpdater updateFabric(IProgressCallback callback, String versionMc, String versionFabric) throws BuilderException, URISyntaxException, MalformedURLException {
        if (HttpRecup.offline) {
            return null;
        }

        final VanillaVersion version = new VanillaVersion.VanillaVersionBuilder()
                .withName(versionMc)
                .withSnapshot(false)
                .withVersionType(VersionType.FABRIC).build();
        FabricVersion fabricVersion = new FabricVersion.FabricVersionBuilder()
                .withFabricVersion(versionFabric)
                .withMods(MvWildLauncher.getMods())
                .build();
        UpdaterOptions options = new UpdaterOptions.UpdaterOptionsBuilder()
                .withSilentRead(false)
                .withReExtractNatives(false)
                .build();
        return new FlowUpdater.FlowUpdaterBuilder().
                withVersion(version).
                withFabricVersion(fabricVersion).
                withLogger(MvWildLauncher.logger).
                withUpdaterOptions(options)
                .withExternalFiles(ExternalFile.getExternalFilesFromJson(new URI(MvWildLauncher.SITE_URL+"launcher/externalfiles/externalfiles.php").toURL()))
                .withProgressCallback(callback)
                .build();
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public String getPseudo() {
        return this.pseudo;
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
                    File modFolder = new File(this.fileManager.getGameFolder(strVersion)+"/mods");
                    if (modFolder.isDirectory()) {
                        List<String> name = new ArrayList<>();
                        for (Mod mod : MvWildLauncher.getMods()) {
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

                    updater.update(dir);
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
        GameFolder gameFolder = GameFolder.FLOW_UPDATER;
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
        File modFolder = new File(dir+"/mods");
        return modFolder.exists();
    }

    public void resetLauncher() {
        File launcherFolder = fileManager.getGameFolder();
        System.out.println(launcherFolder.getPath());
        deleteDirectory(launcherFolder);
    }

    private boolean deleteDirectory(File directoryToBeDeleted) {
        File[] allContents = directoryToBeDeleted.listFiles();
        if (allContents != null) {
            for (File file : allContents) {
                if (!file.getName().startsWith("launcher.")) {
                    deleteDirectory(file);
                }
            }
        }
        return directoryToBeDeleted.delete();
    }
}

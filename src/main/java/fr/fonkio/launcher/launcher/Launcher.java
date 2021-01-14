package fr.fonkio.launcher.launcher;

import fr.flowarg.flowlogger.Logger;
import fr.flowarg.flowupdater.FlowUpdater;
import fr.flowarg.flowupdater.download.IProgressCallback;
import fr.flowarg.flowupdater.download.json.ExternalFile;
import fr.flowarg.flowupdater.download.json.Mod;
import fr.flowarg.flowupdater.utils.UpdaterOptions;
import fr.flowarg.flowupdater.utils.builderapi.BuilderException;
import fr.flowarg.flowupdater.versions.AbstractForgeVersion;
import fr.flowarg.flowupdater.versions.ForgeVersionBuilder;
import fr.flowarg.flowupdater.versions.VanillaVersion;
import fr.flowarg.flowupdater.versions.VersionType;
import fr.flowarg.openlauncherlib.NewForgeVersionDiscriminator;
import fr.fonkio.launcher.Main;
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
import java.util.List;

public class Launcher {

    private final FileManager fileManager = new FileManager(MvWildLauncher.SERVEUR_NAME.toLowerCase());
    private final Saver saver = new Saver(fileManager.getLauncherProperties());
    private final PanelManager panelManager;
    String pseudo;
    String strVersion;
    String strForgeVersion = null;
    String strMCPVersion = null;
    boolean offline = false;
    IProgressCallback dlCallback;
    FlowUpdater updater;
    private final File dir;

    public Launcher(PanelManager panelManager) throws MalformedURLException, BuilderException, URISyntaxException {
        this.panelManager = panelManager;

        if (!fileManager.createGameDir().exists()) {
            boolean created = fileManager.createGameDir().mkdir();
            if (!created) {
                Main.logger.log("Le dossier n'a pas pu être créé");
            }
        }

        dlCallback = new MvCallback(this.panelManager);

        //Recuperation des versions
        strVersion = HttpRecup.getVersion(MvWildLauncher.SITE_URL +"version.php");
        Main.logger.log("Version MC : "+ strVersion);
        if (HttpRecup.offline || strVersion == null) {
            if (saver.get("mcVersion") == null) {
                MvWildLauncher.stopRP();
                System.exit(0);
            } else {

                HttpRecup.offline = true;
                strVersion = saver.get("mcVersion");
                Main.logger.log("Mode hors ligne ... Version mc recuperee : "+strVersion);
            }
        }
        saver.set("mcVersion", strVersion);
        dir = fileManager.getGameFolder(strVersion);

        if (!HttpRecup.offline) {
            strForgeVersion = HttpRecup.getVersion(MvWildLauncher.SITE_URL +"launcher/forgeVersion.php");
            Main.logger.log("Version Forge : "+ strForgeVersion);
        }
        if (strForgeVersion == null) {
            if (saver.get("forgeVersion") == null) {
                MvWildLauncher.stopRP();
                System.exit(0);
            } else {
                this.panelManager.setInstallButtonText("Jouer hors ligne");
                HttpRecup.offline = true;
                strForgeVersion = saver.get("forgeVersion");
                Main.logger.log("Mode hors ligne ... Version Forge recuperee : "+strForgeVersion);
            }

        }
        saver.set("forgeVersion", strForgeVersion);

        if (!HttpRecup.offline) {
            strMCPVersion = HttpRecup.getVersion(MvWildLauncher.SITE_URL +"launcher/mcpVersion.php");
            Main.logger.log("Version MCP : "+ strMCPVersion);
        }
        if (strMCPVersion == null) {
            if (saver.get("mcpVersion") == null) {
                MvWildLauncher.stopRP();
                System.exit(0);
            } else {
                this.panelManager.setInstallButtonText("Jouer hors ligne");
                HttpRecup.offline = true;
                strMCPVersion = saver.get("mcpVersion");
                Main.logger.log("Mode hors ligne ... Version MCP recuperee : "+strMCPVersion);
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
        updater = updateForge(dlCallback, strVersion, strForgeVersion);

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
        List<Mod> mods = Mod.getModsFromJson(MvWildLauncher.SITE_URL+"launcher/mods.json");
        Logger logger = new Logger("["+MvWildLauncher.SERVEUR_NAME+"]", fileManager.getLauncherLog());

        final VanillaVersion version = new VanillaVersion.VanillaVersionBuilder()
                .withName(versionMc)
                .withSnapshot(false)
                .withVersionType(VersionType.FORGE).build();
        AbstractForgeVersion forgeVersion = new ForgeVersionBuilder(ForgeVersionBuilder.ForgeVersionType.NEW)
                .withForgeVersion(versionForge)
                .withVanillaVersion(version)
                .withLogger(logger)
                .withProgressCallback(callback)
                .withMods(mods)
                .withNoGui(true)
                .build();
        UpdaterOptions options = new UpdaterOptions.UpdaterOptionsBuilder().withSilentRead(false).withReExtractNatives(false).withEnableModsFromCurseForge(false).withInstallOptifineAsMod(false)
                .build();
        return new FlowUpdater.FlowUpdaterBuilder().
                withVersion(version).
                withForgeVersion(forgeVersion).
                withUpdaterOptions(options)
                .withExternalFiles(ExternalFile.getExternalFilesFromJson(new URI(MvWildLauncher.SITE_URL+"launcher/externalfiles.json").toURL()))
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
            Main.logger.log("Connexion avec le pseudo : "+pseudo);
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
                    File modFolder = new File(this.fileManager.createGameDir().getPath()+"/mods");
                    if (modFolder != null && modFolder.isDirectory()) {
                        for (File mod : modFolder.listFiles()) {
                            if (mod.getName().startsWith("AI-")) {
                                if(!mod.getName().contains(strVersion)) {
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
                    launch(strVersion, strForgeVersion, strMCPVersion);
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
    public void launch(String version, String versionForge, String versionMCP) throws LaunchException {
        GameVersion gameVersion = new GameVersion(version, GameType.V1_13_HIGER_FORGE.setNewForgeVersionDiscriminator(new NewForgeVersionDiscriminator(versionForge, version, "net.minecraftforge", versionMCP)));
        GameInfos gameInfos = new GameInfos(MvWildLauncher.SERVEUR_NAME+"/"+version, gameVersion, new GameTweak[0]);
        GameFolder gameFolder = new GameFolder("/assets/", "/libraries/", "/natives/", "/client.jar");
        AuthInfos authInfos = new AuthInfos(pseudo, "compte", "crack");

        ExternalLaunchProfile profile = MinecraftLauncher.createExternalProfile(gameInfos, gameFolder, authInfos);
        if(saver.get("RAM")!=null) {
            profile.getVmArgs().add("-Xmx"+saver.get("RAM")+"M");
        }

        ExternalLauncher launcher = new ExternalLauncher(profile);
        //Lancement
        MvWildLauncher.updatePresence(version, "En jeu", "mvwildlogo", pseudo);
        this.panelManager.setStatus("Jeu lancé");
        Process p = launcher.launch();

        this.panelManager.getStage().setIconified(true);
        Thread t = new Thread(() -> {
            try {
                p.waitFor(); //Attente fermeture du jeu
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

    public void setDRP(boolean selected) {
        if(selected) {
            MvWildLauncher.stopRP();
        }
        saver.set("DRP", selected+"");
    }

    public Boolean getDRP() {
        return Boolean.parseBoolean(saver.get("DRP"));
    }

    public String getVersion() {
        return strVersion;
    }

    public String getForgeVersion() {
        return strForgeVersion;
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

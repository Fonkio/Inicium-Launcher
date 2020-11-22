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
import java.util.Timer;
import java.util.TimerTask;

public class Launcher {

    private final FileManager fileManager = new FileManager(MvWildLauncher.SERVEUR_NAME.toLowerCase());
    private Saver saver = new Saver(fileManager.getLauncherProperties());
    private PanelManager panelManager;
    String pseudo;
    String strVersion = null;
    String strForgeVersion = null;
    String strMCPVersion = null;
    boolean offline = false;
    IProgressCallback dlCallback;
    FlowUpdater updater;
    private Timer timerUpdateBar;
    private File dir = fileManager.getGameFolder();

    public Launcher(PanelManager panelManager) throws MalformedURLException, BuilderException, URISyntaxException {
        this.panelManager = panelManager;

        if (!fileManager.createGameDir().exists()) {
            fileManager.createGameDir().mkdir();
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
        final FlowUpdater updater = new FlowUpdater.FlowUpdaterBuilder().
                withVersion(version).
                withForgeVersion(forgeVersion).
                withUpdaterOptions(new UpdaterOptions(false, false, false))
                .withExternalFiles(ExternalFile.getExternalFilesFromJson(new URI(MvWildLauncher.SITE_URL+"launcher/externalfiles.json").toURL()))
                .withProgressCallback(callback)
                .build();
        return updater;
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
        } else {
            TimerTask updateBar = new TimerTask() {
                public void run() {
                    float dl = updater.getDownloadInfos().getDownloaded()*1.0f;
                    float dlTot = updater.getDownloadInfos().getTotalToDownload()*1.0f;
                    panelManager.setProgress(dl, dlTot);
                }
            };
            this.timerUpdateBar = new Timer("timerUpdateBar");
            long delay  = 50L;
            long period = 50L;
            timerUpdateBar.scheduleAtFixedRate(updateBar, delay, period);
        }

        Thread t = new Thread() {
            @Override
            public void run() {
                if (!offline) {
                    try {
                        updater.update(dir, false);
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                        panelManager.setInstallButtonText("Erreur");
                        panelManager.setDisableInstallButton(true);
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    } finally {
                        timerUpdateBar.cancel();
                    }
                }
                Platform.runLater(() -> {
                    panelManager.setStatus("Lancement ...");
                    try {
                        launch(strVersion, strForgeVersion, strMCPVersion);
                    } catch (Exception e) {
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
            }
        };
        t.start();
    }
    public void launch(String version, String versionForge, String versionMCP) throws LaunchException {
        GameVersion gameVersion = new GameVersion(version, GameType.V1_13_HIGER_FORGE.setNewForgeVersionDiscriminator(new NewForgeVersionDiscriminator(versionForge, version, "net.minecraftforge", versionMCP)));
        GameInfos gameInfos = new GameInfos(MvWildLauncher.SERVEUR_NAME, gameVersion, new GameTweak[0]);
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
        Runnable target;
        Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    p.waitFor(); //Attente fermeture du jeu
                    Platform.runLater(()->{
                        panelManager.getStage().setIconified(false);
                        panelManager.setDisableInstallButton(false);
                        panelManager.setInstallButtonText("Relancer");
                        panelManager.setStatus("");
                    });

                    MvWildLauncher.updatePresence(version, "Retour sur le launcher", "mvwildlogo", pseudo);

                } catch (InterruptedException e) {
                }
            }
        };
        t.start();
    }

    public String getRAM() {
        return saver.get("RAM");
    }

    public void setRAM(double ramD) {
        saver.set("RAM", (Math.round(ramD)+""));
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

    public String getMCPVersion() {
        return strMCPVersion;
    }
}

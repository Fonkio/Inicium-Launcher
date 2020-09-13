package fr.fonkio.launcher;

import club.minnced.discord.rpc.DiscordEventHandlers;
import club.minnced.discord.rpc.DiscordRPC;
import club.minnced.discord.rpc.DiscordRichPresence;
import fr.fonkio.launcher.ui.PanelManager;
import fr.fonkio.launcher.ui.panels.PanelLogin;
import javafx.stage.Stage;

public class MvWildLauncher {

    public static final String LAUNCHER_VERSION = "0.2";
    public static final String SERVEUR_IP = "survie.mvwild.org";
    public static final String TWITTER_URL = "https://twitter.com/MvWild_Serveur";
    public static final String DISCORD_URL = "https://discord.gg/5JcvM2B";
    public static final String FACEBOOK_URL = "https://www.facebook.com/MvwildServeur/";
    private PanelManager panelManager;
    public static final String SERVEUR_NAME = "MvWild";
    public static final String SITE_URL = "https://www.mvwild.org/";
    public static final String CONFIG_WEB = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11(KHTML, like Gecko) Chrome/23/0/1271.95 Safari/53.7.11";
    private static DiscordRPC library = DiscordRPC.INSTANCE;
    private static Thread threadRP;
    public void init(Stage stage) {

        String appName = "752142344240889867";
        String steam = "";
        DiscordEventHandlers handlers = new DiscordEventHandlers();
        handlers.ready = (user) -> System.out.println("DRP - Lancement");
        library.Discord_Initialize(appName, handlers, true, steam);
        updatePresence(null, "Démarrage du launcher ...", "mvwildlogo", null);
        threadRP = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                library.Discord_RunCallbacks();
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException ignored) {}
            }
        }, "RPC-Callback-Handler");
        threadRP.start();

        this.panelManager = new PanelManager(stage);
        this.panelManager.init();
        this.panelManager.showPanel(new PanelLogin(stage));
    }
    public static void stopRP() {
        Main.logger.log("Arret - RP");
        threadRP.interrupt();
        library.Discord_Shutdown();
    }
    public static void updatePresence(String version, String state, String largeImageKey, String pseudo) {
        DiscordRichPresence presence = new DiscordRichPresence();
        presence.startTimestamp = System.currentTimeMillis() / 1000;
        if (version == null) {
            presence.details = SERVEUR_IP;
        } else {
            presence.details = SERVEUR_IP+" - " + version;
        }
        presence.state = state;
        presence.largeImageKey = largeImageKey;
        presence.largeImageText = "Serveur Minecraft 100% Survie [Crack OK]";
        presence.smallImageKey = "minecraft";
        if (pseudo != null) {
            presence.smallImageText = "Pseudo : "+pseudo;
        }
        library.Discord_UpdatePresence(presence);
    }
}

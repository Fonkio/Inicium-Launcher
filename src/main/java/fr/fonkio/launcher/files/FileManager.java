package fr.fonkio.launcher.files;

import fr.fonkio.launcher.MvWildLauncher;
import fr.fonkio.launcher.utils.OperatingSystem;
import fr.theshark34.openlauncherlib.util.Saver;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileManager {

    private static final String SERVER_NAME = MvWildLauncher.SERVEUR_NAME.toLowerCase();

    public static File createGameDir() {

        final String userHome = System.getProperty("user.home");

        final String fileSeparator = File.separator;
        File gameDir = switch (OperatingSystem.getCurrentPlatform()) {
            case WINDOWS -> new File(userHome + fileSeparator + "AppData" + fileSeparator + "Roaming" + fileSeparator + "." + SERVER_NAME);
            case MACOS -> new File(userHome + fileSeparator + "Library" + fileSeparator + "Application Support" + fileSeparator + SERVER_NAME);
            default -> new File(userHome + fileSeparator + "." + SERVER_NAME);
        };
        if (!gameDir.exists()) {
            boolean created = gameDir.mkdirs();
            if (!created) {
                MvWildLauncher.logger.info("Le dossier n'a pas pu être créé");
            }
        }

        return gameDir;
    }

    public static Path getLauncherLogPath() { return Paths.get(createGameDir()+"/launcher.log");  }

    public static Saver getSaver() {
        File fileProp = new File(createGameDir()+"/", "launcher.properties");
        if (!fileProp.exists()) {
            try {
                fileProp.createNewFile();
            } catch (IOException e) {
                return null;
            }
        }
        return new Saver(Paths.get(fileProp.getPath()));
    }

    public static File getGameFolder() {
        return createGameDir();
    }
    public static Path getGameFolderPath() {
        return Paths.get(createGameDir().getPath() +"/");
    }

    public static Path getModFolderPath() {
        return Paths.get(createGameDir().getPath() +"/mods");
    }


}

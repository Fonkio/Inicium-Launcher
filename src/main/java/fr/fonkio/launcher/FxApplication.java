package fr.fonkio.launcher;

import fr.flowarg.flowupdater.utils.builderapi.BuilderException;
import fr.fonkio.launcher.files.FileManager;
import javafx.application.Application;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.IOException;
import java.net.URISyntaxException;


public class FxApplication extends Application {
    private static final FileManager fileManager = new FileManager(MvWildLauncher.SERVEUR_NAME.toLowerCase());
    @Override
    public void start(Stage stage) {
        try {
            new MvWildLauncher().init(stage);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null,"Impossible de créer le fichier de config", "Erreur création", JOptionPane.ERROR_MESSAGE);
        } catch (BuilderException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
}

package fr.fonkio.launcher;

import fr.flowarg.flowupdater.utils.builderapi.BuilderException;
import javafx.application.Application;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.IOException;
import java.net.URISyntaxException;


public class FxApplication extends Application {
    @Override
    public void start(Stage stage) {
        try {
            new MvWildLauncher().init(stage);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null,"Impossible de créer le fichier de config", "Erreur création", JOptionPane.ERROR_MESSAGE);
        } catch (BuilderException | URISyntaxException e) {
            e.printStackTrace();
        }
    }
}

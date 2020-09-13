package fr.fonkio.launcher;

import fr.arinonia.arilibfx.utils.AriLogger;
import javafx.application.Application;

import javax.swing.*;

public class Main {
    public static AriLogger logger;

    public static void main(String[] args) {
        logger = new AriLogger("MvWild");
        try{
            Class.forName("javafx.application.Application");
            Application.launch(FxApplication.class, args);

        }catch (ClassNotFoundException e) {
            logger.warn("JavaFX introuvable");
            JOptionPane.showMessageDialog(null, "Erreur : \n"+e.getMessage()+ " not found","Erreur Java", JOptionPane.ERROR_MESSAGE);
        }
    }
}

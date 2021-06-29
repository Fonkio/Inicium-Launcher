package fr.fonkio.launcher;

import javafx.application.Application;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {

        try{
            Class.forName("javafx.application.Application");
            Application.launch(FxApplication.class, args);

        } catch (ClassNotFoundException e) {
            System.out.println("JavaFx introuvable");
            JOptionPane.showMessageDialog(null, "Erreur : \n"+e.getMessage()+ " not found","Erreur Java", JOptionPane.ERROR_MESSAGE);
        }
    }
}

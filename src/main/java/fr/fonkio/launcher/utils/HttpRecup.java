package fr.fonkio.launcher.utils;

import fr.fonkio.launcher.Main;
import fr.fonkio.launcher.MvWildLauncher;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;

public class HttpRecup {

    public static boolean offline = false;

    public static String getNbCo() {
        String nbCo = null;
        try{
            URLConnection connection = (new URL(MvWildLauncher.SITE_URL+"launcher/status.php").openConnection());
            connection.setRequestProperty("User-Agent", MvWildLauncher.CONFIG_WEB);
            connection.connect();
            InputStream is = connection.getInputStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            String line;
            while ((line = in.readLine()) != null) {
                if (line.contains("connect")) {
                    nbCo = line;
                }
            }
        } catch (Exception e) {
            if (!offline) {
                JOptionPane.showMessageDialog(null, "Erreur de récupération de la liste des joueurs.\nContactez-nous si le problème persiste", "Erreur url connection", JOptionPane.ERROR_MESSAGE);
                return "";
            }
        }
        return nbCo.split(" joueur")[0].replaceAll(" ", "");
    }

    public static String getList() {
        StringBuilder stringBuilder = new StringBuilder("");
        try{
            URLConnection connection = (new URL(MvWildLauncher.SITE_URL+"launcher/playerList.php").openConnection());
            connection.setRequestProperty("User-Agent", MvWildLauncher.CONFIG_WEB);
            connection.connect();
            InputStream is = connection.getInputStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            String line;
            while ((line = in.readLine()) != null) {
                if (!line.startsWith(" ")) {
                    stringBuilder.append(line+System.lineSeparator());
                }

            }
        } catch (Exception e) {
            if (!offline) {
                JOptionPane.showMessageDialog(null, "Erreur de récupération de la liste des joueurs.\nContactez-nous si le problème persiste", "Erreur url connection", JOptionPane.ERROR_MESSAGE);
            }
        }
        return stringBuilder.toString();
    }

    //Récupération du texte sur un URL pour les versions
    public static String getVersion(String url) {
        String inputline = null;
        try{
            URLConnection connection = (new URL(url).openConnection());
            connection.setRequestProperty("User-Agent", MvWildLauncher.CONFIG_WEB);
            connection.connect();
            InputStream is = connection.getInputStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            inputline = in.readLine();
        } catch (Exception e) {
            if (!offline) {
                JOptionPane.showMessageDialog(null, "Erreur de récupération de la dernière version minecraft.\nContactez-nous si le problème persiste", "Erreur url connection", JOptionPane.ERROR_MESSAGE);
            }
            return null;
        }
        if (inputline == null) {
            return null;
        }
        Main.logger.log("Version recupérée : "+inputline);
        return inputline;
    }
}

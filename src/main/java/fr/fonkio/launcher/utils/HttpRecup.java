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
import java.util.*;

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
                offline = true;
                JOptionPane.showMessageDialog(null, "Erreur de récupération de la liste des joueurs.\nPassage en mode hors-ligne", "Erreur url connection", JOptionPane.ERROR_MESSAGE);
                return "";
            }
        }

        if (nbCo != null) {
            return offline?"":nbCo.split(" joueur")[0].replaceAll(" ", "");
        } else {
            return "";
        }
    }


    public static Map<String, List<String>> getList() {
        StringBuilder stringBuilder = new StringBuilder();
        try{
            URLConnection connection = (new URL(MvWildLauncher.SITE_URL+"launcher/jsonPlayerList.php").openConnection());
            connection.setRequestProperty("User-Agent", MvWildLauncher.CONFIG_WEB);
            connection.connect();
            InputStream is = connection.getInputStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            String line;
            while ((line = in.readLine()) != null) {
                    stringBuilder.append(line);
            }
        } catch (Exception e) {
            if (!offline) {
                offline = true;
                JOptionPane.showMessageDialog(null, "Erreur de récupération de la liste des joueurs.\nPassage en mode hors-ligne", "Erreur url connection", JOptionPane.ERROR_MESSAGE);
            }
        }
        String res = stringBuilder.toString();
        String[] tabResServPlayerList = res.split("/");
        Map<String, List<String>> map = new HashMap<>();
        if (tabResServPlayerList.length >= 2) {
            for(int i = 1; i < tabResServPlayerList.length; i++) {
                if (i%2==0) { //Liste joueurs
                    map.put(tabResServPlayerList[i-1], Arrays.asList(tabResServPlayerList[i].split(",")));
                }
            }
        }
        return map;
    }

    //Récupération du texte sur un URL pour les versions
    public static String getVersion(String url) {
        String inputline;
        try{
            URLConnection connection = (new URL(url).openConnection());
            connection.setRequestProperty("User-Agent", MvWildLauncher.CONFIG_WEB);
            connection.connect();
            InputStream is = connection.getInputStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            inputline = in.readLine();
        } catch (Exception e) {
            if (!offline) {
                offline = true;
                JOptionPane.showMessageDialog(null, "Erreur de récupération de la dernière version minecraft.\nPassage en mode hors-ligne", "Erreur url connection", JOptionPane.ERROR_MESSAGE);
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

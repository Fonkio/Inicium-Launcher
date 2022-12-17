package fr.fonkio.launcher.utils;

import com.google.gson.Gson;
import fr.fonkio.launcher.MvWildLauncher;
import fr.fonkio.launcher.launcher.Launcher;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class HttpRecup {

    private class Status {
        private boolean online;
        private String ip;
        private int port;
        private Debug debug;
        private Motd motd;
        private Players players;
        private String version;
        private int protocol;
        private String hostname;
        private String icon;
        private String software;
        private String map;

        private class Debug {
            private boolean ping;
            private boolean query;
            private boolean srv;
            private boolean querymismatch;
            private boolean ipinsrv;
            private boolean cnameinsrv;
            private boolean animatedmotd;
            private long cachetime;
            private int apiversion;
        }

        private class Motd {
            private List<String> raw;
            private List<String> clean;
            private List<String> html;
        }

        private class Players {
            private int online;
            private int max;
            private List<String> list;
            private Map<String, String> uuid;
        }
    }

    private static Status serverStatus = null;

    private static Status getInstance() {
        return getInstance(false);
    }

    private static Status getInstance(boolean refresh) {
        if (serverStatus == null || refresh) {
            try {
                HttpRequest getRequest = HttpRequest.newBuilder()
                        .uri(new URI("https://api.mcsrvstat.us/2/"+MvWildLauncher.SERVEUR_IP))
                        .GET().build();
                HttpClient httpClient = HttpClient.newHttpClient();
                HttpResponse<String> getResponse = httpClient.send(getRequest, HttpResponse.BodyHandlers.ofString());
                Gson gson = new Gson();
                serverStatus = gson.fromJson(getResponse.body(), Status.class);
            } catch (URISyntaxException | IOException | InterruptedException e) {
                return null;
            }
        }
        return serverStatus;
    }

    public static String getNbCo() {
        return getNbCo(false);
    }
    public static String getNbCo(boolean refresh) {
        Status status = getInstance(refresh);
        if (status != null) {
            return status.players.online + "/" + getInstance().players.max;
        } else {
            return "?";
        }

    }


    public static List<String> getList(boolean refresh) {
        Status status = getInstance(refresh);
        if (status != null) {
            return status.players.list;
        } else {
            return new ArrayList<>();
        }
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
            if (!Launcher.offline) {
                Launcher.offline = true;
                JOptionPane.showMessageDialog(null, "Erreur de récupération de la dernière version minecraft.\nPassage en mode hors-ligne", "Erreur url connection", JOptionPane.ERROR_MESSAGE);
            }
            return null;
        }
        if (inputline == null) {
            return null;
        }
        MvWildLauncher.logger.info("Version recupérée : "+inputline);
        return inputline;
    }
}

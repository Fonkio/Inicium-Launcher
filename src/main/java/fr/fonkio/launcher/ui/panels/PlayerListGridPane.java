package fr.fonkio.launcher.ui.panels;

import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import fr.fonkio.launcher.utils.HttpRecup;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.util.List;
import java.util.Map;

public class PlayerListGridPane {

    private final PanelMain panelMain;
    private GridPane gpConnect = new GridPane();
    private Label playerListTitle;
    private FlowPane listeConnecte;

    public PlayerListGridPane(PanelMain panelMain) {
        this.panelMain = panelMain;
    }

    //Affichage onglet paramètres
    public void addTopPanel(GridPane topPanel) {
        this.playerListTitle = new Label("Liste des joueurs");
        GridPane.setVgrow(playerListTitle, Priority.ALWAYS);
        GridPane.setHgrow(playerListTitle, Priority.ALWAYS);
        GridPane.setValignment(playerListTitle, VPos.TOP);
        playerListTitle.setStyle("-fx-font-size: 26px; -fx-text-fill: white; -fx-font-weight: bold");
        playerListTitle.setTranslateY(20);
        MaterialDesignIconView logoRefresh = new MaterialDesignIconView(MaterialDesignIcon.REFRESH);
        GridPane.setVgrow(logoRefresh, Priority.ALWAYS);
        GridPane.setHgrow(logoRefresh, Priority.ALWAYS);
        GridPane.setValignment(logoRefresh, VPos.TOP);
        logoRefresh.setTranslateX(220);
        logoRefresh.setTranslateY(20);
        logoRefresh.setFill(Color.rgb(255, 255, 255));
        logoRefresh.setSize("30px");
        logoRefresh.setStyle("-fx-background-color: white");
        logoRefresh.setOnMouseEntered(e->this.panelMain.getLayout().setCursor(Cursor.HAND));
        logoRefresh.setOnMouseExited(e->this.panelMain.getLayout().setCursor(Cursor.DEFAULT));
        logoRefresh.setOnMouseClicked(e->this.panelMain.refreshList());
        createGridConnect();
        topPanel.getChildren().addAll(playerListTitle, logoRefresh, this.listeConnecte);

    }


    private void createGridConnect() {
        //ListeConnectés
        this.listeConnecte = new FlowPane();
        this.listeConnecte.setMaxHeight(20000);
        listeConnecte.setVgap(500);
        listeConnecte.setHgap(100);

        GridPane.setVgrow(listeConnecte, Priority.ALWAYS);
        GridPane.setHgrow(listeConnecte, Priority.ALWAYS);
        GridPane.setValignment(listeConnecte, VPos.TOP);
        GridPane.setHalignment(listeConnecte, HPos.LEFT);
        listeConnecte.setVgap(50);
        listeConnecte.setTranslateY(80);
        Map<String, List<String>> map = HttpRecup.getList();
        int indiceServer = 0;

        for(String serverName : map.keySet()) {
            GridPane gpServer = new GridPane();
            GridPane.setVgrow(gpServer, Priority.ALWAYS);
            GridPane.setHgrow(gpServer, Priority.ALWAYS);
            GridPane.setValignment(gpServer, VPos.TOP);
            GridPane.setHalignment(gpServer, HPos.LEFT);
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < map.get(serverName).size(); i++) {
                sb.append(map.get(serverName).get(i)+"\n");
            }
            Label titre = new Label (serverName+" ["+map.get(serverName).size()+"]");
            Label liste = new Label(sb.toString());
            liste.setTranslateY(40);
            liste.setStyle("-fx-font-size: 14px; -fx-text-fill: white;");
            titre.setTranslateY(0);
            titre.setStyle("-fx-font-size: 20px; -fx-text-fill: white;  -fx-font-weight: bold");
            GridPane.setVgrow(liste, Priority.ALWAYS);
            GridPane.setHgrow(liste, Priority.ALWAYS);
            GridPane.setValignment(liste, VPos.TOP);
            GridPane.setHalignment(liste, HPos.LEFT);
            GridPane.setVgrow(titre, Priority.ALWAYS);
            GridPane.setHgrow(titre, Priority.ALWAYS);
            GridPane.setValignment(titre, VPos.TOP);
            GridPane.setHalignment(titre, HPos.LEFT);
            gpServer.getChildren().addAll(titre, liste);
            listeConnecte.getChildren().add(gpServer);
            indiceServer ++;
        }
    }

}

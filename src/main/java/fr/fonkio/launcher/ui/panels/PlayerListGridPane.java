package fr.fonkio.launcher.ui.panels;

import fr.fonkio.launcher.Main;
import fr.fonkio.launcher.utils.HttpRecup;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

import java.util.List;

public class PlayerListGridPane {

    private final PanelMain panelMain;
    private final ListView<Player> listeConnecte = new ListView<>();

    private Button buttonRefresh;


    public PlayerListGridPane(PanelMain panelMain) {
        this.panelMain = panelMain;
    }

    //Affichage onglet liste des joueurs
    public void addTopPanel(GridPane topPanelListPlayer) {
        Label playerListTitle = new Label("Liste des joueurs");
        GridPane.setVgrow(playerListTitle, Priority.ALWAYS);
        GridPane.setHgrow(playerListTitle, Priority.ALWAYS);
        GridPane.setValignment(playerListTitle, VPos.TOP);
        playerListTitle.setStyle("-fx-font-size: 26px; -fx-text-fill: white; -fx-font-weight: bold");
        playerListTitle.setTranslateY(20);

        Image refreshImage = new Image(Main.class.getResource("/reload.png").toExternalForm());
        ImageView imageRefresh = new ImageView(refreshImage);
        imageRefresh.setFitHeight(30);
        imageRefresh.setFitWidth(30);
        buttonRefresh = new Button();
        buttonRefresh.setBackground(Background.EMPTY);
        buttonRefresh.setGraphic(imageRefresh);
        GridPane.setVgrow(buttonRefresh, Priority.ALWAYS);
        GridPane.setHgrow(buttonRefresh, Priority.ALWAYS);
        GridPane.setValignment(buttonRefresh, VPos.TOP);
        buttonRefresh.setTranslateX(220);
        buttonRefresh.setTranslateY(20);
        buttonRefresh.setOnMouseEntered(e->this.panelMain.getLayout().setCursor(Cursor.HAND));
        buttonRefresh.setOnMouseExited(e->this.panelMain.getLayout().setCursor(Cursor.DEFAULT));
        buttonRefresh.setOnMouseClicked(e-> {
            Thread t = new Thread(this.panelMain::refreshList);
            t.start();
        });
        Thread t = new Thread(this::createGridConnect);
        t.start();
        topPanelListPlayer.getChildren().addAll(playerListTitle, buttonRefresh, this.listeConnecte);

    }

    public void refreshList() {
        List<String> playersNames = HttpRecup.getList(true);
        ObservableList<Player> playerList = FXCollections.observableArrayList();

        namesToPlayerList(playersNames, playerList);
        Platform.runLater(() -> listeConnecte.setItems(playerList));
    }

    private void namesToPlayerList(List<String> playersNames, ObservableList<Player> playerList) {
        if (playersNames == null) {
            return;
        }
        for (String str : playersNames) {
            playerList.add(new Player(str,new ImageView("https://mc-heads.net/avatar/"+str+"/50")));
        }
    }


    private void createGridConnect() {
        //ListeConnectés
        this.listeConnecte.setMaxHeight(550);
        this.listeConnecte.setMinHeight(550);


        GridPane.setVgrow(listeConnecte, Priority.ALWAYS);
        GridPane.setHgrow(listeConnecte, Priority.ALWAYS);
        GridPane.setValignment(listeConnecte, VPos.TOP);
        GridPane.setHalignment(listeConnecte, HPos.LEFT);
        listeConnecte.setTranslateY(80);
        listeConnecte.setStyle("-fx-background-color: transparent;");
        List<String> playersNames = HttpRecup.getList(false);

        ObservableList<Player> playerList = FXCollections.observableArrayList();

        namesToPlayerList(playersNames, playerList);

        listeConnecte.setCellFactory(listView -> new ListCell<>() {
            @Override
            protected void updateItem(Player player, boolean empty) {
                super.updateItem(player, empty);
                this.setStyle("-fx-background-color: transparent;");
                if (empty) {
                    setGraphic(null);
                } else {

                    // Create a HBox to hold our displayed value
                    HBox hBox = new HBox(15);
                    hBox.setAlignment(Pos.CENTER_LEFT);

                    Label label = new Label(player.getName());
                    label.setStyle("-fx-font-size: 26px; -fx-text-fill: white;");
                    // Add the values from our piece to the HBox
                    hBox.getChildren().addAll(
                            player.getHead(),
                            label
                    );

                    // Set the HBox as the display
                    setGraphic(hBox);
                }
            }
        });

        listeConnecte.setItems(playerList);
    }

    public void setRefreshButtonVisible(boolean visible) {
        buttonRefresh.setVisible(visible);
    }

    private class Player {
        private String name;
        private ImageView head;

        public Player(String name, ImageView head) {
            this.name = name;
            this.head = head;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public ImageView getHead() {
            return head;
        }

        public void setHead(ImageView head) {
            this.head = head;
        }
    }
}

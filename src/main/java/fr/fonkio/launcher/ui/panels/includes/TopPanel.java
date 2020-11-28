package fr.fonkio.launcher.ui.panels.includes;

import fr.fonkio.launcher.Main;
import fr.fonkio.launcher.MvWildLauncher;
import fr.fonkio.launcher.ui.PanelManager;
import fr.fonkio.launcher.ui.panel.Panel;
import javafx.geometry.HPos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class TopPanel extends Panel {

    public TopPanel(Stage stage) {
        super(stage);
    }

    @Override
    public void init(PanelManager panelManager) {
        super.init(panelManager);
        this.layout.setStyle("-fx-background-color: rgb(31,35,37);");
        GridPane topBarButton = new GridPane();
        this.layout.getChildren().add(topBarButton);
        Label title = new Label(MvWildLauncher.SERVEUR_NAME+" Launcher");
        this.layout.getChildren().add(title);
        title.setFont(Font.font("Helvetica", FontWeight.THIN, FontPosture.REGULAR, 15.0f));
        title.setStyle("-fx-text-fill: white;");
        GridPane.setHalignment(title, HPos.CENTER);
        topBarButton.setMinWidth(100.0d);
        topBarButton.setMaxWidth(100.0d);
        GridPane.setHgrow(topBarButton, Priority.ALWAYS);
        GridPane.setVgrow(topBarButton, Priority.ALWAYS);
        GridPane.setHalignment(topBarButton, HPos.RIGHT);

        Image closeImage = new Image(Main.class.getResource("/close.png").toExternalForm());
        ImageView imageClose = new ImageView(closeImage);
        imageClose.setFitHeight(18);
        imageClose.setFitWidth(18);
        Button buttonClose = new Button();
        buttonClose.setBackground(Background.EMPTY);
        buttonClose.setGraphic(imageClose);
        GridPane.setVgrow(buttonClose, Priority.ALWAYS);
        buttonClose.setOpacity(0.70f);
        buttonClose.setOnMouseEntered(e-> buttonClose.setOpacity(1.0f));
        buttonClose.setOnMouseExited(e-> buttonClose.setOpacity(0.70f));
        buttonClose.setOnMouseClicked(e -> {
            MvWildLauncher.stopRP();
            System.exit(0);

        });
        buttonClose.setTranslateX(70.0d);

        Image hideImage = new Image(Main.class.getResource("/hide.png").toExternalForm());
        ImageView imageHide = new ImageView(hideImage);
        imageHide.setFitHeight(18);
        imageHide.setFitWidth(18);
        Button buttonHide = new Button();
        buttonHide.setBackground(Background.EMPTY);
        buttonHide.setGraphic(imageHide);
        GridPane.setVgrow(buttonHide, Priority.ALWAYS);
        buttonHide.setOpacity(0.70f);
        buttonHide.setOnMouseEntered(e-> buttonHide.setOpacity(1.0f));
        buttonHide.setOnMouseExited(e-> buttonHide.setOpacity(0.70f));
        buttonHide.setOnMouseClicked(e -> this.panelManager.getStage().setIconified(true));
        buttonHide.setTranslateX(26.0d);
        topBarButton.getChildren().addAll(buttonClose, buttonHide);
    }
}

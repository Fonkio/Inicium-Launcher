package fr.fonkio.launcher.ui.panels.includes;

import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import fr.fonkio.launcher.MvWildLauncher;
import fr.fonkio.launcher.ui.PanelManager;
import fr.fonkio.launcher.ui.panel.Panel;
import javafx.geometry.HPos;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class TopPanel extends Panel {

    private GridPane topBar;

    public TopPanel(Stage stage) {
        super(stage);
    }

    @Override
    public void init(PanelManager panelManager) {
        super.init(panelManager);
        this.topBar = this.layout;
        this.layout.setStyle("-fx-background-color: rgb(31,35,37);");
        GridPane topBarButton = new GridPane();
        this.layout.getChildren().add(topBarButton);
        Label title = new Label("MvWild Launcher");
        this.layout.getChildren().add(title);
        title.setFont(Font.font("Consolas", FontWeight.THIN, FontPosture.REGULAR, 22.0f));
        title.setStyle("-fx-text-fill: white;");
        GridPane.setHalignment(title, HPos.CENTER);
        topBarButton.setMinWidth(100.0d);
        topBarButton.setMaxWidth(100.0d);
        GridPane.setHgrow(topBarButton, Priority.ALWAYS);
        GridPane.setVgrow(topBarButton, Priority.ALWAYS);
        GridPane.setHalignment(topBarButton, HPos.RIGHT);
        MaterialDesignIconView close = new MaterialDesignIconView(MaterialDesignIcon.WINDOW_CLOSE);
        MaterialDesignIconView fullScreen = new MaterialDesignIconView(MaterialDesignIcon.WINDOW_MAXIMIZE);
        MaterialDesignIconView hide = new MaterialDesignIconView(MaterialDesignIcon.WINDOW_MINIMIZE);
        GridPane.setVgrow(close, Priority.ALWAYS);
        GridPane.setVgrow(fullScreen, Priority.ALWAYS);
        GridPane.setVgrow(hide, Priority.ALWAYS);

        close.setFill(Color.WHITE);
        close.setOpacity(0.70f);
        close.setSize("18.0px");
        close.setOnMouseEntered(e-> close.setOpacity(1.0f));
        close.setOnMouseExited(e-> close.setOpacity(0.70f));
        close.setOnMouseClicked(e -> {
            MvWildLauncher.stopRP();
            System.exit(0);

        });
        close.setTranslateX(70.0d);



        hide.setFill(Color.WHITE);
        hide.setOpacity(0.70f);
        hide.setSize("18.0px");
        hide.setOnMouseEntered(e-> hide.setOpacity(1.0f));
        hide.setOnMouseExited(e-> hide.setOpacity(0.70f));
        hide.setOnMouseClicked(e -> this.panelManager.getStage().setIconified(true));
        hide.setTranslateX(26.0d);
        topBarButton.getChildren().addAll(close, hide);
    }
}

package fr.fonkio.launcher.ui;

import fr.arinonia.arilibfx.AriLibFX;
import fr.arinonia.arilibfx.ui.utils.ResizeHelper;
import fr.fonkio.launcher.Main;
import fr.fonkio.launcher.MvWildLauncher;
import fr.fonkio.launcher.ui.panel.IPanel;
import fr.fonkio.launcher.ui.panels.includes.TopPanel;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class PanelManager {

    private final MvWildLauncher mvWildLauncher;
    private final Stage stage;
    private GridPane layout;
    private TopPanel topPanel;
    private GridPane centerPanel = new GridPane();

    public PanelManager(MvWildLauncher mvWildLauncher, Stage stage) {
        topPanel = new TopPanel(stage);
        this.mvWildLauncher = mvWildLauncher;
        this.stage = stage;
    }

    public void init() {
        this.stage.setTitle("MvWild Launcher");
        this.stage.setMinWidth(1280);
        this.stage.setWidth(1280);
        this.stage.setMinHeight(720);
        this.stage.setHeight(720);
        this.stage.initStyle(StageStyle.UNDECORATED);
        this.stage.centerOnScreen();
        this.stage.getIcons().add(new Image(Main.class.getResource("/logoNBG.png").toExternalForm()));
        this.stage.show();

        this.layout = new GridPane();
        this.layout.setStyle(AriLibFX.setResponsiveBackground(Main.class.getResource("/fondLauncher.png").toExternalForm()));
        this.stage.setScene(new Scene(this.layout));
        this.stage.setResizable(false);

        RowConstraints topPanelConstraints = new RowConstraints();
        topPanelConstraints.setValignment(VPos.TOP);
        topPanelConstraints.setMinHeight(25);
        topPanelConstraints.setMaxHeight(25);
        this.layout.getRowConstraints().addAll(topPanelConstraints, new RowConstraints());
        this.layout.add(this.topPanel.getLayout(), 0, 0);
        this.topPanel.init(this);
        this.layout.add(this.centerPanel, 0, 1);
        GridPane.setVgrow(this.centerPanel, Priority.ALWAYS);
        GridPane.setHgrow(this.centerPanel, Priority.ALWAYS);
        ResizeHelper.addResizeListener(this.stage);
        //this.stage.sizeToScene();
    }


    public void showPanel(IPanel panel) {
        this.centerPanel.getChildren().clear();
        this.centerPanel.getChildren().add(panel.getLayout());
        panel.init(this);
        panel.onShow();
    }
    public Stage getStage() {
        return stage;
    }

    public MvWildLauncher getMvWildLauncher() {
        return mvWildLauncher;
    }

    public TopPanel getTopPanel() {
        return topPanel;
    }
}

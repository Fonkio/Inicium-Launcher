package fr.fonkio.launcher.ui.panel;

import fr.fonkio.launcher.ui.PanelManager;
import javafx.animation.FadeTransition;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Panel implements IPanel {

    protected GridPane layout = new GridPane();
    protected PanelManager panelManager;
    private final Stage stage;

    public Panel(Stage stage) {
        this.stage = stage;
    }

    @Override
    public void init(PanelManager panelManager) {
        this.panelManager = panelManager;
        GridPane.setHgrow(layout, Priority.ALWAYS);
        GridPane.setVgrow(layout, Priority.ALWAYS);
    }

    @Override
    public GridPane getLayout() {
        return this.layout;
    }

    @Override
    public void onShow() {
        FadeTransition transition = new FadeTransition(Duration.millis(300), this.layout);
        transition.setFromValue(0);
        transition.setToValue(1);
        transition.setAutoReverse(true);
        transition.play();
    }
}

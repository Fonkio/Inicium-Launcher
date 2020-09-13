package fr.fonkio.launcher.ui.panel;

import fr.fonkio.launcher.ui.PanelManager;
import javafx.scene.layout.GridPane;

public interface IPanel {
    void init (PanelManager panelManager);
    GridPane getLayout();
    void onShow();
}

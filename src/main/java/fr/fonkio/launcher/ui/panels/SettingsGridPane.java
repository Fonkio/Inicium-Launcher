package fr.fonkio.launcher.ui.panels;

import fr.fonkio.launcher.MvWildLauncher;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

public class SettingsGridPane {


    private final PanelMain panelMain;

    public SettingsGridPane(PanelMain panelMain) {
        this.panelMain = panelMain;
    }

    //Affichage onglet paramètres
    public void addTopPanel(GridPane topPanelSettings) {
        Label settingsTitle = new Label("Paramètres");
        GridPane.setVgrow(settingsTitle, Priority.ALWAYS);
        GridPane.setHgrow(settingsTitle, Priority.ALWAYS);
        GridPane.setValignment(settingsTitle, VPos.TOP);
        settingsTitle.setStyle("-fx-font-size: 26px; -fx-text-fill: white; -fx-font-weight: bold");
        settingsTitle.setTranslateY(20);

        Label ramTitle = new Label("RAM attribuée à votre minecraft (en GB)");
        GridPane.setVgrow(ramTitle, Priority.ALWAYS);
        GridPane.setHgrow(ramTitle, Priority.ALWAYS);
        GridPane.setValignment(ramTitle, VPos.TOP);
        ramTitle.setStyle("-fx-font-size: 20px; -fx-text-fill: white;  -fx-font-weight: bold");
        ramTitle.setTranslateY(80);

        Label ram0 = new Label("0 = par défaut");
        GridPane.setVgrow(ram0, Priority.ALWAYS);
        GridPane.setHgrow(ram0, Priority.ALWAYS);
        GridPane.setValignment(ram0, VPos.TOP);
        ram0.setStyle("-fx-font-size: 14px; -fx-text-fill: white;");
        ram0.setTranslateY(110);

        String ramStr = panelMain.getRAM();
        int ram = 0;
        if (ramStr != null) {
            ram = Integer.parseInt(ramStr);
        }
        Slider slider = new Slider(0, 16, ram/1024.0);
        GridPane.setVgrow(slider, Priority.ALWAYS);
        GridPane.setHgrow(slider, Priority.ALWAYS);
        GridPane.setValignment(slider, VPos.TOP);
        slider.setTranslateY(130);
        slider.setShowTickMarks(true);
        slider.setShowTickLabels(true);
        slider.setMajorTickUnit(1);
        slider.setMinorTickCount(1);
        slider.setSnapToTicks(true);

        CheckBox checkBox = new CheckBox();
        if(this.panelMain.getDRP() != null) {
            checkBox.setSelected(this.panelMain.getDRP());
        }
        checkBox.setTranslateY(25);
        Label checkText = new Label("Désactiver le Discord Rich Presence");
        checkText.setTranslateY(25);
        checkText.setTranslateX(25);
        checkText.setOnMouseEntered(e-> this.panelMain.getLayout().setCursor(Cursor.HAND));
        checkText.setOnMouseExited(e-> this.panelMain.getLayout().setCursor(Cursor.DEFAULT));
        checkBox.setOnMouseEntered(e-> this.panelMain.getLayout().setCursor(Cursor.HAND));
        checkBox.setOnMouseExited(e-> this.panelMain.getLayout().setCursor(Cursor.DEFAULT));

        checkText.setStyle("-fx-font-size: 14px; -fx-text-fill: white;");

        Button save = new Button("Sauvegarder");
        GridPane.setVgrow(save, Priority.ALWAYS);
        GridPane.setHgrow(save, Priority.ALWAYS);
        GridPane.setValignment(save, VPos.BOTTOM);
        GridPane.setHalignment(save, HPos.RIGHT);
        save.setMinWidth(140);
        save.setMaxHeight(40);
        save.setStyle("-fx-background-color: #2a4c13; -fx-background-insets: 0; -fx-font-size: 14px; -fx-text-fill: white;");
        save.setOnMouseEntered(e->this.panelMain.getLayout().setCursor(Cursor.HAND));
        save.setOnMouseExited(e->this.panelMain.getLayout().setCursor(Cursor.DEFAULT));
        save.setOnMouseClicked(e-> {
            double ramD = slider.getValue()*1024;
            this.panelMain.setRAM(ramD);
            this.panelMain.setDRP(checkBox.isSelected());
            save.setText("Sauvegardé !");
            save.setStyle("-fx-background-color: #52872F; -fx-background-insets: 0; -fx-font-size: 14px; -fx-text-fill: white;");
        });
        slider.setOnDragDetected(e-> {
            save.setText("Sauvegarder");
            save.setStyle("-fx-background-color: #2a4c13; -fx-background-insets: 0; -fx-font-size: 14px; -fx-text-fill: white;");
        });
        checkText.setOnMouseClicked(e->{
            checkBox.setSelected(!checkBox.isSelected());
            save.setText("Sauvegarder");
            save.setStyle("-fx-background-color: #2a4c13; -fx-background-insets: 0; -fx-font-size: 14px; -fx-text-fill: white;");
        });
        checkBox.setOnMouseClicked(e -> {
            save.setText("Sauvegarder");
            save.setStyle("-fx-background-color: #2a4c13; -fx-background-insets: 0; -fx-font-size: 14px; -fx-text-fill: white;");
        });

        Label credit = new Label("MvWildLauncher par Fonkio (v"+ MvWildLauncher.LAUNCHER_VERSION+")");
        GridPane.setVgrow(credit, Priority.ALWAYS);
        GridPane.setHgrow(credit, Priority.ALWAYS);
        GridPane.setValignment(credit, VPos.TOP);
        credit.setTranslateX(675);
        credit.setTranslateY(25);
        credit.setStyle("-fx-font-size: 14px; -fx-text-fill: white;");

        topPanelSettings.getChildren().addAll(settingsTitle, ramTitle, ram0, slider, save, checkBox, checkText, credit);
    }
}

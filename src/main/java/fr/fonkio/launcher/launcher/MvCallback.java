package fr.fonkio.launcher.launcher;

import fr.flowarg.flowlogger.ILogger;
import fr.flowarg.flowupdater.download.IProgressCallback;
import fr.flowarg.flowupdater.download.Step;
import fr.fonkio.launcher.Main;
import fr.fonkio.launcher.ui.PanelManager;
import javafx.application.Platform;

public class MvCallback implements IProgressCallback {
    private String status = "";
    private final PanelManager panelManager;

    public MvCallback(PanelManager panelManager) {
        this.panelManager = panelManager;
    }

    @Override
    public void init(ILogger logger) {
        Main.logger.log("Création callback "+this.getClass().getName());
    }

    @Override
    public void step(Step step) {
        switch (step.toString()) {
            case "PREREQUISITES":
                this.status = "Chargement des prérequis... ";
                break;
            case "READ":
                this.status = "Verification des fichiers ";
                break;
            case "DL_LIBS":
                this.status = "Téléchargement des librairies ";
                break;
            case "DL_ASSETS":
                this.status = "Téléchargement des assets ";
                break;
            case "EXTRACT_NATIVES":
                this.status = "Extraction en cours, veuillez patienter ";
                break;
            case "MODS":
                this.status = "Récupération des mods ";
                break;
            case "FORGE":
                this.status = "Install. de forge (Cette étape peu prendre du temps lors d'une MAJ) ";
                break;
            case "INTERNAL_FORGE_HACKS":
                this.status = "Forge installé, lancement... ";
                break;
            case "END":
                this.status = "Terminé ";
                break;
            case "EXTERNAL_FILES":
                this.status = "Téléchargement de la configuration... ";
                break;
            default:
                this.status = "Chargement... ";
                Main.logger.warn(step.toString());
                break;
        }
        Platform.runLater(()-> this.panelManager.setStatus(this.status+"..."));
    }
    @Override
    public void update(int downloaded, int max) {
        Platform.runLater(()-> {
            this.panelManager.setStatus(this.status + downloaded+"/"+max +"...");
            if (max != 0) {
                this.panelManager.setProgress(downloaded, max);
            }
        }
        );
    }
}

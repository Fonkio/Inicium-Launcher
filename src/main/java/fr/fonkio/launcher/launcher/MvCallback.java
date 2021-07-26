package fr.fonkio.launcher.launcher;

import fr.flowarg.flowlogger.ILogger;
import fr.flowarg.flowupdater.download.IProgressCallback;
import fr.flowarg.flowupdater.download.Step;
import fr.fonkio.launcher.Main;
import fr.fonkio.launcher.MvWildLauncher;
import fr.fonkio.launcher.ui.PanelManager;
import javafx.application.Platform;

public class MvCallback implements IProgressCallback {
    private String status = "";
    private final PanelManager panelManager;
    private boolean forge;

    public MvCallback(PanelManager panelManager) {
        this.panelManager = panelManager;
    }

    @Override
    public void init(ILogger logger) {
        MvWildLauncher.logger.info("Création callback "+this.getClass().getName());
    }

    @Override
    public void step(Step step) {
        this.forge = false;
        switch (step.toString()) {
            case "PREREQUISITES":
                this.status = "Chargement des prérequis...";
                break;
            case "READ":
                this.status = "Verification des fichiers...";
                break;
            case "DL_LIBS":
                this.status = "Téléchargement des librairies...";
                break;
            case "DL_ASSETS":
                this.status = "Téléchargement des assets...";
                break;
            case "EXTRACT_NATIVES":
                this.status = "Extraction en cours, veuillez patienter...";
                break;
            case "MODS":
                this.status = "Récupération des mods...";
                break;
            case "FORGE":
                this.status = "Install. de forge (Cette étape peu prendre du temps lors d'une MAJ)";
                this.forge = true;
                break;
            case "FABRIC":
                this.status = "Install. de fabric ...";
                this.forge = true;
                break;
            case "INTERNAL_FORGE_HACKS":
                this.status = "Forge installé, lancement... ";
                break;
            case "END":
                this.status = "Terminé !";
                break;
            case "EXTERNAL_FILES":
                this.status = "Téléchargement de la configuration...";
                break;
            default:
                this.status = "Chargement...";
                MvWildLauncher.logger.err("Nouvelle étape de téléchargement non renseigné : " + step);
                break;
        }
        Platform.runLater(()-> this.panelManager.setStatus(this.status));
    }
    @Override
    public void update(long downloaded, long max) {
        Platform.runLater(()-> {
            if (max != 0) {
                this.panelManager.setProgress(downloaded, max);
            }
        }
        );
    }
}

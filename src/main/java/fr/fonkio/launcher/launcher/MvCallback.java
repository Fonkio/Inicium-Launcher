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

    public MvCallback(PanelManager panelManager) {
        this.panelManager = panelManager;
    }

    @Override
    public void init(ILogger logger) {
        MvWildLauncher.logger.info("Création callback "+this.getClass().getName());
    }

    @Override
    public void step(Step step) {

        switch (step.toString()) {
            case "PREREQUISITES" -> this.status = "Chargement des prérequis...";
            case "READ" -> this.status = "Verification des fichiers...";
            case "DL_LIBS" -> this.status = "Téléchargement des librairies...";
            case "DL_ASSETS" -> this.status = "Téléchargement des assets...";
            case "EXTRACT_NATIVES" -> this.status = "Extraction en cours, veuillez patienter...";
            case "MODS" -> this.status = "Récupération des mods...";
            case "FORGE" -> this.status = "Install. de forge (Cette étape peu prendre du temps lors d'une MAJ)";
            case "FABRIC" -> this.status = "Install. de fabric ...";
            case "INTERNAL_FORGE_HACKS" -> this.status = "Forge installé, lancement... ";
            case "END" -> this.status = "Terminé !";
            case "EXTERNAL_FILES" -> this.status = "Téléchargement de la configuration...";
            default -> {
                this.status = "Chargement...";
                MvWildLauncher.logger.err("Nouvelle étape de téléchargement non renseigné : " + step);
            }
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

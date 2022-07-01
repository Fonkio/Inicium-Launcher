package fr.fonkio.launcher.launcher;

import fr.flowarg.flowlogger.ILogger;
import fr.flowarg.flowupdater.download.DownloadList;
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
            case "MOD_LOADER" -> this.status = "Install. de fabric ...";
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
    public void update(DownloadList.DownloadInfo dlInfo) {
        Platform.runLater(()-> {
            if (dlInfo.getTotalToDownloadBytes() != 0) {
                this.panelManager.setProgress(dlInfo.getDownloadedBytes(), dlInfo.getTotalToDownloadBytes());
            }
        }
        );
    }
}

package fr.fonkio.launcher.launcher;

import fr.flowarg.flowupdater.FlowUpdater;
import fr.flowarg.flowupdater.download.IProgressCallback;
import fr.flowarg.flowupdater.download.json.ExternalFile;
import fr.flowarg.flowupdater.download.json.Mod;
import fr.flowarg.flowupdater.utils.builderapi.BuilderException;
import fr.flowarg.flowupdater.versions.FabricVersion;
import fr.flowarg.flowupdater.versions.VanillaVersion;
import fr.fonkio.launcher.MvWildLauncher;
import fr.fonkio.launcher.files.FileManager;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.List;

public class Updater {

    private static final String PATH_TO_EXT_FILE = "launcher/externalfiles.php";
    private static final String PATH_TO_MODS = "launcher/mods.php";
    private final FlowUpdater updater;

    public Updater(final String strVersionMc, String strVersionFabric, IProgressCallback mvCallback) throws BuilderException, URISyntaxException, MalformedURLException {

        final VanillaVersion version = new VanillaVersion.VanillaVersionBuilder()
                .withName(strVersionMc)
                .build();
        final FabricVersion fabricVersion = new FabricVersion.FabricVersionBuilder()
                .withFabricVersion(strVersionFabric)
                .withMods(getMods())
                .build();
        this.updater = new FlowUpdater.FlowUpdaterBuilder()
                .withModLoaderVersion(fabricVersion)
                .withVanillaVersion(version)
                .withLogger(MvWildLauncher.logger)
                .withExternalFiles(ExternalFile.getExternalFilesFromJson(MvWildLauncher.SITE_URL + PATH_TO_EXT_FILE))
                .withProgressCallback(mvCallback)
                .build();
    }

    public void update() throws Exception {
        Path dir = FileManager.getGameFolderPath();
        updater.update(dir);
    }

    public static List<Mod> getMods() {
        return Mod.getModsFromJson(MvWildLauncher.SITE_URL+ PATH_TO_MODS);
    }
}

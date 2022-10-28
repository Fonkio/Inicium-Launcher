package fr.fonkio.launcher.files;

import fr.fonkio.launcher.utils.EnumSaver;
import fr.theshark34.openlauncherlib.util.Saver;

public class MvSaver {
    Saver saver = FileManager.getSaver();

    public String get(EnumSaver prop) {
        if (saver != null) {
            return saver.get(prop.getKey());
        } else {
            return null;
        }
    }

    public void set(EnumSaver prop, String value) {
        if (saver != null) {
            saver.set(prop.getKey(), value);
        }
    }

    public void remove(EnumSaver prop) {
        if (saver != null) {
            saver.remove(prop.getKey());
        }
    }

    public void save() {
        if (saver != null) {
            saver.save();
        }
    }
}

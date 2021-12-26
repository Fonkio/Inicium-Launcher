package fr.fonkio.launcher.files;

import fr.theshark34.openlauncherlib.util.Saver;

public class MvSaver {
    Saver saver = FileManager.getSaver();

    public String get(String prop) {
        if (saver != null) {
            return saver.get(prop);
        } else {
            return null;
        }
    }

    public void set(String prop, String value) {
        if (saver != null) {
            saver.set(prop, value);
        }
    }

    public void remove(String prop) {
        if (saver != null) {
            saver.remove(prop);
        }
    }

    public void save() {
        if (saver != null) {
            saver.save();
        }
    }
}

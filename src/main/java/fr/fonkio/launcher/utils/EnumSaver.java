package fr.fonkio.launcher.utils;

public enum EnumSaver {
    ACCESS_TOKEN("accessToken"),
    REFRESH_TOKEN("refreshToken"),
    RAM("RAM"),
    DISABLE_DRP("disableDRP"),
    VERSION_FABRIC("fabricVersion"),
    VERSION_MC("mcVersion")
    ;

    private final String key;
    EnumSaver(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}

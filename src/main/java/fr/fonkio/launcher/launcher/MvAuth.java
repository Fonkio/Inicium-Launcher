package fr.fonkio.launcher.launcher;

import fr.fonkio.launcher.files.MvSaver;
import fr.fonkio.launcher.ui.PanelManager;
import fr.fonkio.launcher.utils.EnumSaver;
import fr.litarvan.openauth.microsoft.MicrosoftAuthResult;
import fr.litarvan.openauth.microsoft.MicrosoftAuthenticationException;
import fr.litarvan.openauth.microsoft.MicrosoftAuthenticator;
import fr.theshark34.openlauncherlib.minecraft.AuthInfos;
import javafx.scene.control.Alert;

import java.util.function.BiConsumer;


public class MvAuth implements BiConsumer<MicrosoftAuthResult, Throwable> {

    MicrosoftAuthenticator authenticator = new MicrosoftAuthenticator();
    private final MvSaver saver = new MvSaver();

    private AuthInfos authInfos;

    PanelManager panelManager;

    public MvAuth(PanelManager panelManager) {
        this.panelManager = panelManager;
    }

    public boolean isConnected() {
        if (saver.get(EnumSaver.ACCESS_TOKEN) != null && saver.get(EnumSaver.REFRESH_TOKEN) != null) {
            try {
                MicrosoftAuthResult refreshResponse = authenticator.loginWithRefreshToken(saver.get(EnumSaver.REFRESH_TOKEN));
                updateTokens(refreshResponse);
                updateAuthInfos(refreshResponse);
            } catch (MicrosoftAuthenticationException e) {
                deconnexion();
                return false;
            }
            return true;
        } else {
            return false;
        }
    }

    private void updateTokens(MicrosoftAuthResult response) {
        saver.set(EnumSaver.ACCESS_TOKEN, response.getAccessToken());
        saver.set(EnumSaver.REFRESH_TOKEN, response.getRefreshToken());
        saver.save();
    }

    public void connexion() {
        MicrosoftAuthenticator authenticator = new MicrosoftAuthenticator();
        authenticator.loginWithAsyncWebview().whenComplete(this);
        System.out.println("Connexion avec Microsoft...");
    }

    private void updateAuthInfos(MicrosoftAuthResult response) {
        authInfos = new AuthInfos(
                response.getProfile().getName(),
                response.getAccessToken(),
                response.getProfile().getId()
        );
    }

    public AuthInfos getAuthInfos() {
        return authInfos;
    }

    @Override
    public void accept(MicrosoftAuthResult microsoftAuthResult, Throwable error) {
        if (error != null) {
            panelManager.deconnexion();
        } else {
            updateTokens(microsoftAuthResult);
            updateAuthInfos(microsoftAuthResult);
            panelManager.connected();
        }
    }

    public void deconnexion() {
        authInfos = null;
        authenticator = new MicrosoftAuthenticator();
        saver.remove(EnumSaver.ACCESS_TOKEN);
        saver.remove(EnumSaver.REFRESH_TOKEN);
        saver.save();
    }
}

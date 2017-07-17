package co.touchlab.droidconandroid.shared.presenter;

/**
 * Created by kgalligan on 4/25/16.
 */
public interface EventDetailHost
{
    void dataRefresh();
    void videoDataRefresh();
    void resetStreamProgress();
    void reportError(String error);
    void showTicketOptions(String email, String link, String cover);
    void openSlack(String slackLink, String slackLinkHttp, boolean showSlackDialog);
}

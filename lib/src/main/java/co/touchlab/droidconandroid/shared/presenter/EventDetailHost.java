package co.touchlab.droidconandroid.shared.presenter;
import co.touchlab.droidconandroid.shared.tasks.StartWatchVideoTask;

/**
 * Created by kgalligan on 4/25/16.
 */
public interface EventDetailHost
{
    void dataRefresh();
    void videoDataRefresh();
    void callStreamActivity(StartWatchVideoTask task);
    void resetStreamProgress();
    void reportError(String error);
    void showTicketOptions(String email, String link, String cover);
    void openSlack(String slackLink, String slackLinkHttp, boolean showSlackDialog);
}

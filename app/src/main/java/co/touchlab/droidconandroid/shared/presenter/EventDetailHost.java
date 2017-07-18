package co.touchlab.droidconandroid.shared.presenter;

import co.touchlab.droidconandroid.shared.data.EventInfo;

/**
 * Created by kgalligan on 4/25/16.
 */
public interface EventDetailHost
{
    void dataRefresh(EventInfo eventInfo);
    void resetStreamProgress();
    void reportError(String error);
    void updateRsvp();
    void showTicketOptions(String email, String link, String cover);
    void openSlack(String slackLink, String slackLinkHttp, boolean showSlackDialog);
}

package co.touchlab.droidconandroid.shared.utils;

import android.support.annotation.NonNull;

import co.touchlab.droidconandroid.shared.data.Venue;

public class SlackUtils
{

    public static final String TEAM_ID = "T26RNU7R9";
    public static final long TRACK_1_ID = 68650;
    public static final String TRACK_1_SLACK_ID = "C26UZCM6U";
    public static final long TRACK_2_ID = 71950;
    public static final String TRACK_2_SLACK_ID = "C26V6M621";
    public static final long TRACK_3_ID = 71951;
    public static final String TRACK_3_SLACK_ID = "C26V6NK4M";
    public static final long TRACK_4_ID = 71952;
    public static final String TRACK_4_SLACK_ID = "C26VA6XMF";
    public static final String DEFAULT_SLACK_ID = "C26RNUDV1";

    public static String createSlackLink(Venue venue)
    {

        if(venue == null)
        {
            return "slack://open?team=" + TEAM_ID;
        }

        return "slack://channel?team=" + TEAM_ID + "&id=" + getChannelId(venue);
    }

    @NonNull
    private static String getChannelId(Venue venue)
    {
        String channelId;
        if(venue.id == TRACK_1_ID)
        {
            channelId = TRACK_1_SLACK_ID;

        }
        else if(venue.id == TRACK_2_ID)
        {
            channelId = TRACK_2_SLACK_ID;

        }
        else if(venue.id == TRACK_3_ID)
        {
            channelId = TRACK_3_SLACK_ID;

        }
        else if(venue.id == TRACK_4_ID)
        {
            channelId = TRACK_4_SLACK_ID;

        }
        else
        {
            channelId = DEFAULT_SLACK_ID;

        }
        return channelId;
    }

    public static String createSlackLinkHttp(Venue venue)
    {
        String link = "https://droidcon-nyc.slack.com/messages/";
        if(venue == null)
        {
            link += "random";
        }
        else
        {
            link += getChannelId(venue);
        }
        return link;
    }
}

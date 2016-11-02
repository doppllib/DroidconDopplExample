package co.touchlab.droidconandroid.network.dao;
/**
 * Created by kgalligan on 9/15/16.
 */
public class EventVideoDetails
{
    String streamLink;
    String videoLink;
    String streamArchiveLink;
    int streamArchiveStart;

    public String getStreamLink()
    {
        return streamLink;
    }

    public String getVideoLink()
    {
        return videoLink;
    }

    public String getStreamArchiveLink()
    {
        return streamArchiveLink;
    }

    public int getStreamArchiveStart()
    {
        return streamArchiveStart;
    }
}

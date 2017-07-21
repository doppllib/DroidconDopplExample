package co.touchlab.droidconandroid.shared.network.dao;

import co.touchlab.droidconandroid.shared.utils.StringUtils;

/**
 * Created by kgalligan on 9/15/16.
 */
public class EventVideoDetails {
    String streamLink;
    String videoLink;
    String streamArchiveLink;
    int streamArchiveStart;

    public String getStreamLink() {
        return streamLink;
    }

    public String getVideoLink() {
        return videoLink;
    }

    public String getStreamArchiveLink() {
        return streamArchiveLink;
    }

    public int getStreamArchiveStart() {
        return streamArchiveStart;
    }

    public boolean hasStream() {
        return StringUtils.isNotEmpty(getStreamLink()) || StringUtils.isNotEmpty(getStreamArchiveLink());
    }

    public boolean isNow() {
        return StringUtils.isNotEmpty(getStreamLink());
    }

    public String getMergedStreamLink() {
        return StringUtils.isNotEmpty(getStreamLink()) ? getStreamLink() : getStreamArchiveLink();
    }
}

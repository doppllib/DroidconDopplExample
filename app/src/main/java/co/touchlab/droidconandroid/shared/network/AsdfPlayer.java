package co.touchlab.droidconandroid.shared.network;
/**
 * Created by kgalligan on 8/14/17.
 */

public class AsdfPlayer
{
    private Long id;

    private long facebookId;
    private String facebookName;

    private Long joined;

    private Long lostTime;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public long getFacebookId()
    {
        return facebookId;
    }

    public void setFacebookId(long facebookId)
    {
        this.facebookId = facebookId;
    }

    public String getFacebookName()
    {
        return facebookName;
    }

    public void setFacebookName(String facebookName)
    {
        this.facebookName = facebookName;
    }

    public Long getJoined()
    {
        return joined;
    }

    public void setJoined(Long joined)
    {
        this.joined = joined;
    }

    public Long getLostTime()
    {
        return lostTime;
    }

    public void setLostTime(Long lostTime)
    {
        this.lostTime = lostTime;
    }
}

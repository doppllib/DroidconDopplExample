package co.touchlab.droidconandroid.shared.network.dao;
/**
 * Created by kgalligan on 4/6/16.
 */
public class NetworkUserAccount
{
    public Long id;
    public String name;
    public String profile;
    public String avatarKey;
    public String userCode;
    public String company;
    public String twitter;
    public String linkedIn;
    public String website;
    public String email;
    public String phone;
    public String gPlus;
    public String coverKey;
    public String facebook;
    public Boolean emailPublic;
    public Boolean following;

    public String getName()
    {
        return name;
    }
}

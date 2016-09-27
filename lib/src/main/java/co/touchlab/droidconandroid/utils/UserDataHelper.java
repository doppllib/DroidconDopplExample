package co.touchlab.droidconandroid.utils;
/**
 * Created by kgalligan on 9/26/16.
 */
public class UserDataHelper
{
    public static void userAccountToDb(co.touchlab.droidconandroid.network.dao.UserAccount ua, co.touchlab.droidconandroid.data.UserAccount dbUa)
    {
        dbUa.id = ua.id;
        dbUa.name = ua.name;
        dbUa.profile = ua.profile;
        dbUa.avatarKey = ua.avatarKey;
        dbUa.userCode = ua.userCode;
        dbUa.company = ua.company;
        dbUa.twitter = ua.twitter;
        dbUa.linkedIn = ua.linkedIn;
        dbUa.website = ua.website;
        dbUa.following = ua.following;
        dbUa.gPlus = ua.gPlus;
        dbUa.phone = ua.phone;
        dbUa.email = ua.email;
        dbUa.coverKey = ua.coverKey;
        dbUa.facebook = ua.facebook;
        dbUa.emailPublic = ua.emailPublic;
    }
}

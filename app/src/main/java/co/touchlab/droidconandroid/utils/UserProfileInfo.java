package co.touchlab.droidconandroid.utils;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.util.Log;

/**
 * Created by kgalligan on 7/27/14.
 */
public class UserProfileInfo
{
    public static String findUserName(Context context)
    {
        String displayName = null;

        try
        {
            Cursor c = context.getContentResolver().query(
                    ContactsContract.Profile.CONTENT_URI, null, null, null,
                    null);
            int count = c.getCount();
            c.moveToFirst();
            int position = c.getPosition();
            if (count == 1 && position == 0)
            {
                displayName = c.getString(c.getColumnIndex(ContactsContract.Profile.DISPLAY_NAME));
            }
            c.close();
        }
        catch (Exception e)
        {
            //OK.  I will usually yell at people for this, but this is sort of a "best efforts" situation.  If it doesn't work, no big deal
            Log.w("Hi", "Didn't work", e);
        }

        return displayName;
    }
}

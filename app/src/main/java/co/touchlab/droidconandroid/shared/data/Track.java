package co.touchlab.droidconandroid.shared.data;
import android.text.TextUtils;

/**
 * Created by izzyoji :) on 8/9/15.
 */
public enum Track
{
    DEVELOPMENT("Develop", "development", "droidcon_blue", "selector_blue"),
    DESIGN("Design", "design", "droidcon_pink", "selector_pink"),
    BUSINESS("Business", "business", "orange", "selector_orange"),
    DEVDESIGN("Dev/Design", "devdesign", "droidcon_blue", "selector_blue"),
    DESIGNLAB("Design Lab", "designlab", "droidcon_blue", "selector_blue");

    public String getServerName()
    {
        return serverName;
    }

    public String getDisplayNameRes()
    {
        return displayNameRes;
    }

    public String getCheckBoxSelectorRes()
    {
        return checkBoxSelectorRes;
    }

    public String getTextColorRes()
    {
        return textColorRes;
    }

    String serverName;
    String    displayNameRes;
    String    textColorRes;
    String    checkBoxSelectorRes;

    Track(String serverName, String displayNameRes, String textColor, String checkBoxSelector)
    {
        this.serverName = serverName;
        this.displayNameRes = displayNameRes;
        this.textColorRes = textColor;
        this.checkBoxSelectorRes = checkBoxSelector;
    }

    public static Track findByServerName(String serverName)
    {
        for(Track track : values())
        {
            if(TextUtils.equals(track.serverName, serverName))
            {
                return track;
            }
        }
        return null;
    }
}

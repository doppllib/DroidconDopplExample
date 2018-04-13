package co.touchlab.droidconandroid.shared.network.sessionize;


import android.text.TextUtils;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import co.touchlab.droidconandroid.shared.data.TimeBlock;
import co.touchlab.droidconandroid.shared.utils.TimeUtils;


import static co.touchlab.droidconandroid.shared.utils.TimeUtils.SESSIONIZE_DATE_FORMAT;

public class SessionWithSpeakers implements TimeBlock
{

    private final Session       session;
    private final List<Speaker> speakers;

    public SessionWithSpeakers(Session session, List<Speaker> speakers)
    {
        this.session = session;
        this.speakers = speakers;
    }

    public Session getSession()
    {
        return session;
    }

    public List<Speaker> getSpeakers()
    {
        return speakers;
    }

    public String getSpeakerString()
    {
        List<String> names = new ArrayList<>();
        for(Speaker speaker : speakers)
        {
            if(speaker != null)
            {
                names.add(speaker.getFullName());
            }
        }

        return TextUtils.join(", ", names);
    }

    @Override
    public boolean isBlock()
    {
        return false;
    }

    @Override
    public Long getStartLong()
    {
        try
        {
            return TimeUtils.makeDateFormat(SESSIONIZE_DATE_FORMAT).parse(session.getStartsAt()).getTime();
        }
        catch(ParseException e)
        {
            return 0L;
        }
    }

    @Override
    public Long getEndLong()
    {
        try
        {
            return TimeUtils.makeDateFormat(SESSIONIZE_DATE_FORMAT).parse(session.getEndsAt()).getTime();
        }
        catch(ParseException e)
        {
            return 0L;
        }
    }
}

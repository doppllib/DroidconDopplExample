package co.touchlab.droidconandroid.shared.network.sessionize;


import java.util.List;

public class SessionsAndSpeakers
{
    private final List<Session> sessions;
    private final List<Speaker> speakers;

    // This is just here so zipping the two streams together is easier
    public SessionsAndSpeakers(List<Session> sessions, List<Speaker> speakers)
    {
        this.sessions = sessions;
        this.speakers = speakers;
    }

    public List<Session> getSessions()
    {
        return sessions;
    }

    public List<Speaker> getSpeakers()
    {
        return speakers;
    }
}

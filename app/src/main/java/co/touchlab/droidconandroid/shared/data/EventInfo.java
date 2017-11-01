package co.touchlab.droidconandroid.shared.data;

import java.util.List;

public class EventInfo
{
    private final Event             event;
    private final List<UserAccount> speakers;
    private final boolean           conflict;

    public EventInfo(Event event, List<UserAccount> speakers, boolean conflict) {
        this.event = event;
        this.speakers = speakers;
        this.conflict = conflict;
    }

    public Event getEvent() {
        return event;
    }

    public List<UserAccount> getSpeakers() {
        return speakers;
    }

    public boolean getConflict() {
        return conflict;
    }

    public int getSpeakerCount(){
        return speakers.size();
    }

    public UserAccount getSpeaker(int speaker)
    {
        return speakers.get(speaker);
    }
}

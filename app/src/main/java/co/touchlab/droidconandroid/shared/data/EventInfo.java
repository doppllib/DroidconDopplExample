package co.touchlab.droidconandroid.shared.data;


import java.util.List;

import co.touchlab.droidconandroid.shared.network.dao.EventVideoDetails;

public class EventInfo {
    public Event event;
    public List<UserAccount> speakers;
    public boolean conflict;
    public EventVideoDetails videoDetails;
}

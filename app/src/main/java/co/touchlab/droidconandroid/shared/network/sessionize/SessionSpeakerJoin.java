package co.touchlab.droidconandroid.shared.network.sessionize;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.support.annotation.NonNull;

@Entity(primaryKeys = {"sessionId", "speakerId"}, foreignKeys = {
        @ForeignKey(entity = Session.class, parentColumns = "id", childColumns = "sessionId"),
        @ForeignKey(entity = Speaker.class, parentColumns = "id", childColumns = "speakerId")
})
public class SessionSpeakerJoin
{
    @NonNull
    private final String sessionId;
    @NonNull
    private final String speakerId;

    public SessionSpeakerJoin(@NonNull String sessionId, @NonNull String speakerId)
    {
        this.sessionId = sessionId;
        this.speakerId = speakerId;
    }

    @NonNull
    public String getSessionId()
    {
        return sessionId;
    }

    @NonNull
    public String getSpeakerId()
    {
        return speakerId;
    }
}

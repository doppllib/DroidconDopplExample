package co.touchlab.droidconandroid.shared.network.sessionize;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface ScheduleDao
{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertSessions(List<Session> session);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertSpeakers(List<Speaker> speaker);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertJoins(List<SessionSpeakerJoin> join);

    @Query("SELECT * FROM Session")
    List<Session> getSessions();

    @Query("SELECT * FROM Session WHERE id = :id")
    Session getSession(String id);

    @Query("SELECT * FROM Speaker")
    List<Speaker> getSpeakers();

    @Query("SELECT * FROM Speaker WHERE id = :id")
    Speaker getSpeaker(String id);

    @Query("SELECT * FROM Speaker INNER JOIN SessionSpeakerJoin ON Speaker.id=SessionSpeakerJoin.speakerId " +
            "WHERE SessionSpeakerJoin.sessionId=:sessionId")
    List<Speaker> getSpeakersForSession(String sessionId);

    @Query("SELECT * FROM Session INNER JOIN SessionSpeakerJoin ON Session.id=SessionSpeakerJoin.sessionId " +
            "WHERE SessionSpeakerJoin.speakerId=:speakerId")
    List<Session> getSessionsForSpeaker(String speakerId);
}

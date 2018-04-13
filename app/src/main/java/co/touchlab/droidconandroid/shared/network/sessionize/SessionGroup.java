package co.touchlab.droidconandroid.shared.network.sessionize;


import java.util.List;

public class SessionGroup
{
    private String        groupId;
    private String        groupName;
    private List<Session> sessions;

    public String getGroupId()
    {
        return groupId;
    }

    public void setGroupId(String groupId)
    {
        this.groupId = groupId;
    }

    public String getGroupName()
    {
        return groupName;
    }

    public void setGroupName(String groupName)
    {
        this.groupName = groupName;
    }

    public List<Session> getSessions()
    {
        return sessions;
    }

    public void setSessions(List<Session> sessions)
    {
        this.sessions = sessions;
    }
}

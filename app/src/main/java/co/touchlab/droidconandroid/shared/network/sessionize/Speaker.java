package co.touchlab.droidconandroid.shared.network.sessionize;


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

@Entity
public class Speaker
{
    @NonNull
    @PrimaryKey
    private String     id;
    @Nullable
    private String     firstName;
    @Nullable
    private String     lastName;
    @Nullable
    private String     fullName;
    @Nullable
    private String     bio;
    @Nullable
    private String     tagLine;
    @Nullable
    private String     profilePicture;
    @Nullable
    private boolean    isTopSpeaker;
    @Ignore
    @Nullable
    private List<Link> links;

    public Speaker(@NonNull String id)
    {
        this.id = id;
    }

    @NonNull
    public String getId()
    {
        return id;
    }

    public void setId(@NonNull String id)
    {
        this.id = id;
    }

    @Nullable
    public String getFirstName()
    {
        return firstName;
    }

    public void setFirstName(@Nullable String firstName)
    {
        this.firstName = firstName;
    }

    @Nullable
    public String getLastName()
    {
        return lastName;
    }

    public void setLastName(@Nullable String lastName)
    {
        this.lastName = lastName;
    }

    @Nullable
    public String getFullName()
    {
        return fullName;
    }

    public void setFullName(@Nullable String fullName)
    {
        this.fullName = fullName;
    }

    @Nullable
    public String getBio()
    {
        return bio;
    }

    public void setBio(@Nullable String bio)
    {
        this.bio = bio;
    }

    @Nullable
    public String getTagLine()
    {
        return tagLine;
    }

    public void setTagLine(@Nullable String tagLine)
    {
        this.tagLine = tagLine;
    }

    @Nullable
    public String getProfilePicture()
    {
        return profilePicture;
    }

    public void setProfilePicture(@Nullable String profilePicture)
    {
        this.profilePicture = profilePicture;
    }

    @Nullable
    public boolean isTopSpeaker()
    {
        return isTopSpeaker;
    }

    public void setTopSpeaker(@Nullable boolean topSpeaker)
    {
        isTopSpeaker = topSpeaker;
    }

    @Nullable
    public List<Link> getLinks()
    {
        return links;
    }

    public void setLinks(@Nullable List<Link> links)
    {
        this.links = links;
    }
}

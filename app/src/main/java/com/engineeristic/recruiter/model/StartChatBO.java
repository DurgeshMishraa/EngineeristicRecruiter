package com.engineeristic.recruiter.model;

public class StartChatBO
{
    private String logout;

    private String status;

    private String notify;

    private String notification;

    private Chat chat;

    private String success;

    private Profile profile;

    private Version version;

    public String getLogout ()
    {
        return logout;
    }

    public void setLogout (String logout)
    {
        this.logout = logout;
    }

    public String getStatus ()
    {
        return status;
    }

    public void setStatus (String status)
    {
        this.status = status;
    }

    public String getNotify ()
    {
        return notify;
    }

    public void setNotify (String notify)
    {
        this.notify = notify;
    }

    public String getNotification ()
    {
        return notification;
    }

    public void setNotification (String notification)
    {
        this.notification = notification;
    }

    public Chat getChat ()
    {
        return chat;
    }

    public void setChat (Chat chat)
    {
        this.chat = chat;
    }

    public String getSuccess ()
    {
        return success;
    }

    public void setSuccess (String success)
    {
        this.success = success;
    }

    public Profile getProfile ()
    {
        return profile;
    }

    public void setProfile (Profile profile)
    {
        this.profile = profile;
    }

    public Version getVersion ()
    {
        return version;
    }

    public void setVersion (Version version)
    {
        this.version = version;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [logout = "+logout+", status = "+status+", notify = "+notify+", notification = "+notification+", chat = "+chat+", success = "+success+", profile = "+profile+", version = "+version+"]";
    }
}
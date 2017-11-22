package com.engineeristic.recruiter.model;

/**
 * Created by dell on 10/6/2016.
 */
public class PrefLocationModel {
    private String message;

    private String status;

    private LocationsModel[] locations;

    private String notify;

    private String notification;

    private String success;

    public String getMessage ()
    {
        return message;
    }

    public void setMessage (String message)
    {
        this.message = message;
    }

    public String getStatus ()
    {
        return status;
    }

    public void setStatus (String status)
    {
        this.status = status;
    }

    public LocationsModel[] getLocations ()
    {
        return locations;
    }

    public void setLocations (LocationsModel[] locations)
    {
        this.locations = locations;
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

    public String getSuccess ()
    {
        return success;
    }

    public void setSuccess (String success)
    {
        this.success = success;
    }


    @Override
    public String toString()
    {
        return "ClassPojo [message = "+message+", status = "+status+", locations = "+locations+", notify = "+notify+", notification = "+notification+", success = "+success+"]";
    }
}

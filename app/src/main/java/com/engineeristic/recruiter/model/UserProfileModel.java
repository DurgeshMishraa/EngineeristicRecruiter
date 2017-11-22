package com.engineeristic.recruiter.model;

import java.io.Serializable;

/**
 * Created by dell on 10/5/2016.
 */
public class UserProfileModel implements Serializable{
    private String message;

    private String en_cookie;

    private String error;

    private String status;

    private String notify;

    private String success;

    private String error_msg;

    private ProfileModel profile;

    public String getMessage ()
    {
        return message;
    }

    public void setMessage (String message)
    {
        this.message = message;
    }

    public String getEn_cookie ()
    {
        return en_cookie;
    }

    public void setEn_cookie (String en_cookie)
    {
        this.en_cookie = en_cookie;
    }

    public String getError ()
    {
        return error;
    }

    public void setError (String error)
    {
        this.error = error;
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

    public String getSuccess ()
    {
        return success;
    }

    public void setSuccess (String success)
    {
        this.success = success;
    }

    public String getError_msg ()
    {
        return error_msg;
    }

    public void setError_msg (String error_msg)
    {
        this.error_msg = error_msg;
    }


    public ProfileModel getProfile ()
    {
        return profile;
    }

    public void setProfile (ProfileModel profile)
    {
        this.profile = profile;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [message = "+message+", en_cookie = "+en_cookie+", error = "+error+", status = "+status+", notify = "+notify+", success = "+success+", error_msg = "+error_msg+", profile = "+profile+"]";
    }
}

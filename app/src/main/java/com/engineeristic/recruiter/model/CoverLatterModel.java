package com.engineeristic.recruiter.model;

/**
 * Created by dell on 10/19/2016.
 */

public class CoverLatterModel {
    private String message;

    private String covertext;

    private String error;

    private String status;

    private String notify;

    private String name;

    private String jobseeker_id;

    private String success;

    private String error_msg;

    public String getMessage ()
    {
        return message;
    }

    public void setMessage (String message)
    {
        this.message = message;
    }

    public String getCovertext ()
    {
        return covertext;
    }

    public void setCovertext (String covertext)
    {
        this.covertext = covertext;
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

    public String getName ()
    {
        return name;
    }

    public void setName (String name)
    {
        this.name = name;
    }

    public String getJobseeker_id ()
    {
        return jobseeker_id;
    }

    public void setJobseeker_id (String jobseeker_id)
    {
        this.jobseeker_id = jobseeker_id;
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


    @Override
    public String toString()
    {
        return "ClassPojo [message = "+message+", covertext = "+covertext+", error = "+error+", status = "+status+", notify = "+notify+", name = "+name+", jobseeker_id = "+jobseeker_id+", success = "+success+", error_msg = "+error_msg+"]";
    }
}

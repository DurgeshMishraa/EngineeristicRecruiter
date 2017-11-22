package com.engineeristic.recruiter.model;

/**
 * Created by dell on 10/6/2016.
 */
public class ChangePasswordModel {

    private String message;

    private String error;

    private String status;

    private String notify;

    private String success_msg;

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

    public String getSuccess_msg ()
    {
        return success_msg;
    }

    public void setSuccess_msg (String success_msg)
    {
        this.success_msg = success_msg;
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
        return "ClassPojo [message = "+message+", error = "+error+", status = "+status+", notify = "+notify+", success_msg = "+success_msg+", success = "+success+", error_msg = "+error_msg+"]";
    }
}

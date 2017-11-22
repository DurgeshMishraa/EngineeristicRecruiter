package com.engineeristic.recruiter.model;

/**
 * Created by dell on 10/4/2016.
 */
public class PostCommentModel {
    private String message;

    private String error;

    private String status;

    private String notify;

    private String comment;

    private String success;

    private String error_msg;

    private String apply_id;

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

    public String getComment ()
    {
        return comment;
    }

    public void setComment (String comment)
    {
        this.comment = comment;
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


    public String getApply_id ()
    {
        return apply_id;
    }

    public void setApply_id (String apply_id)
    {
        this.apply_id = apply_id;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [message = "+message+", error = "+error+", status = "+status+", notify = "+notify+", comment = "+comment+", success = "+success+", error_msg = "+error_msg+", apply_id = "+apply_id+"]";
    }
}

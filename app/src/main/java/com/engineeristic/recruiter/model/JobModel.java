package com.engineeristic.recruiter.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by dell on 9/20/2016.
 */
public class JobModel implements Serializable
{
    private String message;
    private String error;
    private ArrayList<JobItemsModel> jobsarray;
    private String status;
    private int noofpages;
    private int page;
    private String start;
    private String totaljobs;
    private String notify;
    private String success;
    private String error_msg;
    private String ChatEnable;

    public String getChatEnable() {
        return ChatEnable;
    }

    public void setChatEnable(String chatEnable) {
        ChatEnable = chatEnable;
    }
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

    public ArrayList<JobItemsModel> getJobsarray ()
    {
        return jobsarray;
    }

    public void setJobsarray (ArrayList<JobItemsModel> jobsarray)
    {
        this.jobsarray = jobsarray;
    }

    public String getStatus ()
    {
        return status;
    }

    public void setStatus (String status)
    {
        this.status = status;
    }

    public int getnoofpages ()
    {
        return noofpages;
    }

    public void setnoofpages (int noofpages)
    {
        this.noofpages = noofpages;
    }

    public int getPage ()
    {
        return page;
    }

    public void setPage (int page)
    {
        this.page = page;
    }

    public String getStart ()
    {
        return start;
    }

    public void setStart (String start)
    {
        this.start = start;
    }

    public String getTotaljobs ()
    {
        return totaljobs;
    }

    public void setTotaljobs (String totaljobs)
    {
        this.totaljobs = totaljobs;
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


    @Override
    public String toString()
    {
        return "ClassPojo [message = "+message+", error = "+error+", status = "+status+", noofpages = "+noofpages+", page = "+page+", ChatEnable = "+ChatEnable+" ,start = "+start+", totaljobs = "+totaljobs+", notify = "+notify+", success = "+success+", error_msg = "+error_msg+"]";
    }



}

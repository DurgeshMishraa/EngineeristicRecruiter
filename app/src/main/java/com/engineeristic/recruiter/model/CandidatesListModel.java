package com.engineeristic.recruiter.model;

import java.util.ArrayList;

/**
 * Created by dell on 9/26/2016.
 */
public class CandidatesListModel {
    private String totalsave;

    private String totalshorlist;

    private ArrayList<CandidateJobapplicationsModel> jobapplications;

    private String error;

    private String status;

    private String totalapplications;

    private String totalreject;

    private String error_msg;

    private String message;

    private String page;

    private String noofpages;

    private String notify;

    private String totalallapplication;

    private String totalnewapplication;

    private String view_cnt;

    private String success;

    private String view_cnt_msg;

    public String getTotalsave ()
    {
        return totalsave;
    }

    public void setTotalsave (String totalsave)
    {
        this.totalsave = totalsave;
    }

    public String getTotalshorlist ()
    {
        return totalshorlist;
    }

    public void setTotalshorlist (String totalshorlist)
    {
        this.totalshorlist = totalshorlist;
    }

    public ArrayList<CandidateJobapplicationsModel> getCandidateJobapplicationsModel ()
    {
        return jobapplications;
    }

    public void setCandidateJobapplicationsModel (ArrayList<CandidateJobapplicationsModel> jobapplications)
    {
        this.jobapplications = jobapplications;
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

    public String getTotalapplications ()
    {
        return totalapplications;
    }

    public void setTotalapplications (String totalapplications)
    {
        this.totalapplications = totalapplications;
    }

    public String getTotalreject ()
    {
        return totalreject;
    }

    public void setTotalreject (String totalreject)
    {
        this.totalreject = totalreject;
    }

    public String getError_msg ()
    {
        return error_msg;
    }

    public void setError_msg (String error_msg)
    {
        this.error_msg = error_msg;
    }


    public String getMessage ()
    {
        return message;
    }

    public void setMessage (String message)
    {
        this.message = message;
    }

    public String getPage ()
    {
        return page;
    }

    public void setPage (String page)
    {
        this.page = page;
    }

    public String getNoofpages ()
    {
        return noofpages;
    }

    public void setNoofpages (String noofpages)
    {
        this.noofpages = noofpages;
    }

    public String getNotify ()
    {
        return notify;
    }

    public void setNotify (String notify)
    {
        this.notify = notify;
    }

    public String getTotalallapplication ()
    {
        return totalallapplication;
    }

    public void setTotalallapplication (String totalallapplication)
    {
        this.totalallapplication = totalallapplication;
    }

    public String getTotalnewapplication ()
    {
        return totalnewapplication;
    }

    public void setTotalnewapplication (String totalnewapplication)
    {
        this.totalnewapplication = totalnewapplication;
    }

    public String getView_cnt ()
    {
        return view_cnt;
    }

    public void setView_cnt (String view_cnt)
    {
        this.view_cnt = view_cnt;
    }

    public String getSuccess ()
    {
        return success;
    }

    public void setSuccess (String success)
    {
        this.success = success;
    }

    public String getView_cnt_msg ()
    {
        return view_cnt_msg;
    }

    public void setView_cnt_msg (String view_cnt_msg)
    {
        this.view_cnt_msg = view_cnt_msg;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [totalsave = "+totalsave+", totalshorlist = "+totalshorlist+", jobapplications = "+jobapplications+", error = "+error+", status = "+status+", totalapplications = "+totalapplications+", totalreject = "+totalreject+", error_msg = "+error_msg+", message = "+message+", page = "+page+", noofpages = "+noofpages+", notify = "+notify+", totalallapplication = "+totalallapplication+", totalnewapplication = "+totalnewapplication+", view_cnt = "+view_cnt+", success = "+success+", view_cnt_msg = "+view_cnt_msg+"]";
    }
}

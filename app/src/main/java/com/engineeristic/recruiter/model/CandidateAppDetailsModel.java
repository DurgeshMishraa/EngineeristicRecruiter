package com.engineeristic.recruiter.model;

import java.io.Serializable;

/**
 * Created by dell on 9/27/2016.
 */
public class CandidateAppDetailsModel implements Serializable{
    private String coverid;

    private String exp_year;

    private String phone;

    private String userid;

    private String functional_area;

    private String id;

    private String name;

    private String recruiter_id;

    private String preferred_location;

    private String expected_ctc;

    private String gender;

    private String industry;

    private String confidential;

    private String app_status;

    private String exp_month;

    private String applydate;

    private String current_location;

    private String jobid;

    private String first_letter;

    private String exp_status;

    private String followup;

    private String color;

    private String email;

    private String current_ctc;

    private String dob;

    private String seekerage;

    private String comment;

    private String notice_period;

    public String getCoverid ()
    {
        return coverid;
    }

    public void setCoverid (String coverid)
    {
        this.coverid = coverid;
    }

    public String getExp_year ()
    {
        return exp_year;
    }

    public void setExp_year (String exp_year)
    {
        this.exp_year = exp_year;
    }

    public String getPhone ()
    {
        return phone;
    }

    public void setPhone (String phone)
    {
        this.phone = phone;
    }

    public String getUserid ()
    {
        return userid;
    }

    public void setUserid (String userid)
    {
        this.userid = userid;
    }

    public String getFunctional_area ()
    {
        return functional_area;
    }

    public void setFunctional_area (String functional_area)
    {
        this.functional_area = functional_area;
    }

    public String getId ()
    {
        return id;
    }

    public void setId (String id)
    {
        this.id = id;
    }

    public String getName ()
    {
        return name;
    }

    public void setName (String name)
    {
        this.name = name;
    }

    public String getRecruiter_id ()
    {
        return recruiter_id;
    }

    public void setRecruiter_id (String recruiter_id)
    {
        this.recruiter_id = recruiter_id;
    }

    public String getPreferred_location ()
    {
        return preferred_location;
    }

    public void setPreferred_location (String preferred_location)
    {
        this.preferred_location = preferred_location;
    }

    public String getExpected_ctc ()
    {
        return expected_ctc;
    }

    public void setExpected_ctc (String expected_ctc)
    {
        this.expected_ctc = expected_ctc;
    }

    public String getGender ()
    {
        return gender;
    }

    public void setGender (String gender)
    {
        this.gender = gender;
    }

    public String getIndustry ()
    {
        return industry;
    }

    public void setIndustry (String industry)
    {
        this.industry = industry;
    }

    public String getConfidential ()
    {
        return confidential;
    }

    public void setConfidential (String confidential)
    {
        this.confidential = confidential;
    }

    public String getApp_status ()
    {
        return app_status;
    }

    public void setApp_status (String app_status)
    {
        this.app_status = app_status;
    }

    public String getExp_month ()
    {
        return exp_month;
    }

    public void setExp_month (String exp_month)
    {
        this.exp_month = exp_month;
    }

    public String getApplydate ()
    {
        return applydate;
    }

    public void setApplydate (String applydate)
    {
        this.applydate = applydate;
    }

    public String getCurrent_location ()
    {
        return current_location;
    }

    public void setCurrent_location (String current_location)
    {
        this.current_location = current_location;
    }

    public String getJobid ()
    {
        return jobid;
    }

    public void setJobid (String jobid)
    {
        this.jobid = jobid;
    }

    public String getFirst_letter ()
    {
        return first_letter;
    }

    public void setFirst_letter (String first_letter)
    {
        this.first_letter = first_letter;
    }

    public String getExp_status ()
    {
        return exp_status;
    }

    public void setExp_status (String exp_status)
    {
        this.exp_status = exp_status;
    }

    public String getFollowup ()
    {
        return followup;
    }

    public void setFollowup (String followup)
    {
        this.followup = followup;
    }

    public String getColor ()
    {
        return color;
    }

    public void setColor (String color)
    {
        this.color = color;
    }

    public String getEmail ()
    {
        return email;
    }

    public void setEmail (String email)
    {
        this.email = email;
    }

    public String getCurrent_ctc ()
    {
        return current_ctc;
    }

    public void setCurrent_ctc (String current_ctc)
    {
        this.current_ctc = current_ctc;
    }

    public String getDob ()
    {
        return dob;
    }

    public void setDob (String dob)
    {
        this.dob = dob;
    }

    public String getSeekerage ()
    {
        return seekerage;
    }

    public void setSeekerage (String seekerage)
    {
        this.seekerage = seekerage;
    }

    public String getComment ()
    {
        return comment;
    }

    public void setComment (String comment)
    {
        this.comment = comment;
    }

    public String getNotice_period ()
    {
        return notice_period;
    }

    public void setNotice_period (String notice_period)
    {
        this.notice_period = notice_period;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [coverid = "+coverid+", exp_year = "+exp_year+", phone = "+phone+", userid = "+userid+", functional_area = "+functional_area+", id = "+id+", name = "+name+", recruiter_id = "+recruiter_id+", preferred_location = "+preferred_location+", expected_ctc = "+expected_ctc+", gender = "+gender+", industry = "+industry+", confidential = "+confidential+", app_status = "+app_status+", exp_month = "+exp_month+", applydate = "+applydate+", current_location = "+current_location+", jobid = "+jobid+", first_letter = "+first_letter+", exp_status = "+exp_status+", followup = "+followup+", color = "+color+", email = "+email+", current_ctc = "+current_ctc+", dob = "+dob+", seekerage = "+seekerage+", comment = "+comment+", notice_period = "+notice_period+"]";
    }
}

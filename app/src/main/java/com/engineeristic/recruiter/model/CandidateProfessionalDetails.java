package com.engineeristic.recruiter.model;

import java.io.Serializable;

/**
 * Created by dell on 9/26/2016.
 */
public class CandidateProfessionalDetails implements Serializable{
    private String from_exp_year;

    private String organization;

    private String from_exp_month;

    private String designation;

    private String is_current;

    private String to_exp_month;

    private String to_exp_year;

    private String education_icon;

    public String getFrom_exp_year ()
    {
        return from_exp_year;
    }

    public void setFrom_exp_year (String from_exp_year)
    {
        this.from_exp_year = from_exp_year;
    }

    public String getOrganization ()
    {
        return organization;
    }

    public void setOrganization (String organization)
    {
        this.organization = organization;
    }

    public String getFrom_exp_month ()
    {
        return from_exp_month;
    }

    public void setFrom_exp_month (String from_exp_month)
    {
        this.from_exp_month = from_exp_month;
    }

    public String getDesignation ()
    {
        return designation;
    }

    public void setDesignation (String designation)
    {
        this.designation = designation;
    }

    public String getIs_current ()
    {
        return is_current;
    }

    public void setIs_current (String is_current)
    {
        this.is_current = is_current;
    }

    public String getTo_exp_month ()
    {
        return to_exp_month;
    }

    public void setTo_exp_month (String to_exp_month)
    {
        this.to_exp_month = to_exp_month;
    }

    public String getTo_exp_year ()
    {
        return to_exp_year;
    }

    public void setTo_exp_year (String to_exp_year)
    {
        this.to_exp_year = to_exp_year;
    }

    public String getEducation_icon ()
    {
        return education_icon;
    }

    public void setEducation_icon (String education_icon)
    {
        this.education_icon = education_icon;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [from_exp_year = "+from_exp_year+", organization = "+organization+", from_exp_month = "+from_exp_month+", designation = "+designation+", is_current = "+is_current+", to_exp_month = "+to_exp_month+", to_exp_year = "+to_exp_year+", education_icon = "+education_icon+"]";
    }
}

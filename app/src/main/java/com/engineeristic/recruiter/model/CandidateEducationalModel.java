package com.engineeristic.recruiter.model;

import java.io.Serializable;

/**
 * Created by dell on 9/26/2016.
 */
public class CandidateEducationalModel implements Serializable{
    private String institute;

    private String degree;

    private String batch_from;

    private String is_primary;

    private String batch_to;

    private String course_type;

    private String education_icon;

    public String getInstitute ()
    {
        return institute;
    }

    public void setInstitute (String institute)
    {
        this.institute = institute;
    }

    public String getDegree ()
    {
        return degree;
    }

    public void setDegree (String degree)
    {
        this.degree = degree;
    }

    public String getBatch_from ()
    {
        return batch_from;
    }

    public void setBatch_from (String batch_from)
    {
        this.batch_from = batch_from;
    }

    public String getIs_primary ()
    {
        return is_primary;
    }

    public void setIs_primary (String is_primary)
    {
        this.is_primary = is_primary;
    }

    public String getBatch_to ()
    {
        return batch_to;
    }

    public void setBatch_to (String batch_to)
    {
        this.batch_to = batch_to;
    }

    public String getCourse_type ()
    {
        return course_type;
    }

    public void setCourse_type (String course_type)
    {
        this.course_type = course_type;
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
        return "ClassPojo [institute = "+institute+", degree = "+degree+", batch_from = "+batch_from+", is_primary = "+is_primary+", batch_to = "+batch_to+", course_type = "+course_type+", education_icon = "+education_icon+"]";
    }
}

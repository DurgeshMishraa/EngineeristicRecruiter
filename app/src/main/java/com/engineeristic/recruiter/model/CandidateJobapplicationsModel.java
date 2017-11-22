package com.engineeristic.recruiter.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by dell on 9/27/2016.
 */
public class CandidateJobapplicationsModel implements Serializable{
    private ArrayList<CandidateEducationalModel> educationdetails;

    private CandidateAppDetailsModel applicationdetails;

    private ArrayList<CandidateProfessionalDetails> professiondetails;

    public ArrayList<CandidateEducationalModel> getCandidateEducationalModel ()
    {
        return educationdetails;
    }

    public void setCandidateEducationalModel (ArrayList<CandidateEducationalModel> educationdetails)
    {
        this.educationdetails = educationdetails;
    }

    public CandidateAppDetailsModel getCandidateAppDetailsModel ()
    {
        return applicationdetails;
    }

    public void setCandidateAppDetailsModel (CandidateAppDetailsModel applicationdetails)
    {
        this.applicationdetails = applicationdetails;
    }

    public ArrayList<CandidateProfessionalDetails> getCandidateProfessionalDetails ()
    {
        return professiondetails;
    }

    public void setCandidateProfessionalDetails (ArrayList<CandidateProfessionalDetails> professiondetails)
    {
        this.professiondetails = professiondetails;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [educationdetails = "+educationdetails+", applicationdetails = "+applicationdetails+", professiondetails = "+professiondetails+"]";
    }
}

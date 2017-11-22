package com.engineeristic.recruiter.model;

/**
 * Created by dell on 10/7/2016.
 */
public class LoginProfileModel {
    private String phone;

    private String email;

    private String organisation;

    private String name;

    private String UUID;

    public String getAUTHKEY() {
        return AUTHKEY;
    }

    public void setAUTHKEY(String AUTHKEY) {
        this.AUTHKEY = AUTHKEY;
    }

    private String AUTHKEY;

    private String designation;

    private String recruiter_id;

    private String image;

    private String mynotification;

    public String getPhone ()
    {
        return phone;
    }

    public void setPhone (String phone)
    {
        this.phone = phone;
    }

    public String getEmail ()
    {
        return email;
    }

    public void setEmail (String email)
    {
        this.email = email;
    }

    public String getOrganisation ()
    {
        return organisation;
    }

    public void setOrganisation (String organisation)
    {
        this.organisation = organisation;
    }

    public String getName ()
    {
        return name;
    }

    public void setName (String name)
    {
        this.name = name;
    }

    public String getUUID ()
    {
        return UUID;
    }

    public void setUUID (String UUID)
    {
        this.UUID = UUID;
    }

    public String getDesignation ()
    {
        return designation;
    }

    public void setDesignation (String designation)
    {
        this.designation = designation;
    }

    public String getRecruiter_id ()
    {
        return recruiter_id;
    }

    public void setRecruiter_id (String recruiter_id)
    {
        this.recruiter_id = recruiter_id;
    }

    public String getImage ()
    {
        return image;
    }

    public void setImage (String image)
    {
        this.image = image;
    }

    public String getMynotification ()
    {
        return mynotification;
    }

    public void setMynotification (String mynotification)
    {
        this.mynotification = mynotification;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [phone = "+phone+", email = "+email+", organisation = "+organisation+", name = "+name+", UUID = "+UUID+", authKey = "+AUTHKEY+", designation = "+designation+", recruiter_id = "+recruiter_id+", image = "+image+", mynotification = "+mynotification+"]";
    }
}
